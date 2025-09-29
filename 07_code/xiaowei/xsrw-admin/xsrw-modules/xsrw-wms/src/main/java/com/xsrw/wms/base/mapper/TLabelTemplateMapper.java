package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TLabelTemplate;
import com.xsrw.wms.base.domain.vo.LabelByMaterialVo;

/**
 * 标签打印Mapper接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface TLabelTemplateMapper extends BaseMapper<TLabelTemplate> {

    /**
     * 查询标签打印列表
     *
     * @param tLabelTemplate 标签打印
     * @return 标签打印集合
     */
    public List<TLabelTemplate> selectTLabelTemplateList(TLabelTemplate tLabelTemplate);


    /**
     * 删除标签打印
     *
     * @param id 标签打印主键
     * @return 结果
     */
    public int deleteTLabelTemplateById(Long id);

    /**
     * 批量删除标签打印
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTLabelTemplateByIds(Long[] ids);

    List<LabelByMaterialVo> getMaterialList(LabelByMaterialVo labelByMaterialVo);

}
