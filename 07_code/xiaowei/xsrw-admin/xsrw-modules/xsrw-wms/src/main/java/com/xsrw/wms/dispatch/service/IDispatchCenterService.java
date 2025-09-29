package com.xsrw.wms.dispatch.service;

import com.xsrw.wms.dispatch.domain.vo.AllotDispatchVO;
import com.xsrw.wms.dispatch.domain.vo.BusinessMonitorsVO;
import com.xsrw.wms.dispatch.domain.vo.WareHouseStatusVO;

import java.util.List;

/**
 * 调度中台Service接口
 *
 * @author tyk
 * @date 2023-06-25
 */
public interface IDispatchCenterService {
    /**
     * 业务监控列表
     * @param taskNo
     * @param materialName
     * @param taskStatus
     * @return
     */
    List<BusinessMonitorsVO> businessMonitorsList(String taskNo, String materialName, String taskStatus);

    /**
     * 仓库状态列表
     *
     * @param materialName
     * @param deptId
     * @return
     */
    List<WareHouseStatusVO> wareHouseStatusList(String materialName, Integer deptId);

    /**
     * 调拨调度列表
     * @param code
     * @param materialName
     * @param allotStatus
     * @return
     */
    List<AllotDispatchVO> allotDispatchList(String code, String materialName, String allotStatus);
}
