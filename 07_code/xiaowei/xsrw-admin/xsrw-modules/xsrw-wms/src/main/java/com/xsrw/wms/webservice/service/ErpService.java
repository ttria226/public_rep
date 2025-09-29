package com.xsrw.wms.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.webservice.domain.dto.erp2wms.InOutRequest;
import com.xsrw.wms.webservice.domain.dto.erp2wms.InventoryRequest;
import com.xsrw.wms.webservice.domain.dto.erp2wms.StockQueryRequest;
import com.xsrw.wms.webservice.domain.vo.erp2wms.InOutResponse;
import com.xsrw.wms.webservice.domain.vo.erp2wms.InventoryResponse;
import com.xsrw.wms.webservice.domain.vo.erp2wms.StockQueryResponse;

@WebService
public interface ErpService {

	/**
	 * ERP-WMS
	  *  出入库
	 */
	@WebMethod(operationName="wsInOut")
	public List<InOutResponse> inOut(List<InOutRequest> inOutRequestList);

	/**
	 * ERP-WMS
	  * 盘点
	 */
	@WebMethod(operationName="wsInventory")
	public List<InventoryResponse> inventory(List<InventoryRequest> inventoryRequestList);

	/**
	 * ERP-WMS
	  * 库存查询
	 */
	@WebMethod(operationName="wsStockQuery")
	public StockQueryResponse stockQuery(List<StockQueryRequest> stockQueryRequestList);

	AjaxResult test(String ivnum);

}
