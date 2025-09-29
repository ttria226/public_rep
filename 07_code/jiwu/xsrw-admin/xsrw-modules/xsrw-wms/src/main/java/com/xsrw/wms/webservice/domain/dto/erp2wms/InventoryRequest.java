package com.xsrw.wms.webservice.domain.dto.erp2wms;

import java.util.List;

/**
 * ERP→WMS
  *  盘点请求
 */
public class InventoryRequest {
	
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
	
	private List<InventoryDetailRequest> inventoryDetailRequestList;

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

	public List<InventoryDetailRequest> getInventoryDetailRequestList() {
		return inventoryDetailRequestList;
	}

	public void setInventoryDetailRequestList(List<InventoryDetailRequest> inventoryDetailRequestList) {
		this.inventoryDetailRequestList = inventoryDetailRequestList;
	}
	
}
