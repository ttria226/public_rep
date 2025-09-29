package com.xsrw.wms.webservice.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.*;
import com.xsrw.wms.webservice.domain.TTaskErpRecord;
import com.xsrw.wms.webservice.domain.vo.wms2erp.TErpInOutInfoVO;
import com.xsrw.wms.webservice.domain.vo.wms2erp.TErpInventoryDetailInfoVO;
import com.xsrw.wms.webservice.mapper.TErpInOutMapper;
import com.xsrw.wms.webservice.mapper.TErpInventoryDetailMapper;
import com.xsrw.wms.webservice.mapper.TTaskErpRecordMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WmsToErpUtils {

    private static Logger logger = LoggerFactory.getLogger(WmsToErpUtils.class);

    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private TTaskErpRecordMapper tTaskErpRecordMapper;
    @Autowired
    private TErpInventoryDetailMapper tErpInventoryDetailMapper;
    @Autowired
    private TErpInOutMapper tErpInOutMapper;

    //装运类型E-上架 A-下架
    private static final String TRART_UP = "E";
    //装运类型E-上架 A-下架
    private static final String TRART_DOWN = "A";

    //库存地点:Z001一楼一号库 Z002二号库的地堆 Z003二楼三号库
    private static final String LGORT_WARE1 = "Z001";
    //工厂编码--后续替换为正式
    private static final String WERKS_CODE = "5866";
    //用户编码--后续替换为正式
    private static final String ZZYHZH_CODE = "10675564";
    //仓库编码--后续替换为正式
    private static final String LGNUM_CODE = "YW1";
    //物料前导零
    private static final String MATERIAL_CODE_PREFIX = "0000000000";


    /**
     * 出入库回调
     *
     * @param type
     * @param wcsId
     */
    public void inoutPut(String type, Long wcsId, String zzdjbm, String zzdjhh) {
        List<ZSWMYDD2ERPHCZCSQXXSWAQFD> item = new ArrayList<>();
        //获取出入库回调信息
        List<TErpInOutInfoVO> tTaskInList = tErpInOutMapper.selectTTaskInErpById(type, wcsId, zzdjbm, zzdjhh);
        if (CollectionUtils.isEmpty(tTaskInList)) {
            return;
        }
        if (tErpInOutMapper.selectTTaskErpquantity(wcsId)>0){
            return;
        }
        try{
            tTaskInList.forEach(material -> {
                ZSWMYDD2ERPHCZCSQXXSWAQFD erpRequest = new ZSWMYDD2ERPHCZCSQXXSWAQFD();
                erpRequest.setWERKS(material.getWerks());//工厂
                erpRequest.setLGORT(material.getLgort());//库存地点
                erpRequest.setLGNUM(material.getLgnum());//仓库号
                //获取当前操作用户信息，如果是设备调用，无用户信息，固定一个用户信息
                String userRemark ;
                //TTaskErpRecord record = new TTaskErpRecord(wcsId, null, type, JSONObject.toJSONString(params), Constants.NO);
               try {
                   userRemark = SecurityUtils.getLoginUser().getSysUser().getRemark();
               }catch (Exception e){
                    userRemark = ZZYHZH_CODE;
               }
                erpRequest.setZZYHZH(userRemark);//用户编码
                erpRequest.setTRART(Constants.WCS_TASK_TYPE_IN.equals(type) ? TRART_UP : TRART_DOWN);//装运类型E-上架 A-下架
                erpRequest.setTBNUM(material.getTbnum());//转移要求号
                erpRequest.setTBPOS(material.getTbpos());//行项目
                erpRequest.setMATNR(MATERIAL_CODE_PREFIX + material.getMaterialCode());//物料号
                erpRequest.setCHARG(material.getBatchCode());//批次
                erpRequest.setBESTQ(material.getBestq() == null ? "" : material.getBestq());//库存类别
                erpRequest.setSOBKZ(material.getSobkz() == null ? "" : material.getSobkz());//特殊库存标识
                erpRequest.setPSPNR(material.getPspnr() == null ? "" : material.getPspnr());//WBS 元素(项目号）
                erpRequest.setZZWZTM("");//物资条码----可以为空值
                erpRequest.setZZCWTM(material.getErpCode());//仓位条码
                erpRequest.setMENGE(material.getActualCount());//数量
                erpRequest.setCONFORM("X");//确认转储单项目（固定值X）
                erpRequest.setZZYL1(material.getZzywlx());
                erpRequest.setZZYL2("");
                erpRequest.setZZYL3("");
                erpRequest.setZZYL4("");
                item.add(erpRequest);
            });
            //根据单据号分组
            Map<String, List<ZSWMYDD2ERPHCZCSQXXSWAQFD>> codeMap = item.stream().collect(Collectors.groupingBy(ZSWMYDD2ERPHCZCSQXXSWAQFD::getTBNUM));
            codeMap.forEach((code, sendItem) -> {
                ZWMXSJWHCZCSQXX params = new ZWMXSJWHCZCSQXX();
                //循环发送数据
                ZWMXSJWHCZCSQXX.INPUT value = new ZWMXSJWHCZCSQXX.INPUT();
                value.setItem(sendItem);
                params.setINPUT(value);
                //记录erp发送记录
                TTaskErpRecord record = new TTaskErpRecord(wcsId, null, type, JSONObject.toJSONString(params), Constants.NO);
                record.setZzdjbm(sendItem.get(0).getTBNUM());
                try {
                    ZWMXSJWHCZCSQXXResponse result = WebServiceUtils.SI03KL8XSTLZHCC2ERPHCZCSQXX(params);
                    logger.info("=============上下架返回信息：" + result.toString());
                    if (result != null && result.getOUTPUT() != null && CollectionUtils.isNotEmpty(result.getOUTPUT().getItem())) {
                        List<ZSWMYDD2ERPHCZCSQXXRE> itemListResult = result.getOUTPUT().getItem();
                        ZSWMYDD2ERPHCZCSQXXRE itemResult = itemListResult.get(0);
                        if ("S".equals(itemResult.getZFLAG())) {
                            record.setStatus(Constants.YES);
                        }
                    }
                    record.setAcceptData(ObjectUtils.toString(JSONObject.toJSONString(result)));
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("WMS-ERP出入库传输错误:" + e.getMessage(), e);
                    record.setAcceptData("WMS-ERP出入库传输错误:" + e.getMessage());
                }
                tTaskErpRecordMapper.insert(record);
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 移库回调
     *
     * @param wcsVO
     */
    public void movePut(TTaskWcs wcsVO) {
        try{
            List<String> locationCodes = new ArrayList<>();
            locationCodes.add(wcsVO.getStartPosition());
            locationCodes.add(wcsVO.getPurposePosition());
            Map<String, String> locationMap = getLocationCode(locationCodes);
            //移库任务，都是整盘移动（只需要工厂、库存地点、仓库号、用户编码、源仓位条码、目标仓位条码）
            ZSWMYDD2ERPHCCWYDXX movementRequest = new ZSWMYDD2ERPHCCWYDXX();
            movementRequest.setWERKS(WERKS_CODE);//工厂
//        movementRequest.setLGORT(LGORT_WARE1);//库存地点-只有立体库才有移库任务
            //获取当前操作用户信息，如果是设备调用，无用户信息，固定一个用户信息
            String userRemark ;
            try {
                userRemark = SecurityUtils.getLoginUser().getSysUser().getRemark();
           }catch (Exception e){
                userRemark = ZZYHZH_CODE;
            }
            movementRequest.setZZYHZH(userRemark);//用户编码
            movementRequest.setLGNUM(LGNUM_CODE);//仓库号
            movementRequest.setZZYWLX("2");//操作类型（1物料移动 2  整托盘移动）
            movementRequest.setBWLVS("999");//移动类型（999）
            movementRequest.setMATNR("");//物料号--不需要
            movementRequest.setCHARG("");//批次--不需要
            movementRequest.setBESTQ("");//库存类别--不需要
            movementRequest.setSOBKZ("");//特殊库存标识--不需要
            movementRequest.setPSPNR("");//WBS 元素(项目号）--不需要
            movementRequest.setGESME(BigDecimal.ZERO);//数量--不需要
            movementRequest.setZYCWTM(locationMap.get(wcsVO.getStartPosition()));//源仓位条码
            movementRequest.setZMDCWTM(locationMap.get(wcsVO.getPurposePosition()));//目标仓位条码
            movementRequest.setZZYL1("");
            movementRequest.setZZYL2("");
            movementRequest.setZZYL3("");
            movementRequest.setZZYL4("");
            ZWMXSJWHCCWYDXX params = new ZWMXSJWHCCWYDXX();
            ZWMXSJWHCCWYDXX.INPUT value = new ZWMXSJWHCCWYDXX.INPUT();
            value.setItem(Collections.singletonList(movementRequest));
            params.setINPUT(value);

            //记录erp发送记录
            TTaskErpRecord record = new TTaskErpRecord(wcsVO.getId(), null, "5", JSONObject.toJSONString(params), Constants.NO);
            try {
                ZWMXSJWHCCWYDXXResponse result = WebServiceUtils.SI03KL9XSTLZHCC2ERPHCCWYDXX(params);
                logger.info("=============移库返回信息：" + result.toString());
                if (result != null && result.getOUTPUT() != null && CollectionUtils.isNotEmpty(result.getOUTPUT().getItem())) {
                    List<ZSWMYDD2ERPHCCWYDXXRE> itemListResult = result.getOUTPUT().getItem();
                    ZSWMYDD2ERPHCCWYDXXRE itemResult = itemListResult.get(0);
                    if ("S".equals(itemResult.getZFLAG())) {
                        record.setStatus(Constants.YES);
                    }
                }
                record.setAcceptData(ObjectUtils.toString(JSONObject.toJSONString(result)));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("WMS-ERP仓位移动传输错误:" + e.getMessage(), e);
                record.setAcceptData("WMS-ERP仓位移动传输错误:" + e.getMessage());
            }
            tTaskErpRecordMapper.insert(record);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 盘点回调
     *
     * @param taskId
     */
    public void checkPut(Long taskId) {
        try{
            //获取出盘点回调信息
            List<TErpInventoryDetailInfoVO> detailInfoVOList = tErpInventoryDetailMapper.selectErpInfo(taskId);
            if (CollectionUtils.isEmpty(detailInfoVOList)) {
                return;
            }
            //根据单据号分组
            Map<String, List<TErpInventoryDetailInfoVO>> codeMap = detailInfoVOList.stream().collect(Collectors.groupingBy(TErpInventoryDetailInfoVO::getIvnum));
            codeMap.forEach((code, taskDetailList) -> {
                TErpInventoryDetailInfoVO firdstVO = taskDetailList.get(0);
                ZWMXSJW2ERPRECEIVE params = new ZWMXSJW2ERPRECEIVE();
                ZSMWMZLCK2ERPHEAD checkRequest = new ZSMWMZLCK2ERPHEAD();
                checkRequest.setWMSCODE("");//WMS系统任务号-已有，可为空
                checkRequest.setIVNUM(firdstVO.getIvnum());//盘点凭证
                checkRequest.setPDATU(firdstVO.getPdatu());//盘点时间
                checkRequest.setUNAME(firdstVO.getUname());//创建人
                checkRequest.setLGNUM(firdstVO.getLgnum());//仓库号
                checkRequest.setWERKS(firdstVO.getWerks());//工厂
                params.setIHEAD(checkRequest);
                List<ZSWMXSWM2ERPITEM> item = new ArrayList<>();
                taskDetailList.forEach(detail -> {
                    ZSWMXSWM2ERPITEM checkDetail = new ZSWMXSWM2ERPITEM();
                    checkDetail.setIVNUM(detail.getIvnum());//盘点凭证
                    checkDetail.setIVPOS(detail.getIvpos());//行项目号
                    checkDetail.setLGNUM(detail.getLgnum());//仓库号
                    checkDetail.setLGTYP(detail.getLgtyp());//仓储类型
                    checkDetail.setLTYPT(detail.getLtypt());//仓储类型名称
                    checkDetail.setLGPLA(detail.getLgpla());//仓位
                    checkDetail.setWERKS(detail.getWerks());//工厂
                    checkDetail.setLGORT(detail.getLgort());//库存地点
                    checkDetail.setMATNR(MATERIAL_CODE_PREFIX + detail.getMatnr());//物料编码
                    checkDetail.setMAKTX(detail.getMaktx());//MAKTX
                    checkDetail.setCHARG(detail.getCharg());//批次
                    checkDetail.setBESTQ("Z");//库存状态-A部分盘点L已清账N没盘点Z已盘点S已取消
                    checkDetail.setGESME(detail.getPredictCount());//在库数量
                    checkDetail.setSPSL2(detail.getActualCount());//实盘数量
                    checkDetail.setMEINS(detail.getMeins());//基本计量单位
                    checkDetail.setSOBKZ(detail.getSobkz() == null ? "" : detail.getSobkz());//特殊库存标识
                    checkDetail.setPDATU(detail.getPdatu());//盘点日期
                    checkDetail.setUNAME(detail.getUname());//创建人
                    checkDetail.setPSPSPT(detail.getPsPspt() == null ? "" : detail.getPsPspt());//WBS元素显示编号
                    checkDetail.setPSPOSNR(detail.getPsPosnr() == null ? "" : detail.getPsPosnr());//WBS元素的描述
                    checkDetail.setFIELD1(detail.getField1());
                    item.add(checkDetail);
                });
                ZWMXSJW2ERPRECEIVE.TITEM value = new ZWMXSJW2ERPRECEIVE.TITEM();
                value.setItem(item);
                params.setTITEM(value);
                //记录erp发送记录
                TTaskErpRecord record = new TTaskErpRecord(firdstVO.getTaskId(), String.valueOf(taskId), "3", JSONObject.toJSONString(params), Constants.NO);
                try {
                    ZWMXSJW2ERPRECEIVEResponse result = WebServiceUtils.SI03KM2XSTLZHCC2ERPRECEIVE(params);
                    if (result != null && result.getERETURN() != null) {
                        ZSMWMZLCK2ERPRETURN itemResult = result.getERETURN();
                        if ("S".equals(itemResult.getZFLAG())) {
                            record.setStatus(Constants.YES);
                        }
                    }
                    record.setAcceptData(ObjectUtils.toString(JSONObject.toJSONString(result)));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("WMS-ERP盘点结果上传错误:" + e.getMessage(), e);
                }
                tTaskErpRecordMapper.insert(record);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取库位对应的仓位条码
     *
     * @param codes
     * @return
     */
    private Map<String, String> getLocationCode(List<String> codes) {
        Map<String, String> resMap = new HashMap<>();
        List<TLocation> location = tLocationMapper.selectErpCodeByCodes(codes);
        if (!CollectionUtils.isEmpty(location)) {
            resMap = location.stream().collect(Collectors.toMap(TLocation::getCode, TLocation::getErpCode));
        }
        return resMap;
    }
}
