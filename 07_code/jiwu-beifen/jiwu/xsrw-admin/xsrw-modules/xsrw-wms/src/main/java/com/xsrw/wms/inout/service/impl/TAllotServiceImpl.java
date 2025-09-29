package com.xsrw.wms.inout.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TAllotVO;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryVO;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryService;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAllotMapper;
import com.xsrw.wms.inout.domain.TAllot;
import com.xsrw.wms.inout.service.ITAllotService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调拨单Service业务层处理
 *
 * @author zjj
 * @date 2023-06-26
 */
@Service
public class TAllotServiceImpl extends ServiceImpl<TAllotMapper, TAllot> implements ITAllotService
{
    @Autowired
    private TAllotMapper tAllotMapper;

    @Autowired
    private TStockMapper tStockMapper;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private ITOutDeliveryService outDeliveryService;

    @Autowired
    private TMaterialMapper materialMapper;

    @Autowired
    private ITAdvanceDeliveryService tAdvanceDeliveryService;


    /**
     * 查询调拨单列表
     *
     * @param tAllot 调拨单
     * @return 调拨单
     */
    @Override
    public List<TAllotVO> selectTAllotList(TAllot tAllot)
    {
        return tAllotMapper.selectTAllotList(tAllot);
    }

    /**
     * 查询调拨单
     *
     * @param id 调拨单主键
     * @return 调拨单
     */
    @Override
    public TAllot selectTAllotById(Long id)
    {
        return tAllotMapper.selectById(id);
    }

    /**
     * 新增调拨单
     *
     * @param tAllot 调拨单
     * @return 结果
     */
    @Override
    public AjaxResult insertTAllot(TAllot tAllot)
    {
        if (tAllot.getStockId() == null || tAllot.getAllotNum() == null ||
                tAllot.getInWarehouseId() == null || tAllot.getOutWarehouseId() == null){
            return AjaxResult.error("参数不可为空");
        }
        // 查询库存信息
        TStock tStock = tStockMapper.selectById(tAllot.getStockId());
        if (tAllot.getAllotNum().intValue() > tStock.getAvailableCount().intValue()){
            return AjaxResult.error("调拨数量不可大于库存数量");
        }

        // 获取编码
        String code = codeConfigService.getCode(CodeEnum.MDB.getCodeName());
        tAllot.setCode(code);
        tAllot.setAllotStatus(Constants.ALLOT_STATUS_CHECK);
        tAllotMapper.insert(tAllot);
        return AjaxResult.success();
    }

    /**
     * 修改调拨单
     *
     * @param tAllot 调拨单
     * @return 结果
     */
    @Override
    public int updateTAllot(TAllot tAllot)
    {
        return tAllotMapper.updateById(tAllot);
    }


    /**
     * 批量删除调拨单
     *
     * @param ids 需要删除的调拨单主键
     * @return 结果
     */
    @Override
    public int deleteTAllotByIds(Long[] ids)
    {
        return tAllotMapper.deleteTAllotByIds(ids);
    }

    /**
     * 删除调拨单信息
     *
     * @param id 调拨单主键
     * @return 结果
     */
    @Override
    public int deleteTAllotById(Long id)
    {
        return tAllotMapper.deleteTAllotById(id);
    }


    /**
     * 调拨生成对应的出库计划、入库计划
     * @param id
     * @return
     */
    @Transactional
    @Override
    public AjaxResult createDelivery(Long id,String remark,String allotStatus) {

        if (id == null){
            return AjaxResult.error("参数不可为空");
        }

        TAllot tAllot = tAllotMapper.selectById(id);
        if (Constants.ALLOT_STATUS_FAIL.equals(allotStatus)){
            tAllot.setRemark(remark);
            tAllot.setAllotStatus(Constants.ALLOT_STATUS_FAIL);
            tAllotMapper.updateById(tAllot);
            return AjaxResult.success();
        }

        if (!Constants.ALLOT_STATUS_CHECK.equals(tAllot.getAllotStatus())){
            return AjaxResult.error("单据状态错误");
        }

        // 查询物料信息
        TMaterial tMaterial = materialMapper.selectById(tAllot.getMaterialId());
        // 查询库存信息
        TStock tStock = tStockMapper.selectById(tAllot.getStockId());

        // 创建出库计划
        TOutDeliveryVO tOutDeliveryVO = new TOutDeliveryVO();
        // 单据类型  4系统生成
        tOutDeliveryVO.setType("4");
        // 来源字段 3调拨单
        tOutDeliveryVO.setNewLocal("3");
        // 设置出库部门
        tOutDeliveryVO.setDeptId(tAllot.getOutWarehouseId());
        tOutDeliveryVO.setDeptName(tAllot.getOutWarehouseName());
        tOutDeliveryVO.setOriginCode(tAllot.getCode());
        tOutDeliveryVO.setOriginDate(new Date());

        List<TOutDeliveryDetail> deliveryDetails = new ArrayList<>();

        TOutDeliveryDetail detail = new TOutDeliveryDetail();
        detail.setMaterialCode(tMaterial.getCode());
        detail.setMaterialName(tMaterial.getName());
        detail.setMaterialId(tMaterial.getId());
        detail.setPredictCount(tAllot.getAllotNum());
        deliveryDetails.add(detail);

        tOutDeliveryVO.settOutDeliveryDetailList(deliveryDetails);
        AjaxResult ajaxResult = outDeliveryService.insertTOutDelivery(tOutDeliveryVO);
        if ("500".equals(ajaxResult.get("code").toString())){
            throw new RuntimeException("出库计划创建:"+ajaxResult.get("msg").toString());
        }

        // 创建入库计划
        TAdvanceDeliveryDTO tAdvanceDelivery = new TAdvanceDeliveryDTO();
        // 单据类型 6 系统生成
        tAdvanceDelivery.setType("6");
        tAdvanceDelivery.setNewLocal(Constants.DELIVERY_IN_TYPE_ALLOT);
        tAdvanceDelivery.setOriginCode(tAllot.getCode());
        tAdvanceDelivery.setOriginDate(new Date());

        List<TAdvanceDeliveryDetail> deliveryDetailList = new ArrayList<>();
        TAdvanceDeliveryDetail deliveryDetail = new TAdvanceDeliveryDetail();
        deliveryDetail.setMaterialId(tMaterial.getId());
        deliveryDetail.setPredictCount(tAllot.getAllotNum());
        deliveryDetail.setBatchCode(tStock.getBatchCode());
        deliveryDetailList.add(deliveryDetail);

        tAdvanceDelivery.setDeliveryDetailList(deliveryDetailList);
        tAdvanceDeliveryService.insertTAdvanceDelivery(tAdvanceDelivery);

        // 更新调拨单状态
        tAllot.setAllotStatus(Constants.ALLOT_STATUS_OUT);
        tAllotMapper.updateById(tAllot);

        return AjaxResult.success();
    }

}
