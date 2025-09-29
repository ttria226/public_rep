package com.xsrw.wms.inout.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.inout.domain.TTaskOut;
import com.xsrw.wms.inout.domain.vo.PADTOutDeliveryDetailVO;
import com.xsrw.wms.stock.domain.TStock;
import org.apache.ibatis.annotations.Param;

/**
 * 出库任务详情Mapper接口
 *
 * @author zyq
 * @date 2023-05-08
 */
public interface TTaskOutMapper extends BaseMapper<TTaskOut> {

    /**
     * 查询出库任务详情列表
     *
     * @param tTaskOut 出库任务详情
     * @return 出库任务详情集合
     */
    public List<TTaskOut> selectTTaskOutList(TTaskOut tTaskOut);


    /**
     * 执行出库选择载具列表
     *
     * @return 出库任务详情集合
     */
    public List<TTrayVO> selectTTrayList(TStock tStock);


    public PADTOutDeliveryDetailVO scanTray(@Param("trayId") String trayId, @Param("rfid") String rfid);


    public Map scanTrayNew(@Param("trayId") Long trayId);


    /**
     * 删除出库任务详情
     *
     * @param id 出库任务详情主键
     * @return 结果
     */
    public int deleteTTaskOutById(Long id);

    /**
     * 批量删除出库任务详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTTaskOutByIds(Long[] ids);










    /**
     * 当日出库库数量
     * @return
     */
    Long getNowDayNum();

    /**
     * 当月出库总额
     * @return
     */
    BigDecimal outMonthMoney();

    /**
     * 重点物资月入库情况
     * @return
     */
    List<Map<String,Object>> outKeyPointMaterial();
}
