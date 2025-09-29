package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvanceRegistration;
import com.xsrw.wms.inout.domain.dto.TAdvanceRegistrationDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceRegistrationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 入库登记Mapper接口
 *
 * @author wxr
 * @date 2023-05-09
 */
@Repository
public interface TAdvanceRegistrationMapper extends BaseMapper<TAdvanceRegistration> {

    /**
     * 查询入库登记列表
     *
     * @param tAdvanceRegistration 入库登记
     * @return 入库登记集合
     */
    public List<TAdvanceRegistrationVO> selectTAdvanceRegistrationList(TAdvanceRegistrationDTO tAdvanceRegistration);


    /**
     * 删除入库登记
     *
     * @param id 入库登记主键
     * @return 结果
     */
    public int deleteTAdvanceRegistrationById(Long id);

    /**
     * 批量删除入库登记
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvanceRegistrationByIds(Long[] ids);

    List<TAdvanceRegistrationVO> selectRegistrationInfoList(@Param("ids") List<Long> ids);

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    TAdvanceRegistrationVO selectInfoById(Long id);

    /**
     * 根据ids批量更新状态
     * @param ids
     * @return
     */
    int updateNextFlagByIds(@Param("ids") List<Long> ids);
}
