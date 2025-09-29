package com.xsrw.wms.webservice.domain.vo.wms2erp;

/**
 * WMS→ERP
  * 盘点结果响应
 */
public class InventoryResultResponse {
	
	/** WMS系统任务号 */
	private String WMS_CODE;
	
	/** 盘点凭证 */
	private String IVNUM;
	
	/** 操作状态（S成功；E失败） */
	private String ZFLAG;
	
	/** 错误原因 */
	private String ZMSG;

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
