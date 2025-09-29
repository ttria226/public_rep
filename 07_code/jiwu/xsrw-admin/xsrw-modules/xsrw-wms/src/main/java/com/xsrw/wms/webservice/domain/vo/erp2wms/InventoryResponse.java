package com.xsrw.wms.webservice.domain.vo.erp2wms;

/**
 * ERP→WMS
  *  盘点响应
 */
public class InventoryResponse {
	
	/** 盘点凭证 */
	private String IVNUM;
	
	/** 操作状态（S成功；E失败） */
	private String ZFLAG;
	
	/** 错误原因 */
	private String ZMSG;

	public String getIVNUM() {
		return IVNUM;
	}

	public void setIVNUM(String iVNUM) {
		IVNUM = iVNUM;
	}

	public String getZFLAG() {
		return ZFLAG;
	}

	public void setZFLAG(String zFLAG) {
		ZFLAG = zFLAG;
	}

	public String getZMSG() {
		return ZMSG;
	}

	public void setZMSG(String zMSG) {
		ZMSG = zMSG;
	}
	
}
