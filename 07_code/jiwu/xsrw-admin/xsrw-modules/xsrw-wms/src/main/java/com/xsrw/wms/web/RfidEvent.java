package com.xsrw.wms.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hiklife.rfidSdk.Common.SDKType;
import com.hiklife.rfidSdk.FixReaderAPI;
import com.hiklife.rfidSdk.Model.Reader.*;
import com.xsrw.system.api.factory.RemoteLogFallbackFactory;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TRfidRecord;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.inout.service.TRfidRecordService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/** 
 * @description: RFID射频天线启动
 * @author XMING
 * @return
 */ 
@Component
public class RfidEvent implements CommandLineRunner {

    @Autowired
    private TRfidRecordService tRfidRecordService;
    @Autowired
    private ITMaterialDetailService itMaterialDetailService;

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
                System.out.printf("---------------RFID射频天线设备(\"+getDate()+\"):数据上报开始---------------");
                HashSet<TRfidRecord> rfid = new HashSet<>();
                List<TMaterialDetail> updateList = new ArrayList<>();
                for (int i = 0; i < apiResultParamsAsync.result.count; i++) {
                    System.err.println("[EPC] " + apiResultParamsAsync.result.inventoryData.get(i).epc + " ; [Rssi] " + apiResultParamsAsync.result.inventoryData.get(i).rssi + " ; [InOut] " + apiResultParamsAsync.result.inventoryData.get(i).inOutType + "\n");
                    log.info("[EPC] " + apiResultParamsAsync.result.inventoryData.get(i).epc + " ; [Rssi] " + apiResultParamsAsync.result.inventoryData.get(i).rssi + " ; [InOut] " + apiResultParamsAsync.result.inventoryData.get(i).inOutType + "\n");
                    TRfidRecord tRfidRecord = new TRfidRecord();
                    String strRfid=apiResultParamsAsync.result.inventoryData.get(i).epc;
                    System.out.printf("扫描到的卡号="+strRfid);
                    tRfidRecord.setRfid(strRfid);
                    rfid.add(tRfidRecord);

                    TMaterialDetail tMaterialDetail = itMaterialDetailService.getOne(new QueryWrapper<TMaterialDetail>().eq("rfid",strRfid));
                    if (tMaterialDetail != null) {
                        if (tMaterialDetail.getRukuSaomiao() == null || tMaterialDetail.getRukuSaomiao().equals(0)) {  //入库
                            tMaterialDetail.setRukuSaomiao(1);
                            tMaterialDetail.setRukuSaomiaoTime(new Date());
                        } else if (Integer.parseInt(tMaterialDetail.getStatus()) >= 1 && (tMaterialDetail.getChukuSaomiao() == null || tMaterialDetail.getChukuSaomiao().equals(0))) {  //出库
                            //判断是否为违法出库：
                            // 判断当前id不存在拣选记录，但存在上架记录，则为违法出库，
                            // 同时触发声音报警，报警文本参考【检测到违法出库】，连续两次
                            if (!tMaterialDetail.getStatus().equals("2")) {
                                System.out.printf("执行违法出库,rfid编号："+tMaterialDetail.getRfid());
                                tMaterialDetail.setChukuSaomiao(1);
                                tMaterialDetail.setChukuSaomiaoFlag(1);
                                tMaterialDetail.setChukuSaomiaoTime(new Date());
                                //播报语音
                                playWarning();
                            }
                        } else if (Integer.parseInt(tMaterialDetail.getStatus()) > 1 && (tMaterialDetail.getRukuSaomiao().equals(1) && tMaterialDetail.getChukuSaomiao().equals(1))) {  //重复扫描
                            System.out.printf("执行已入库和已出库的判断,rfid编号："+tMaterialDetail.getRfid());
                            //如果上次出库扫描为违法出库，则修改为正常出库。
                            if (tMaterialDetail.getChukuSaomiaoFlag().equals(1)) {
                                System.out.printf("执行出库扫描状态为违法出库，更新为正常,rfid编号："+tMaterialDetail.getRfid());
                                tMaterialDetail.setChukuSaomiaoFlag(0);
                            }
                        }else {
                                System.out.printf("执行正常出库,rfid编号："+tMaterialDetail.getRfid());
                                //更改出库扫描等于1，出库时间
                                tMaterialDetail.setChukuSaomiao(1);
                                tMaterialDetail.setChukuSaomiaoTime(new Date());
                        }
                        itMaterialDetailService.updateById(tMaterialDetail);
                    }
                }
                tRfidRecordService.saveBatch(rfid);
                log.info("---------------RFID射频天线设备("+getDate()+"):数据上报结束---------------");
            }
            private  void playWarning() {
                // 确保音频文件的路径是正确的
                String path = "D:\\tishi.wav";
                byte[] auBuffer = new byte[1024 * 128];
                AudioInputStream audioInputStream = null; // 新建的音频输入流对象
                SourceDataLine auline = null; // 混频器源数据行
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File(path)); // 从文件中获取数据流
                    AudioFormat format = audioInputStream.getFormat(); // 获取文件格式

                    // 利用数据行类型和音频格式创建数据行对象
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                    // 利用音频系统类获取指定对象中匹配的行对象
                    auline = (SourceDataLine) AudioSystem.getLine(info);
                    auline.open(format); // 用指定格式打开数据行
                    auline.start();
                    int byteCount = 0;
                    while (byteCount != -1) {
                        byteCount = audioInputStream.read(auBuffer, 0, auBuffer.length); // 从数据流中读出128KB数据
                        if (byteCount >= 0) {
                            auline.write(auBuffer, 0, byteCount); // 将数据写入数据行
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } finally {
                    auline.drain(); // 清空数据行
                    auline.close(); // 关闭数据行
                }
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
