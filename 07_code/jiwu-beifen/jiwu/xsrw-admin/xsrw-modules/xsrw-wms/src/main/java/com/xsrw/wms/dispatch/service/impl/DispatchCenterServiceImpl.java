package com.xsrw.wms.dispatch.service.impl;

import com.xsrw.wms.dispatch.domain.vo.AllotDispatchVO;
import com.xsrw.wms.dispatch.domain.vo.BusinessMonitorsVO;
import com.xsrw.wms.dispatch.domain.vo.WareHouseStatusVO;
import com.xsrw.wms.dispatch.service.IDispatchCenterService;
import com.xsrw.wms.inout.mapper.TAllotMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 调度中台Service业务层处理
 *
 * @author tyk
 * @date 2023-06-25
 */
@Service
public class DispatchCenterServiceImpl implements IDispatchCenterService {
    @Resource
    private TTaskWcsMapper tTaskWcsMapper;

    @Resource
    private TAllotMapper allotMapper;


    /**
     * 业务监控列表
     * @param taskNo
     * @param materialName
     * @param taskStatus
     * @return
     */
    @Override
    public List<BusinessMonitorsVO> businessMonitorsList(String taskNo, String materialName, String taskStatus) {
        return tTaskWcsMapper.selectListByParam(taskNo,materialName,taskStatus);
    }

    /**
     * 仓库状态列表
     *
     * @param materialName
     * @param deptId
     * @return
     */
    @Override
    public List<WareHouseStatusVO> wareHouseStatusList(String materialName, Integer deptId) {
        return tTaskWcsMapper.selectListByKey(materialName,deptId);
    }

    /**
     * 调拨调度列表
     * @param code
     * @param materialName
     * @param allotStatus
     * @return
     */
    @Override
    public List<AllotDispatchVO> allotDispatchList(String code, String materialName, String allotStatus) {
        return allotMapper.selectListByParam(code,materialName,allotStatus);
    }
}
