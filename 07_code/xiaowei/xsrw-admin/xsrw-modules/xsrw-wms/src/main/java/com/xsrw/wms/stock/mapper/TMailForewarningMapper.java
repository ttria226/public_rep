package com.xsrw.wms.stock.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.stock.domain.TMailForewarning;

/**
 * 预警邮件配置Mapper接口
 *
 * @author wxr
 * @date 2023-06-19
 */
public interface TMailForewarningMapper extends BaseMapper<TMailForewarning> {

    /**
     * 查询预警邮件配置列表
     *
     * @param tMailForewarning 预警邮件配置
     * @return 预警邮件配置集合
     */
    public List<TMailForewarning> selectTMailForewarningList(TMailForewarning tMailForewarning);


    /**
     * 删除预警邮件配置
     *
     * @param id 预警邮件配置主键
     * @return 结果
     */
    public int deleteTMailForewarningById(Long id);

    /**
     * 批量删除预警邮件配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMailForewarningByIds(Long[] ids);

    /**
     * 根据类型查询发送地址
     * @param type
     * @return
     */
    List<String> selectEmailAddressByType(String type);
}
