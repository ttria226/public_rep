package com.xsrw.wms.webservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.webservice.domain.TTaskErpRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存-ERPMapper接口
 *
 * @author zhanglc
 * @date 2024-08-09
 */
@Repository
public interface TTaskErpRecordMapper extends BaseMapper<TTaskErpRecord> {

    /**
     * 查询列表
     *
     * @param taskErpRecord
     * @return
     */
    List<TTaskErpRecord> selectRecordList(TTaskErpRecord taskErpRecord);

}
