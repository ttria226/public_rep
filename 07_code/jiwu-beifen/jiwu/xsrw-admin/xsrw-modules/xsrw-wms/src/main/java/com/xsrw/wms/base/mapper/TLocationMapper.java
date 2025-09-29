package com.xsrw.wms.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.dto.DemandCheckDTO;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.DemandCheckVO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.report.domain.vo.FrequencyOfLocationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 库位Mapper接口
 *
 * @author wxr
 * @date 2023-05-05
 */
@Repository
public interface TLocationMapper extends BaseMapper<TLocation> {

    /**
     * 查询库位列表
     *
     * @param tLocation 库位
     * @return 库位集合
     */
    public List<TLocationVO> selectTLocationList(TLocationDTO tLocation);


    /**
     * 删除库位
     *
     * @param id 库位主键
     * @return 结果
     */
    public int deleteTLocationById(Long id);

    /**
     * 批量删除库位
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTLocationByIds(Long[] ids);

    void deleteTLocationStatusByReservoirIds(@Param("status") String status, @Param("id") Long ids);

    /**
     * 库位批量修改
     * @param location
     * @return
     */
    int plcUpdate(TLocationDTO location);

    /**
     * 获取最大的排列层数
     * @param location
     * @return
     */
    Map<String, Integer> plcCount(TLocation location);

    /**
     * 获取空闲库位列表
     * @param areaId
     * @return
     */
    List<TLocation> selectOtherList(@Param("areaId") Long areaId);

    /**
     * 库位使用情况
     * @return
     */
    List<Map<String,Object>>getLocationUsedInfo(Long reservoirId);

    /**
     * 库位实时情况
     * @param locationRow
     * @param reservoirId
     * @return
     */
    List<Map<String,Object>>getLocationCurrentDetail(@Param("locationRow") Integer locationRow,@Param("reservoirId") Long reservoirId);
    /**
     * 获取库位上物料信息
     * @param locationId
     * @return
     */
    List<Map<String,Object>>getLocationStock(Long locationId);

    List<Map<String,Object>>getLocationListByReservoirId(Long locationId);

    int updateNullById(@Param("tLocation") TLocation tLocation);

    /**
     * 获取推荐库位
     * @param locationDTO
     * @return
     */
    List<TLocation> getRecommendedLocationList(@Param("loca") TLocation locationDTO,@Param("removeIds") List<Long> removeLocations);

    /**
     * 获取库位列表
     * @param tLocation
     * @return
     */
    List<TLocationVO> getLocationList(TLocationDTO tLocation);

    List<FrequencyOfLocationVO> frequencyOfLocationList(@Param("areaId") Integer areaId,@Param("reservoirId")  Integer reservoirId);

    /**
     * 需盘点列表
     * @param request
     * @return
     */
    List<DemandCheckVO> demandCheckList(DemandCheckDTO request);

    /**
     * 查询
     * @param tLocation
     * @return
     */
    TLocation selectByFirstNodeId(TLocation tLocation);

    /**
     * 通过库位id查询同楼层暂存位信息
     * @param trayId
     * @return
     */
    TLocation selectTempLocationInfoById(@Param("trayId") Long trayId, @Param("tempPallet") String tempPallet);
}
