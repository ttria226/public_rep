package com.xsrw.wms.stock.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.stock.mapper.TMoveLibraryDetailMapper;
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;
import com.xsrw.wms.stock.service.ITMoveLibraryDetailService;

/**
 * 移库详情Service业务层处理
 *
 * @author lyx
 * @date 2023-05-11
 */
@Service
public class TMoveLibraryDetailServiceImpl extends ServiceImpl<TMoveLibraryDetailMapper, TMoveLibraryDetail> implements ITMoveLibraryDetailService
{
    @Autowired
    private TMoveLibraryDetailMapper tMoveLibraryDetailMapper;


    /**
     * 查询移库详情列表
     *
     * @param tMoveLibraryDetail 移库详情
     * @return 移库详情
     */
    @Override
    public List<TMoveLibraryDetail> selectTMoveLibraryDetailList(TMoveLibraryDetail tMoveLibraryDetail)
    {
        return tMoveLibraryDetailMapper.selectTMoveLibraryDetailList(tMoveLibraryDetail);
    }

    /**
     * 查询移库详情
     *
     * @param id 移库详情主键
     * @return 移库详情
     */
    @Override
    public TMoveLibraryDetail selectTMoveLibraryDetailById(Long id)
    {
        return tMoveLibraryDetailMapper.selectById(id);
    }

    /**
     * 新增移库详情
     *
     * @param tMoveLibraryDetail 移库详情
     * @return 结果
     */
    @Override
    public int insertTMoveLibraryDetail(TMoveLibraryDetail tMoveLibraryDetail)
    {
        return tMoveLibraryDetailMapper.insert(tMoveLibraryDetail);
    }

    /**
     * 修改移库详情
     *
     * @param tMoveLibraryDetail 移库详情
     * @return 结果
     */
    @Override
    public int updateTMoveLibraryDetail(TMoveLibraryDetail tMoveLibraryDetail)
    {
        return tMoveLibraryDetailMapper.updateById(tMoveLibraryDetail);
    }


    /**
     * 批量删除移库详情
     *
     * @param ids 需要删除的移库详情主键
     * @return 结果
     */
    @Override
    public int deleteTMoveLibraryDetailByIds(Long[] ids)
    {
        return tMoveLibraryDetailMapper.deleteTMoveLibraryDetailByIds(ids);
    }

    /**
     * 删除移库详情信息
     *
     * @param id 移库详情主键
     * @return 结果
     */
    @Override
    public int deleteTMoveLibraryDetailById(Long id)
    {
        return tMoveLibraryDetailMapper.deleteTMoveLibraryDetailById(id);
    }
}
