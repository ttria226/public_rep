package com.xsrw.wms.inout.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TTaskOut;
import com.xsrw.wms.inout.domain.dto.TTaskOutDTO;
import com.xsrw.wms.inout.domain.vo.TTaskOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;
import com.xsrw.wms.stock.domain.TStock;

/**
 * 出库任务详情Service接口
 *
 * @author zyq
 * @date 2023-05-08
 */
public interface ITTaskOutService extends IService<TTaskOut> {

    /**
     * 查询出库任务详情列表
     *
     * @param tTaskOut 出库任务详情
     * @return 出库任务详情集合
     */
    public List<TTaskOut> selectTTaskOutList(TTaskOut tTaskOut);

    /**
     * 执行出库选择载具列表
     *
     * @return 出库任务详情集合
     */
    public List<TTrayVO> selectTTrayList(String id, TStock tStock);


    /**
     * 执行出库--自动分配载具
     * @return
     */
    AjaxResult voluntarily(Long outDeliveryId,Long materialId);
    AjaxResult voluntarilyAll(Long outDeliveryId);
    AjaxResult voluntarilyAllQuick(Long outDeliveryId);

    /**
     * 地堆出库--自动分配载具
     * @return
     */
    AjaxResult groundPileTrayListVoluntarily(Long outDeliveryId,Long materialId);

    Map<String, Object> getOutDeliveryCount(String id);

    AjaxResult groundPileOutbound(TTaskOutVO tTaskOutVO);

    /**
     * 查询出库任务详情
     *
     * @param id 出库任务详情主键
     * @return 出库任务详情
     */
    public TTaskOutDTO selectTTaskOutById(Long id, Integer type);

    /**
     * 获取物料rfid列表
     * @param locationId
     * @param batchCode
     * @return
     */
    List<TMaterialDetail> getMaterialRfidList(Long locationId, String batchCode, String rfidHead,Long materialId);

    public List<TTaskOutDTO> selectTTaskOut(Long id);

    AjaxResult executeOutTask(TTaskWcsOutVO tTaskWcs);

    AjaxResult executeOutTaskPDA(TTaskWcsOutVO tTaskWcs);

    AjaxResult scanTray(String trayCode, String rfid);

    AjaxResult trayBack(String trayCode);

    AjaxResult scanTrayNew(String trayCode);

    /**
     * 新增出库任务详情
     *
     * @param tTaskOut 出库任务详情
     * @return 结果
     */
    public AjaxResult insertTTaskOut(TTaskOutVO tTaskOut);

    /**
     * 修改出库任务详情
     *
     * @param tTaskOut 出库任务详情
     * @return 结果
     */
    public int updateTTaskOut(TTaskOut tTaskOut);

    /**
     * 批量删除出库任务详情
     *
     * @param ids 需要删除的出库任务详情主键集合
     * @return 结果
     */
    public int deleteTTaskOutByIds(Long[] ids);

    /**
     * 删除出库任务详情信息
     *
     * @param id 出库任务详情主键
     * @return 结果
     */
    public int deleteTTaskOutById(Long id);

    /**
     * 通过载具拣出出库
     *
     * @param tTaskOut
     * @return
     */
    AjaxResult executeOutByTray(TTaskOutVO tTaskOut);

    /**
     * 回流
     * @param tOutDelivery
     * @return
     */
    AjaxResult refluxOutDelivery(TOutDelivery tOutDelivery);
}
