package com.xsrw.wms.stock.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.stock.domain.TMailForewarning;

/**
 * 预警邮件配置Service接口
 *
 * @author wxr
 * @date 2023-06-19
 */
public interface ITMailForewarningService extends IService<TMailForewarning> {

    /**
     * 查询预警邮件配置列表
     *
     * @param tMailForewarning 预警邮件配置
     * @return 预警邮件配置集合
     */
    public List<TMailForewarning> selectTMailForewarningList(TMailForewarning tMailForewarning);

    /**
     * 查询预警邮件配置
     *
     * @param id 预警邮件配置主键
     * @return 预警邮件配置
     */
    public TMailForewarning selectTMailForewarningById(Long id);

    /**
     * 新增预警邮件配置
     *
     * @param tMailForewarning 预警邮件配置
     * @return 结果
     */
    public int insertTMailForewarning(TMailForewarning tMailForewarning);

    /**
     * 修改预警邮件配置
     *
     * @param tMailForewarning 预警邮件配置
     * @return 结果
     */
    public int updateTMailForewarning(TMailForewarning tMailForewarning);

    /**
     * 批量删除预警邮件配置
     *
     * @param ids 需要删除的预警邮件配置主键集合
     * @return 结果
     */
    public int deleteTMailForewarningByIds(Long[] ids);

    /**
     * 删除预警邮件配置信息
     *
     * @param id 预警邮件配置主键
     * @return 结果
     */
    public int deleteTMailForewarningById(Long id);

    /**
     * 根据类型发送邮件
     * @param tMailForewarning
     * @return
     */
    AjaxResult sendEmail(TMailForewarning tMailForewarning);
}
