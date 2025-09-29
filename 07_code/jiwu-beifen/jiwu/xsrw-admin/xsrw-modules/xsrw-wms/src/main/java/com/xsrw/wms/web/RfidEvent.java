package com.xsrw.wms.web;

import com.hiklife.rfidSdk.Common.SDKType;
import com.hiklife.rfidSdk.FixReaderAPI;
import com.hiklife.rfidSdk.Model.Reader.*;
import com.xsrw.system.api.factory.RemoteLogFallbackFactory;
import com.xsrw.wms.inout.domain.TRfidRecord;
import com.xsrw.wms.inout.service.TRfidRecordService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/** 
 * @description: RFID射频天线启动
 * @author XMING
 * @return
 */ 
@Component
public class RfidEvent implements CommandLineRunner {

    @Autowired
    private TRfidRecordService tRfidRecordService;

    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    private static final Integer EPC = 1; //EPC区
    private static final Integer TID = 2; //TID区
    private static final Integer USR = 3; //USER区

    public FixReaderAPI INSTANCE = new FixReaderAPI();


    ObservableMap<String, Integer> map = FXCollections.observableHashMap();


    // RFID射频设备上报数据 服务器ip
    @Value("${web.rfid-device.ip}")
    private String rfidServeIP;


    // RFID射频设备上报数据 服务器端口
    @Value("${web.rfid-device.port}")
    private Integer rfidServePort;

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }


    private String getDate(){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return date;
    }


    /**
     * 连接
     * deviceIndex: 0 -> 读写器为tcp客户端(HEX协议),调用StartServerGeneric
     * deviceIndex: 1 -> 读写器为tcp服务端(HEX协议),调用ConnectGeneric
     * deviceIndex: 2 ->读写器为串口(HEX协议), 调用StartServerGeneric
     * deviceIndex: 3 ->读写器为tcp客户端(XML协议),调用StartServerGeneric
     * deviceIndex: 4 ->读写器为tcp服务端(XML协议),调用ConnectGeneric
     */
    public void start() {
        log.info("---------------------RFID射频天线设备("+getDate()+"):被动连接服务启动---------------------");
        //读写器为client
        SDKParams<BaseConnectParam> sdkParams = new SDKParams<BaseConnectParam>();
        sdkParams.type = SDKType.TYPE_XML_NET.ordinal();
        ServerAddr serverAddr = new ServerAddr();
        serverAddr.ip = rfidServeIP;
        serverAddr.port = rfidServePort;
        sdkParams.params = serverAddr;

        int ret = INSTANCE.StartServerGeneric(sdkParams, new ServerCallback() {

            @Override
            public void Connected(DeviceInfo deviceInfo) {
                log.info("---------------RFID射频天线设备("+getDate()+"):"+deviceInfo.deviceID+"设备上线---------------");
                map.put(deviceInfo.deviceID, deviceInfo.fd);
            }

            @Override
            public void Disconnected(DeviceInfo deviceInfo) {
                log.error("---------------RFID射频天线设备("+getDate()+"):"+deviceInfo.deviceID+"设备离线---------------");
                map.remove(deviceInfo.deviceID);
            }

            @Override
            public void InvReport(ApiResultParamsAsync<InventoryResult> apiResultParamsAsync) {
                log.info("---------------RFID射频天线设备("+getDate()+"):数据上报开始---------------");
                HashSet<TRfidRecord> rfid = new HashSet<>();
                for (int i = 0; i < apiResultParamsAsync.result.count; i++) {
                    System.err.println("[EPC] " + apiResultParamsAsync.result.inventoryData.get(i).epc + " ; [Rssi] " + apiResultParamsAsync.result.inventoryData.get(i).rssi + " ; [InOut] " + apiResultParamsAsync.result.inventoryData.get(i).inOutType + "\n");
                    log.info("[EPC] " + apiResultParamsAsync.result.inventoryData.get(i).epc + " ; [Rssi] " + apiResultParamsAsync.result.inventoryData.get(i).rssi + " ; [InOut] " + apiResultParamsAsync.result.inventoryData.get(i).inOutType + "\n");
                    TRfidRecord tRfidRecord = new TRfidRecord();
                    tRfidRecord.setRfid(apiResultParamsAsync.result.inventoryData.get(i).epc);
                    rfid.add(tRfidRecord);
                }
                tRfidRecordService.saveBatch(rfid);
                log.info("---------------RFID射频天线设备("+getDate()+"):数据上报结束---------------");
            }

            /**
             * 内存访问上报
             * @param apiResultParamsAsync
             */
            @Override
            public void MemAccessReport(ApiResultParamsAsync<MemAccessResult> apiResultParamsAsync) {

            }

            /**
             * 错误信息上报
             * @param apiResultParamsAsync
             */
            @Override
            public void ErrorReport(ApiResultParamsAsync<ErrorResult> apiResultParamsAsync) {
                log.info("---------------RFID射频天线设备("+getDate()+"):盘点错误上报开始---------------");

                log.error("[ErrCode] " + apiResultParamsAsync.result.errCode + "; [MacErrCode] " + apiResultParamsAsync.result.macErrCode);

                log.info("---------------RFID射频天线设备("+getDate()+"):盘点错误上报结束---------------");
            }

            /**
             * 升级状态上报
             * @param apiResultParamsAsync
             */
            @Override
            public void UpgradeReport(ApiResultParamsAsync<UpgradeResult> apiResultParamsAsync) {

            }
        });

        if (ret == 0) {
            log.info("---------------RFID射频天线设备("+getDate()+"):被动连接服务启动成功---------------");
        } else {
            log.error("---------------RFID射频天线设备("+getDate()+"):被动连接服务启动失败---------------");
        }
    }

}
