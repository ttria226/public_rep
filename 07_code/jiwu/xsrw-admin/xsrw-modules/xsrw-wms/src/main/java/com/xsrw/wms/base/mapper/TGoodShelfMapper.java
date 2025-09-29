package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TGoodShelf;
import com.xsrw.wms.base.domain.vo.TGoodShelfVO;

/**
 * 货架Mapper接口
 *
 * @author wxr
 * @date 2023-06-01
 */
public interface TGoodShelfMapper extends BaseMapper<TGoodShelf> {

    /**
     * 查询货架列表
     *
     * @param tGoodShelf 货架
     * @return 货架集合
     */
    public List<TGoodShelfVO> selectTGoodShelfList(TGoodShelf tGoodShelf);

    /**
     * 单表查询
     * @param tGoodShelf
     * @return
     */
    public List<TGoodShelf> selectTGoodShelfSimpleList(TGoodShelf tGoodShelf);


    /**
     * 删除货架
     *
     * @param id 货架主键
     * @return 结果
     */
    public int deleteTGoodShelfById(Long id);

    /**
     * 批量删除货架
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTGoodShelfByIds(Long[] ids);
}
