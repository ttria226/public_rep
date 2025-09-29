package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TRejectionDetail;
import com.xsrw.wms.inout.domain.dto.TRejectionDetailDTO;
import com.xsrw.wms.inout.domain.vo.TRejectionDetailVO;

/**
 * 拒收管理Mapper接口
 *
 * @author wxr
 * @date 2023-05-09
 */
public interface TRejectionDetailMapper extends BaseMapper<TRejectionDetail> {

    /**
     * 查询拒收管理列表
     *
     * @param tRejectionDetail 拒收管理
     * @return 拒收管理集合
     */
    public List<TRejectionDetailVO> selectTRejectionDetailList(TRejectionDetailDTO tRejectionDetail);


    /**
     * 删除拒收管理
     *
     * @param id 拒收管理主键
     * @return 结果
     */
    public int deleteTRejectionDetailById(Long id);

    /**
     * 批量删除拒收管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRejectionDetailByIds(Long[] ids);
}
