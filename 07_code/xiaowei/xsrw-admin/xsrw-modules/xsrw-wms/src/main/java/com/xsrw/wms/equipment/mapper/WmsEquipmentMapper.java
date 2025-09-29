package com.xsrw.wms.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.kanban.domain.vo.EquipmentStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 设备台账Mapper接口
 * 
 * @author zjj
 * @date 2023-05-08
 */
public interface WmsEquipmentMapper extends BaseMapper<WmsEquipment>
{

    /**
     * 查询设备台账列表
     * 
     * @param wmsEquipment 设备台账
     * @return 设备台账集合
     */
    public List<WmsEquipment> selectWmsEquipmentList(WmsEquipment wmsEquipment);


    /**
     * 删除设备台账
     * 
     * @param id 设备台账主键
     * @return 结果
     */
    public int deleteWmsEquipmentById(Long id);

    /**
     * 批量删除设备台账
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsEquipmentByIds(Long[] ids);

    @Select("SELECT u.nick_name FROM sys_user u WHERE u.user_id = #{userid}")
    String getUserByUserId(String userid);

    @Select("SELECT u.nick_name FROM sys_user u WHERE u.user_name = #{userName}")
    String getNickName(String userName);

    /**
     * 设备统计
     * @param beginDate
     * @return
     */
    EquipmentStatisticsVO equipmentStatistics(@Param("beginDate") Date beginDate);

}
