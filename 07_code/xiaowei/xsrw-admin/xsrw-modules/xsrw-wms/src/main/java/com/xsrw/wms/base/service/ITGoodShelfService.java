package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TGoodShelf;
import com.xsrw.wms.base.domain.vo.TGoodShelfVO;

/**
 * 货架Service接口
 *
 * @author wxr
 * @date 2023-06-01
 */
public interface ITGoodShelfService extends IService<TGoodShelf> {

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
    List<TGoodShelf> selectTGoodShelfSimpleList(TGoodShelf tGoodShelf);

    /**
     * 查询货架
     *
     * @param id 货架主键
     * @return 货架
     */
    public TGoodShelf selectTGoodShelfById(Long id);

    /**
     * 新增货架
     *
     * @param tGoodShelf 货架
     * @return 结果
     */
    public int insertTGoodShelf(TGoodShelf tGoodShelf);

    /**
     * 修改货架
     *
     * @param tGoodShelf 货架
     * @return 结果
     */
    public int updateTGoodShelf(TGoodShelf tGoodShelf);

    /**
     * 批量删除货架
     *
     * @param ids 需要删除的货架主键集合
     * @return 结果
     */
    public int deleteTGoodShelfByIds(Long[] ids);

    /**
     * 删除货架信息
     *
     * @param id 货架主键
     * @return 结果
     */
    public int deleteTGoodShelfById(Long id);

}
