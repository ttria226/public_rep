package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TBom;
import com.xsrw.wms.base.domain.vo.TMaterialVO;

/**
 * bomService接口
 *
 * @author zjj
 * @date 2023-06-10
 */
public interface ITBomService extends IService<TBom> {

    /**
     * 查询bom列表
     *
     * @param tBom bom
     * @return bom集合
     */
    public List<TBom> selectTBomList(TBom tBom);

    /**
     * 查询bom
     *
     * @param id bom主键
     * @return bom
     */
    public TBom selectTBomById(Long id);

    /**
     * 新增bom
     *
     * @param tBom bom
     * @return 结果
     */
    public int insertTBom(TBom tBom);

    /**
     * 修改bom
     *
     * @param tBom bom
     * @return 结果
     */
    public int updateTBom(TBom tBom);

    /**
     * 批量删除bom
     *
     * @param ids 需要删除的bom主键集合
     * @return 结果
     */
    public int deleteTBomByIds(Long[] ids);

    /**
     * 删除bom信息
     *
     * @param id bom主键
     * @return 结果
     */
    public int deleteTBomById(Long id);

    /**
     * 根据bom-id获取物料信息列表
     * @param id
     * @return
     */
    List<TMaterialVO> getMaterialListByBomId(Long id);

    /**
     * 通过id获取bom详情库存信息列表
     */
    List<TMaterialVO> selectTBomDetailStockList(Long id);

}
