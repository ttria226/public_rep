package com.xsrw.wms.webservice.domain.dto.wms2erp;

import java.math.BigDecimal;

/**
 * WMS→ERP
  * 仓位移动请求
 */
public class PositionMovementRequest {
	
	/** 工厂 */
	private String WERKS;
	
	/** 库存地点 */
	private String LGORT;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 用户编码 */
	private String ZZYHZH;
	
	/** 操作类型（1物料移动 2  整托盘移动） */
	private String ZZYWLX;
	
	/** 移动类型（999） */
	private String BWLVS;
	
	/** 物料号 */
	private String MATNR;
	
	/** 批次 */
	private String CHARG;
	
	/** 库存类别 */
	private String BESTQ;
	
	/** 特殊库存标识 */
	private String SOBKZ;
	
	/** WBS 元素（项目号） */
	private String PSPNR;
	
	/** 数量 */
	private BigDecimal GESME;
	
	/** 源仓位条码 */
	private String ZYCWTM;
	
	/** 目标仓位条码 */
	private String ZMDCWTM;
	
	/** 备用字段1 */
	private String ZZYL1;
	
	/** 备用字段2 */
	private String ZZYL2;
	
	/** 备用字段3 */
	private String ZZYL3;
	
	/** 备用字段4 */
	private String ZZYL4;

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

	public String getZZYHZH() {
		return ZZYHZH;
	}

	public void setZZYHZH(String zZYHZH) {
		ZZYHZH = zZYHZH;
	}

	public String getZZYWLX() {
		return ZZYWLX;
	}

	public void setZZYWLX(String zZYWLX) {
		ZZYWLX = zZYWLX;
	}

	public String getBWLVS() {
		return BWLVS;
	}

	public void setBWLVS(String bWLVS) {
		BWLVS = bWLVS;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getCHARG() {
		return CHARG;
	}

	public void setCHARG(String cHARG) {
		CHARG = cHARG;
	}

	public String getBESTQ() {
		return BESTQ;
	}

	public void setBESTQ(String bESTQ) {
		BESTQ = bESTQ;
	}

	public String getSOBKZ() {
		return SOBKZ;
	}

	public void setSOBKZ(String sOBKZ) {
		SOBKZ = sOBKZ;
	}

	public String getPSPNR() {
		return PSPNR;
	}

	public void setPSPNR(String pSPNR) {
		PSPNR = pSPNR;
	}

	public BigDecimal getGESME() {
		return GESME;
	}

	public void setGESME(BigDecimal gESME) {
		GESME = gESME;
	}

	public String getZYCWTM() {
		return ZYCWTM;
	}

	public void setZYCWTM(String zYCWTM) {
		ZYCWTM = zYCWTM;
	}

	public String getZMDCWTM() {
		return ZMDCWTM;
	}

	public void setZMDCWTM(String zMDCWTM) {
		ZMDCWTM = zMDCWTM;
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
	
}
