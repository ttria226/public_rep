package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TTaskIn;
import com.xsrw.wms.inout.domain.vo.TTaskInVO;

/**
 * 入库任务详情Service接口
 *
 * @author wxr
 * @date 2023-05-09
 */
public interface ITTaskInService extends IService<TTaskIn> {

    /**
     * 查询入库任务详情列表
     *
     * @param tTaskIn 入库任务详情
     * @return 入库任务详情集合
     */
    public List<TTaskIn> selectTTaskInList(TTaskIn tTaskIn);

    /**
     * 查询入库任务详情
     *
     * @param id 入库任务详情主键
     * @return 入库任务详情
     */
    public TTaskIn selectTTaskInById(Long id);

    /**
     * 新增入库任务详情
     *
     * @param tTaskIn 入库任务详情
     * @return 结果
     */
    public int insertTTaskIn(TTaskIn tTaskIn);

    /**
     * 修改入库任务详情
     *
     * @param tTaskIn 入库任务详情
     * @return 结果
     */
    public int updateTTaskIn(TTaskIn tTaskIn);

    /**
     * 批量删除入库任务详情
     *
     * @param ids 需要删除的入库任务详情主键集合
     * @return 结果
     */
    public int deleteTTaskInByIds(Long[] ids);

    /**
     * 删除入库任务详情信息
     *
     * @param id 入库任务详情主键
     * @return 结果
     */
    public int deleteTTaskInById(Long id);


    /**
     * 入库执行后更新状态
     * @param tTaskInList
     * @return
     */
    int executeEndTask(List<TTaskInVO> tTaskInList);

    /**
     * 根据ids查询对应的详情信息
     * @param originIds
     * @return
     */
    List<TTaskInVO> selectTTaskInInfoByIds(List<Long> originIds);

    /**
     * 根据入库登记id查询详情
     * @param id
     * @return
     */
    List<TTaskInVO> selectTTaskInInfoByRegistrationId(Long id);

}
