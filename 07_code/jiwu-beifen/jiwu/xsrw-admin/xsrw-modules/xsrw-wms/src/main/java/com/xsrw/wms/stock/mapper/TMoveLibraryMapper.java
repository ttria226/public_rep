package com.xsrw.wms.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库内移位Mapper接口
 * 
 * @author lyx
 * @date 2023-05-09
 */
public interface TMoveLibraryMapper extends BaseMapper<TMoveLibrary>
{

    /**
     * 查询库内移位列表
     * 
     * @param tMoveLibrary 库内移位
     * @return 库内移位集合
     */
    public List<TMoveLibrary> selectTMoveLibraryList(TMoveLibrary tMoveLibrary);


    /**
     * 删除库内移位
     * 
     * @param id 库内移位主键
     * @return 结果
     */
    public int deleteTMoveLibraryById(Long id);

    /**
     * 批量删除库内移位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMoveLibraryByIds(Long[] ids);

    /**
     * 查询库内移位列表
     * @param moveLibrary 库内移位
     * @param materialIds 物料id
     * @return 库内移位集合
     */
    List<MoveLibraryVo> selectMoveLibraryList(@Param("moveLibrary") MoveLibraryVo moveLibrary, @Param("materialIds") List<Long> materialIds);
}
