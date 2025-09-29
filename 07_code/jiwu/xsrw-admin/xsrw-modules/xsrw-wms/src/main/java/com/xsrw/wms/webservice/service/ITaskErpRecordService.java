package com.xsrw.wms.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.webservice.domain.TTaskErpRecord;

import java.util.List;

/**
 * @author 863Soft
 * @date 2024/8/27
 * @description <p>备注：</p>
 */
public interface ITaskErpRecordService extends IService<TTaskErpRecord> {


    /**
     * 查询列表
     * @param taskErpRecord
     * @return
     */
    List<TTaskErpRecord> selectList(TTaskErpRecord taskErpRecord);

    /**
     * 重新发送
     * @param id
     * @return
     */
    AjaxResult sendRecord(Long id);
}
