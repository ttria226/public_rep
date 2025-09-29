package com.xsrw.wms.inout.service;

import java.util.List;

import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialSelectVO;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TTaskOutVO;
import com.xsrw.wms.inout.domain.vo.TTraySelectVO;

/**
 * 出库单Service接口
 *
 * @author zyq
 * @date 2023-05-09
 */
public interface ITOutDeliveryService extends IService<TOutDelivery>
{

    /**
     * 查询出库单列表
     *
     * @param tOutDelivery 出库单
     * @return 出库单集合
     */
    public List<TOutDeliveryVO> selectTOutDeliveryList(TOutDelivery tOutDelivery);

   /**
     * 查询出库任务列表
     *
     * @param tOutDelivery 出库单
     * @return 出库单集合
     */
    public List<TOutDeliveryDetailVO> outTasklist(TOutDeliveryDetail tOutDelivery);

    /**
     * 执行出库载具选择列表
     *
     * @param tTray 载具管理
     * @return 出库单集合
     */
    public List<TTrayApiVO> traylist(TTray tTray);

    /**
     * 查询出库单
     *
     * @param id 出库单主键
     * @return 出库单
     */
    public TOutDeliveryVO selectTOutDeliveryById(Long id);

    /**
     * 新增出库单
     *
     * @param tOutDeliveryVO 出库单
     * @return 结果
     */
    public AjaxResult insertTOutDelivery(TOutDeliveryVO tOutDeliveryVO);

    /**
     * 出库计划审核
     *
     * @param tOutDelivery 出库单
     * @return 结果
     */
    public AjaxResult approveTOutDelivery(TOutDelivery tOutDelivery);

    /**
     * 生成出库任务
     *
     * @param ids 出库单
     * @return 结果
     */
    public AjaxResult toOutTask(Long[] ids);

   /**
     * 获取选择的物料信息
     *
     * @param
     * @return 结果
     */
    public List<TMaterialSelectVO> getMaterialSelectList(TMaterialDTO tMaterial);

   /**
     * 出库计划审核
     *
     * @param tOutDelivery 出库单
     * @return 结果
     */
    public AjaxResult updateTOutDelivery(TOutDeliveryVO tOutDelivery);

    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的出库单主键集合
     * @return 结果
     */
    public AjaxResult deleteTOutDeliveryByIds(Long[] ids);

    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的出库单主键集合
     * @return 结果
     */
    public AjaxResult deleteTOutDeliveryDetailByIds(Long[] ids);

    /**
     * 删除出库单信息
     *
     * @param id 出库单主键
     * @return 结果
     */
    public int deleteTOutDeliveryById(Long id);
    AjaxResult outBound(TOutDeliveryVO tOutDeliveryVO);


    /**
     * 查询可以合并为波次的出库单
     * @param tOutDelivery
     * @return
     */
    List<TOutDelivery> getMergeList(TOutDelivery tOutDelivery);


    /**
     * 齐套出库新增
     * @param tOutDeliveryVO
     * @return
     */
    AjaxResult suitAdd(TOutDeliveryVO tOutDeliveryVO) throws ServiceException;


    /**
     * 查询物料分配
     * @param materialId
     * @return
     */
    List<TTrayVO> suitMaterial(Long materialId, String type);


    /**
     * 执行出库
     * @param tTaskOutVO
     * @return
     */
    AjaxResult suitAddTask(List<TTaskOutVO> tTaskOutVO);


    /**
     * 地堆出库
     * @param tTaskOutVO
     * @return
     */
    AjaxResult addTaskPile(List<TTaskOutVO> tTaskOutVO);




































   /**
    * 删除出库所有相关单据 -- 谨慎使用
    * @param tOutDelivery
    * @return
    */
   AjaxResult delOutAll(TOutDelivery tOutDelivery);


}
