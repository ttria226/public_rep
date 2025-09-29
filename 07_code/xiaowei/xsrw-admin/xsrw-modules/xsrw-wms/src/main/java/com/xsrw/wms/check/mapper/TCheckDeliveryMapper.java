package com.xsrw.wms.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.check.domain.TCheckDelivery;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.vo.CheckDeliveryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点计划Mapper接口
 * 
 * @author lyx
 * @date 2023-05-09
 */
public interface TCheckDeliveryMapper extends BaseMapper<TCheckDelivery>
{

    /**
     * 查询盘点计划列表
     * 
     * @param tCheckDelivery 盘点计划
     * @return 盘点计划集合
     */
    public List<TCheckDelivery> selectTCheckDeliveryList(TCheckDelivery tCheckDelivery);


    /**
     * 删除盘点计划
     * 
     * @param id 盘点计划主键
     * @return 结果
     */
    public int deleteTCheckDeliveryById(Long id);

    /**
     * 批量删除盘点计划
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckDeliveryByIds(Long[] ids);

    /**
     * 查询盘点计划列表
     *
     * @param checkDelivery 盘点计划
     * @return 盘点计划集合
     */
    List<CheckDeliveryVO> selectCheckDeliveryList(CheckDeliveryDTO checkDelivery);

    List<CheckDeliveryVO> selectCheckMaterialDetailList(CheckDeliveryDTO checkDelivery);

    public int updateCheckDeliveryStatus(@Param("planId")Long planId,@Param("status") String status,@Param("taskId") Long taskId);
}
