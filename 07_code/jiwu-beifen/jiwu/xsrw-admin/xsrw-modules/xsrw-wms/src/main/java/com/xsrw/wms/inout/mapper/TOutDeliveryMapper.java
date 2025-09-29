package com.xsrw.wms.inout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialSelectVO;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryVO;
import com.xsrw.wms.kanban.domain.vo.TaskStatusVO;
import com.xsrw.wms.report.domain.dto.ReportCenterDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 出库单Mapper接口
 *
 * @author zyq
 * @date 2023-05-09
 */
@Repository
public interface TOutDeliveryMapper extends BaseMapper<TOutDelivery> {

    /**
     * 查询出库单列表
     *
     * @param tOutDelivery 出库单
     * @return 出库单集合
     */
    public List<TOutDeliveryVO> selectTOutDeliveryList(TOutDelivery tOutDelivery);


    /**
     * 删除出库单
     *
     * @param id 出库单主键
     * @return 结果
     */
    public int deleteTOutDeliveryById(Long id);

    public List<TMaterialSelectVO> getMaterialSelectList(TMaterialDTO tMaterial);

    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutDeliveryByIds(Long[] ids);

    /**
     * 批量删除出库单详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutDeliveryDetailByAdvanceDeliveryIds(Long[] ids);

    /**
     * 批量新增出库单详情
     *
     * @param tOutDeliveryDetailList 出库单详情列表
     * @return 结果
     */
    public int batchTOutDeliveryDetail(List<TOutDeliveryDetail> tOutDeliveryDetailList);


    /**
     * 通过出库单主键删除出库单详情信息
     *
     * @param id 出库单ID
     * @return 结果
     */
    public int deleteTOutDeliveryDetailByAdvanceDeliveryId(Long id);

    List<Map<String, Object>> materilaCollect(@Param("dto") ReportCenterDTO reportCenterDTO);


    List<TOutDelivery> getMergeList(TOutDelivery tOutDelivery);

    TaskStatusVO selectOutCount();
}
