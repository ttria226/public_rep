package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 任务设备执行记录对象 t_task_wcs_record
 *
 * @author wxr
 * @date 2023-10-23
 */
@TableName("t_task_wcs_record")
public class TTaskWcsRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联wcs任务id
     */
    @Excel(name = "关联wcs任务id")
    private Long taskWcsId;

    /**
     * 任务类型：wcs agv:picking agv:slim
     */
    private String wcsType;

    /**
     * 起始位置
     */
    @Excel(name = "起始位置")
    private String startPosition;

    /**
     * 目的位置
     */
    @Excel(name = "目的位置")
    private String purposePosition;

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
     * 执行订单id
     */
    @Excel(name = "执行订单id")
    private Long orderId;

    /**
     * 状态
     */
    @Excel(name = "状态", readConverterExp = "0=失败;1=成功")
    private String status;

    /**
     * 接口类型 1发送任务  2接受上报信息
     */
    private String interfaceType;
    /**
     * 主任务编号（双伸位移库时候）
     */
    private String mainTaskNo;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskWcsId(Long taskWcsId) {
        this.taskWcsId = taskWcsId;
    }

    public Long getTaskWcsId() {
        return taskWcsId;
    }

    public String getWcsType() {
        return wcsType;
    }

    public void setWcsType(String wcsType) {
        this.wcsType = wcsType;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setPurposePosition(String purposePosition) {
        this.purposePosition = purposePosition;
    }

    public String getPurposePosition() {
        return purposePosition;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public String getSendData() {
        return sendData;
    }

    public void setAcceptData(String acceptData) {
        this.acceptData = acceptData;
    }

    public String getAcceptData() {
        return acceptData;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getMainTaskNo() {
        return mainTaskNo;
    }

    public void setMainTaskNo(String mainTaskNo) {
        this.mainTaskNo = mainTaskNo;
    }

    @Override
    public String toString() {
        return "TTaskWcsRecord{" +
                "id=" + id +
                ", taskWcsId=" + taskWcsId +
                ", wcsType='" + wcsType + '\'' +
                ", startPosition='" + startPosition + '\'' +
                ", purposePosition='" + purposePosition + '\'' +
                ", sendData='" + sendData + '\'' +
                ", acceptData='" + acceptData + '\'' +
                ", orderId=" + orderId +
                ", status='" + status + '\'' +
                ", interfaceType='" + interfaceType + '\'' +
                '}';
    }
}
