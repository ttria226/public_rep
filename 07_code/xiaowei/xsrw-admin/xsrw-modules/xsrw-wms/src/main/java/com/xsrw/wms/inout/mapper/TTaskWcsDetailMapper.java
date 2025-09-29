package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.TTaskWcsDetail;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * wcs任务详情Mapper接口
 *
 * @author wxr
 * @date 2023-05-10
 */
@Repository
public interface TTaskWcsDetailMapper extends BaseMapper<TTaskWcsDetail> {

    /**
     * 查询wcs任务详情列表
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return wcs任务详情集合
     */
    public List<TTaskWcsDetail> selectTTaskWcsDetailList(TTaskWcsDetail tTaskWcsDetail);


    List<TTaskWcsDetailVO> selectStatusWcsListByTrayId(@Param("trayId") Long trayId, @Param("taskType") String taskType);


    /**
     * 删除wcs任务详情
     *
     * @param id wcs任务详情主键
     * @return 结果
     */
    public int deleteTTaskWcsDetailById(Long id);

    /**
     * 根据任务id删除任务详情
     * @param taskId
     * @return
     */
    public int deleteTTaskWcsDetailByTaskId(Long taskId);

    /**
     * 批量删除wcs任务详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTTaskWcsDetailByIds(Long[] ids);

    /**
     * 根据任务主表id获取子表列表
     * @param taskId
     * @return
     */
    List<TTaskWcsDetailVO> getListByTaskId(@Param("taskId") Long taskId, @Param("taskType") String taskType);

    List<TTaskWcsDetailVO> getShiftDetail(Long taskwcsId);

    /**
     * 根据任务id关联的入库单信息
     * @param taskId
     * @return
     */
    List<TAdvanceDelivery> getDeliveryIdsByTaskId(Long taskId);

    /**
     * 根据载具获取入库组盘信息
     * @param trayId
     * @return
     */
    List<TTaskWcsDetailVO> getDeliveryDetailByTray(Long trayId);

}
