package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvancePut;
import com.xsrw.wms.inout.domain.dto.TAdvancePutDTO;
import com.xsrw.wms.inout.domain.vo.TAdvancePutVO;
import org.springframework.stereotype.Repository;

/**
 * 入库入库单Mapper接口
 *
 * @author wxr
 * @date 2023-06-05
 */
@Repository
public interface TAdvancePutMapper extends BaseMapper<TAdvancePut> {

    /**
     * 查询入库入库单列表
     *
     * @param tAdvancePut 入库入库单
     * @return 入库入库单集合
     */
    public List<TAdvancePutVO> selectTAdvancePutList(TAdvancePutDTO tAdvancePut);


    /**
     * 删除入库入库单
     *
     * @param id 入库入库单主键
     * @return 结果
     */
    public int deleteTAdvancePutById(Long id);

    /**
     * 批量删除入库入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvancePutByIds(Long[] ids);
}
