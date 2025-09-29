package com.xsrw.wms.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.task.domain.TPerson;

import java.util.List;

/**
 * 人员Mapper接口
 * 
 * @author zjj
 * @date 2023-06-29
 */
public interface TPersonMapper extends BaseMapper<TPerson>
{

    /**
     * 查询人员列表
     * 
     * @param tPerson 人员
     * @return 人员集合
     */
    public List<TPerson> selectTPersonList(TPerson tPerson);


    /**
     * 删除人员
     * 
     * @param id 人员主键
     * @return 结果
     */
    public int deleteTPersonById(Long id);

    /**
     * 批量删除人员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPersonByIds(Long[] ids);
}
