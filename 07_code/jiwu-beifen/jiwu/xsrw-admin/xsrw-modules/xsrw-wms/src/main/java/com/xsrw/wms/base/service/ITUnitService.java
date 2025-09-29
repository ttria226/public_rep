package com.xsrw.wms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TUnit;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 单位Service接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface ITUnitService extends IService<TUnit>
{

    /**
     * 查询单位列表
     *
     * @param tUnit 单位
     * @return 单位集合
     */
    List<TUnit> selectTUnitList(TUnit tUnit);

    /**
     * 查询单位
     *
     * @param id 单位主键
     * @return 单位
     */
    TUnit selectTUnitById(Long id);

    /**
     * 新增单位
     *
     * @param tUnit 单位
     * @return 结果
     */
    AjaxResult insertTUnit(TUnit tUnit);

    /**
     * 修改单位
     *
     * @param tUnit 单位
     * @return 结果
     */
    AjaxResult updateTUnit(TUnit tUnit);

    /**
     * 批量删除单位
     *
     * @param ids 需要删除的单位主键集合
     * @return 结果
     */
    int deleteTUnitByIds(Long[] ids);

    /**
     * 删除单位信息
     *
     * @param id 单位主键
     * @return 结果
     */
    int deleteTUnitById(Long id);

    /**
     * 导入单位列表
     * @param file
     * @return
     * @throws Exception
     */
    AjaxResult importUnit(MultipartFile file) throws Exception;
}
