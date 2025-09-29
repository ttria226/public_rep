package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TClasses;
import com.xsrw.wms.base.domain.vo.TClassesVO;

/**
 * 班次管理Mapper接口
 *
 * @author wxr
 * @date 2023-06-12
 */
public interface TClassesMapper extends BaseMapper<TClasses> {

    /**
     * 查询班次管理列表
     *
     * @param tClasses 班次管理
     * @return 班次管理集合
     */
    public List<TClassesVO> selectTClassesList(TClasses tClasses);


    /**
     * 删除班次管理
     *
     * @param id 班次管理主键
     * @return 结果
     */
    public int deleteTClassesById(Long id);

    /**
     * 批量删除班次管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTClassesByIds(Long[] ids);

    /**
     * 查询是否已存在数据
     * @param tClasses
     * @return
     */
    int getExistCount(TClasses tClasses);
}
