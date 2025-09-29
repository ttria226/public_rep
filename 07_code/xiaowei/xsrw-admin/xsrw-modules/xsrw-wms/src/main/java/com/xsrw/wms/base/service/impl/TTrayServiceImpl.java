package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import com.xsrw.wms.base.domain.vo.ExcelTrayVO;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.inout.strategy.RecommendedLocationUtil;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import com.xsrw.wms.web.util.AgvReportUtil;
import com.xsrw.wms.web.util.WcsMoveUtil;
import com.xsrw.wms.web.util.WcsReportUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 载具管理Service业务层处理
 *
 * @author lyx
 * @date 2023-05-05
 */
@Service
public class TTrayServiceImpl extends ServiceImpl<TTrayMapper, TTray> implements ITTrayService {
    @Autowired
    private TTrayMapper tTrayMapper;
    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;
    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private TTaskWcsDetailMapper tTaskWcsDetailMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;
    @Autowired
    private ITStockDetailService stockDetailService;
    @Autowired
    private ITCodeConfigService itCodeConfigService;
    @Autowired
    private WcsReportUtil wcsReportUtil;
    @Autowired
    private AgvReportUtil agvReportUtil;
    @Autowired
    private WcsMoveUtil wcsMoveUtil;
    @Autowired
    private RecommendedLocationUtil recommendedLocationUtil;
    @Autowired
    private TTaskDetailMapper tTaskDetailMapper;

    @Autowired
    private TTaskMapper tTaskMapper;

    /**
     * 查询载具管理列表
     *
     * @param entity 载具管理
     * @return 载具管理
     */
    @Override
    public List<TTrayApiVO> selectTTrayList(TTrayDTO entity) {
        return tTrayMapper.selectTTrayInfoList(entity);
    }

    /**
     * 获取载具详情列表
     *
     * @param tTray
     * @return
     */
    @Override
    public List<TTrayApiVO> selectTTrayInfoList(TTrayDTO tTray) {
        List<TTrayApiVO> list = tTrayMapper.selectTTrayInfoList(tTray);
        return list;
    }

    /**
     * 查询载具管理
     *
     * @param id 载具管理主键
     * @return 载具管理
     */
    @Override
    public TTray selectTTrayById(Long id) {
        return tTrayMapper.selectById(id);
    }

    /**
     * 新增载具管理
     *
     * @param entity 载具管理
     * @return 结果
     */
    @Override
    public AjaxResult insertTTray(TTray entity) {
        // 托盘
        if (Constants.TRAY_CATEGORY_TRAY.equals(entity.getTrayCategory())) {
            entity.setCode(entity.getCode() + "T");
        }
        // 料箱
        if (Constants.TRAY_CATEGORY_WORKBIN.equals(entity.getTrayCategory())) {
            entity.setCode(entity.getCode() + "L");
        }
        // 货笼
        if (Constants.TRAY_CATEGORY_CAGE.equals(entity.getTrayCategory())) {
            entity.setCode(entity.getCode() + "H");
        }
        // 设置默认是否打印条码（否）
        entity.setLabelTemplateType(Constants.LOCATION_LABELTEMPLATETYPE_NO);
        entity.setCode(itCodeConfigService.getCode(CodeEnum.MTP.getCodeName()));
        return AjaxResult.success(tTrayMapper.insert(entity));
    }

    /**
     * 修改载具管理
     *
     * @param entity 载具管理
     * @return 结果
     */
    @Override
    public AjaxResult updateTTray(TTray entity) {
        return AjaxResult.success(tTrayMapper.updateById(entity));
    }


    /**
     * 批量删除载具管理
     *
     * @param ids 需要删除的载具管理主键
     * @return 结果
     */
    @Override
    public int deleteTTrayByIds(Long[] ids) {
        return tTrayMapper.deleteTTrayByIds(ids);
    }

    /**
     * 删除载具管理信息
     *
     * @param id 载具管理主键
     * @return 结果
     */
    @Override
    public int deleteTTrayById(Long id) {
        return tTrayMapper.deleteTTrayById(id);
    }

    /**
     * 导入载具管理
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        if (file == null) {
            return AjaxResult.error("文件不可为空");
        }

        // 文件名称
        String fileName = file.getOriginalFilename();
        // 校验文件格式
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        if (fileType.contains("xlsx") || fileType.contains("xls")) {
            InputStream inputStream = file.getInputStream();

            // 转换Excel数据
            ExcelUtil<ExcelTrayVO> util = new ExcelUtil<ExcelTrayVO>(ExcelTrayVO.class);
            List<ExcelTrayVO> trayList = util.importExcel(inputStream);
            if (trayList.size() > 0) {
                int notNullCount = 0;
                int count = 0;
                //校验导入字段是否为空
                for (int i = 0; i < trayList.size(); i++) {
                    notNullCount = notNullCount + 1;
                    ExcelTrayVO excelTrayVO = trayList.get(i);
                    // 校验类型
                    if (StringUtils.isEmpty(excelTrayVO.getTrayCategory())) {
                        return AjaxResult.error("第:" + notNullCount + "条载具类型不可为空");
                    }
                    // 校验托盘状态
                    if (StringUtils.isEmpty(excelTrayVO.getStatus())) {
                        return AjaxResult.error("第:" + notNullCount + "条载具状态不可为空");
                    }
                }

                //校验导入信息是否正确
                for (int i = 0; i < trayList.size(); i++) {
                    ExcelTrayVO excelTrayVO = trayList.get(i);
                    count = count + 1;
                    // 载具类型
                    if (!Constants.TRAY_CATEGORY_TRAY.equals(excelTrayVO.getTrayCategory())
                            && !Constants.TRAY_CATEGORY_WORKBIN.equals(excelTrayVO.getTrayCategory())
                            && !Constants.TRAY_CATEGORY_CAGE.equals(excelTrayVO.getTrayCategory())) {
                        return AjaxResult.error("第:" + count + "条载具类型不存在,请检查载具类型信息");
                    }
                    // 载具状态
                    if (!Constants.TRAY_STATUS_LEISURE.equals(excelTrayVO.getStatus())
                            && !Constants.TRAY_STATUS_HALF.equals(excelTrayVO.getStatus())
                            && !Constants.TRAY_STATUS_FULL.equals(excelTrayVO.getStatus())) {
                        return AjaxResult.error("第:" + count + "条是载具状态不存在,请检查载具状态(空闲/半托/满托)");
                    }
                    //批量导入数据
                    TTray tray = new TTray();
                    //设置创建类型
                    excelTrayVO.setType(Constants.TRAY_CATEGORY_SYSTEM);
                    BeanUtils.copyProperties(excelTrayVO, tray);
                    //设置编码
                    String code = itCodeConfigService.getCode("MTP");
                    if (StringUtils.isEmpty(code)) {
                        throw new ServiceException("编号生成失败");
                    }
                    // 托盘
                    if (Constants.TRAY_CATEGORY_TRAY.equals(excelTrayVO.getTrayCategory())) {
                        code = code + "T";
                    }
                    // 料箱
                    if (Constants.TRAY_CATEGORY_WORKBIN.equals(excelTrayVO.getTrayCategory())) {
                        code = code + "L";
                    }
                    // 货笼
                    if (Constants.TRAY_CATEGORY_CAGE.equals(excelTrayVO.getTrayCategory())) {
                        code = code + "H";
                    }
                    tray.setCode(code);
                    SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
                    if (sysUser.getDeptId() != null && sysUser.getDeptName() != null) {
                        tray.setDeptId(sysUser.getDeptId());
                        tray.setDeptName(sysUser.getDeptName());
                    } else {
                        throw new ServiceException("获取组织信息失败");
                    }
                    //设置默认否打印标签
                    tray.setLabelTemplateType(Constants.LOCATION_LABELTEMPLATETYPE_NO);
                    try {
                        tTrayMapper.insert(tray);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:" + count + "条数据出现错误请检查信息:" + tray);
                    }
                }

            } else {
                return AjaxResult.error("数据不可为空");
            }
        } else {
            return AjaxResult.error("文件格式错误");
        }
        return AjaxResult.success();
    }

    /**
     * 查询条码所对应的托盘信息
     *
     * @param tTray
     * @return
     */
    @Override
    public List<TTray> selectListBylabelTemplateId(TTray tTray) {
//        return tTrayMapper.selectList(Wrappers.lambdaQuery(TTray.class)
//                .eq(TTray::getLabelTemplateId, labelTemplateId)
//                .eq(TTray::getDelFlag, Constants.NO)
//                .orderByDesc(TTray::getCreateTime));
        LambdaQueryWrapper<TTray> queryWrapper = Wrappers.lambdaQuery(TTray.class)
                .eq(TTray::getDelFlag, Constants.NO)
                .orderByDesc(TTray::getCreateTime);
        if (StringUtils.isNotEmpty(tTray.getCode())) {
            queryWrapper.like(TTray::getCode, tTray.getCode());
        }
        if (StringUtils.isNotEmpty(tTray.getTrayCategory())) {
            queryWrapper.like(TTray::getTrayCategory, tTray.getTrayCategory());
        }
        if (StringUtils.isNotEmpty(tTray.getStatus())) {
            queryWrapper.like(TTray::getStatus, tTray.getStatus());
        }
        List<TTray> data = tTrayMapper.selectList(queryWrapper);
        return data;
    }

    /**
     * 根据载具id获取对应编码
     *
     * @param trays
     * @return
     */
    @Override
    public Map<Long, String> getTrayCodeByIds(List<Long> trays) {
        Map<Long, String> resMap = new HashMap<>();
        QueryWrapper<TTray> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("id", trays);
        List<TTray> tTrayList = tTrayMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(tTrayList)) {
            resMap = tTrayList.stream().collect(Collectors.toMap(TTray::getId, TTray::getCode));
        }
        return resMap;
    }

    /**
     * 根据载具id获取对应载具信息
     *
     * @param trays
     * @return
     */
    @Override
    public Map<Long, TTray> getTrayByIds(List<Long> trays) {
        Map<Long, TTray> resMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(trays)) {
            QueryWrapper<TTray> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
            queryWrapper.in("id", trays);
            List<TTray> tTrayList = tTrayMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(tTrayList)) {
                List<String> codes = tTrayList.stream().map(TTray::getCode).collect(Collectors.toList());
                List<TLocation> locations = this.getLocationsByCodes(codes);
                if (locations != null) {
                    Map<String, Long> locationIdMap = locations.stream().collect(Collectors.toMap(TLocation::getPalletNum, TLocation::getId, (key1, key2) -> key1));
                    tTrayList.forEach(e -> {
                        e.setLocationId(locationIdMap.get(e.getCode()));
                    });
                }
                resMap = tTrayList.stream().collect(Collectors.toMap(TTray::getId, Function.identity()));
            }
        }
        return resMap;
    }

    /**
     * 根据编号查询托盘信息
     *
     * @param trayCode
     * @return
     */
    @Override
    public TTray selectTTrayByCode(String trayCode) {
        QueryWrapper<TTray> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("code", trayCode);
        return tTrayMapper.selectOne(queryWrapper);
    }

    /**
     * 托盘出库
     *
     * @param id
     * @return
     */
    @Override
    public AjaxResult outStockByTrayId(Long id) {
        TTray tTrayVO = tTrayMapper.selectById(id);
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        if (tTrayVO.getLocationId() != null) {
            TLocation tLocation = tLocationMapper.selectById(tTrayVO.getLocationId());
            //半托全托空闲可出
            if (tLocation != null && (Constants.LOCATION_GOODS_ALLOCATION_STATUS_3.equals(tLocation.getGoodsAllocationStatus())
                    || !Constants.LOCATION_GOODS_ALLOCATION_STATUS_4.equals(tLocation.getGoodsAllocationStatus()))) {
                return AjaxResult.error("当前状态不可出库");
            }
        }
        TTray tTray = new TTray();
        tTray.setId(id);
        tTray.setStatus(Constants.TRAY_STATUS_LEISURE);
        return AjaxResult.success(tTrayMapper.updateById(tTray));
    }

    @Override
    public List<Map<String, Object>> getTrayListByTaskId(Long taskId) {
        return tTrayMapper.getTrayListByTaskId(taskId);
    }

    @Override
    @Transactional
    public AjaxResult takeOut(TTray tTray) {
//        托盘取出：1.task_wcs记录
//        2.库位location   pallet_num托盘编号 为空    goods_allocation_status货位状态(1,无货,2,有货,3,标记出库,4,标记入库)  变为无货
        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        if (StringUtils.isEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("托盘未在库");
        }
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
        }
        if (tTrayVO.getLocationId() == null) {
            return AjaxResult.error("未查询到托盘库位信息");
        }
        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_OUT);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记出库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_3));
            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                String endStation = WcsReportUtil.stationOut;
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsOutReport(orderDTO);
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getCode();
                String endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                tTaskWcsMapper.updateStuasById(taskWcs.getId(), status);
            }
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum("");
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }


    @Override
    @Transactional
    public AjaxResult takeOutCheck(TTrayDTO tTray) {

        if (tTray.getTaskDetailId() == null) {
            return AjaxResult.error("盘点任务详情id不可为空");
        }

        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        if (StringUtils.isEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("托盘未在库");
        }

        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
        }
        if (tTrayVO.getLocationId() == null) {
            return AjaxResult.error("未查询到托盘库位信息");
        }
        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_OUT);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记出库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_3));
            //更新wcs任务id到盘点任务详情
            tTaskDetailMapper.update(new TTaskDetail(), new UpdateWrapper<TTaskDetail>()
                    .eq("id", tTray.getTaskDetailId())
                    .set("wcs_id", taskWcs.getId()));

            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                String endStation = WcsReportUtil.stationOut;
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsOutReport(orderDTO);
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getCode();
                String endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                tTaskWcsMapper.updateStuasById(taskWcs.getId(), status);
            }
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum("");
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }


    @Override
    @Transactional
    public AjaxResult takeOutCheckNew(TTrayDTO tTray) {

        if ( tTray.getTaskId() == null) {
            return AjaxResult.error("盘点任务id不可为空");
        }

        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        if (StringUtils.isEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("托盘未在库");
        }

        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
        }
        if (tTrayVO.getLocationId() == null) {
            return AjaxResult.error("未查询到托盘库位信息");
        }
        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_OUT);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记出库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_3));
            /* //更新wcs任务id到盘点任务详情
            tTaskDetailMapper.update(new TTaskDetail(), new UpdateWrapper<TTaskDetail>()
                    .eq("id", tTray.getTaskDetailId())
                    .set("wcs_id", taskWcs.getId()));*/

            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                String endStation = WcsReportUtil.stationOut;
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsOutReport(orderDTO);
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getCode();
                String endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                tTaskWcsMapper.updateStuasById(taskWcs.getId(), status);
            }
            //手动盘点载具出库 更新盘点任务状态
            TTask task = new TTask();
            task.setId(tTray.getTaskId());
            task.setActivateStatus("1");
            task.setTaskStatus("1");
            tTaskMapper.updateById(task);
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum("");
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }



    /**
     * 托盘回库
     *
     * @param tTray
     * @return
     */
    @Override
    @Transactional
    public AjaxResult recycle(TTray tTray) {
//        托盘回库：1.task_wcs记录
//        2.如果tray中有location值则回到原始位置，如果没有没有推荐库位 填充
//        3.库位location   pallet_num托盘编号 为有值    goods_allocation_status货位状态(1,无货,2,有货,3,标记出库,4,标记入库)  根据托盘是否有货  填充是否有货

        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
        }
        if (StringUtils.isNotEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("当前托盘不可执行回库操作");
        }
        if (tTrayVO.getLocationId() == null) {
            TLocation locationInfoById = tTrayMapper.getLocationById(tTrayVO.getId());
            if (locationInfoById == null) {
                Long locationId = recommendedLocationUtil.recommendedLocation(null, tTrayVO.getId(), null, null);
                if (locationId == null) {
                    return AjaxResult.error("未获取到可用库位");
                } else {
                    tTrayVO.setLocationId(locationId);
                    //推荐库位时，不推荐地堆库位
                    tTrayVO.setLocationType(Constants.LOCATION_TYPE_DEFAULT);
                }
            } else {
                tTrayVO.setLocationId(locationInfoById.getId());
                tTrayVO.setLocationType(locationInfoById.getLocationType());
            }
        }
        TTray taryDTO = new TTray();
        taryDTO.setId(tTrayVO.getId());
        taryDTO.setLocationId(tTrayVO.getLocationId());
        tTrayMapper.updateById(taryDTO);
        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_BACK);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记入库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_4));
            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = WcsReportUtil.stationIn;
                String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsInReport(orderDTO);
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
//                //发送命令
//                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
//                String startStation = Constants.SHELF_POINT_SECOND_LINE_IN;
//                String endStation = locationInfo.getCode();
//                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
//                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
//                tTaskWcsMapper.updateStuasById(taskWcs.getId(),status);
            }
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum(tTrayVO.getCode());
            //根据载具去查stock表查询是否有货
            QueryWrapper<TStock> stockQw = new QueryWrapper<>();
            stockQw.eq("del_flag", Constants.DEL_FLAG_NO);
            stockQw.eq("tray_id", tTrayVO.getId());
            Long trayStockCount = tStockMapper.selectCount(stockQw);
            if (trayStockCount != null && trayStockCount > 0) {//有货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            } else {//无货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }

    /**
     * 出库任务--托盘强制回库
     *
     * @param tTray
     * @return
     */
    @Override
    @Transactional
    public AjaxResult recycleOut(TTray tTray) {
//        托盘回库：1.task_wcs记录
//        2.如果tray中有location值则回到原始位置，如果没有没有推荐库位 填充
//        3.库位location   pallet_num托盘编号 为有值    goods_allocation_status货位状态(1,无货,2,有货,3,标记出库,4,标记入库)  根据托盘是否有货  填充是否有货
        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());

        /** 取消了任务完成的校验，此为出库的强制回库与普通回库的区别 **/
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
//        if (taskCount > 0) {
//            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
//        }
        if (StringUtils.isNotEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("当前托盘不可执行回库操作");
        }
        if (tTrayVO.getLocationId() == null) {
            TLocation locationInfoById = tTrayMapper.getLocationById(tTrayVO.getId());
            if (locationInfoById == null) {
                Long locationId = recommendedLocationUtil.recommendedLocation(null, tTrayVO.getId(), null, null);
                if (locationId == null) {
                    return AjaxResult.error("未获取到可用库位");
                } else {
                    tTrayVO.setLocationId(locationId);
                    //推荐库位时，不推荐地堆库位
                    tTrayVO.setLocationType(Constants.LOCATION_TYPE_DEFAULT);
                }
            } else {
                tTrayVO.setLocationId(locationInfoById.getId());
                tTrayVO.setLocationType(locationInfoById.getLocationType());
            }
        }
        TTray taryDTO = new TTray();
        taryDTO.setId(tTrayVO.getId());
        taryDTO.setLocationId(tTrayVO.getLocationId());
        tTrayMapper.updateById(taryDTO);
        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_BACK);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记入库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_4));
            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = WcsReportUtil.stationIn;
                String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsInReport(orderDTO);
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
//                //发送命令
//                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
//                String startStation = Constants.SHELF_POINT_SECOND_LINE_IN;
//                String endStation = locationInfo.getCode();
//                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
//                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
//                tTaskWcsMapper.updateStuasById(taskWcs.getId(),status);
            }
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum(tTrayVO.getCode());
            //根据载具去查stock表查询是否有货
            QueryWrapper<TStock> stockQw = new QueryWrapper<>();
            stockQw.eq("del_flag", Constants.DEL_FLAG_NO);
            stockQw.eq("tray_id", tTrayVO.getId());
            Long trayStockCount = tStockMapper.selectCount(stockQw);
            if (trayStockCount != null && trayStockCount > 0) {//有货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            } else {//无货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }


    /**
     * 载具出库/回库回调
     *
     * @param wcsVO
     */
    @Transactional
    public int completeTrayBack(TTaskWcsVO wcsVO) {

        TTaskWcs byId = tTaskWcsMapper.selectById(wcsVO.getId());
        if (byId == null) {
            return 0;
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(byId.getTaskStatus())) {
            return 0;
        }

        TTaskWcs taskWcs = new TTaskWcs();
        taskWcs.setId(wcsVO.getId());
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        tTaskWcsMapper.updateById(taskWcs);
        if (Constants.TASK_TYPE_OUT.equals(wcsVO.getTaskType())) {
            //移库任务
            TLocation tLocation = new TLocation();
            tLocation.setId(wcsVO.getLocationId());
            tLocation.setPalletNum("");
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            tLocationMapper.updateById(tLocation);
        } else if (Constants.TASK_TYPE_BACK.equals(wcsVO.getTaskType())) {
            //回库
            TLocation tLocation = new TLocation();
            tLocation.setId(wcsVO.getLocationId());
            tLocation.setPalletNum(wcsVO.getTrayCode());
            //根据载具去查stock表查询是否有货
            QueryWrapper<TStock> stockQw = new QueryWrapper<>();
            stockQw.eq("del_flag", Constants.DEL_FLAG_NO);
            stockQw.eq("tray_id", wcsVO.getTrayId());
            List<TStock> trayStockCount = tStockMapper.selectList(stockQw);
            if (trayStockCount != null && trayStockCount.size() > 0) {//有货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
                //有库存，更新库存信息
                List<TStockDetail> detailsList = new ArrayList<>();
                trayStockCount.forEach(e -> {
                    if (!e.getLocationId().equals(wcsVO.getLocationId())) {
                        TStockDetail tStockDetail = new TStockDetail();
                        tStockDetail.setMaterialId(e.getMaterialId());
                        tStockDetail.setType(Constants.WCS_TASK_TYPE_MOVE);
                        tStockDetail.setLocationId(wcsVO.getLocationId());
                        tStockDetail.setOriginCode(wcsVO.getTaskNo());
                        tStockDetail.setOriginId(-2L);//原单标识(-1为在线拣选标识;-2直接移库标识)
                        tStockDetail.setStatus("0");
                        tStockDetail.setBatchCode(e.getBatchCode());
                        tStockDetail.setBeforeCount(e.getCount());// 操作前数量
                        tStockDetail.setCurrentCount(e.getCount());// 操作后当前数量
                        detailsList.add(tStockDetail);
                    }
                });
                //保存库存详情记录
                if (CollectionUtils.isNotEmpty(detailsList)) {
                    stockDetailService.saveBatch(detailsList);
                }
                //更新库存表
                tStockMapper.update(new TStock(),
                        new UpdateWrapper<TStock>()
                                .eq("tray_id", wcsVO.getTrayId())
                                .set("location_id", wcsVO.getLocationId()));
                //更新物料详情表库位
                tMaterialDetailMapper.update(new TMaterialDetail(),
                        new UpdateWrapper<TMaterialDetail>()
                                .eq("del_flag", Constants.DEL_FLAG_NO)
                                .eq("tray_id", wcsVO.getTrayId())
                                .eq("status", Constants.MATERIAL_DETAIL_STATUS_IN)
                                .set("location_id", wcsVO.getLocationId()));
            } else {//无货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }
            tLocationMapper.updateById(tLocation);
        }
        return 1;
    }

    /**
     * 根据载具编号查询库位信息
     *
     * @param codes
     * @return
     */
    public List<TLocation> getLocationsByCodes(List<String> codes) {
        QueryWrapper<TLocation> locationQw = new QueryWrapper<>();
        locationQw.eq("del_flag", Constants.DEL_FLAG_NO);
        locationQw.in("pallet_num", codes);
        List<TLocation> locations = tLocationMapper.selectList(locationQw);
        return locations;
    }

    /**
     * 根据载具编码获取组盘详情信息
     *
     * @param trayCode
     * @return
     */
    @Override
    public AjaxResult getDeliveryByTrayCode(String trayCode) {
        Map<String, Object> resMap = new HashMap<>();
        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(trayCode), null);
        if (tTrayVO != null) {
            resMap.put("id", tTrayVO.getId());
            resMap.put("code", tTrayVO.getCode());
            if (Constants.LOCATION_GOODS_ALLOCATION_STATUS_4.equals(tTrayVO.getGoodsAllocationStatus())) {
                resMap.put("type", Constants.WCS_TASK_TYPE_IN);
                resMap.put("data", tTaskWcsDetailMapper.getDeliveryDetailByTray(tTrayVO.getId()));
            } else {
                resMap.put("type", Constants.WCS_TASK_TYPE_OUT);
                resMap.put("data", tStockMapper.getDeliveryDetailByTray(tTrayVO.getId()));
//                List<TLocation> locations = this.getLocationsByCodes(Collections.singletonList(tTrayVO.getCode()));
//                if (CollectionUtils.isNotEmpty(locations)) {
//                    //在库，出库
//                    TLocation locationVo = locations.get(0);
//                }
            }
            return AjaxResult.success(resMap);
        }
        return AjaxResult.error("未查询到对应载具信息");
    }

    @Override
    public AjaxResult batchCreate(TTrayDTO trayDTO) {
        List<TTray> saveList = new ArrayList<>();
        //生成载具总条数
        Integer size = trayDTO.getCount();
        //托盘类型
        String trayCategory = trayDTO.getTrayCategory();
        for (int i = 0; i < size; i++) {
            TTray tray = new TTray();
            tray.setTrayCategory(trayCategory);
            // 托盘
            if (Constants.TRAY_CATEGORY_TRAY.equals(tray.getTrayCategory())) {
                tray.setCode(tray.getCode() + "T");
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tray.getTrayCategory())) {
                // 料箱
                tray.setCode(tray.getCode() + "L");
            } else if (Constants.TRAY_CATEGORY_CAGE.equals(tray.getTrayCategory())) {
                // 货笼
                tray.setCode(tray.getCode() + "H");
            }
            // 设置默认是否打印条码（否）
            tray.setLabelTemplateType(Constants.LOCATION_LABELTEMPLATETYPE_NO);
            tray.setCode(itCodeConfigService.getCode(CodeEnum.MTP.getCodeName()));
            tray.setType(Constants.TRAY_CATEGORY_CREATE);
            saveList.add(tray);
        }
        if (this.saveBatch(saveList)) {
            //执行成功返回托盘编号
            List<String> trayCodes = saveList.stream().map(TTray::getCode).collect(Collectors.toList());
            return AjaxResult.success(trayCodes);
        }
        return AjaxResult.error();
    }

    /**
     * 根据条件获取标签列表
     *
     * @param trayDTO
     * @return
     */
    @Override
    public List<String> getBatchList(TTrayDTO trayDTO) {
        return tTrayMapper.getBatchCodeList(trayDTO);
    }

    /**
     * 选择上架载具列表
     *
     * @param tTray
     * @return
     */
    @Override
    public List<TTrayApiVO> selectPutWayList(TTrayDTO tTray) {
        return tTrayMapper.selectPutWayList(tTray);
    }

    /**
     * 根据载具编号查询入库可用状态
     *
     * @param trayCode
     * @return
     */
    @Override
    public AjaxResult getStatusByCode(String trayCode) {
        trayCode = StringUtils.clearLine(trayCode);
        TTray tray = this.selectTTrayByCode(trayCode);
        if (tray == null) {
            return AjaxResult.success("无此载具");
        }
        if (Constants.TRAY_STATUS_FULL.equals(tray.getStatus())) {
            return AjaxResult.success("全托");
        }
        List<String> codes = Collections.singletonList(trayCode);
        List<TLocation> locations = this.getLocationsByCodes(codes);
        if (locations != null) {
            Map<String, Long> locationIdMap = locations.stream().collect(Collectors.toMap(TLocation::getPalletNum, TLocation::getId, (key1, key2) -> key1));
            Long location = locationIdMap.get(trayCode);
            if (location != null) {
                return AjaxResult.success("在库");
            }
        }
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.eq("task_type", Constants.TASK_TYPE_PUT);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_code", trayCode);
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
        if (taskCount > 0) {
            return AjaxResult.success("此载具已有任务");
        }
        return AjaxResult.success("");
    }

    /**
     * 载具表解除绑定库位
     * @param id
     * @return
     */
    @Override
    public AjaxResult relieveLocation(Long id) {
        TTray tray = tTrayMapper.selectById(id);
        if(tray == null){
            return AjaxResult.error("未查到对应信息");
        }
        if(tray.getLocationId() == null){
            return AjaxResult.error("未查到对应库位信息");
        }
        List<TLocation> locationList = this.getLocationsByCodes(Collections.singletonList(tray.getCode()));
        if(CollectionUtils.isNotEmpty(locationList)){
            return AjaxResult.error("载具在库不可修改");
        }
        tTrayMapper.update(new TTray(), new UpdateWrapper<TTray>().set("location_id", null).eq("id", id));
        return AjaxResult.success();
    }

}
