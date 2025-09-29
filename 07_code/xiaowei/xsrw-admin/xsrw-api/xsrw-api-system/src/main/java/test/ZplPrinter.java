//
//import com.xsrw.common.core.utils.DateUtils;
//import com.zebra.sdk.comm.*;
//import com.zebra.sdk.printer.PrinterStatus;
//import com.zebra.sdk.printer.ZebraPrinter;
//import com.zebra.sdk.printer.ZebraPrinterFactory;
//import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import test.HcBind;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <p>RFID打印机测试</p>
// *
// * @author : LIn
// * @date : 2022-06-17 11:14
// **/
//@Service
//public class ZplPrinter {
//
//
//    private byte[] dotFont;
//
//
//    /**
//     * 标签模板
//     */
//    public static String printModel(HcBind hcBind) {
//        ZplPrinter p = new ZplPrinter();
//        StringBuilder begin = new StringBuilder();
//        StringBuilder content = new StringBuilder();
//        // 耗材名称
//        PrintDataBackDTO name = p.getText("名称:" + hcBind.getName(), 54, 70);
//        begin.append(name.getBegin());
//        content.append(name.getContent());
//        // 耗材规格
//        PrintDataBackDTO spec = p.getText("规格:" +  hcBind.getSpec(), 54, 120);
//        begin.append(spec.getBegin());
//        content.append(spec.getContent());
//        // 厂家
//        PrintDataBackDTO manufacturer = p.getText("厂家:" +  hcBind.getManufacturer(), 54, 170);
//        begin.append(manufacturer.getBegin());
//        content.append(manufacturer.getContent());
//        // 批次
//        PrintDataBackDTO batchNo = p.getText("批次:" +  hcBind.getBatchNo(), 54, 220);
//        begin.append(batchNo.getBegin());
//        content.append(batchNo.getContent());
//        // 效期
//        PrintDataBackDTO effectDate = p.getText("效期:" + DateUtils.parseDateToStr("yyyy-MM-dd",hcBind.getEffectDate()), 54, 270);
//        begin.append(effectDate.getBegin());
//        content.append(effectDate.getContent());
//
//        //返回EPC在这里设置 （）,,到^FS前可填入参数名称 "^RS8 ^RFR,H,0,8,E^FN1^FS^HV1,,EPC:^FS"   ^RS8,,,1表示这里的1表示只读一个标签
//        content.append("^RS8,,,1 ^RFR,H,0,8,E^FN1^FS^HV1,,^FS");
//
//        // 条形码
//        content.append("^BY2,3,104^FT791,297^BCN,,Y,N^FH\\^FD>;").append(hcBind.getUniqueCode()).append("^FS");
//        ///二维码
//        content.append("^FT1015,313^BQN,2,5^FH\\^FDLA,").append(hcBind.getUniqueCode()).append("^FS");
//
//        ///EPC打印出来
//        content.append("^FPH,6^FT54,320^AQN,28,9^FN2^FS^FN2^RFR,H,2,16,E^FS");
//
//        //打印数量
//        content.append("^PQ1,0,1,Y");
//        return begin.toString() + content.toString() + "^XZ";
//    }
//
//    /**
//     * 打印并获取Epc
//     */
//    public Map<String,Object> printGetEpc(HcBind hcBind) throws ConnectionException {
//        String sendStr = printModel(hcBind);
//        String printerIp = "";//configService.selectConfigByKey("rfid.printer.ip");
//        Connection connection =   ConnectionBuilder.build("TCP_MULTI:" + printerIp + ":9100:9200");
//        Map<String,Object> map = new HashMap<String, Object>();
//        try {
//            connection.open();
//            //获得tid指令
//            //String date="^XA^RS8^RFR,H,0,12,E^FN1^FS^HV1,,TID:^FS^XZ";
//            byte[] sendAndWaitForResponse = connection.sendAndWaitForResponse(sendStr.getBytes(),8000,8000,"" );
//            StringBuilder epcBuilder = new StringBuilder();
//            for (byte b : sendAndWaitForResponse) {
//                char c = (char) b;
//                String str = String.valueOf(c);
//                epcBuilder.append(str);
//            }
//            String epc = epcBuilder.toString();
//            //判断EPC是否为空 并且长度是否为24 这里根据实际标签信息决定
//            if(StringUtils.isNotEmpty(epc) && epc.length() == 24){
//                map.put("dealState", "success");
//                map.put("epc", epc);
//            }else{
//                map.put("dealState", "error");
//                map.put("dealMsg","获取标签RFID失败，请检查打印机打印机状态，并完成RFID校准");
//            }
//        } catch (ConnectionException e) {
//            map.put("dealState", "error");
//            map.put("dealMsg","打印出现异常，请联系管理员");
//        } finally {
//            connection.close();
//        }
//        return map;
//    }
//
//    /**
//     * 根据ip获得打印机状态
//     * @return
//     * @throws ConnectionException
//     */
//    public Map<String,Object> getPrinterStatus() throws ConnectionException {
////        String printerIp = configService.selectConfigByKey("rfid.printer.ip");
//        //打印机连接信息
//        Connection connection = new TcpConnection("192.168.36.95", TcpConnection.DEFAULT_ZPL_TCP_PORT);
//        //返回打印机状态用
//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("dealState", "success");
//        try {
//            //打开与打印机的链接
//            connection.open();
//            //获得打印机工厂
//            ZebraPrinter zPrinter = ZebraPrinterFactory.getInstance(connection);
//            // 获得打印机状态
//            /**{"isHeadCold":false,  是否过冷
//             "isHeadOpen":false, 是否打开
//             "isHeadTooHot":false, 是否过热
//             "isPaperOut":false, 是否缺纸
//             "isPartialFormatInProgress":false, 是否正在进行格式化
//             "isPaused":true, 暂停
//             "isReadyToPrint":false, 准备好打印
//             "isReceiveBufferFull":false, 接收缓冲区满了
//             "isRibbonOut":false, 带出
//             "labelLengthInDots":1956, 用点标注长度
//             "labelsRemainingInBatch":0, 批号仍在分批
//             "numberOfFormatsInReceiveBuffer":0,接收缓冲区的格式数
//             "printMode":"TEAR_OFF"} 打印模式
//             *
//             * **/
//            PrinterStatus currentStatus = zPrinter.getCurrentStatus();
//            map.put("currentStatus", currentStatus);
//            connection.close();
//        } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {
//            map.put("dealState", "error");
//            map.put("dealMsg", e);
//        } finally {
//            //关闭打印机连接
//            connection.close();
//        }
//        return map;
//    }
//
//    /**
//     * 构造方法
//     *
//     */
//    public ZplPrinter() {
//        File file = new File("C://ts24.lib");//这里放自己的ts24.lib位置
//        FileInputStream fis;
//        try {
//            fis = new FileInputStream(file);
//            dotFont = new byte[fis.available()];
//            fis.read(dotFont);
//            fis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 打印内容
//     *
//     * @param str 内容
//     * @param x   x坐标
//     * @param y   y坐标
//     */
//    private PrintDataBackDTO getText(String str, int x, int y) {
//        PrintDataBackDTO result = new PrintDataBackDTO();
//        StringBuilder begin = new StringBuilder();
//        begin.append("^XA~SD25^PW1146^PR1,1");
//        StringBuilder content = new StringBuilder();
//        byte[] ch = str2bytes(str);
//        for (int off = 0; off < ch.length; ) {
//            if (((int) ch[off] & 0x00ff) >= 0xA0) {
//                try {
//                    //中文字符
//                    int qcode = ch[off] & 0xff;
//                    int wcode = ch[off + 1] & 0xff;
//                    content.append(String.format("^FO%d,%d^XG0000%01X%01X,%d,%d^FS\n", x, y, qcode, wcode, 1, 1));
//                    begin.append(String.format("~DG0000%02X%02X,00072,003,\n", qcode, wcode));
//                    qcode = (qcode + 128 - 32) & 0x00ff;
//                    wcode = (wcode + 128 - 32) & 0x00ff;
//                    int offset = (qcode - 16) * 94 * 72 + (wcode - 1) * 72;
//                    for (int j = 0; j < 72; j += 3) {
//                        qcode = (int) dotFont[j + offset] & 0x00ff;
//                        wcode = (int) dotFont[j + offset + 1] & 0x00ff;
//                        int qcode1 = (int) dotFont[j + offset + 2] & 0x00ff;
//                        begin.append(String.format("%02X%02X%02X\n", qcode, wcode, qcode1));
//                    }
//                    x = x + 24;
//                    off = off + 2;
//                } catch (Exception e) {
//                    //替换成X号
//                    content.append(getChar("X", x, y, 30, 20));
//                    x = x + 15;
//                    off = off + 2;
//                }
//            } else if (((int) ch[off] & 0x00FF) < 0xA0) {
//                content.append(getChar(String.format("%c", ch[off]), x, y, 30, 20));
//                x = x + 15;
//                off++;
//            }
//        }
//
//        result.setBegin(begin);
//        result.setContent(content);
//        return result;
//    }
//
//    /**
//     * 英文字符串(包含数字)
//     *
//     * @param str 英文字符串
//     * @param x   x坐标
//     * @param y   y坐标
//     * @param h   高度
//     * @param w   宽度
//     */
//    private String getChar(String str, int x, int y, int h, int w) {
//        return "^FO" + x + "," + y + "^A0," + h + "," + w + "^FD" + str + "^FS";
//    }
//
//    /**
//     * 字符串转byte[]
//     */
//    private byte[] str2bytes(String s) {
//        if (null == s || "".equals(s)) {
//            return null;
//        }
//        byte[] abytes = null;
//        try {
//            abytes = s.getBytes("gb2312");
//        } catch (UnsupportedEncodingException ex) {
//            System.out.println("字符串转byte出错");
//        }
//
//        return abytes;
//    }
//}