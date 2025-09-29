package com.xsrw.wms.webservice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * erp同步记录表 t_task_erp_record
 *
 * @author zhanglc
 * @date 2024-08-09
 */
@TableName("t_task_erp_record")
public class TTaskErpRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 关联id
     */
    private Long taskWcsId;
    /**
     * 关联批量id
     */
    private String paramId;

    /**
     * 任务类型（1上架 2下架 3盘点单 5移库）
     */
    @Excel(name = "任务类型（1上架 2下架 3盘点单 5移库）")
    private String erpType;

    /**
     * 单据编码
     */
    private String zzdjbm;

    /**
     * 单据行号
     */
    private String zzdjhh;
    /**
     * 请求参数
     */
    @Excel(name = "请求参数")
    private String sendData;

    /**
     * 返回数据
     */
    @Excel(name = "返回数据")
    private String acceptData;

    /**
     * 状态
     */
    @Excel(name = "状态", readConverterExp = "0=失败,1=成功")
    /**
     * 是机务库还是小微库 1:机务 2:小微
     */
    private String type;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskWcsId() {
        return taskWcsId;
    }

    public void setTaskWcsId(Long taskWcsId) {
        this.taskWcsId = taskWcsId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getErpType() {
        return erpType;
    }

    public void setErpType(String erpType) {
        this.erpType = erpType;
    }

    public String getZzdjbm() {
        return zzdjbm;
    }

    public void setZzdjbm(String zzdjbm) {
        this.zzdjbm = zzdjbm;
    }

    public String getZzdjhh() {
        return zzdjhh;
    }

    public void setZzdjhh(String zzdjhh) {
        this.zzdjhh = zzdjhh;
    }

    public String getSendData() {
        return sendData;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public String getAcceptData() {
        return acceptData;
    }

    public void setAcceptData(String acceptData) {
        this.acceptData = acceptData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TTaskErpRecord() {

    }

    public TTaskErpRecord(Long taskWcsId, String paramId, String erpType, String sendData, String status) {
        this.taskWcsId = taskWcsId;
        this.paramId = paramId;
        this.erpType = erpType;
        this.sendData = sendData;
        this.status = status;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    

}
