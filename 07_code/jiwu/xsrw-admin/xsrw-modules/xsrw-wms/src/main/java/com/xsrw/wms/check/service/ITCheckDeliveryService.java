package com.xsrw.wms.check.service;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.check.domain.TCheckDelivery;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.vo.CheckDeliveryVO;

import java.util.List;
import java.util.Map;

/**
 * 盘点计划Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITCheckDeliveryService extends IService<TCheckDelivery>
{

    /**
     * 查询盘点计划列表
     *
     * @param checkDelivery 盘点计划
     * @return 盘点计划集合
     */
    List<CheckDeliveryVO> selectTCheckDeliveryList(CheckDeliveryDTO checkDelivery);

    /**
     * 查询盘点计划
     *
     * @param id 盘点计划主键
     * @return 盘点计划
     */
    TCheckDelivery selectTCheckDeliveryById(Long id);

    /**
     * 新增盘点计划
     *
     * @param data 盘点计划
     * @return 结果
     */
    AjaxResult insertTCheckDelivery(Map<String,Object> data);

    /**
     * 修改盘点计划
     *
     * @param tCheckDelivery 盘点计划
     * @return 结果
     */
    int updateTCheckDelivery(TCheckDelivery tCheckDelivery);

    /**
     * 批量删除盘点计划
     *
     * @param ids 需要删除的盘点计划主键集合
     * @return 结果
     */
    int deleteTCheckDeliveryByIds(Long[] ids);

    /**
     * 删除盘点计划信息
     *
     * @param id 盘点计划主键
     * @return 结果
     */
    int deleteTCheckDeliveryById(Long id);


    /**
     * PDA查询库位信息
     * @param code
     * @return
     */
    AjaxResult locationInfo(String code);


    /**
     * 提交盘点
     * @param map
     * @return
     */
    AjaxResult checkData(List<Map<String,Object>> map);


    /**
     * 提交盘点数据
     * @param checkDeliveryDTO
     * @return
     */
    AjaxResult checkdeliverySubmit(CheckDeliveryDTO checkDeliveryDTO);


    /**
     * 盘点详情
     * @param taskId
     * @param trayCode
     * @param checkType
     * @param batch
     * @return
     */
    AjaxResult executeTask(Long taskId,String trayCode,String checkType,String batch,String rfid);

    List<CheckDeliveryVO> selectCheckMaterialDetailList(CheckDeliveryDTO checkDelivery);
}
