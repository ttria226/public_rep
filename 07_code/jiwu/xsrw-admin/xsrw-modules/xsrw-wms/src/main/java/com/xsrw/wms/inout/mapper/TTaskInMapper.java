package com.xsrw.wms.inout.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TTaskIn;
import com.xsrw.wms.inout.domain.vo.TTaskInVO;
import org.apache.ibatis.annotations.Param;

/**
 * 入库任务详情Mapper接口
 *
 * @author wxr
 * @date 2023-05-09
 */
public interface TTaskInMapper extends BaseMapper<TTaskIn> {

    /**
     * 查询入库任务详情列表
     *
     * @param tTaskIn 入库任务详情
     * @return 入库任务详情集合
     */
    public List<TTaskIn> selectTTaskInList(TTaskIn tTaskIn);


    /**
     * 删除入库任务详情
     *
     * @param id 入库任务详情主键
     * @return 结果
     */
    public int deleteTTaskInById(Long id);

    /**
     * 批量删除入库任务详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTTaskInByIds(Long[] ids);

    /**
     * 根据ids批量修改状态
     * @param ids
     * @param status
     * @return
     */
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") String status);

    List<Long> selectRegistrationIdList(List<Long> originIds);

    /**
     * 查询入库任务内容详情
     * @param ids
     * @return
     */
    List<TTaskInVO> selectTTaskInInfoByIds(@Param("ids") List<Long> ids);

    /**
     * 根据入库登记id查询详情
     * @param registrationId
     * @return
     */
    List<TTaskInVO> selectTTaskInInfoByRegistrationId(Long registrationId);







    /**
     * 当日入库数量
     * @return
     */
    Long getNowDayNum();


    /**
     * 当月入库总额
     * @return
     */
    BigDecimal inMonthMoney();


    /**
     * 重点物资月入库情况
     * @return
     */
    List<Map<String,Object>> inKeyPointMaterial();

}
