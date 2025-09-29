package com.xsrw.wms.stock.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;

/**
 * 移库详情Service接口
 *
 * @author lyx
 * @date 2023-05-11
 */
public interface ITMoveLibraryDetailService extends IService<TMoveLibraryDetail>
{

    /**
     * 查询移库详情列表
     *
     * @param tMoveLibraryDetail 移库详情
     * @return 移库详情集合
     */
    public List<TMoveLibraryDetail> selectTMoveLibraryDetailList(TMoveLibraryDetail tMoveLibraryDetail);

    /**
     * 查询移库详情
     *
     * @param id 移库详情主键
     * @return 移库详情
     */
    public TMoveLibraryDetail selectTMoveLibraryDetailById(Long id);

    /**
     * 新增移库详情
     *
     * @param tMoveLibraryDetail 移库详情
     * @return 结果
     */
    public int insertTMoveLibraryDetail(TMoveLibraryDetail tMoveLibraryDetail);

    /**
     * 修改移库详情
     *
     * @param tMoveLibraryDetail 移库详情
     * @return 结果
     */
    public int updateTMoveLibraryDetail(TMoveLibraryDetail tMoveLibraryDetail);

    /**
     * 批量删除移库详情
     *
     * @param ids 需要删除的移库详情主键集合
     * @return 结果
     */
    public int deleteTMoveLibraryDetailByIds(Long[] ids);

    /**
     * 删除移库详情信息
     *
     * @param id 移库详情主键
     * @return 结果
     */
    public int deleteTMoveLibraryDetailById(Long id);
}
