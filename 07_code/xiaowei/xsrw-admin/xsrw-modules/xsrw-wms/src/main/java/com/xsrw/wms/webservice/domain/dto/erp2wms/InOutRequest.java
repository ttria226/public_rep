package com.xsrw.wms.webservice.domain.dto.erp2wms;

import java.math.BigDecimal;

/**
 * ERP→WMS
  *  出入库请求
 */
public class InOutRequest {
	
	/** 装运类型 */
	private String TRART;
	
	/** 业务类型 */
	private String ZZYWLX;
	
	/** 单据编码 */
	private String ZZDJBM;
	
	/** 单据行号 */
	private String ZZDJHH;
	
	/** 工厂 */
	private String WERKS;
	
	/** 库存地点 */
	private String LGORT;

	/** 仓库号 */
	private String LGNUM;

	/** 转移要求号 */
	private String TBNUM;

	/** 行项目 */
	private String TBPOS;

	/** 物料号 */
	private String MATNR;

	/** 物料描述 */
	private String MAKTX;

	/** 物料长描述 */
	private String MAKTG;

	/** 物料组 */
	private String MATKL;

	/** 物料组描述 */
	private String WGBEZ;

	/** 批次 */
	private String CHARG;

	/** 库存类别 */
	private String BESTQ;

	/** 特殊库存标识 */
	private String SOBKZ;

	/** WBS元素(项目号） */
	private String PSPNR;

	/** 数量 */
	private BigDecimal MENGE;

	/** 计量单位 */
	private String MEINS;

	/** 金额 */
	private BigDecimal DMBTR;

	/** 供应商编号 */
	private String LIFNR;

	/** 供应商描述 */
	private String NAME1;

	/** 采购订单号 */
	private String EBELN;

	/** 采购订单行项目 */
	private String EBELP;

	/** 领料单号 */
	private String RSNUM;

	/** 领料单行项目 */
	private String RSPOS;

	/** 用户名 */
	private String USNAM_MKPF;

	/** 备用字段1 */
	private String ZZYL1;

	/** 备用字段2 */
	private String ZZYL2;
	
	/** 备用字段3 */
	private String ZZYL3;

	public String getTRART() {
		return TRART;
	}

	public void setTRART(String tRART) {
		TRART = tRART;
	}

	public String getZZYWLX() {
		return ZZYWLX;
	}

	public void setZZYWLX(String zZYWLX) {
		ZZYWLX = zZYWLX;
	}

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

	public String getTBNUM() {
		return TBNUM;
	}

	public void setTBNUM(String tBNUM) {
		TBNUM = tBNUM;
	}

	public String getTBPOS() {
		return TBPOS;
	}

	public void setTBPOS(String tBPOS) {
		TBPOS = tBPOS;
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

	public String getMAKTG() {
		return MAKTG;
	}

	public void setMAKTG(String mAKTG) {
		MAKTG = mAKTG;
	}

	public String getMATKL() {
		return MATKL;
	}

	public void setMATKL(String mATKL) {
		MATKL = mATKL;
	}

	public String getWGBEZ() {
		return WGBEZ;
	}

	public void setWGBEZ(String wGBEZ) {
		WGBEZ = wGBEZ;
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

	public BigDecimal getMENGE() {
		return MENGE;
	}

	public void setMENGE(BigDecimal mENGE) {
		MENGE = mENGE;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public BigDecimal getDMBTR() {
		return DMBTR;
	}

	public void setDMBTR(BigDecimal dMBTR) {
		DMBTR = dMBTR;
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

	public String getEBELN() {
		return EBELN;
	}

	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}

	public String getEBELP() {
		return EBELP;
	}

	public void setEBELP(String eBELP) {
		EBELP = eBELP;
	}

	public String getRSNUM() {
		return RSNUM;
	}

	public void setRSNUM(String rSNUM) {
		RSNUM = rSNUM;
	}

	public String getRSPOS() {
		return RSPOS;
	}

	public void setRSPOS(String rSPOS) {
		RSPOS = rSPOS;
	}

	public String getUSNAM_MKPF() {
		return USNAM_MKPF;
	}

	public void setUSNAM_MKPF(String uSNAM_MKPF) {
		USNAM_MKPF = uSNAM_MKPF;
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

}
