package com.xsrw.wms.webservice.domain.dto.wms2erp;

import java.math.BigDecimal;

/**
 * WMS→ERP
  * 盘点结果请求明细
 */
public class InventoryResultDetailRequest {
	
	/** 盘点凭证 */
	private String IVNUM;
	
	/** 行项目号 */
	private String IVPOS;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 仓储类型 */
	private String LGTYP;
	
	/** 仓储类型名称 */
	private String LTYPT;
	
	/** 仓位 */
	private String LGPLA;
	
	/** 工厂 */
	private String WERKS;
	
	/** 库存地点 */
	private String LGORT;
	
	/** 物料编码 */
	private String MATNR;
	
	/** 物料描述 */
	private String MAKTX;
	
	/** 批次 */
	private String CHARG;
	
	/** 库存状态 */
	private String BESTQ;
	
	/** 在库数量 */
	private BigDecimal GESME;
	
	/** 实盘数量 */
	private BigDecimal SPSL2;
	
	/** 基本计量单位 */
	private String MEINS;
	
	/** 特殊库存标识 */
	private String SOBKZ;
	
	/** 盘点日期 */
	private String PDATU;
	
	/** 创建人 */
	private String UNAME;
	
	/** WBS元素显示编号 */
	private String PS_POSNR;
	
	/** WBS元素的描述 */
	private String PS_PSPT;

	/** 预留字段1 */
	private String FIELD1;

	/** 预留字段2 */
	private String FIELD2;
	
	/** 预留字段3 */
	private String FIELD3;

	/** 预留字段4 */
	private String FIELD4;
	
	/** 预留字段5 */
	private String FIELD5;

	public String getIVNUM() {
		return IVNUM;
	}

	public void setIVNUM(String iVNUM) {
		IVNUM = iVNUM;
	}

	public String getIVPOS() {
		return IVPOS;
	}

	public void setIVPOS(String iVPOS) {
		IVPOS = iVPOS;
	}

	public String getLGNUM() {
		return LGNUM;
	}

	public void setLGNUM(String lGNUM) {
		LGNUM = lGNUM;
	}

	public String getLGTYP() {
		return LGTYP;
	}

	public void setLGTYP(String lGTYP) {
		LGTYP = lGTYP;
	}

	public String getLTYPT() {
		return LTYPT;
	}

	public void setLTYPT(String lTYPT) {
		LTYPT = lTYPT;
	}

	public String getLGPLA() {
		return LGPLA;
	}

	public void setLGPLA(String lGPLA) {
		LGPLA = lGPLA;
	}

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

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getMAKTX() {
		return MAKTX;
	}

	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
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

	public BigDecimal getGESME() {
		return GESME;
	}

	public void setGESME(BigDecimal gESME) {
		GESME = gESME;
	}

	public BigDecimal getSPSL2() {
		return SPSL2;
	}

	public void setSPSL2(BigDecimal sPSL2) {
		SPSL2 = sPSL2;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public String getSOBKZ() {
		return SOBKZ;
	}

	public void setSOBKZ(String sOBKZ) {
		SOBKZ = sOBKZ;
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

	public String getPS_POSNR() {
		return PS_POSNR;
	}

	public void setPS_POSNR(String pS_POSNR) {
		PS_POSNR = pS_POSNR;
	}

	public String getPS_PSPT() {
		return PS_PSPT;
	}

	public void setPS_PSPT(String pS_PSPT) {
		PS_PSPT = pS_PSPT;
	}

	public String getFIELD1() {
		return FIELD1;
	}

	public void setFIELD1(String fIELD1) {
		FIELD1 = fIELD1;
	}

	public String getFIELD2() {
		return FIELD2;
	}

	public void setFIELD2(String fIELD2) {
		FIELD2 = fIELD2;
	}

	public String getFIELD3() {
		return FIELD3;
	}

	public void setFIELD3(String fIELD3) {
		FIELD3 = fIELD3;
	}

	public String getFIELD4() {
		return FIELD4;
	}

	public void setFIELD4(String fIELD4) {
		FIELD4 = fIELD4;
	}

	public String getFIELD5() {
		return FIELD5;
	}

	public void setFIELD5(String fIELD5) {
		FIELD5 = fIELD5;
	}
	
}
