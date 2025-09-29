package com.xsrw.wms.base.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TCodeConfig;

import java.util.Date;
import java.util.List;

/**
 * 编码配置Service接口
 */
public interface ITCodeConfigService extends IService<TCodeConfig> {
    /**
     * 查询编码配置
     *
     * @param id 编码配置主键
     * @return 编码配置
     */
    TCodeConfig selectCodeConfigById(Long id);

    /**
     * 查询编码配置列表
     *
     * @param codeConfig 编码配置
     * @return 编码配置集合
     */
    List<TCodeConfig> selectCodeConfigList(TCodeConfig codeConfig);

    /**
     * 新增编码配置
     *
     * @param codeConfig 编码配置
     * @return 结果
     */
    AjaxResult insertCodeConfig(TCodeConfig codeConfig);

    /**
     * 修改编码配置
     *
     * @param codeConfig 编码配置
     * @return 结果
     */
    AjaxResult updateCodeConfig(TCodeConfig codeConfig);

    /**
     * 批量删除编码配置
     *
     * @param ids 需要删除的编码配置主键集合
     * @return 结果
     */
    int deleteCodeConfigByIds(Long[] ids);

    /**
     * 删除编码配置信息
     *
     * @param id 编码配置主键
     * @return 结果
     */
    int deleteCodeConfigById(Long id);

    /**
     * 根据编码配置生成编码
     *
     * @param modelName
     * @return
     */
    String getCode(String modelName);

    /**
     * 生成批次号
     * @param materialId
     * @param makeDate
     * @return
     */
    public String getBatchCode(Long materialId, Date makeDate);

    /**
     * 生成批次号
     * @param materialId
     * @return
     */
    public String getBatchCode1(Long materialId, Date makeDate);

    /**
     * 生成rfid编码
     * @param materialCode
     * @return
     */
    public String getRfIdCode(String materialCode,String batchCode);

}
