package com.xsrw.wms.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.task.domain.TPerson;

import java.util.List;

/**
 * 人员Service接口
 *
 * @author zjj
 * @date 2023-06-29
 */
public interface ITPersonService extends IService<TPerson>
{

    /**
     * 查询人员列表
     *
     * @param tPerson 人员
     * @return 人员集合
     */
    public List<TPerson> selectTPersonList(TPerson tPerson);

    /**
     * 查询人员
     *
     * @param id 人员主键
     * @return 人员
     */
    public AjaxResult selectTPersonById(Long id);

    /**
     * 新增人员
     *
     * @param tPerson 人员
     * @return 结果
     */
    public AjaxResult insertTPerson(TPerson tPerson);

    /**
     * 修改人员
     *
     * @param tPerson 人员
     * @return 结果
     */
    public AjaxResult updateTPerson(TPerson tPerson);

    /**
     * 批量删除人员
     *
     * @param ids 需要删除的人员主键集合
     * @return 结果
     */
    public int deleteTPersonByIds(Long[] ids);

    /**
     * 删除人员信息
     *
     * @param id 人员主键
     * @return 结果
     */
    public int deleteTPersonById(Long id);
}
