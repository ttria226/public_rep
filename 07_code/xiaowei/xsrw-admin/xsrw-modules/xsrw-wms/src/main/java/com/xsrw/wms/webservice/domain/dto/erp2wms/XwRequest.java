package com.xsrw.wms.webservice.domain.dto.erp2wms;

import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJWHCZCSQXX;

/**
 * 
 *封装小微参数对象传给机务库
 *
 */
public class XwRequest {

	private ZWMXSJWHCZCSQXX params;
	
	private Long wcsId;
	
	private String zzdjbm;
	
	private String erpType;
	
	private String type;

	public ZWMXSJWHCZCSQXX getParams() {
		return params;
	}

	public void setParams(ZWMXSJWHCZCSQXX params) {
		this.params = params;
	}

	public Long getWcsId() {
		return wcsId;
	}

	public void setWcsId(Long wcsId) {
		this.wcsId = wcsId;
	}

	public String getZzdjbm() {
		return zzdjbm;
	}

	public void setZzdjbm(String zzdjbm) {
		this.zzdjbm = zzdjbm;
	}

	public String getErpType() {
		return erpType;
	}

	public void setErpType(String erpType) {
		this.erpType = erpType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
