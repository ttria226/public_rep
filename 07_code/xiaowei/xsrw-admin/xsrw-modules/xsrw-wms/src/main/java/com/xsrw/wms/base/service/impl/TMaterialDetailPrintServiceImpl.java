package com.xsrw.wms.base.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.print.ZplPrint;
import com.xsrw.common.core.print.ZplUtils;
import com.xsrw.common.core.text.Convert;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.domain.dto.TMaterialDetailPrintDTO;
import com.xsrw.wms.base.domain.vo.TMaterialDetailPrintVO;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TMaterialDetailPrintMapper;
import com.xsrw.wms.base.domain.TMaterialDetailPrint;
import com.xsrw.wms.base.service.ITMaterialDetailPrintService;
import org.springframework.transaction.annotation.Transactional;

/**
 * rfid打印记录Service业务层处理
 *
 * @author wxr
 * @date 2023-11-09
 */
@Service
public class TMaterialDetailPrintServiceImpl extends ServiceImpl<TMaterialDetailPrintMapper, TMaterialDetailPrint> implements ITMaterialDetailPrintService {
    @Autowired
    private TMaterialDetailPrintMapper tMaterialDetailPrintMapper;
    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;
    @Autowired
    private ITCodeConfigService codeConfigService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TMaterialMapper tMaterialMapper;
    @Autowired
    private TUnitMapper tUnitMapper;

    /**
     * 查询rfid打印记录列表
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return rfid打印记录
     */
    @Override
    public List<TMaterialDetailPrintVO> selectTMaterialDetailPrintList(TMaterialDetailPrintVO tMaterialDetailPrint) {
        return tMaterialDetailPrintMapper.selectTMaterialDetailPrintList(tMaterialDetailPrint);
    }

    /**
     * 查询rfid打印记录
     *
     * @param id rfid打印记录主键
     * @return rfid打印记录
     */
    @Override
    public TMaterialDetailPrint selectTMaterialDetailPrintById(Long id) {
        TMaterialDetailPrintVO print = new TMaterialDetailPrintVO();
        print.setId(id);
        List<TMaterialDetailPrintVO> tMaterialDetailPrintVOS = tMaterialDetailPrintMapper.selectTMaterialDetailPrintList(print);
        if (CollectionUtils.isNotEmpty(tMaterialDetailPrintVOS)) {
            return tMaterialDetailPrintVOS.get(0);
        }
        return null;
    }

    /**
     * 新增rfid打印记录
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return 结果
     */
    @Override
    public AjaxResult insertTMaterialDetailPrint(TMaterialDetailPrint tMaterialDetailPrint) {
        tMaterialDetailPrint.setPrintCount(0);
//        //如果没有打印过
//        List<TMaterialDetailVO> materialDeList = tMaterialDetailMapper.selectRfIdInfoByDetailId(tMaterialDetailPrint.getAdvanceRegistrationId());
//        if (CollectionUtils.isEmpty(materialDeList)) {
//            return AjaxResult.error("未查询到物料信息");
//        }
//        Integer sum = tMaterialDetailPrint.getSumCount() * tMaterialDetailPrint.getConvertCount();
//        if (sum > materialDeList.size()) {
//            return AjaxResult.error("打印总数超出剩余可打印数量");
//        }

        // 查询物料数量
        TAdvanceDeliveryDetail deliveryDetail = tAdvanceDeliveryDetailMapper.selectById(tMaterialDetailPrint.getAdvanceRegistrationId());
        BigDecimal detectionCount = deliveryDetail.getDetectionCount();

        // 查询已打印数量
        List<TMaterialDetail> materialDetails = tMaterialDetailMapper.selectList(new QueryWrapper<TMaterialDetail>()
                .eq("advance_registration_id", tMaterialDetailPrint.getAdvanceRegistrationId())
                .eq("material_id", deliveryDetail.getMaterialId())
                .eq("batch_code", deliveryDetail.getBatchCode())
                .eq("del_flag", Constants.DEL_FLAG_NO)
        );
        BigDecimal reduce = materialDetails.stream().map(TMaterialDetail::getRfidCount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal subtract = detectionCount.subtract(reduce);
        if (subtract.compareTo(BigDecimal.ZERO) == 0){
             return AjaxResult.error("已打印完成 或 单据状态错误，请查正数据");
        }

        // 判断剩余未打印数量 能够转换的标签数量
        long longValue = subtract.divide(new BigDecimal(tMaterialDetailPrint.getConvertCount()),BigDecimal.ROUND_UP).setScale(0,BigDecimal.ROUND_UP).longValue();
        if (tMaterialDetailPrint.getSumCount().longValue() > longValue){
            return AjaxResult.error("换算存在问题，无法添加打印");
        }

        // 计算此次打印对应的物料总数量
        int materialNum = tMaterialDetailPrint.getConvertCount() * tMaterialDetailPrint.getSumCount();
        if (new BigDecimal(materialNum).compareTo(subtract) == 1 ){
            tMaterialDetailPrint.setMaterialCount(subtract);
        }else {
            tMaterialDetailPrint.setMaterialCount(new BigDecimal(materialNum));
        }

        tMaterialDetailPrintMapper.insert(tMaterialDetailPrint);
        return AjaxResult.success();
    }

    /**
     * 修改rfid打印记录
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return 结果
     */
    @Override
    public AjaxResult updateTMaterialDetailPrint(TMaterialDetailPrint tMaterialDetailPrint) {
        //如果没有打印过
        List<TMaterialDetailVO> materialDeList = tMaterialDetailMapper.selectRfIdInfoByDetailId(tMaterialDetailPrint.getAdvanceRegistrationId());
        if (CollectionUtils.isEmpty(materialDeList)) {
            return AjaxResult.error("未查询到物料信息");
        }
        Integer sum = tMaterialDetailPrint.getSumCount() * tMaterialDetailPrint.getConvertCount();
        if (sum > materialDeList.size()) {
            return AjaxResult.error("打印总数超出剩余可打印数量");
        }
        tMaterialDetailPrintMapper.updateById(tMaterialDetailPrint);
        return AjaxResult.success();
    }


    /**
     * 批量删除rfid打印记录
     *
     * @param ids 需要删除的rfid打印记录主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialDetailPrintByIds(Long[] ids) {
        return tMaterialDetailPrintMapper.deleteTMaterialDetailPrintByIds(ids);
    }

    /**
     * 删除rfid打印记录信息
     *
     * @param id rfid打印记录主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialDetailPrintById(Long id) {
        return tMaterialDetailPrintMapper.deleteTMaterialDetailPrintById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult print(TMaterialDetailPrintDTO print) {
        //查询此打印设置信息
        TMaterialDetailPrint printVO = tMaterialDetailPrintMapper.selectById(print.getId());
        if (printVO == null) {
            return AjaxResult.error("未查询到信息");
        }
        TMaterialDetailPrint materialDePrintDTO = new TMaterialDetailPrint();
        materialDePrintDTO.setId(printVO.getId());
        materialDePrintDTO.setPrintTime(DateUtils.getNowDate());
        Integer printCount = printVO.getPrintCount();

        // 查询入库详情
        TAdvanceDeliveryDetail deliveryDetail = tAdvanceDeliveryDetailMapper.selectById(printVO.getAdvanceRegistrationId());

        //查询此单据相关关联的物料详情列表
        if ((printCount == null || printCount == 0) && StringUtils.isNull(printVO.getRfidHeads())) {
//            //如果没有打印过
//            List<TMaterialDetailVO> materialDeList = tMaterialDetailMapper.selectRfIdInfoByDetailId(printVO.getAdvanceRegistrationId());
//            if (CollectionUtils.isEmpty(materialDeList)) {
//                return AjaxResult.error("未查询到物料信息");
//            }
//            Integer sum = printVO.getSumCount() * printVO.getConvertCount();
//            if (sum > materialDeList.size()) {
//                return AjaxResult.error("打印总数超出剩余可打印数量");
//            }
//            StringJoiner rfidHeads = new StringJoiner(",");
//            Integer count = printVO.getSumCount();
//            for (int i = 0; i < count; i++) {
//                Integer pageIndex = i * printVO.getConvertCount();
//                List<TMaterialDetailVO> details = materialDeList.subList(pageIndex, pageIndex + printVO.getConvertCount());
//                if (CollectionUtils.isNotEmpty(details)) {
//                    List<Long> detailIds = details.stream().map(TMaterialDetailVO::getId).collect(Collectors.toList());
//                    TMaterialDetailVO detail = details.get(0);
//                    String rfidHead = detail.getRfid();
//                    //查询检测通过的数量
//                    List<TMaterialDetailVO> suscessList = details.stream().filter(e -> !Constants.MATERIAL_DETAIL_CHECK_FAIL.equals(e.getDetectionFailStatus())).collect(Collectors.toList());
//                    if (CollectionUtils.isNotEmpty(suscessList)) {
//                        List<String> rfids = suscessList.stream().map(TMaterialDetailVO::getRfid).collect(Collectors.toList());
//                        //redis存放
//                        TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
//                        tMaterialDetailVO.setBatchCode(detail.getBatchCode());
//                        tMaterialDetailVO.setMaterialId(detail.getMaterialId());
//                        tMaterialDetailVO.setMaterialName(detail.getMaterialName());
//                        tMaterialDetailVO.setRfid(rfidHead);
//                        tMaterialDetailVO.setCount(rfids.size());
//                        tMaterialDetailVO.setRfids(rfids);
//                        redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);
//                    }
//                    tMaterialDetailMapper.update(new TMaterialDetail(),
//                            new UpdateWrapper<TMaterialDetail>()
//                                    .eq("del_flag", Constants.DEL_FLAG_NO)
//                                    .in("id", detailIds)
//                                    .set("rfid_head", rfidHead));
//                    rfidHeads.add(rfidHead);
//                    ZplPrint zplPrint = new ZplPrint(detail.getDescription(), detail.getMaterialCode(), rfidHead, detail.getBatchCode(), detail.getUnitName());
//                    this.printMaterialDetail(zplPrint,print.getPrintFloor());
//                }
//            }


            // 添加物料详情及RFID
            // 根据打印配置计算物料详情每个RFID标签对应的物料数量
            int materialSum = printVO.getSumCount() * printVO.getConvertCount();

            TMaterial tMaterial = tMaterialMapper.selectById(deliveryDetail.getMaterialId());
            // 查询物料单位
            TUnit tUnit = tUnitMapper.selectById(tMaterial.getUnitId());

            StringJoiner rfidHeads = new StringJoiner(",");

            // 打印配置刚好整除
            if (new BigDecimal(materialSum).compareTo(printVO.getMaterialCount()) == 0){

                // 生成物料详情及redis相关信息
                for (Integer i = 0; i < printVO.getSumCount(); i++) {
                    TMaterialDetail materialDetail = new TMaterialDetail();
                    materialDetail.setCode(codeConfigService.getCode(CodeEnum.IWLD.getCodeName()));
                    materialDetail.setMaterialId(deliveryDetail.getMaterialId());
                    materialDetail.setBatchCode(deliveryDetail.getBatchCode());
                    materialDetail.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_SUCESS);

                    // 生成RFID
                    String rfId = codeConfigService.getRfIdCode(tMaterial.getCode(),
                            // 生成RFID的批次号 单据批次号存在重复 不可使用
                            codeConfigService.getBatchCode1(deliveryDetail.getMaterialId(), DateUtils.getNowDate()));
                    rfidHeads.add(rfId);

                    materialDetail.setRfid(rfId);
                    materialDetail.setMaterialCode(tMaterial.getCode());
                    materialDetail.setMaterialName(tMaterial.getName());
                    materialDetail.setPrice(tMaterial.getUnitPrice());
                    materialDetail.setWeight(tMaterial.getRoughWeight());
                    materialDetail.setRfidCount(new BigDecimal(printVO.getConvertCount()));
                    materialDetail.setRfidHead(rfId);
                    materialDetail.setAdvanceRegistrationId(printVO.getAdvanceRegistrationId());
                    materialDetail.setStatus(Constants.MATERIAL_DETAIL_STATUS_ADD);
                    tMaterialDetailMapper.insert(materialDetail);

                    //redis存放
                    TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                    tMaterialDetailVO.setBatchCode(deliveryDetail.getBatchCode());
                    tMaterialDetailVO.setMaterialId(deliveryDetail.getMaterialId());
                    tMaterialDetailVO.setMaterialName(tMaterial.getName());
                    tMaterialDetailVO.setRfid(materialDetail.getRfid());
                    tMaterialDetailVO.setCount(materialDetail.getRfidCount());
                    tMaterialDetailVO.setRfids(new ArrayList<>());
                    redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);

                    ZplPrint zplPrint = new ZplPrint(tMaterial.getDescription(), tMaterial.getCode(), rfId, deliveryDetail.getBatchCode(), tUnit.getName());
                    this.printMaterialDetail(zplPrint,print.getPrintFloor());
                }
            }else {
                // 打印配置存在一个RFID标签不满足转换数量的 则最后一个RFID标签对应剩余的数量
                BigDecimal materialCount = printVO.getMaterialCount();

                for (Integer i = 0; i < printVO.getSumCount(); i++) {

                    TMaterialDetail materialDetail = new TMaterialDetail();
                    materialDetail.setCode(codeConfigService.getCode(CodeEnum.IWLD.getCodeName()));
                    materialDetail.setMaterialId(deliveryDetail.getMaterialId());
                    materialDetail.setBatchCode(deliveryDetail.getBatchCode());
                    materialDetail.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_SUCESS);

                    // 生成RFID
                    String rfId = codeConfigService.getRfIdCode(tMaterial.getCode(),
                            // 生成RFID的批次号 单据批次号存在重复 不可使用
                            codeConfigService.getBatchCode1(deliveryDetail.getMaterialId(), DateUtils.getNowDate()));
                    rfidHeads.add(rfId);

                    materialDetail.setRfid(rfId);
                    materialDetail.setMaterialCode(tMaterial.getCode());
                    materialDetail.setMaterialName(tMaterial.getName());
                    materialDetail.setPrice(tMaterial.getUnitPrice());
                    materialDetail.setWeight(tMaterial.getRoughWeight());

                    if (materialCount.compareTo(new BigDecimal(printVO.getConvertCount())) == -1){
                        materialDetail.setRfidCount(materialCount);
                    }else {
                        materialDetail.setRfidCount(new BigDecimal(printVO.getConvertCount()));
                    }

                    materialDetail.setRfidHead(rfId);
                    materialDetail.setAdvanceRegistrationId(printVO.getAdvanceRegistrationId());
                    materialDetail.setStatus(Constants.MATERIAL_DETAIL_STATUS_ADD);
                    tMaterialDetailMapper.insert(materialDetail);

                    //redis存放
                    TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                    tMaterialDetailVO.setBatchCode(deliveryDetail.getBatchCode());
                    tMaterialDetailVO.setMaterialId(deliveryDetail.getMaterialId());
                    tMaterialDetailVO.setMaterialName(tMaterial.getName());
                    tMaterialDetailVO.setRfid(materialDetail.getRfid());
                    tMaterialDetailVO.setCount(materialDetail.getRfidCount());
                    tMaterialDetailVO.setRfids(new ArrayList<>());
                    redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);

                    if (new BigDecimal(printVO.getConvertCount()).compareTo(materialCount) == -1){
                        materialCount = materialCount.subtract(new BigDecimal(printVO.getConvertCount()));
                    }

                    ZplPrint zplPrint = new ZplPrint(tMaterial.getDescription(), tMaterial.getCode(), rfId, deliveryDetail.getBatchCode(), tUnit.getName());
                    this.printMaterialDetail(zplPrint,print.getPrintFloor());
                }
            }

            materialDePrintDTO.setPrintCount(1);
            materialDePrintDTO.setRfidHeads(rfidHeads.toString());
            materialDePrintDTO.setPrintTime(DateUtils.getNowDate());
        } else {
            //如果已打印过，直接查找数据直接打印
            String rfidHeads = printVO.getRfidHeads();
            String[] split = rfidHeads.split(",");
            List<TMaterialDetailVO> materialDeList = tMaterialDetailMapper.selectMDetailByRfidHeadIds(split);
            for (TMaterialDetailVO detail : materialDeList) {
                ZplPrint zplPrint = new ZplPrint(detail.getDescription(), detail.getMaterialCode(), detail.getRfidHead(), detail.getBatchCode(), detail.getUnitName());
                this.printMaterialDetail(zplPrint,print.getPrintFloor());
            }
            materialDePrintDTO.setPrintCount(printVO.getPrintCount() + 1);
            materialDePrintDTO.setPrintTime(DateUtils.getNowDate());
        }

        // 打印类型  1 RFID打印、2普通标签打印
        materialDePrintDTO.setPrintType("1");
        tMaterialDetailPrintMapper.updateById(materialDePrintDTO);
        return AjaxResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult printErCode(TMaterialDetailPrint print) {
        //查询此打印设置信息
        TMaterialDetailPrint printVO = tMaterialDetailPrintMapper.selectById(print.getId());
        if (printVO == null) {
            return AjaxResult.error("未查询到信息");
        }
        TMaterialDetailPrint materialDePrintDTO = new TMaterialDetailPrint();
        materialDePrintDTO.setId(printVO.getId());
        materialDePrintDTO.setPrintTime(DateUtils.getNowDate());
        Integer printCount = printVO.getPrintCount();
        //查询此单据相关关联的物料详情列表

        // 查询入库详情
        TAdvanceDeliveryDetail deliveryDetail = tAdvanceDeliveryDetailMapper.selectById(printVO.getAdvanceRegistrationId());

        if ((printCount == null || printCount == 0) && StringUtils.isNull(printVO.getRfidHeads())) {
//            //如果没有打印过
//            List<TMaterialDetailVO> materialDeList = tMaterialDetailMapper.selectRfIdInfoByDetailId(printVO.getAdvanceRegistrationId());
//            if (CollectionUtils.isEmpty(materialDeList)) {
//                return AjaxResult.error("未查询到物料信息");
//            }
//            Integer sum = printVO.getSumCount() * printVO.getConvertCount();
//            if (sum > materialDeList.size()) {
//                return AjaxResult.error("打印总数超出剩余可打印数量");
//            }
//            StringJoiner rfidHeads = new StringJoiner(",");
//            Integer count = printVO.getSumCount();
//            for (int i = 0; i < count; i++) {
//                Integer pageIndex = i * printVO.getConvertCount();
//                List<TMaterialDetailVO> details = materialDeList.subList(pageIndex, pageIndex + printVO.getConvertCount());
//                if (CollectionUtils.isNotEmpty(details)) {
//                    List<Long> detailIds = details.stream().map(TMaterialDetailVO::getId).collect(Collectors.toList());
//                    TMaterialDetailVO detail = details.get(0);
//                    String rfidHead = detail.getRfid();
//                    //查询检测通过的数量
//                    List<TMaterialDetailVO> suscessList = details.stream().filter(e -> !Constants.MATERIAL_DETAIL_CHECK_FAIL.equals(e.getDetectionFailStatus())).collect(Collectors.toList());
//                    if (CollectionUtils.isNotEmpty(suscessList)) {
//                        List<String> rfids = suscessList.stream().map(TMaterialDetailVO::getRfid).collect(Collectors.toList());
//                        //redis存放
//                        TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
//                        tMaterialDetailVO.setBatchCode(detail.getBatchCode());
//                        tMaterialDetailVO.setMaterialId(detail.getMaterialId());
//                        tMaterialDetailVO.setMaterialName(detail.getMaterialName());
//                        tMaterialDetailVO.setRfid(rfidHead);
//                        tMaterialDetailVO.setCount(rfids.size());
//                        tMaterialDetailVO.setRfids(rfids);
//                        redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);
//                    }
//                    tMaterialDetailMapper.update(new TMaterialDetail(),
//                            new UpdateWrapper<TMaterialDetail>()
//                                    .eq("del_flag", Constants.DEL_FLAG_NO)
//                                    .in("id", detailIds)
//                                    .set("rfid_head", rfidHead));
//                    rfidHeads.add(rfidHead);
////                    ZplPrint zplPrint = new ZplPrint(detail.getDescription(), detail.getMaterialCode(), rfidHead, detail.getBatchCode(), detail.getUnitName());
////                    this.printMaterialDetail(zplPrint);
//                }
//            }

            // 添加物料详情及RFID
            // 根据打印配置计算物料详情每个RFID标签对应的物料数量
            int materialSum = printVO.getSumCount() * printVO.getConvertCount();

            TMaterial tMaterial = tMaterialMapper.selectById(deliveryDetail.getMaterialId());

            StringJoiner rfidHeads = new StringJoiner(",");

            // 打印配置刚好整除
            if (new BigDecimal(materialSum).compareTo(printVO.getMaterialCount()) == 0){

                // 生成物料详情及redis相关信息
                for (Integer i = 0; i < printVO.getSumCount(); i++) {
                    TMaterialDetail materialDetail = new TMaterialDetail();
                    materialDetail.setCode(codeConfigService.getCode(CodeEnum.IWLD.getCodeName()));
                    materialDetail.setMaterialId(deliveryDetail.getMaterialId());
                    materialDetail.setBatchCode(deliveryDetail.getBatchCode());
                    materialDetail.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_SUCESS);

                    // 生成RFID
                    String rfId = codeConfigService.getRfIdCode(tMaterial.getCode(),
                            // 生成RFID的批次号 单据批次号存在重复 不可使用
                            codeConfigService.getBatchCode1(deliveryDetail.getMaterialId(), DateUtils.getNowDate()));
                    rfidHeads.add(rfId);

                    materialDetail.setRfid(rfId);
                    materialDetail.setMaterialCode(tMaterial.getCode());
                    materialDetail.setMaterialName(tMaterial.getName());
                    materialDetail.setPrice(tMaterial.getUnitPrice());
                    materialDetail.setWeight(tMaterial.getRoughWeight());
                    materialDetail.setRfidCount(new BigDecimal(printVO.getConvertCount()));
                    materialDetail.setRfidHead(rfId);
                    materialDetail.setAdvanceRegistrationId(printVO.getAdvanceRegistrationId());
                    materialDetail.setStatus(Constants.MATERIAL_DETAIL_STATUS_ADD);
                    tMaterialDetailMapper.insert(materialDetail);

                    //redis存放
                    TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                    tMaterialDetailVO.setBatchCode(deliveryDetail.getBatchCode());
                    tMaterialDetailVO.setMaterialId(deliveryDetail.getMaterialId());
                    tMaterialDetailVO.setMaterialName(tMaterial.getName());
                    tMaterialDetailVO.setRfid(materialDetail.getRfid());
                    tMaterialDetailVO.setCount(materialDetail.getRfidCount());
                    tMaterialDetailVO.setRfids(new ArrayList<>());
                    redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);

//                    ZplPrint zplPrint = new ZplPrint(tMaterial.getDescription(), tMaterial.getCode(), rfId, deliveryDetail.getBatchCode(), tUnit.getName());
//                    this.printMaterialDetail(zplPrint,print.getPrintFloor());
                }
            }else {
                // 打印配置存在一个RFID标签不满足转换数量的 则最后一个RFID标签对应剩余的数量
                BigDecimal materialCount = printVO.getMaterialCount();

                for (Integer i = 0; i < printVO.getSumCount(); i++) {

                    TMaterialDetail materialDetail = new TMaterialDetail();
                    materialDetail.setCode(codeConfigService.getCode(CodeEnum.IWLD.getCodeName()));
                    materialDetail.setMaterialId(deliveryDetail.getMaterialId());
                    materialDetail.setBatchCode(deliveryDetail.getBatchCode());
                    materialDetail.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_SUCESS);

                    // 生成RFID
                    String rfId = codeConfigService.getRfIdCode(tMaterial.getCode(),
                            // 生成RFID的批次号 单据批次号存在重复 不可使用
                            codeConfigService.getBatchCode1(deliveryDetail.getMaterialId(), DateUtils.getNowDate()));
                    rfidHeads.add(rfId);

                    materialDetail.setRfid(rfId);
                    materialDetail.setMaterialCode(tMaterial.getCode());
                    materialDetail.setMaterialName(tMaterial.getName());
                    materialDetail.setPrice(tMaterial.getUnitPrice());
                    materialDetail.setWeight(tMaterial.getRoughWeight());

                    if (materialCount.compareTo(new BigDecimal(printVO.getConvertCount())) == -1){
                        materialDetail.setRfidCount(materialCount);
                    }else {
                        materialDetail.setRfidCount(new BigDecimal(printVO.getConvertCount()));
                    }

                    materialDetail.setRfidHead(rfId);
                    materialDetail.setAdvanceRegistrationId(printVO.getAdvanceRegistrationId());
                    materialDetail.setStatus(Constants.MATERIAL_DETAIL_STATUS_ADD);
                    tMaterialDetailMapper.insert(materialDetail);

                    //redis存放
                    TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                    tMaterialDetailVO.setBatchCode(deliveryDetail.getBatchCode());
                    tMaterialDetailVO.setMaterialId(deliveryDetail.getMaterialId());
                    tMaterialDetailVO.setMaterialName(tMaterial.getName());
                    tMaterialDetailVO.setRfid(materialDetail.getRfid());
                    tMaterialDetailVO.setCount(materialDetail.getRfidCount());
                    tMaterialDetailVO.setRfids(new ArrayList<>());
                    redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);

//                    ZplPrint zplPrint = new ZplPrint(tMaterial.getDescription(), tMaterial.getCode(), rfId, deliveryDetail.getBatchCode(), tUnit.getName());
//                    this.printMaterialDetail(zplPrint,print.getPrintFloor());

                    if (new BigDecimal(printVO.getConvertCount()).compareTo(materialCount) == -1){
                        materialCount = materialCount.subtract(new BigDecimal(printVO.getConvertCount()));
                    }

                }
            }

            materialDePrintDTO.setPrintCount(1);
            materialDePrintDTO.setRfidHeads(rfidHeads.toString());
            materialDePrintDTO.setPrintTime(DateUtils.getNowDate());
        } else {
            //如果已打印过，直接查找数据直接打印
            String rfidHeads = printVO.getRfidHeads();
            String[] split = rfidHeads.split(",");
            List<TMaterialDetailVO> materialDeList = tMaterialDetailMapper.selectMDetailByRfidHeadIds(split);
//            for (TMaterialDetailVO detail : materialDeList) {
//                ZplPrint zplPrint = new ZplPrint(detail.getDescription(), detail.getMaterialCode(), detail.getRfidHead(), detail.getBatchCode(), detail.getUnitName());
//                this.printMaterialDetail(zplPrint);
//            }
            materialDePrintDTO.setPrintCount(printVO.getPrintCount() + 1);
            materialDePrintDTO.setPrintTime(DateUtils.getNowDate());
        }
        // 打印类型  1 RFID打印、2普通标签打印
        materialDePrintDTO.setPrintType("2");
        tMaterialDetailPrintMapper.updateById(materialDePrintDTO);
        return AjaxResult.success();
    }

    /**
     * 打印
     *
     * @param zplPrint
     */
    public void printMaterialDetail(ZplPrint zplPrint,String printFloor) {
        //打印机ip
        String printIp = "";
        // 一楼
        if (printFloor.equals("1")){
            printIp = Convert.toStr(redisService.getCacheObject(Constants.PRINT_IP_ONE_FLOOR), "");
        }
        //二楼
        if (printFloor.equals("2")){
            printIp = Convert.toStr(redisService.getCacheObject(Constants.PRINT_IP_TWO_FLOOR), "");
        }

        //打印机端口
        String printPort = Convert.toStr(redisService.getCacheObject(Constants.PRINT_PORT), "");
        try {
            ZplUtils.zplPrint(printIp, printPort, zplPrint);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("打印失败:" + e.getMessage());
        }
    }

    /**
     * 根据入库单据id删除打印信息
     * @param advanceRegistrationId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult deleteByDeliveryId(Long advanceRegistrationId) {
        TAdvanceDeliveryDetail tAdvanceDeliveryDetail = tAdvanceDeliveryDetailMapper.selectById(advanceRegistrationId);
        if (tAdvanceDeliveryDetail == null) {
            return AjaxResult.error("未查询到入库单详情");
        }
        if (tAdvanceDeliveryDetail.getPutawayCount().compareTo(BigDecimal.ZERO) == 1) {
            return AjaxResult.error("已上架不可删除");
        }

        //删除物料详情以及redis值
        QueryWrapper<TMaterialDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        detailWrapper.eq("advance_registration_id", advanceRegistrationId);
        List<TMaterialDetail> tMaterialDetails = tMaterialDetailMapper.selectList(detailWrapper);
        if (CollectionUtils.isNotEmpty(tMaterialDetails)) {
            List<String> rfidHeads = tMaterialDetails.stream().filter(e -> StringUtils.isNotEmpty(e.getRfidHead())).map(TMaterialDetail::getRfidHead).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(rfidHeads)) {
                for (String rfidHead : rfidHeads) {
                    redisService.deleteObject("wms:materialDetail:" + rfidHead);
                }
            }
        }
        tMaterialDetailMapper.update(new TMaterialDetail(),
                new UpdateWrapper<TMaterialDetail>().eq("advance_registration_id",advanceRegistrationId)
                        .isNotNull("rfid_head").set("del_flag",Constants.DEL_FLAG_YES));
        //删除打印配置
        tMaterialDetailPrintMapper.deletePrintByRegisterId(advanceRegistrationId);
        return AjaxResult.success();
    }


}
