package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TAdvanceRegistrationApiDTO;
import com.xsrw.wms.inout.domain.TAdvanceRegistration;
import com.xsrw.wms.inout.domain.dto.TAdvanceRegistrationDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceRegistrationVO;

/**
 * 入库登记Service接口
 *
 * @author wxr
 * @date 2023-05-09
 */
public interface ITAdvanceRegistrationService extends IService<TAdvanceRegistration> {

    /**
     * 查询入库登记列表
     *
     * @param tAdvanceRegistration 入库登记
     * @return 入库登记集合
     */
    public List<TAdvanceRegistrationVO> selectTAdvanceRegistrationList(TAdvanceRegistrationDTO tAdvanceRegistration);

    /**
     * 查询入库登记
     *
     * @param id 入库登记主键
     * @return 入库登记
     */
    public TAdvanceRegistrationVO selectTAdvanceRegistrationById(Long id);

    /**
     * 新增入库登记
     *
     * @param tAdvanceRegistration 入库登记
     * @return 结果
     */
    public int insertTAdvanceRegistration(TAdvanceRegistration tAdvanceRegistration);

    /**
     * 修改入库登记
     *
     * @param tAdvanceRegistration 入库登记
     * @return 结果
     */
    public int updateTAdvanceRegistration(TAdvanceRegistration tAdvanceRegistration);

    /**
     * 批量删除入库登记
     *
     * @param ids 需要删除的入库登记主键集合
     * @return 结果
     */
    public int deleteTAdvanceRegistrationByIds(Long[] ids);

    /**
     * 删除入库登记信息
     *
     * @param id 入库登记主键
     * @return 结果
     */
    public int deleteTAdvanceRegistrationById(Long id);

    /**
     * 生成上架任务
     * @param tAdvanceRegistration
     * @return
     */
    AjaxResult putaway(TAdvanceRegistrationDTO tAdvanceRegistration);

    /**
     * 根据ids获取列表
     * @param advanceRegistrationIds
     * @return
     */
    List<TAdvanceRegistration> getListByIds(List<Long> advanceRegistrationIds);


    /**
     * 地堆上架
     * @param tAdvanceRegistration
     * @return
     */
    AjaxResult floorStocking(TAdvanceRegistrationDTO tAdvanceRegistration);

    /**
     * pda上架
     * @param tAdvanceRegistrationApiDTO
     * @return
     */
    AjaxResult putawayTask(TAdvanceRegistrationApiDTO tAdvanceRegistrationApiDTO);

    /**
     * 通过物料ids获取对应的推荐载具类型
     * @param ids
     * @return
     */
    AjaxResult getTrayTypeByMaterials(Long[] ids);

}
