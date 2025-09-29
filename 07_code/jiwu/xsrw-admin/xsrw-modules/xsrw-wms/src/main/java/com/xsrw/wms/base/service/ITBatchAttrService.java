package com.xsrw.wms.base.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TBatchAttr;

/**
 * 批次属性Service接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface ITBatchAttrService extends IService<TBatchAttr>
{

    /**
     * 查询批次属性列表
     *
     * @param tBatchAttr 批次属性
     * @return 批次属性集合
     */
    List<TBatchAttr> selectTBatchAttrList(TBatchAttr tBatchAttr);

    /**
     * 查询批次属性
     *
     * @param id 批次属性主键
     * @return 批次属性
     */
    TBatchAttr selectTBatchAttrById(Long id);

    /**
     * 新增批次属性
     *
     * @param tBatchAttr 批次属性
     * @return 结果
     */
    int insertTBatchAttr(TBatchAttr tBatchAttr);

    /**
     * 修改批次属性
     *
     * @param tBatchAttr 批次属性
     * @return 结果
     */
    int updateTBatchAttr(TBatchAttr tBatchAttr);

    /**
     * 批量删除批次属性
     *
     * @param ids 需要删除的批次属性主键集合
     * @return 结果
     */
    int deleteTBatchAttrByIds(Long[] ids);

    /**
     * 删除批次属性信息
     *
     * @param id 批次属性主键
     * @return 结果
     */
    int deleteTBatchAttrById(Long id);
}
