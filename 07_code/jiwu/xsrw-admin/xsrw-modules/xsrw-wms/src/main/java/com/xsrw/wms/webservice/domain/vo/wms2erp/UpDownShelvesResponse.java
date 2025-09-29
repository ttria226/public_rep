package com.xsrw.wms.webservice.domain.vo.wms2erp;

/**
 * WMS→ERP
  * 上下架信息传输响应
 */
public class UpDownShelvesResponse {
	
	/** 工厂 */
	private String WERKS;
	
	/** 库存地点 */
	private String LGORT;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 转储单编号 */
	private String TANUM;
	
	/** 备用字段1 */
	private String ZZYL1;
	
	/** 备用字段2 */
	private String ZZYL2;
	
	/** 备用字段3 */
	private String ZZYL3;
	
	/** 备用字段4 */
	private String ZZYL4;
	
	/** 返回标识 */
	private String ZFLAG;
	
	/** 返回消息 */
	private String ZMSG;

	public String getWERKS() {
		return WERKS;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public String getLGORT() {
		return LGORT;
	}

	public void setLGORT(String lGORT) {
		LGORT = lGORT;
	}

	public String getLGNUM() {
		return LGNUM;
	}

	public void setLGNUM(String lGNUM) {
		LGNUM = lGNUM;
	}

	public String getTANUM() {
		return TANUM;
	}

	public void setTANUM(String tANUM) {
		TANUM = tANUM;
	}

	public String getZZYL1() {
		return ZZYL1;
	}

	public void setZZYL1(String zZYL1) {
		ZZYL1 = zZYL1;
	}

	public String getZZYL2() {
		return ZZYL2;
	}

	public void setZZYL2(String zZYL2) {
		ZZYL2 = zZYL2;
	}

	public String getZZYL3() {
		return ZZYL3;
	}

	public void setZZYL3(String zZYL3) {
		ZZYL3 = zZYL3;
	}

	public String getZZYL4() {
		return ZZYL4;
	}

	public void setZZYL4(String zZYL4) {
		ZZYL4 = zZYL4;
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
