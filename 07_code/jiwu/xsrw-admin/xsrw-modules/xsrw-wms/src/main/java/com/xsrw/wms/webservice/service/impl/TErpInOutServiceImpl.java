package com.xsrw.wms.webservice.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.wms.webservice.domain.TErpInOut;
import com.xsrw.wms.webservice.mapper.TErpInOutMapper;
import com.xsrw.wms.webservice.service.ITErpInOutService;

/**
 * ERP-出入库信息Service业务层处理
 */
@Service
public class TErpInOutServiceImpl  extends ServiceImpl<TErpInOutMapper, TErpInOut> implements ITErpInOutService {
    @Autowired
    private TErpInOutMapper tErpInOutMapper;

    /**
     * 查询ERP-出入库信息
     *
     * @param id ERP-出入库信息主键
     * @return ERP-出入库信息
     */
    @Override
    public TErpInOut selectTErpInOutById(Long id)
    {
        return tErpInOutMapper.selectTErpInOutById(id);
    }

    /**
     * 查询ERP-出入库信息列表
     *
     * @param tErpInOut ERP-出入库信息
     * @return ERP-出入库信息
     */
    @Override
    public List<TErpInOut> selectTErpInOutList(TErpInOut tErpInOut)
    {
        return tErpInOutMapper.selectTErpInOutList(tErpInOut);
    }

    /**
     * 新增ERP-出入库信息
     *
     * @param tErpInOut ERP-出入库信息
     * @return 结果
     */
    @Override
    public int insertTErpInOut(TErpInOut tErpInOut)
    {
        tErpInOut.setCreateTime(DateUtils.getNowDate());
        return tErpInOutMapper.insertTErpInOut(tErpInOut);
    }

    /**
     * 修改ERP-出入库信息
     *
     * @param tErpInOut ERP-出入库信息
     * @return 结果
     */
    @Override
    public int updateTErpInOut(TErpInOut tErpInOut)
    {
        tErpInOut.setUpdateTime(DateUtils.getNowDate());
        return tErpInOutMapper.updateTErpInOut(tErpInOut);
    }

    /**
     * 批量删除ERP-出入库信息
     *
     * @param ids 需要删除的ERP-出入库信息主键
     * @return 结果
     */
    @Override
    public int deleteTErpInOutByIds(Long[] ids)
    {
        return tErpInOutMapper.deleteTErpInOutByIds(ids);
    }

    /**
     * 删除ERP-出入库信息信息
     *
     * @param id ERP-出入库信息主键
     * @return 结果
     */
    @Override
    public int deleteTErpInOutById(Long id)
    {
        return tErpInOutMapper.deleteTErpInOutById(id);
    }
}
