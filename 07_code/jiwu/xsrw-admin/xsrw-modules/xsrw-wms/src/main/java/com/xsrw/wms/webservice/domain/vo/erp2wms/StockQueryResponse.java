package com.xsrw.wms.webservice.domain.vo.erp2wms;

/**
 * ERP→WMS
  * 仓位库存查询响应
 */
public class StockQueryResponse {
	
	/** 工厂 */
	private String WERKS;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 操作状态 */
	private String ZFLAG;
	
	/** 错误原因 */
	private String ZMSG;

	public String getWERKS() {
		return WERKS;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public String getLGNUM() {
		return LGNUM;
	}

	public void setLGNUM(String lGNUM) {
		LGNUM = lGNUM;
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
