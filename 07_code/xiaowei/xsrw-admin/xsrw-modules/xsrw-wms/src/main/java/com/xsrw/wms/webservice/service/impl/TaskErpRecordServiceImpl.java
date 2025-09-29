package com.xsrw.wms.webservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.webservice.domain.TTaskErpRecord;
import com.xsrw.wms.webservice.mapper.TTaskErpRecordMapper;
import com.xsrw.wms.webservice.service.ITaskErpRecordService;
import com.xsrw.wms.webservice.util.WmsToErpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 863Soft
 * @date 2024/8/27
 * @description <p>备注：</p>
 */
@Service
public class TaskErpRecordServiceImpl extends ServiceImpl<TTaskErpRecordMapper, TTaskErpRecord> implements ITaskErpRecordService {
    @Autowired
    private TTaskErpRecordMapper tTaskErpRecordMapper;
    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;
    @Autowired
    private WmsToErpUtils wmsToErpUtils;

    /**
     * 查询列表
     *
     * @param taskErpRecord
     * @return
     */
    @Override
    public List<TTaskErpRecord> selectList(TTaskErpRecord taskErpRecord) {
        return tTaskErpRecordMapper.selectRecordList(taskErpRecord);
    }

    /**
     * 重新发送
     *
     * @param id
     * @return
     */
    @Override
    public AjaxResult sendRecord(Long id) {
        TTaskErpRecord record = tTaskErpRecordMapper.selectById(id);
        if (record != null) {
            if ("1".equals(record.getErpType())) {
                wmsToErpUtils.inoutPut(Constants.TASK_TYPE_PUT, record.getTaskWcsId(), record.getZzdjbm(), record.getZzdjhh());
            } else if ("2".equals(record.getErpType())) {
                wmsToErpUtils.inoutPut(Constants.TASK_TYPE_PICK, record.getTaskWcsId(), record.getZzdjbm(), record.getZzdjhh());
            } else if ("3".equals(record.getErpType())) {
                wmsToErpUtils.checkPut(Long.valueOf(record.getParamId()));
            } else if ("5".equals(record.getErpType())) {
                TTaskWcs tTaskWcsVO = tTaskWcsMapper.selectById(record.getTaskWcsId());
                if (tTaskWcsVO != null) {
                    wmsToErpUtils.movePut(tTaskWcsVO);
                }
            }
        }
        return AjaxResult.success();
    }
}
