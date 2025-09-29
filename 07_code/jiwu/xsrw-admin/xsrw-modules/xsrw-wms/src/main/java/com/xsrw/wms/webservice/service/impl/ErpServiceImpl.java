package com.xsrw.wms.webservice.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.jws.WebService;

import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.stock.domain.vo.TStockVO;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.check.domain.TCheckDelivery;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryVO;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryService;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.wms.webservice.domain.TErpInOut;
import com.xsrw.wms.webservice.domain.TErpInventory;
import com.xsrw.wms.webservice.domain.TErpInventoryDetail;
import com.xsrw.wms.webservice.domain.TErpStock;
import com.xsrw.wms.webservice.domain.dto.erp2wms.InOutRequest;
import com.xsrw.wms.webservice.domain.dto.erp2wms.InventoryDetailRequest;
import com.xsrw.wms.webservice.domain.dto.erp2wms.InventoryRequest;
import com.xsrw.wms.webservice.domain.dto.erp2wms.StockQueryRequest;
import com.xsrw.wms.webservice.domain.vo.erp2wms.InOutResponse;
import com.xsrw.wms.webservice.domain.vo.erp2wms.InventoryResponse;
import com.xsrw.wms.webservice.domain.vo.erp2wms.StockQueryResponse;
import com.xsrw.wms.webservice.service.ErpService;
import com.xsrw.wms.webservice.service.ITErpInOutService;
import com.xsrw.wms.webservice.service.ITErpInventoryDetailService;
import com.xsrw.wms.webservice.service.ITErpInventoryService;
import com.xsrw.wms.webservice.service.ITErpStockService;
import com.xsrw.wms.webservice.util.HttpRequestUtil;

@Component
@WebService(endpointInterface = "com.xsrw.wms.webservice.service.ErpService", targetNamespace = "http://service.xsrw.com/")
public class ErpServiceImpl implements ErpService {

    private static ITErpStockService tErpStockService;

    @Autowired
    public void setTErpStockService(ITErpStockService tErpStockService) {
        ErpServiceImpl.tErpStockService = tErpStockService;
    }

    private static ITErpInOutService tErpInOutService;

    @Autowired
    public void setTErpInOutService(ITErpInOutService tErpInOutService) {
        ErpServiceImpl.tErpInOutService = tErpInOutService;
    }

    private static ITErpInventoryService tErpInventoryService;

    @Autowired
    public void setTErpInventoryService(ITErpInventoryService tErpInventoryService) {
        ErpServiceImpl.tErpInventoryService = tErpInventoryService;
    }

    private static ITErpInventoryDetailService tErpInventoryDetailService;

    @Autowired
    public void setTErpInventoryDetailService(ITErpInventoryDetailService tErpInventoryDetailService) {
        ErpServiceImpl.tErpInventoryDetailService = tErpInventoryDetailService;
    }

    private static ITCheckDeliveryService tCheckDeliveryService;

    @Autowired
    public void setTCheckDeliveryService(ITCheckDeliveryService tCheckDeliveryService) {
        ErpServiceImpl.tCheckDeliveryService = tCheckDeliveryService;
    }

    private static ITMaterialService materialService;

    @Autowired
    public void setTMaterialService(ITMaterialService materialService) {
        ErpServiceImpl.materialService = materialService;
    }

    private static ITAdvanceDeliveryService tAdvanceDeliveryService;

    @Autowired
    public void setTAdvanceDeliveryService(ITAdvanceDeliveryService tAdvanceDeliveryService) {
        ErpServiceImpl.tAdvanceDeliveryService = tAdvanceDeliveryService;
    }

    private static ITOutDeliveryService tOutDeliveryService;

    @Autowired
    public void setTOutDeliveryService(ITOutDeliveryService tOutDeliveryService) {
        ErpServiceImpl.tOutDeliveryService = tOutDeliveryService;
    }

    private static ITStockService tStockService;

    @Autowired
    public void setTStockService(ITStockService tStockService) {
        ErpServiceImpl.tStockService = tStockService;
    }
    
    @Value("${web.xiaowei.url}")
    private String xiaoweiUrl;

    /**
     * ERP-WMS
     * 出入库
     */
    @Override
    public List<InOutResponse> inOut(List<InOutRequest> inOutRequestList) {
        Object o = JSONArray.toJSONString(inOutRequestList);
        System.out.println("出入库===============" + o.toString());
        List<InOutResponse> inOutResponseList = new ArrayList<InOutResponse>();

        for (InOutRequest i : inOutRequestList) {
        	//判断是机务库数据还是小微库数据,若是小微数据则通过httpClient调用接口将数据传给小微库
        	if("5866".equals(i.getWERKS()) && "YW1".equals(i.getLGNUM())) {
	            i.setMATNR(i.getMATNR().substring(10));//截取一下物料，去掉前导零
	
	            TErpInOut t = new TErpInOut();
	            t.setMatnr(i.getMATNR());
	            t.setTrart(i.getTRART());
	            t.setZzywlx(i.getZZYWLX());
	            t.setZzdjbm(i.getZZDJBM());
	            t.setZzdjhh(i.getZZDJHH());
	            t.setWerks(i.getWERKS());
	            t.setLgort(i.getLGORT());
	            t.setLgnum(i.getLGNUM());
	            t.setTbnum(i.getTBNUM());
	            t.setTbpos(i.getTBPOS());
	            t.setMaktx(i.getMAKTX());
	            t.setMaktg(i.getMAKTG());
	            t.setMatkl(i.getMATKL());
	            t.setWgbez(i.getWGBEZ());
	            t.setCharg(i.getCHARG());
	            t.setBestq(i.getBESTQ());
	            t.setSobkz(i.getSOBKZ());
	            t.setPspnr(i.getPSPNR());
	            t.setMenge(i.getMENGE());
	            t.setMeins(i.getMEINS());
	            t.setDmbtr(i.getDMBTR());
	            t.setLifnr(i.getLIFNR());
	            t.setName1(i.getNAME1());
	            t.setEbeln(i.getEBELN());
	            t.setEbelp(i.getEBELP());
	            t.setRsnum(i.getRSNUM());
	            t.setRspos(i.getRSPOS());
	            t.setUsnamMkpf(i.getUSNAM_MKPF());
	            t.setZzyl1(i.getZZYL1());
	            t.setZzyl2(i.getZZYL2());
	            t.setZzyl3(i.getZZYL3());
	
	            tErpInOutService.insertTErpInOut(t);
        	}else {
        		String url = xiaoweiUrl+"/webservice/erp/record/saveByErp";
        		
        		String jsonString = HttpRequestUtil.doPost(url, JSON.toJSONString(i), "utf-8");
        		System.out.println("httpClient 调用小微接口->jsonString=" + jsonString);
        	}
        }

        TAdvanceDeliveryDTO tAdvanceDelivery = new TAdvanceDeliveryDTO();
        List<TAdvanceDeliveryDetail> deliveryDetailList = new ArrayList<TAdvanceDeliveryDetail>();
        TOutDeliveryVO tOutDeliveryVO = new TOutDeliveryVO();
        List<TOutDeliveryDetail> tOutDeliveryDetailList = new ArrayList<>();
        if (inOutRequestList != null && !inOutRequestList.isEmpty()) {
            //根据物料编码获取对应的物料信息
            List<String> materialCodes = inOutRequestList.stream().map(InOutRequest::getMATNR).collect(Collectors.toList());
            Map<String, TMaterial> materialMap = materialService.getMaterialByCodes(materialCodes);
            //------不知道数据具体是什么样的
            InOutRequest requestInfo = inOutRequestList.get(0);
            String type = requestInfo.getTRART();
            if("5866".equals(requestInfo.getWERKS()) && "YW1".equals(requestInfo.getLGNUM())) {
	            if ("E".equals(type)) {
	                //入库
	                tAdvanceDelivery.setType("0");//erp
	                tAdvanceDelivery.setCode(requestInfo.getZZDJBM());
	                tAdvanceDelivery.setOriginCode(requestInfo.getZZDJBM());
	                tAdvanceDelivery.setNewLocal("2");//来源：2erp接口
	                tAdvanceDelivery.setCreateBy("ERP");//ERP
	                tAdvanceDelivery.setDeptName("ERP");//ERP
	                tAdvanceDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_ORDER);
	                for (InOutRequest erpDto : inOutRequestList) {
	                	if("5866".equals(erpDto.getWERKS()) && "YW1".equals(erpDto.getLGNUM())) {
		                    InOutResponse inOutResponse = new InOutResponse();
		
		                    TAdvanceDeliveryDetail detail = new TAdvanceDeliveryDetail();
		                    //截取一下物料，去掉前导零
		//					erpDto.setMATNR(erpDto.getMATNR().substring(10));
		                    TMaterial materialDO = materialMap.get(erpDto.getMATNR());
		                    if (materialDO != null) {
		                        detail.setMaterialId(materialDO.getId());//物料
		                    } else {
		                        Long materialId = materialService.saveInToMaterial(erpDto);
		                        if (materialId != null) {
		                            detail.setMaterialId(materialId);//物料
		                        } else {
		                            continue;
		                        }
		                    }
		                    detail.setBatchCode(erpDto.getCHARG());//批次
		                    detail.setTbpos(erpDto.getZZDJHH());//行号
		                    detail.setPredictCount(erpDto.getMENGE());//数量
		                    detail.setCreateBy("ERP");//ERP
		                    detail.setDeptName("ERP");//ERP
		                    deliveryDetailList.add(detail);
		
		                    inOutResponse.setZZDJBM(erpDto.getZZDJBM());
		                    inOutResponse.setZZDJHH(erpDto.getZZDJHH());
		                    inOutResponse.setZRETURNFLAG("S");
		                    inOutResponse.setZMESSAGE("成功");
		                    inOutResponseList.add(inOutResponse);
		                }
	                }
	                tAdvanceDelivery.setDeliveryDetailList(deliveryDetailList);
	                tAdvanceDeliveryService.insertTAdvanceDelivery(tAdvanceDelivery);
	            } else if ("A".equals(type)) {
	                //出库
	                tOutDeliveryVO.setType("0");//erp
	                tOutDeliveryVO.setCode(requestInfo.getZZDJBM());
	                tOutDeliveryVO.setOriginCode(requestInfo.getZZDJBM());
	                tOutDeliveryVO.setNewLocal("2");//来源：2erp接口
	                tOutDeliveryVO.setDeliveryModule("1");//本地单据
	                tOutDeliveryVO.setCreateBy("ERP");//ERP
	                tOutDeliveryVO.setDeptName("ERP");//ERP
	                for (InOutRequest erpDto : inOutRequestList) {
	                	if("5866".equals(erpDto.getWERKS()) && "YW1".equals(erpDto.getLGNUM())) {
		                    InOutResponse inOutResponse = new InOutResponse();
		
		                    TOutDeliveryDetail detail = new TOutDeliveryDetail();
		                    TMaterial materialDO = materialMap.get(erpDto.getMATNR());
		                    if (materialDO != null) {
		                        detail.setMaterialId(materialDO.getId());//物料
		                        detail.setBatchCode(erpDto.getCHARG());//批次
		                        detail.setTbpos(erpDto.getZZDJHH());//行项目
		                        detail.setPredictCount(erpDto.getMENGE());//数量
		                        detail.setCreateBy("ERP");//ERP
		                        detail.setDeptName("ERP");//ERP
		                        tOutDeliveryDetailList.add(detail);
		                    }
		
		                    inOutResponse.setZZDJBM(erpDto.getZZDJBM());
		                    inOutResponse.setZZDJHH(erpDto.getZZDJHH());
		                    inOutResponse.setZRETURNFLAG("S");
		                    inOutResponse.setZMESSAGE("成功");
		                    inOutResponseList.add(inOutResponse);
		                }
	                }
	                tOutDeliveryVO.settOutDeliveryDetailList(tOutDeliveryDetailList);
	                tOutDeliveryService.insertTOutDelivery(tOutDeliveryVO);
	            }
            }
        }
        return inOutResponseList;
    }

    /**
     * ERP-WMS
     * 盘点
     */
    @Override
    public List<InventoryResponse> inventory(List<InventoryRequest> inventoryRequestList) {
        Object o = JSONArray.toJSONString(inventoryRequestList);
        System.out.println("===============" + o.toString());
        List<InventoryResponse> inventoryResponseList = new ArrayList<InventoryResponse>();
        InventoryResponse inventoryResponse = new InventoryResponse();

        for (InventoryRequest i : inventoryRequestList) {
            List<InventoryDetailRequest> inventoryDetailRequestList = i.getInventoryDetailRequestList();
            TErpInventory tErpInventory = new TErpInventory();
            tErpInventory.setIvnum(i.getIVNUM());
            tErpInventory.setPdatu(i.getPDATU());
            tErpInventory.setUname(i.getUNAME());
            tErpInventory.setLgnum(i.getLGNUM());
            tErpInventory.setWerks(i.getWERKS());
            tErpInventoryService.insertTErpInventory(tErpInventory);
            for (InventoryDetailRequest d : inventoryDetailRequestList) {
                d.setMATNR(d.getMATNR().substring(10));//截取一下物料，去掉前导零
            }

            //根据物料编码获取对应的物料信息
            List<String> materialCodes = inventoryDetailRequestList.stream().map(InventoryDetailRequest::getMATNR).collect(Collectors.toList());
            Map<String, TMaterial> materialMap = materialService.getMaterialByCodes(materialCodes);

            for (InventoryDetailRequest d : inventoryDetailRequestList) {
                TErpInventoryDetail tErpInventoryDetail = new TErpInventoryDetail();
                tErpInventoryDetail.setIvnum(d.getIVNUM());
                tErpInventoryDetail.setIvpos(d.getIVPOS());
                tErpInventoryDetail.setLgnum(d.getLGNUM());
                tErpInventoryDetail.setLgtyp(d.getLGTYP());
                tErpInventoryDetail.setLtypt(d.getLTYPT());
                tErpInventoryDetail.setLgpla(d.getLGPLA());
                tErpInventoryDetail.setWerks(d.getWERKS());
                tErpInventoryDetail.setLgort(d.getLGORT());
                tErpInventoryDetail.setMatnr(d.getMATNR());
                tErpInventoryDetail.setMaktx(d.getMAKTX());
                tErpInventoryDetail.setCharg(d.getCHARG());
                tErpInventoryDetail.setBestq(d.getBESTQ());
                tErpInventoryDetail.setGesme(d.getGESME());
                tErpInventoryDetail.setMeins(d.getMEINS());
                tErpInventoryDetail.setSobkz(d.getSOBKZ());
                tErpInventoryDetail.setPdatu(d.getPDATU());
                tErpInventoryDetail.setUname(d.getUNAME());
                tErpInventoryDetail.setPsPosnr(d.getPS_POSNR());
                tErpInventoryDetail.setPsPspt(d.getPS_PSPT());
                tErpInventoryDetail.setField1(d.getFIELD1());
                tErpInventoryDetail.setField2(d.getFIELD2());
                tErpInventoryDetail.setField3(d.getFIELD3());
                tErpInventoryDetail.setField4(d.getFIELD4());
                tErpInventoryDetail.setField5(d.getFIELD5());
                tErpInventoryDetailService.insertTErpInventoryDetail(tErpInventoryDetail);
                TMaterial materialDO = materialMap.get(d.getMATNR());
                if (materialDO != null) {
                    TStockVO tStock = tStockService.selectByLocationAndMaterial(materialDO.getId(), d.getCHARG(), d.getLGPLA());
                    if (tStock != null) {
                        // 新增盘点计划
                        TCheckDelivery checkDelivery = new TCheckDelivery();
                        checkDelivery.setPlanName(i.getIVNUM());
                        checkDelivery.setTrayType(tStock.getLocationType());
                        checkDelivery.setCheckType("1");//物料
                        checkDelivery.setCheckSource("2");//ERP
                        checkDelivery.setAuditor("ERP");
                        checkDelivery.setTbpos(d.getIVPOS());//行项目
                        checkDelivery.setAreaId(tStock.getAreaId());
                        checkDelivery.setLocationId(tStock.getLocationId());
                        checkDelivery.setReservoirId(tStock.getReservoirId());
                        checkDelivery.setBatchCode(tStock.getBatchCode());
                        checkDelivery.setMaterialId(tStock.getMaterialId());
                        checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
                        tCheckDeliveryService.save(checkDelivery);
                    }
                }
            }
            inventoryResponse.setIVNUM(i.getIVNUM());
            inventoryResponse.setZFLAG("S");
            inventoryResponse.setZMSG("成功");
            inventoryResponseList.add(inventoryResponse);
        }
        return inventoryResponseList;
    }

    /**
     * ERP-WMS
     * 库存查询
     */
    @Override
    public StockQueryResponse stockQuery(List<StockQueryRequest> stockQueryRequestList) {
        Object o = JSONArray.toJSONString(stockQueryRequestList);
        System.out.println("库存查询===============" + o.toString());
        StockQueryResponse stockQueryResponse = new StockQueryResponse();
        if (null != stockQueryRequestList && stockQueryRequestList.size() > 0) {
            tErpStockService.cleanTErpStock();

            for (StockQueryRequest s : stockQueryRequestList) {
                s.setMATNR(s.getMATNR().substring(10));//截取一下物料，去掉前导零

                TErpStock erpStock = new TErpStock();

                erpStock.setWerks(s.getWERKS());
                erpStock.setLgort(s.getLGORT());
                erpStock.setLgnum(s.getLGNUM());
                erpStock.setLgtyp(s.getLGTYP());
                erpStock.setMatnr(s.getMATNR());
                erpStock.setMakt(s.getMAKT());
                erpStock.setCharg(s.getCHARG());
                erpStock.setLgpla(s.getLGPLA());
                erpStock.setLgber(s.getLGBER());
                erpStock.setLifnr(s.getLIFNR());
                erpStock.setName1(s.getNAME1());
                erpStock.setGesme(s.getGESME());
                erpStock.setVerpr(s.getVERPR());
                erpStock.setSalkv(s.getSALKV());
                erpStock.setMeins(s.getMEINS());
                erpStock.setBestq(s.getBESTQ());
                erpStock.setSobkz(s.getSOBKZ());
                erpStock.setPosid(s.getPOSID());
                erpStock.setField1(s.getFIELD1());
                erpStock.setField2(s.getFIELD2());
                erpStock.setField3(s.getFIELD3());
                erpStock.setField4(s.getFIELD4());
                erpStock.setField5(s.getFIELD5());
                erpStock.setCreateBy("ERP");
                tErpStockService.insertTErpStock(erpStock);
            }
            stockQueryResponse.setWERKS(stockQueryRequestList.get(0).getWERKS());
            stockQueryResponse.setLGNUM(stockQueryRequestList.get(0).getLGNUM());
            stockQueryResponse.setZFLAG("S");
            stockQueryResponse.setZMSG("成功");
        } else {
            stockQueryResponse.setWERKS("");
            stockQueryResponse.setLGNUM("");
            stockQueryResponse.setZFLAG("E");
            stockQueryResponse.setZMSG("库存数据不能为空");
        }
        return stockQueryResponse;
    }

    @Override
    public AjaxResult test(String ivnum) {
        TErpInventory erpInventory = new TErpInventory();
        erpInventory.setIvnum(ivnum);
        List<TErpInventory> data = tErpInventoryService.selectTErpInventoryList(erpInventory);
        TErpInventory i = data.get(0);
        TErpInventoryDetail detail = new TErpInventoryDetail();
        detail.setIvnum(ivnum);
        List<TErpInventoryDetail> dataList = tErpInventoryDetailService.selectTErpInventoryDetailList(detail);
        //根据物料编码获取对应的物料信息
        List<String> materialCodes = dataList.stream().map(TErpInventoryDetail::getMatnr).collect(Collectors.toList());
        Map<String, TMaterial> materialMap = materialService.getMaterialByCodes(materialCodes);

        for (TErpInventoryDetail d : dataList) {
            TMaterial materialDO = materialMap.get(d.getMatnr());
            if (materialDO != null) {
                TStockVO tStock = tStockService.selectByLocationAndMaterial(materialDO.getId(), d.getCharg(), d.getLgpla());
                if (tStock != null) {
                    // 新增盘点计划
                    TCheckDelivery checkDelivery = new TCheckDelivery();
                    checkDelivery.setPlanName(i.getIvnum());
                    checkDelivery.setTrayType(tStock.getLocationType());
                    checkDelivery.setCheckType("1");//物料
                    checkDelivery.setCheckSource("2");//ERP
                    checkDelivery.setAuditor("ERP");
                    checkDelivery.setTbpos(d.getIvpos());//行项目
                    checkDelivery.setAreaId(tStock.getAreaId());
                    checkDelivery.setLocationId(tStock.getLocationId());
                    checkDelivery.setReservoirId(tStock.getReservoirId());
                    checkDelivery.setBatchCode(tStock.getBatchCode());
                    checkDelivery.setMaterialId(tStock.getMaterialId());
                    checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
                    tCheckDeliveryService.save(checkDelivery);
                }
            }
        }
        return null;
    }
}
