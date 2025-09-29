package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOutRecheck;
import com.xsrw.wms.inout.domain.vo.TOutRecheckVO;
import org.springframework.stereotype.Repository;

/**
 * 出库复核单Mapper接口
 *
 * @author wxr
 * @date 2023-06-07
 */
@Repository
public interface TOutRecheckMapper extends BaseMapper<TOutRecheck> {

    /**
     * 查询出库复核单列表
     *
     * @param tOutRecheck 出库复核单
     * @return 出库复核单集合
     */
    public List<TOutRecheckVO> selectTOutRecheckList(TOutRecheck tOutRecheck);


    /**
     * 删除出库复核单
     *
     * @param id 出库复核单主键
     * @return 结果
     */
    public int deleteTOutRecheckById(Long id);

    /**
     * 批量删除出库复核单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutRecheckByIds(Long[] ids);
}
