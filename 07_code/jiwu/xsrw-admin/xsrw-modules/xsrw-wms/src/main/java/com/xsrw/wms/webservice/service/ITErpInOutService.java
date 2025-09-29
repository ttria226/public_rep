package com.xsrw.wms.webservice.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.webservice.domain.TErpInOut;

/**
 * ERP-出入库信息Service接口
 */
public interface ITErpInOutService extends IService<TErpInOut> {
    /**
     * 查询ERP-出入库信息
     *
     * @param id ERP-出入库信息主键
     * @return ERP-出入库信息
     */
    public TErpInOut selectTErpInOutById(Long id);

    /**
     * 查询ERP-出入库信息列表
     *
     * @param tErpInOut ERP-出入库信息
     * @return ERP-出入库信息集合
     */
    public List<TErpInOut> selectTErpInOutList(TErpInOut tErpInOut);

    /**
     * 新增ERP-出入库信息
     *
     * @param tErpInOut ERP-出入库信息
     * @return 结果
     */
    public int insertTErpInOut(TErpInOut tErpInOut);

    /**
     * 修改ERP-出入库信息
     *
     * @param tErpInOut ERP-出入库信息
     * @return 结果
     */
    public int updateTErpInOut(TErpInOut tErpInOut);

    /**
     * 批量删除ERP-出入库信息
     *
     * @param ids 需要删除的ERP-出入库信息主键集合
     * @return 结果
     */
    public int deleteTErpInOutByIds(Long[] ids);

    /**
     * 删除ERP-出入库信息信息
     *
     * @param id ERP-出入库信息主键
     * @return 结果
     */
    public int deleteTErpInOutById(Long id);
}
