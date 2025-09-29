package com.xsrw.wms.base.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.vo.TGoodShelfVO;
import com.xsrw.wms.base.service.ITCodeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TGoodShelfMapper;
import com.xsrw.wms.base.domain.TGoodShelf;
import com.xsrw.wms.base.service.ITGoodShelfService;

/**
 * 货架Service业务层处理
 *
 * @author wxr
 * @date 2023-06-01
 */
@Service
public class TGoodShelfServiceImpl extends ServiceImpl<TGoodShelfMapper, TGoodShelf> implements ITGoodShelfService {
    @Autowired
    private TGoodShelfMapper tGoodShelfMapper;

    @Autowired
    private ITCodeConfigService codeConfigService;


    /**
     * 查询货架列表
     *
     * @param tGoodShelf 货架
     * @return 货架
     */
    @Override
    public List<TGoodShelfVO> selectTGoodShelfList(TGoodShelf tGoodShelf) {
        return tGoodShelfMapper.selectTGoodShelfList(tGoodShelf);
    }

    /**
     * 单表查询
     * @param tGoodShelf
     * @return
     */
    @Override
    public List<TGoodShelf> selectTGoodShelfSimpleList(TGoodShelf tGoodShelf) {
        return tGoodShelfMapper.selectTGoodShelfSimpleList(tGoodShelf);

    }

    /**
     * 查询货架
     *
     * @param id 货架主键
     * @return 货架
     */
    @Override
    public TGoodShelf selectTGoodShelfById(Long id) {
        return tGoodShelfMapper.selectById(id);
    }

    /**
     * 新增货架
     *
     * @param tGoodShelf 货架
     * @return 结果
     */
    @Override
    public int insertTGoodShelf(TGoodShelf tGoodShelf) {
        tGoodShelf.setCode(codeConfigService.getCode(CodeEnum.IHJ.getCodeName()));
        return tGoodShelfMapper.insert(tGoodShelf);
    }

    /**
     * 修改货架
     *
     * @param tGoodShelf 货架
     * @return 结果
     */
    @Override
    public int updateTGoodShelf(TGoodShelf tGoodShelf) {
        return tGoodShelfMapper.updateById(tGoodShelf);
    }


    /**
     * 批量删除货架
     *
     * @param ids 需要删除的货架主键
     * @return 结果
     */
    @Override
    public int deleteTGoodShelfByIds(Long[] ids) {
        return tGoodShelfMapper.deleteTGoodShelfByIds(ids);
    }

    /**
     * 删除货架信息
     *
     * @param id 货架主键
     * @return 结果
     */
    @Override
    public int deleteTGoodShelfById(Long id) {
        return tGoodShelfMapper.deleteTGoodShelfById(id);
    }

}
