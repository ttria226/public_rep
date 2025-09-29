package com.xsrw.wms.inout.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TAllot;
import com.xsrw.wms.inout.domain.vo.TAllotVO;

/**
 * 调拨单Service接口
 *
 * @author zjj
 * @date 2023-06-26
 */
public interface ITAllotService extends IService<TAllot>
{

    /**
     * 查询调拨单列表
     *
     * @param tAllot 调拨单
     * @return 调拨单集合
     */
    public List<TAllotVO> selectTAllotList(TAllot tAllot);

    /**
     * 查询调拨单
     *
     * @param id 调拨单主键
     * @return 调拨单
     */
    public TAllot selectTAllotById(Long id);

    /**
     * 新增调拨单
     *
     * @param tAllot 调拨单
     * @return 结果
     */
    public AjaxResult insertTAllot(TAllot tAllot);

    /**
     * 修改调拨单
     *
     * @param tAllot 调拨单
     * @return 结果
     */
    public int updateTAllot(TAllot tAllot);

    /**
     * 批量删除调拨单
     *
     * @param ids 需要删除的调拨单主键集合
     * @return 结果
     */
    public int deleteTAllotByIds(Long[] ids);

    /**
     * 删除调拨单信息
     *
     * @param id 调拨单主键
     * @return 结果
     */
    public int deleteTAllotById(Long id);


    /**
     * 调拨生成对应的出库计划、入库计划
     * @param id
     * @return
     */
    AjaxResult createDelivery(Long id,String remark,String allotStatus);
}
