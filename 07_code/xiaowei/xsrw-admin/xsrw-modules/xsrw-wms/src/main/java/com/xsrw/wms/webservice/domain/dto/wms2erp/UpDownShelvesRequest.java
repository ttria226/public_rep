package com.xsrw.wms.webservice.domain.dto.wms2erp;

import java.math.BigDecimal;

/**
 * WMS→ERP
  * 上下架信息传输请求
 */
public class UpDownShelvesRequest {
	
	/** 工厂 */
	private String WERKS;
	
	/** 库存地点 */
	private String LGORT;
	
	/** 用户编码 */
	private String ZZYHZH;
	
	/** 仓库号 */
	private String LGNUM;
	
	/** 装运类型 */
	private String TRART;
	
	/** 转移要求号 */
	private String TBNUM;
	
	/** 行项目 */
	private String TBPOS;
	
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
	
	/** 物资条码 */
	private String ZZWZTM;
	
	/** 仓位条码 */
	private String ZZCWTM;
	
	/** 数量 */
	private BigDecimal MENGE;
	
	/** 确认转储单项目（固定值X） */
	private String CONFORM;
	
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

	public String getZZYHZH() {
		return ZZYHZH;
	}

	public void setZZYHZH(String zZYHZH) {
		ZZYHZH = zZYHZH;
	}

	public String getLGNUM() {
		return LGNUM;
	}

	public void setLGNUM(String lGNUM) {
		LGNUM = lGNUM;
	}

	public String getTRART() {
		return TRART;
	}

	public void setTRART(String tRART) {
		TRART = tRART;
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

	public String getZZWZTM() {
		return ZZWZTM;
	}

	public void setZZWZTM(String zZWZTM) {
		ZZWZTM = zZWZTM;
	}

	public String getZZCWTM() {
		return ZZCWTM;
	}

	public void setZZCWTM(String zZCWTM) {
		ZZCWTM = zZCWTM;
	}

	public BigDecimal getMENGE() {
		return MENGE;
	}

	public void setMENGE(BigDecimal mENGE) {
		MENGE = mENGE;
	}

	public String getCONFORM() {
		return CONFORM;
	}

	public void setCONFORM(String cONFORM) {
		CONFORM = cONFORM;
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
