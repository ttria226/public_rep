package com.xsrw.wms.inout.domain.vo;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wcs任务对象 t_task_wcs
 *
 * @author wxr
 * @date 2023-05-10
 */
@TableName("t_task_wcs")
public class TTaskWcsOutVO{

    /**
     * 主键
     */
    private Long id;


    /**
     * 物料id
     */
    private String materialId;


    private String trayCode;




    /** 实际拣货数量 */
    @Excel(name = "实际拣货数量")
    private Long receiveCount;

    /** 小件实际拣货数量 */
    @Excel(name = "小件实际拣货数量")
    private Long smallReceiveCount;

    @Excel(name = "小件领取的物料")
    private String rfid;

    /** 强制出库物料选择的rfid-PDA **/
    @TableField(exist = false)
    private List<Map<String,Object>> rfidListPda;

    /** 强制出库物料选择的rfid-PC **/
    private List<String> rfidList;


    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Long getSmallReceiveCount() {
        return smallReceiveCount;
    }

    public void setSmallReceiveCount(Long smallReceiveCount) {
        this.smallReceiveCount = smallReceiveCount;
    }

    public List<Map<String, Object>> getRfidListPda() {
        return rfidListPda;
    }

    public void setRfidListPda(List<Map<String, Object>> rfidListPda) {
        this.rfidListPda = rfidListPda;
    }

    public List<String> getRfidList() {
        return rfidList;
    }

    public void setRfidList(List<String> rfidList) {
        this.rfidList = rfidList;
    }
}
