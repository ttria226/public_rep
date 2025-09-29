package com.xsrw.wms.webservice.domain.vo.erp2wms;

/**
 * ERP→WMS
  *  出入库响应
 */
public class InOutResponse {
	
	/** 单据编号 */
	private String ZZDJBM;
	
	/** 单据行号 */
	private String ZZDJHH;
	
	/** 返回处理标识 */
	private String ZRETURNFLAG;
	
	/** 返回提示信息 */
	private String ZMESSAGE;

	public String getZZDJBM() {
		return ZZDJBM;
	}

	public void setZZDJBM(String zZDJBM) {
		ZZDJBM = zZDJBM;
	}

	public String getZZDJHH() {
		return ZZDJHH;
	}

	public void setZZDJHH(String zZDJHH) {
		ZZDJHH = zZDJHH;
	}

	public String getZRETURNFLAG() {
		return ZRETURNFLAG;
	}

	public void setZRETURNFLAG(String zRETURNFLAG) {
		ZRETURNFLAG = zRETURNFLAG;
	}

	public String getZMESSAGE() {
		return ZMESSAGE;
	}

	public void setZMESSAGE(String zMESSAGE) {
		ZMESSAGE = zMESSAGE;
	}
}
