package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TClasses;
import com.xsrw.wms.base.domain.vo.TClassesVO;

/**
 * 班次管理Service接口
 *
 * @author wxr
 * @date 2023-06-12
 */
public interface ITClassesService extends IService<TClasses> {

    /**
     * 查询班次管理列表
     *
     * @param tClasses 班次管理
     * @return 班次管理集合
     */
    public List<TClassesVO> selectTClassesList(TClasses tClasses);

    /**
     * 查询班次管理
     *
     * @param id 班次管理主键
     * @return 班次管理
     */
    public TClasses selectTClassesById(Long id);

    /**
     * 新增班次管理
     *
     * @param tClasses 班次管理
     * @return 结果
     */
    public AjaxResult insertTClasses(TClasses tClasses);

    /**
     * 修改班次管理
     *
     * @param tClasses 班次管理
     * @return 结果
     */
    public AjaxResult updateTClasses(TClasses tClasses);

    /**
     * 批量删除班次管理
     *
     * @param ids 需要删除的班次管理主键集合
     * @return 结果
     */
    public int deleteTClassesByIds(Long[] ids);

    /**
     * 删除班次管理信息
     *
     * @param id 班次管理主键
     * @return 结果
     */
    public int deleteTClassesById(Long id);
}
