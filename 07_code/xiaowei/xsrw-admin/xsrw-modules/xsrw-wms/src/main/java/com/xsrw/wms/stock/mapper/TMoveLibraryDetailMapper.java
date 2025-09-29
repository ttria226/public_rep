package com.xsrw.wms.stock.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;

/**
 * 移库详情Mapper接口
 * 
 * @author lyx
 * @date 2023-05-11
 */
public interface TMoveLibraryDetailMapper extends BaseMapper<TMoveLibraryDetail>
{

    /**
     * 查询移库详情列表
     * 
     * @param tMoveLibraryDetail 移库详情
     * @return 移库详情集合
     */
    public List<TMoveLibraryDetail> selectTMoveLibraryDetailList(TMoveLibraryDetail tMoveLibraryDetail);


    /**
     * 删除移库详情
     * 
     * @param id 移库详情主键
     * @return 结果
     */
    public int deleteTMoveLibraryDetailById(Long id);

    /**
     * 批量删除移库详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMoveLibraryDetailByIds(Long[] ids);
}
