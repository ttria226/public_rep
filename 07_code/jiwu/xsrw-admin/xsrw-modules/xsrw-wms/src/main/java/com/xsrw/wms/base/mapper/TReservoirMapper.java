package com.xsrw.wms.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TReservoir;
import com.xsrw.wms.base.domain.vo.TReservoirVO;
import com.xsrw.wms.kanban.domain.vo.WarehouseUseStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 库区Mapper接口
 *
 * @author wxr
 * @date 2023-05-05
 */
public interface TReservoirMapper extends BaseMapper<TReservoir> {

    /**
     * 查询库区列表
     *
     * @param tReservoir 库区
     * @return 库区集合
     */
    public List<TReservoirVO> selectTReservoirList(TReservoir tReservoir);


    /**
     * 删除库区
     *
     * @param id 库区主键
     * @return 结果
     */
    public int deleteTReservoirById(Long id);

    /**
     * 批量删除库区
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTReservoirByIds(Long[] ids);

    int deleteTReservoirStatusByIds(@Param("status") String status, @Param("id") Long id);

    /**
     * 库区列表
     * @return
     */
    List<Map<String,Object>>getReservoirList(@Param("areaId") Integer areaId);

    /**
     * 仓库使用情况统计
     * @param areaId
     * @return
     */
    List<WarehouseUseStatisticsVO> warehouseUseStatistics(@Param("areaId")Integer areaId);
}
