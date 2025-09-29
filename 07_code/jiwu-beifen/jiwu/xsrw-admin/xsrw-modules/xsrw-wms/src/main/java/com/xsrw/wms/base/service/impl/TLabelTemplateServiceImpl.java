package com.xsrw.wms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.vo.LabelByMaterialVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TLabelTemplateMapper;
import com.xsrw.wms.base.domain.TLabelTemplate;
import com.xsrw.wms.base.service.ITLabelTemplateService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 标签打印Service业务层处理
 *
 * @author wxr
 * @date 2023-05-06
 */
@Service
public class TLabelTemplateServiceImpl extends ServiceImpl<TLabelTemplateMapper, TLabelTemplate> implements ITLabelTemplateService {
    @Autowired
    private TLabelTemplateMapper tLabelTemplateMapper;


    /**
     * 查询标签打印列表
     *
     * @param tLabelTemplate 标签打印
     * @return 标签打印
     */
    @Override
    public List<TLabelTemplate> selectTLabelTemplateList(TLabelTemplate tLabelTemplate) {
        return tLabelTemplateMapper.selectTLabelTemplateList(tLabelTemplate);
    }

    /**
     * 查询标签打印
     *
     * @param id 标签打印主键
     * @return 标签打印
     */
    @Override
    public TLabelTemplate selectTLabelTemplateById(Long id) {
        return tLabelTemplateMapper.selectById(id);
    }

    /**
     * 新增标签打印
     *
     * @param tLabelTemplate 标签打印
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTLabelTemplate(TLabelTemplate tLabelTemplate) {
        return tLabelTemplateMapper.insert(tLabelTemplate);
    }

    /**
     * 修改标签打印
     *
     * @param tLabelTemplate 标签打印
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTLabelTemplate(TLabelTemplate tLabelTemplate) {
        return tLabelTemplateMapper.updateById(tLabelTemplate);
    }


    /**
     * 批量删除标签打印
     *
     * @param ids 需要删除的标签打印主键
     * @return 结果
     */
    @Override
    public int deleteTLabelTemplateByIds(Long[] ids) {
        return tLabelTemplateMapper.deleteTLabelTemplateByIds(ids);
    }

    /**
     * 删除标签打印信息
     *
     * @param id 标签打印主键
     * @return 结果
     */
    @Override
    public int deleteTLabelTemplateById(Long id) {
        return tLabelTemplateMapper.deleteTLabelTemplateById(id);
    }

    /**
     * 标签选择物料列表
     * @param labelByMaterialVo
     * @return
     */
    @Override
    public List<LabelByMaterialVo> getMaterialList(LabelByMaterialVo labelByMaterialVo) {

        if (StringUtils.isNotEmpty(labelByMaterialVo.getMaterialCode())
                || StringUtils.isNotEmpty(labelByMaterialVo.getMaterialName())
                || StringUtils.isNotEmpty(labelByMaterialVo.getSpecifications())){

        }
        //todo wxr 标签选择物料，目前无库存表

//        List<LabelByMaterialVo> labelByMaterialVoList  = labelTemplateMapper.getMaterialList(materialId);
        List<LabelByMaterialVo> labelByMaterialVoList  = new ArrayList<>();
        return labelByMaterialVoList;
    }


}
