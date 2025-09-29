package com.xsrw.wms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.dto.DemandCheckDTO;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.DemandCheckVO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 库位Service接口
 *
 * @author wxr
 * @date 2023-05-05
 */
public interface ITLocationService extends IService<TLocation> {

    /**
     * 查询库位列表
     *
     * @param tLocation 库位
     * @return 库位集合
     */
    public List<TLocationVO> selectTLocationList(TLocationDTO tLocation);

    /**
     * 查询库位
     *
     * @param id 库位主键
     * @return 库位
     */
    public TLocation selectTLocationById(Long id);

    /**
     * 新增库位
     *
     * @param tLocation 库位
     * @return 结果
     */
    public int insertTLocation(TLocation tLocation);

    /**
     * 修改库位
     *
     * @param tLocation 库位
     * @return 结果
     */
    public int updateTLocation(TLocation tLocation);

    /**
     * 批量删除库位
     *
     * @param ids 需要删除的库位主键集合
     * @return 结果
     */
    public int deleteTLocationByIds(Long[] ids);

    /**
     * 删除库位信息
     *
     * @param id 库位主键
     * @return 结果
     */
    public int deleteTLocationById(Long id);

    /**
     * 库位批量修改
     * @param location
     * @return
     */
    AjaxResult plcUpdate(TLocationDTO location);

    /**
     * 导入库位信息
     * @param file
     * @return
     * @throws Exception
     */
    AjaxResult importUnit(MultipartFile file) throws Exception;

    /**
     * 启用禁用
     * @param id
     * @param status
     * @return
     */
    AjaxResult updateStatusById(Long id, String status);

    /**
     * 获取最大的排列层数
     * @param location
     * @return
     */
    Map<String, Integer> plcCount(TLocation location);

    int pinsertTLocation(TLocationDTO locationDto);

    /**
     * 通过区域、库区查询库位
     * @param areaId 区域
     * @param reservoirId 库区
     * @return
     */
    List<TLocation> locationList(Long areaId,Long reservoirId);

    /**
     * @description: 推荐库位--非固定托盘
     * @param categoryId
     */
    List<TLocation> recommendLoactionMove(Long categoryId);

    /**
     * 获取空闲库位列表
     * @return
     */
    List<TLocation> getOtherLocation(Long locationId);
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
    List<Map<String,Object>>getLocationCurrentDetail(Integer locationRow,Long reservoirId);
    /**
     * 获取库位上物料信息
     * @param locationId
     * @return
     */
    List<Map<String,Object>>getLocationStock(Long locationId);
    List<Map<String,Object>>getLocationListByReservoirId(Long locationId);

    /**
     * 查询库位列表
     * @param tLocation
     * @return
     */
    List<TLocation> selectSimpleList(TLocation tLocation);

    /**
     * 获取库位列表
     * @param tLocation
     * @return
     */
    List<TLocationVO> getLocationList(TLocationDTO tLocation);

    /**
     * 通过ids批量获取库位信息
     * @param locations
     * @return
     */
    Map<Long, TLocationVO> getLocationByIds(List<Long> locations);

    /**
     * 需盘点列表
     * @param request
     * @return
     */
    List<DemandCheckVO> demandCheckList(DemandCheckDTO request);

    /**
     * 修改货位状态(标记有货/标记无货)
     * @param tLocation
     * @return
     */
    AjaxResult updateGoodsAllocationStatus(TLocation tLocation);

    /**
     * 通过编号查询详情
     * @param code
     * @return
     */
    TLocation getByCode(String code);
}
