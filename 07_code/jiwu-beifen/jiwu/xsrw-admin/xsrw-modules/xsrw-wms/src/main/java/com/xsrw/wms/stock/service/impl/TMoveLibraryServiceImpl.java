package com.xsrw.wms.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.constant.HttpStatus;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.mapper.*;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.dto.AddTaskDTO;
import com.xsrw.wms.check.service.ITTaskService;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.stock.mapper.TMoveLibraryDetailMapper;
import com.xsrw.wms.stock.mapper.TMoveLibraryMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITMoveLibraryService;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库内移位Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TMoveLibraryServiceImpl extends ServiceImpl<TMoveLibraryMapper, TMoveLibrary> implements ITMoveLibraryService
{
    @Autowired
    private TMoveLibraryMapper tMoveLibraryMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    @Autowired
    private TReservoirMapper tReservoirMapper;

    @Autowired
    private TAreaMapper tAreaMapper;

    @Autowired
    private TLocationMapper tLocationMapper;

    @Autowired
    private TMoveLibraryDetailMapper tMoveLibraryDetailMapper;

    @Autowired
    private TStockMapper tStockMapper;

    @Autowired
    @Lazy
    private ITStockService itStockService;

    @Autowired
    @Lazy
    private ITCodeConfigService itCodeConfigService;

    @Autowired
    @Lazy
    private ITTaskService itTaskService;



    /**
     * 查询库内移位列表
     *
     * @param moveLibrary 库内移位
     * @return 库内移位
     */
    @Override
    public List<MoveLibraryVo> selectTMoveLibraryList(MoveLibraryVo moveLibrary)
    {
        // 检索条件中的物料ID列表
        List<Long> materialIds = null;

        if (StringUtils.isNotEmpty(moveLibrary.getSpecifications()) || StringUtils.isNotEmpty(moveLibrary.getMaterialName())){
            // 根据检索条件中的物料编码和物料名称模糊查询，返回物料ID列表
            List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                    .eq(StringUtils.isNotNull(moveLibrary.getSpecifications()),TMaterial::getSpecifications,moveLibrary.getSpecifications())
                    .like(StringUtils.isNotNull(moveLibrary.getMaterialName()),TMaterial::getName,moveLibrary.getMaterialName())
                    .eq(TMaterial::getDelFlag, Constants.NO));
            materialIds = tMaterials.stream().map(TMaterial::getId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(materialIds)){
                return new ArrayList<>();
            }
        }

        // 获取库内移位列表
        List<MoveLibraryVo> moveLibraryList = tMoveLibraryMapper.selectMoveLibraryList(moveLibrary,materialIds);
        for (MoveLibraryVo model : moveLibraryList){
            // 转出库位信息
            TLocation locationOut = tLocationMapper.selectById(model.getLocationOutId());
            if(ObjectUtils.isNotNull(locationOut)){
                /** 转出区域 */
                TArea tArea = tAreaMapper.selectById(locationOut.getAreaId());
                model.setAreaName(tArea.getName());
                /** 转出库区 */
                TReservoir tReservoir = tReservoirMapper.selectById(locationOut.getReservoirId());
                model.setReservoirName(tReservoir.getName());
                /** 转出库位 */
                model.setLocationOutName(locationOut.getName());
            }
            // 转入库位信息
            TLocation locationIn = tLocationMapper.selectById(model.getLocationInId());
            if(ObjectUtils.isNotNull(locationIn)){
                /** 转入库位 */
                model.setLocationInName(locationIn.getName());
            }
        }
        return moveLibraryList;
    }

    /**
     * 查询库内移位
     *
     * @param id 库内移位主键
     * @return 库内移位
     */
    @Override
    public MoveLibraryVo selectTMoveLibraryById(Long id)
    {
        MoveLibraryVo moveLibraryVo = new MoveLibraryVo();
        // 获取当前主数据
        TMoveLibrary moveLibrary = tMoveLibraryMapper.selectById(id);

        //第一个参数是：目标存储，第二个参数是：源数据
        BeanUtils.copyProperties(moveLibrary, moveLibraryVo);
        // 转出库位信息
        TLocation locationOut = tLocationMapper.selectById(moveLibraryVo.getLocationOutId());
        if(ObjectUtils.isNotNull(locationOut)){
            //转出库区
            TReservoir tReservoir = tReservoirMapper.selectById(locationOut.getReservoirId());
            moveLibraryVo.setReservoirName(tReservoir.getName());
            //转出区域
            TArea tArea = tAreaMapper.selectById(locationOut.getAreaId());
            moveLibraryVo.setAreaName(tArea.getName());
            //转出库位
            moveLibraryVo.setLocationOutName(locationOut.getName());
        }
        // 转入库位信息
        TLocation locationIn = tLocationMapper.selectById(moveLibraryVo.getLocationInId());
        if(ObjectUtils.isNotNull(locationIn)){
            /** 转入库位 */
            moveLibraryVo.setLocationInName(locationIn.getName());
        }

        List<TMoveLibraryDetail> moveLibraryDetailList = tMoveLibraryDetailMapper.selectList(new LambdaQueryWrapper<TMoveLibraryDetail>()
                .eq(TMoveLibraryDetail::getMoveLibraryCode,moveLibrary.getCode())
                .eq(TMoveLibraryDetail::getDelFlag, Constants.DEL_FLAG_NO));

        List<MoveLibraryDetailVo> moveLibraryDetailVoList = new ArrayList<>();
        for(TMoveLibraryDetail moveLibraryDetail : moveLibraryDetailList){
            MoveLibraryDetailVo moveLibraryDetailVo = new MoveLibraryDetailVo();
            //第一个参数是：目标存储，第二个参数是：源数据
            BeanUtils.copyProperties(moveLibraryDetail, moveLibraryDetailVo);

            TMaterial materiaData = tMaterialMapper.selectById(moveLibraryDetailVo.getMaterialId());
            if (materiaData != null) {
                // 物料编码
                moveLibraryDetailVo.setMaterialCode(materiaData.getCode());
                // 规格型号
                moveLibraryDetailVo.setSpecifications(materiaData.getSpecifications());
                // 物料名称
                moveLibraryDetailVo.setMaterialName(materiaData.getName());
                // 单位名称
                TUnit tUnit = tUnitMapper.selectById(materiaData.getUnitId());
                moveLibraryDetailVo.setUnitName(tUnit.getName());
            }
            // 部门编号
            moveLibraryDetailVo.setDeptName(moveLibraryDetail.getDeptName());
            moveLibraryDetailVoList.add(moveLibraryDetailVo);
        }
        moveLibraryVo.setMoveLibraryDetailVoList(moveLibraryDetailVoList);
        return moveLibraryVo;
    }

    /**
     * 新增库内移位
     *
     * @param tMoveLibrary 库内移位
     * @return 结果
     */
    @Override
    public int insertTMoveLibrary(TMoveLibrary tMoveLibrary)
    {
        return tMoveLibraryMapper.insert(tMoveLibrary);
    }

    /**
     * 修改库内移位
     *
     * @param tMoveLibrary 库内移位
     * @return 结果
     */
    @Override
    public int updateTMoveLibrary(TMoveLibrary tMoveLibrary)
    {
        return tMoveLibraryMapper.updateById(tMoveLibrary);
    }


    /**
     * 批量删除库内移位
     *
     * @param ids 需要删除的库内移位主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteTMoveLibraryByIds(Long[] ids)
    {
        // 删除明细ID列表
        List<Long> moveLibraryDetailIds = new ArrayList<>();
        // 库存id列表
        List<Long> stockIds = new ArrayList<>();
        for(Long id:ids){
            TMoveLibrary moveLibrary = tMoveLibraryMapper.selectById(id);

            // 已审核数据不能删除
            if ("1".equals(moveLibrary.getAuditorStatus())){
                return AjaxResult.error("已审核数据不允许删除");
            }

            List<TMoveLibraryDetail> moveLibraryDetailList = tMoveLibraryDetailMapper.selectList(new LambdaQueryWrapper<TMoveLibraryDetail>()
                    .eq(TMoveLibraryDetail::getMoveLibraryCode,moveLibrary.getCode())
                    .eq(TMoveLibraryDetail::getDelFlag, Constants.DEL_FLAG_NO));

            moveLibraryDetailIds.addAll(moveLibraryDetailList.stream().map(TMoveLibraryDetail::getId).collect(Collectors.toList()));
            stockIds.addAll(moveLibraryDetailList.stream().map(TMoveLibraryDetail::getStockId).collect(Collectors.toList()));
        }

        // 解冻库内移位冻结数据
        itStockService.updateFreezeByIds(stockIds,Constants.STOCK_IS_FREEZE_NO,Constants.STOCK_ORIGIN_TYPE_MOVE);

        // 删除库内移位明细
        Long[] detailIds = new Long[moveLibraryDetailIds.size()];
        moveLibraryDetailIds.toArray(detailIds);
        tMoveLibraryDetailMapper.deleteTMoveLibraryDetailByIds(detailIds);
        // 删除库内移位
        tMoveLibraryMapper.deleteTMoveLibraryByIds(ids);
        return AjaxResult.success();
    }

    /**
     * 删除库内移位信息
     *
     * @param id 库内移位主键
     * @return 结果
     */
    @Override
    public int deleteTMoveLibraryById(Long id)
    {
        return tMoveLibraryMapper.deleteTMoveLibraryById(id);
    }

    /**
     * 库内移位添加数据
     * @param stockId 库存id
     * @param locationInId 转入库位ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult shift(Long stockId, Long locationInId){

        // 库存明细ID
        TStock stock = tStockMapper.selectById(stockId);
        // 查询目标库位存放物料类别是否与当前库位一致
        TLocation locationOut = tLocationMapper.selectById(stock.getLocationId());
        TLocation locationIn = tLocationMapper.selectById(locationInId);
        if (locationIn.getCategoryId() != null){
            if (!locationIn.getCategoryId().equals(locationOut.getCategoryId())){
                return AjaxResult.error("目标库位存放物料类别与当前库位不一致");
            }
        }
        // 获取该载具上所有物料数据
        List<TStock> stockList = tStockMapper.selectList(new LambdaQueryWrapper<TStock>()
                .eq(TStock::getTrayId,stock.getTrayId())
                .gt(TStock::getCount,0)
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        // 移库主数据
        TMoveLibrary moveLibrary = new TMoveLibrary();
        // 获取编号
        String code = itCodeConfigService.getCode("MKNYW");
        if (StringUtils.isEmpty(code)){
            throw new ServiceException("编号生成失败");
        }
        moveLibrary.setCode(code);
        // 转出库位ID
        moveLibrary.setLocationOutId(stock.getLocationId());
        // 转入库位ID
        moveLibrary.setLocationInId(locationInId);
        // 审核状态 0未审核 1已审核
        moveLibrary.setAuditorStatus("0");
        // 状态
        moveLibrary.setStatus("0");
        // 载具id
        moveLibrary.setTrayId(stock.getTrayId());
        // 部门
        moveLibrary.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());

        // 移库详情
        for(TStock model:stockList){
            TMoveLibraryDetail moveLibraryDetail = new TMoveLibraryDetail();
            // 移库编码
            moveLibraryDetail.setMoveLibraryCode(moveLibrary.getCode());
            // 库存id
            moveLibraryDetail.setStockId(model.getId());
            // 物料标识
            moveLibraryDetail.setMaterialId(model.getMaterialId());
            // 移库数量
            moveLibraryDetail.setCount(model.getCount());
            // 批次号
            moveLibraryDetail.setBatchCode(model.getBatchCode());
            // 所属部门
            moveLibraryDetail.setDeptId(model.getDeptId());

            tMoveLibraryDetailMapper.insert(moveLibraryDetail);
        }
        tMoveLibraryMapper.insert(moveLibrary);
        // 冻结库存
        List<Long> ids = stockList.stream().map(TStock::getId).collect(Collectors.toList());
        itStockService.updateFreezeByIds(ids,Constants.STOCK_IS_FREEZE_YES,Constants.STOCK_ORIGIN_TYPE_MOVE);
        return AjaxResult.success();
    }

    /**
     * 库内移位，直接生成移库任务
     * @param stockId
     * @param locationInId
     * @return
     */
    @Override
    public AjaxResult shiftMoveLibrary(Long stockId, Long locationInId) {
        // 库存明细ID
        TStock stock = tStockMapper.selectById(stockId);
        // 查询目标库位存放物料类别是否与当前库位一致
        TLocation locationOut = tLocationMapper.selectById(stock.getLocationId());
        TLocation locationIn = tLocationMapper.selectById(locationInId);
        if (locationIn.getCategoryId() != null){
            if (!locationIn.getCategoryId().equals(locationOut.getCategoryId())){
                return AjaxResult.error("目标库位存放物料类别与当前库位不一致");
            }
        }
        // 获取该载具上所有物料数据
        List<TStock> stockList = tStockMapper.selectList(new LambdaQueryWrapper<TStock>()
                .eq(TStock::getTrayId,stock.getTrayId())
                .gt(TStock::getCount,0)
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        // 移库主数据
        TMoveLibrary moveLibrary = new TMoveLibrary();
        // 获取编号
        String code = itCodeConfigService.getCode("MKNYW");
        if (StringUtils.isEmpty(code)){
            throw new ServiceException("编号生成失败");
        }
        moveLibrary.setCode(code);
        // 转出库位ID
        moveLibrary.setLocationOutId(stock.getLocationId());
        // 转入库位ID
        moveLibrary.setLocationInId(locationInId);
        // 审核状态 0未审核 1已审核
        moveLibrary.setAuditorStatus("0");
        // 状态
        moveLibrary.setStatus("0");
        // 载具id
        moveLibrary.setTrayId(stock.getTrayId());
        // 部门
        moveLibrary.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        moveLibrary.setAuditorStatus(Constants.OUTDELIVERY_AUDITOR_STATUS_NO);
        moveLibrary.setAuditor(String.valueOf(SecurityUtils.getUserId()));
        moveLibrary.setAuditorName(SecurityUtils.getUsername());
        // 移库详情
        for(TStock model:stockList){
            TMoveLibraryDetail moveLibraryDetail = new TMoveLibraryDetail();
            // 移库编码
            moveLibraryDetail.setMoveLibraryCode(moveLibrary.getCode());
            // 库存id
            moveLibraryDetail.setStockId(model.getId());
            // 物料标识
            moveLibraryDetail.setMaterialId(model.getMaterialId());
            // 移库数量
            moveLibraryDetail.setCount(model.getCount());
            // 批次号
            moveLibraryDetail.setBatchCode(model.getBatchCode());
            // 所属部门
            moveLibraryDetail.setDeptId(model.getDeptId());

            tMoveLibraryDetailMapper.insert(moveLibraryDetail);
        }
        tMoveLibraryMapper.insert(moveLibrary);
        // 冻结库存
        List<Long> ids = stockList.stream().map(TStock::getId).collect(Collectors.toList());
        itStockService.updateFreezeByIds(ids,Constants.STOCK_IS_FREEZE_YES,Constants.STOCK_ORIGIN_TYPE_MOVE);
        //生成移库任务
        // 生成拣货任务到任务列表
        return this.move(moveLibrary.getId());
    }

    /**
     * 库内移位审核
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateAuditor(Long id){
        // 获取当前主数据
        TMoveLibrary moveLibrary = tMoveLibraryMapper.selectById(id);
        // 更新数据
        TMoveLibrary updateModel = new TMoveLibrary();
        updateModel.setId(moveLibrary.getId());
        updateModel.setAuditorStatus(Constants.OUTDELIVERY_AUDITOR_STATUS_YES);
        updateModel.setAuditor(SecurityUtils.getUserId().toString());
        updateModel.setAuditorName(SecurityUtils.getUsername());
        // 更新
        tMoveLibraryMapper.updateById(updateModel);

        // 生成任务
        this.move(id);

        return AjaxResult.success();
    }

    /**
     * 生成移库任务
     * @param id
     * @return
     */
    @Transactional
    @Override
    public AjaxResult move(Long id){
        // 获取当前主数据
        MoveLibraryVo moveLibrary = selectTMoveLibraryById(id);

        // 未审核不能生成移位任务
        if (Constants.OUTDELIVERY_AUDITOR_STATUS_NO.equals(moveLibrary.getAuditorStatus())){
            return AjaxResult.error("该移位任务未审核，请审核后重试");
        }

        // 校验是否已经生成拣货任务且任务未完成
        TTask task = itTaskService.getOne(new QueryWrapper<TTask>()
                        .eq("origin_code", moveLibrary.getCode())
                        .eq("source_id", moveLibrary.getId())
                        .eq("task_type", Constants.TASK_TYPE_MOVE));
        if (task != null){
            if(task.getTaskStatus().equals("2")){
                return AjaxResult.error("任务已执行，不可重复生成");
            }
            return AjaxResult.error("任务执行中，不可重复生成");
        }

        // 生成拣货任务到任务列表
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setType(Constants.TASK_TYPE_MOVE);
        addTaskDTO.setMoveLibrary(moveLibrary);
        AjaxResult result = itTaskService.addTask(addTaskDTO);
        if (String.valueOf(HttpStatus.ERROR).equals(result.get("code").toString())){
            throw new ServiceException(result.get("msg").toString());
        }

        return AjaxResult.success();
    }

}
