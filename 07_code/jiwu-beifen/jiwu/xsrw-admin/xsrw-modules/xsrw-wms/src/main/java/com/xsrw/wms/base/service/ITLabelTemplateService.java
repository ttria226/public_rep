package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TLabelTemplate;
import com.xsrw.wms.base.domain.vo.LabelByMaterialVo;

/**
 * 标签打印Service接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface ITLabelTemplateService extends IService<TLabelTemplate> {

    /**
     * 查询标签打印列表
     *
     * @param tLabelTemplate 标签打印
     * @return 标签打印集合
     */
    public List<TLabelTemplate> selectTLabelTemplateList(TLabelTemplate tLabelTemplate);

    /**
     * 查询标签打印
     *
     * @param id 标签打印主键
     * @return 标签打印
     */
    public TLabelTemplate selectTLabelTemplateById(Long id);

    /**
     * 新增标签打印
     *
     * @param tLabelTemplate 标签打印
     * @return 结果
     */
    public int insertTLabelTemplate(TLabelTemplate tLabelTemplate);

    /**
     * 修改标签打印
     *
     * @param tLabelTemplate 标签打印
     * @return 结果
     */
    public int updateTLabelTemplate(TLabelTemplate tLabelTemplate);

    /**
     * 批量删除标签打印
     *
     * @param ids 需要删除的标签打印主键集合
     * @return 结果
     */
    public int deleteTLabelTemplateByIds(Long[] ids);

    /**
     * 删除标签打印信息
     *
     * @param id 标签打印主键
     * @return 结果
     */
    public int deleteTLabelTemplateById(Long id);

    /**
     * 标签选择物料列表
     * @param labelByMaterialVo
     * @return
     */
    List<LabelByMaterialVo> getMaterialList(LabelByMaterialVo labelByMaterialVo);

}
