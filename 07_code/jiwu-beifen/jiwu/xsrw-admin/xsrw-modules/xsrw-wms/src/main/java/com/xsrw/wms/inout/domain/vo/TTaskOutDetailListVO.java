package com.xsrw.wms.inout.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库任务详情对象 t_task_out
 *
 * @author zyq
 * @date 2023-05-08
 */
@TableName("t_task_out")
public class TTaskOutDetailListVO {
 private static final long serialVersionUID = 1L;



 @Excel(name = "库存")
 private Long stockId;

 /** 载具 */
 @Excel(name = "载具")
 private Long trayId;

 /** 库位 */
 @Excel(name = "库位")
 private Long locationId;

 /**
  * 预计拣货数量
  */
 private  Long predictCount;
 /**
  * 实际拣货数量
  */
 private  Long receiveCount;

 public Long getPredictCount() {
  return predictCount;
 }

 public void setPredictCount(Long predictCount) {
  this.predictCount = predictCount;
 }

 public Long getStockId() {
  return stockId;
 }

 public void setStockId(Long stockId) {
  this.stockId = stockId;
 }

 public Long getReceiveCount() {
  return receiveCount;
 }

 public void setReceiveCount(Long receiveCount) {
  this.receiveCount = receiveCount;
 }

 public Long getTrayId() {
  return trayId;
 }

 public void setTrayId(Long trayId) {
  this.trayId = trayId;
 }

 public Long getLocationId() {
  return locationId;
 }

 public void setLocationId(Long locationId) {
  this.locationId = locationId;
 }
}
