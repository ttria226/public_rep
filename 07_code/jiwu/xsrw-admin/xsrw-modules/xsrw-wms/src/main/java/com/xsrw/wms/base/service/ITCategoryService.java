package com.xsrw.wms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 物料类别Service接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface ITCategoryService extends IService<TCategory>
{

    /**
     * 查询物料类别列表
     *
     * @param tCategory 物料类别
     * @return 物料类别集合
     */
    List<TCategory> selectTCategoryList(TCategory tCategory);

    /**
     * 查询物料类别
     *
     * @param id 物料类别主键
     * @return 物料类别
     */
    TCategory selectTCategoryById(Long id);

    /**
     * 新增物料类别
     *
     * @param tCategory 物料类别
     * @return 结果
     */
    AjaxResult insertTCategory(TCategory tCategory);

    /**
     * 修改物料类别
     *
     * @param tCategory 物料类别
     * @return 结果
     */
    AjaxResult updateTCategory(TCategory tCategory);

    /**
     * 批量删除物料类别
     *
     * @param ids 需要删除的物料类别主键集合
     * @return 结果
     */
    int deleteTCategoryByIds(Long[] ids);

    /**
     * 删除物料类别信息
     *
     * @param id 物料类别主键
     * @return 结果
     */
    int deleteTCategoryById(Long id);

    /**
     * 导入物料信息
     * @param file
     * @return
     * @throws Exception
     */
    AjaxResult importUnit(MultipartFile file) throws Exception;
}
