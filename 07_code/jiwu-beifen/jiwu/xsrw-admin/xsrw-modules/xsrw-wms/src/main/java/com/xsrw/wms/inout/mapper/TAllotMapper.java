package com.xsrw.wms.inout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.dispatch.domain.vo.AllotDispatchVO;
import com.xsrw.wms.inout.domain.TAllot;
import com.xsrw.wms.inout.domain.vo.TAllotVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调拨单Mapper接口
 *
 * @author zjj
 * @date 2023-06-26
 */
public interface TAllotMapper extends BaseMapper<TAllot>
{

    /**
     * 查询调拨单列表
     *
     * @param tAllot 调拨单
     * @return 调拨单集合
     */
    public List<TAllotVO> selectTAllotList(TAllot tAllot);


    /**
     * 删除调拨单
     *
     * @param id 调拨单主键
     * @return 结果
     */
    public int deleteTAllotById(Long id);

    /**
     * 批量删除调拨单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAllotByIds(Long[] ids);

    /**
     * 根据单号更新状态
     * @param allotStatus
     * @param code
     * @return
     */
    int updateStatusByCode(@Param("allotStatus") String allotStatus,@Param("code") String code);

    /**
     * 调拨调度列表
     * @param code
     * @param materialName
     * @param allotStatus
     * @return
     */
    List<AllotDispatchVO> selectListByParam(@Param("code")String code,@Param("materialName") String materialName,@Param("allotStatus") String allotStatus);
}
