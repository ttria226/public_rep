package com.xsrw.wms.base.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TBomDetailMapper;
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.service.ITBomDetailService;

/**
 * bom详情Service业务层处理
 *
 * @author zjj
 * @date 2023-06-10
 */
@Service
public class TBomDetailServiceImpl extends ServiceImpl<TBomDetailMapper, TBomDetail> implements ITBomDetailService
{
    @Autowired
    private TBomDetailMapper tBomDetailMapper;


    /**
     * 查询bom详情列表
     *
     * @param tBomDetail bom详情
     * @return bom详情
     */
    @Override
    public List<TBomDetail> selectTBomDetailList(TBomDetail tBomDetail)
    {
        return tBomDetailMapper.selectTBomDetailList(tBomDetail);
    }

    /**
     * 查询bom详情
     *
     * @param id bom详情主键
     * @return bom详情
     */
    @Override
    public TBomDetail selectTBomDetailById(Long id)
    {
        return tBomDetailMapper.selectById(id);
    }

    /**
     * 新增bom详情
     *
     * @param tBomDetail bom详情
     * @return 结果
     */
    @Override
    public int insertTBomDetail(TBomDetail tBomDetail)
    {
        return tBomDetailMapper.insert(tBomDetail);
    }

    /**
     * 修改bom详情
     *
     * @param tBomDetail bom详情
     * @return 结果
     */
    @Override
    public int updateTBomDetail(TBomDetail tBomDetail)
    {
        return tBomDetailMapper.updateById(tBomDetail);
    }


    /**
     * 批量删除bom详情
     *
     * @param ids 需要删除的bom详情主键
     * @return 结果
     */
    @Override
    public int deleteTBomDetailByIds(Long[] ids)
    {
        return tBomDetailMapper.deleteTBomDetailByIds(ids);
    }

    /**
     * 删除bom详情信息
     *
     * @param id bom详情主键
     * @return 结果
     */
    @Override
    public int deleteTBomDetailById(Long id)
    {
        return tBomDetailMapper.deleteTBomDetailById(id);
    }
}
