package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvanceQuality;
import com.xsrw.wms.inout.domain.dto.TAdvanceQualityDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceQualityVO;
import org.springframework.stereotype.Repository;

/**
 * 入库质检单Mapper接口
 *
 * @author wxr
 * @date 2023-06-05
 */
@Repository
public interface TAdvanceQualityMapper extends BaseMapper<TAdvanceQuality> {

    /**
     * 查询入库质检单列表
     *
     * @param tAdvanceQuality 入库质检单
     * @return 入库质检单集合
     */
    public List<TAdvanceQualityVO> selectTAdvanceQualityList(TAdvanceQualityDTO tAdvanceQuality);


    /**
     * 删除入库质检单
     *
     * @param id 入库质检单主键
     * @return 结果
     */
    public int deleteTAdvanceQualityById(Long id);

    /**
     * 批量删除入库质检单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvanceQualityByIds(Long[] ids);
}
