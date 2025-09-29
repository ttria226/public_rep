package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import org.apache.ibatis.annotations.Param;

/**
 * bom详情Mapper接口
 *
 * @author zjj
 * @date 2023-06-10
 */
public interface TBomDetailMapper extends BaseMapper<TBomDetail> {

    /**
     * 查询bom详情列表
     *
     * @param tBomDetail bom详情
     * @return bom详情集合
     */
    public List<TBomDetail> selectTBomDetailList(TBomDetail tBomDetail);


    /**
     * 删除bom详情
     *
     * @param id bom详情主键
     * @return 结果
     */
    public int deleteTBomDetailById(Long id);

    /**
     * 批量删除bom详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBomDetailByIds(Long[] ids);

    /**
     * 根据bom主键s批量删除
     *
     * @param bomId
     */
    void deleteTBomDetailByBomId(Long[] bomId);

    /**
     * 通过id获取bom详情物料信息列表
     *
     * @param bomId
     * @return
     */
    List<TMaterialVO> getMaterialListByBomId(Long bomId);

    /**
     * 通过id获取bom详情库存信息列表
     *
     * @param bomId
     * @return
     */
    List<TMaterialVO> selectTBomDetailStockList(@Param("bomId") Long bomId, @Param("id") Long id);

}
