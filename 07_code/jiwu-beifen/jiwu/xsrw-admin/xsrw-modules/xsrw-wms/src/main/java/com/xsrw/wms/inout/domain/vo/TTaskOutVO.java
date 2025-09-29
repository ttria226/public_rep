package com.xsrw.wms.inout.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.inout.domain.TTaskOut;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 出库任务详情对象 t_task_out
 *
 * @author zyq
 * @date 2023-05-08
 */
public class TTaskOutVO extends TTaskOut {


    private List<TTaskOutDetailListVO> tTaskOutDetailListVOS;

    public List<TTaskOutDetailListVO> gettTaskOutDetailListVOS() {
        return tTaskOutDetailListVOS;
    }

    public void settTaskOutDetailListVOS(List<TTaskOutDetailListVO> tTaskOutDetailListVOS) {
        this.tTaskOutDetailListVOS = tTaskOutDetailListVOS;
    }
}
