package com.xsrw.wms.webservice.domain.dto.wms2erp;

import java.util.List;

/**
 * WMS→ERP
  * 盘点结果请求
 */
public class InventoryResultRequest {
	
	/** WMS系统任务号 */
	private String WMS_CODE;
	
	/** 盘点凭证 */
	private String IVNUM;
	
	/** 盘点时间 */
	private String PDATU;
	
	/** 创建人 */
	private String UNAME;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 工厂 */
	private String WERKS;
	
	private List<InventoryResultDetailRequest> inventoryResultDetailRequestList;

	public String getWMS_CODE() {
		return WMS_CODE;
	}

	public void setWMS_CODE(String wMS_CODE) {
		WMS_CODE = wMS_CODE;
	}

	public String getIVNUM() {
		return IVNUM;
	}

	public void setIVNUM(String iVNUM) {
		IVNUM = iVNUM;
	}

	public String getPDATU() {
		return PDATU;
	}

	public void setPDATU(String pDATU) {
		PDATU = pDATU;
	}

	public String getUNAME() {
		return UNAME;
	}

	public void setUNAME(String uNAME) {
		UNAME = uNAME;
	}

	public String getLGNUM() {
		return LGNUM;
	}

	public void setLGNUM(String lGNUM) {
		LGNUM = lGNUM;
	}

	public String getWERKS() {
		return WERKS;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public List<InventoryResultDetailRequest> getInventoryResultDetailRequestList() {
		return inventoryResultDetailRequestList;
	}

	public void setInventoryResultDetailRequestList(List<InventoryResultDetailRequest> inventoryResultDetailRequestList) {
		this.inventoryResultDetailRequestList = inventoryResultDetailRequestList;
	}
	
}
