package com.xsrw.wms.webservice.domain.dto.erp2wms;

import java.math.BigDecimal;

/**
 * ERP→WMS
  * 仓位库存查询请求
 */
public class StockQueryRequest {
	
	/** 工厂 */
	private String WERKS;
	
	/** 库存地点 */
	private String LGORT;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 存储类型 */
	private String LGTYP;
	
	/** 物料号 */
	private String MATNR;
	
	/** 物料描述 */
	private String MAKT;
	
	/** 批次 */
	private String CHARG;
	
	/** 仓位 */
	private String LGPLA;
	
	/** 存储区 */
	private String LGBER;
	
	/** 供应商 */
	private String LIFNR;
	
	/** 供应商名称 */
	private String NAME1;
	
	/** 仓位存量 */
	private BigDecimal GESME;
	
	/** 单价 */
	private BigDecimal VERPR;
	
	/** 总价 */
	private BigDecimal SALKV;
	
	/** 计量单位 */
	private String MEINS;
	
	/** 库存类别 */
	private String BESTQ;
	
	/** 特殊库存标识 */
	private String SOBKZ;
	
	/** 特殊库存编号 */
	private String POSID;

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

	public String getLGTYP() {
		return LGTYP;
	}

	public void setLGTYP(String lGTYP) {
		LGTYP = lGTYP;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getMAKT() {
		return MAKT;
	}

	public void setMAKT(String mAKT) {
		MAKT = mAKT;
	}

	public String getCHARG() {
		return CHARG;
	}

	public void setCHARG(String cHARG) {
		CHARG = cHARG;
	}

	public String getLGPLA() {
		return LGPLA;
	}

	public void setLGPLA(String lGPLA) {
		LGPLA = lGPLA;
	}

	public String getLGBER() {
		return LGBER;
	}

	public void setLGBER(String lGBER) {
		LGBER = lGBER;
	}

	public String getLIFNR() {
		return LIFNR;
	}

	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}

	public String getNAME1() {
		return NAME1;
	}

	public void setNAME1(String nAME1) {
		NAME1 = nAME1;
	}

	public BigDecimal getGESME() {
		return GESME;
	}

	public void setGESME(BigDecimal gESME) {
		GESME = gESME;
	}

	public BigDecimal getVERPR() {
		return VERPR;
	}

	public void setVERPR(BigDecimal vERPR) {
		VERPR = vERPR;
	}

	public BigDecimal getSALKV() {
		return SALKV;
	}

	public void setSALKV(BigDecimal sALKV) {
		SALKV = sALKV;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
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

	public String getPOSID() {
		return POSID;
	}

	public void setPOSID(String pOSID) {
		POSID = pOSID;
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
