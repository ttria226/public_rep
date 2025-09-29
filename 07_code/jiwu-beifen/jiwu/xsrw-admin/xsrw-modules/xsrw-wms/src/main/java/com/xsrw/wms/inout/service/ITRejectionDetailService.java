package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.TRejectionDetail;
import com.xsrw.wms.inout.domain.dto.TRejectionDetailDTO;
import com.xsrw.wms.inout.domain.vo.TRejectionDetailVO;

/**
 * 拒收管理Service接口
 *
 * @author wxr
 * @date 2023-05-09
 */
public interface ITRejectionDetailService extends IService<TRejectionDetail> {

    /**
     * 查询拒收管理列表
     *
     * @param tRejectionDetail 拒收管理
     * @return 拒收管理集合
     */
    public List<TRejectionDetailVO> selectTRejectionDetailList(TRejectionDetailDTO tRejectionDetail);

    /**
     * 查询拒收管理
     *
     * @param id 拒收管理主键
     * @return 拒收管理
     */
    public TRejectionDetail selectTRejectionDetailById(Long id);

    /**
     * 新增拒收管理
     *
     * @param tRejectionDetail 拒收管理
     * @return 结果
     */
    public int insertTRejectionDetail(TRejectionDetail tRejectionDetail);

    /**
     * 修改拒收管理
     *
     * @param tRejectionDetail 拒收管理
     * @return 结果
     */
    public int updateTRejectionDetail(TRejectionDetail tRejectionDetail);

    /**
     * 批量删除拒收管理
     *
     * @param ids 需要删除的拒收管理主键集合
     * @return 结果
     */
    public int deleteTRejectionDetailByIds(Long[] ids);

    /**
     * 删除拒收管理信息
     *
     * @param id 拒收管理主键
     * @return 结果
     */
    public int deleteTRejectionDetailById(Long id);

    Boolean saveRejectionList(List<TRejectionDetail> deliveryDetailList);

}
