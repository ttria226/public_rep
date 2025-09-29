package com.xsrw.common.core.print;

import com.alibaba.fastjson2.JSONObject;
import com.xsrw.common.core.exception.ServiceException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Description:
 * @Author XMING
 * @Date 2023-10-13
 */
public class ZplUtils {


    String zpl = "^XA\n" +
            "^RS,,,3,N,,,2\n" +
            "^RR3\n" +
            "^CW0,E:SIMSUN.TTF^FS\n" +
            "^CI28\n" +
            "^SZ2^JMA\n" +
            "^MCY^PMN\n" +
            "^PW1242\n" +
            "~JSN\n" +
            "^JZY\n" +
            "^LH0,0^LRN\n" +
            "^FT793,173\n" +
            "^A0N,51,51^FDAAAAAAA^FS\n" +
            "^FT557,173\n" +
            "^A0N,41,51^FD物料名称:^FS\n" +
            "^FT557,242\n" +
            "^A0N,41,51^FD物料编码:^FS\n" +
            "^FT557,317\n" +
            "^A0N,41,51^FD批次号:^FS\n" +
            "^FT557,388\n" +
            "^A0N,41,51^FD单位:^FS\n" +
            "^FT557,462\n" +
            "^A0N,41,51^FD生产日期:^FS\n" +
            "^FT793,242\n" +
            "^A0N,51,51^FDBBBBBBB^FS\n" +
            "^FT742,317\n" +
            "^A0N,51,51^FDCCCCCCC^FS\n" +
            "^FT689,388\n" +
            "^A0N,51,51^FDDDDDDDDD^FS\n" +
            "^FT793,462\n" +
            "^A0N,51,51^FDEEEEEEE^FS\n" +
            "^FO144,121\n" +
            "^BQN,1,50^FDHM,12345789123456789^FS\n" +
            "^RFW,H,1,2,1^FD2400^FS\n" +
            "^RFW,H,2,8,1^FDD1F9B1BECEC4B1BE^FS\n" +
            "^PQ1,0,1,Y\n" +
            "^XZ";

    String zpl2 = "^XA\n" +
            "^RS,,,3,N,,,2\n" +
            "^RR3\n" +
            "^CW0,E:SIMSUN.TTF^FS\n" +
            "^CI28\n" +
            "^SZ2^JMA\n" +
            "^MCY^PMN\n" +
            "^PW1242\n" +
            "~JSN\n" +
            "^JZY\n" +
            "^LH0,0^LRN\n" +
            "^FT477,173\n" +
            "^A0N,31,31^FD音频采集插件\\201080000225\\机车运用安全及防火监视装置YDVS-V5^FS\n" +
            "^FT328,173\n" +
            "^A0N,51,51^FD名称:^FS\n" +
            "^FT328,242\n" +
            "^A0N,51,51^FD规格:^FS\n" +
            "^FT328,317\n" +
            "^A0N,51,51^FD批次号:^FS\n" +
            "^FT328,388\n" +
            "^A0N,51,51^FD单位:^FS\n" +
            "^FT328,462\n" +
            "^A0N,51,51^FD生产日期:^FS\n" +
            "^FT480,242\n" +
            "^A0N,51,51^FD60/件^FS\n" +
            "^FT520,317\n" +
            "^A0N,51,51^FD2308254304^FS\n" +
            "^FT472,388\n" +
            "^A0N,51,51^FD件^FS\n" +
            "^FT570,462\n" +
            "^A0N,51,51^FD203-01-01^FS\n" +
            "^FO70,184\n" +
            "^BQN,2,11^FDLA,W00000065^FS\n" +
            "^RFW,H,1,2,1^FD2400^FS\n" +
            "^RFW,H,2,8,1^FD123456789123456789^FS\n" +
            "^PQ1,0,1,Y\n" +
            "^XZ";

    String zpl3 = "^XA\n" +
            "^RS,,,3,N,,,2\n" +
            "^RR3\n" +
            "^CW0,E:SIMSUN.TTF^FS\n" +
            "^CI28\n" +
            "^SZ2^JMA\n" +
            "^MCY^PMN\n" +
            "^PW1242\n" +
            "~JSN\n" +
            "^JZY\n" +
            "^LH0,0^LRN\n" +
            "^FT67,161\n" +
            "^A0N,41,41^FD名称:音频采集插件\\201080000225\\机车运用安全及防火监视装置YDVS-V5^FS\n" +
            "^FT328,242\n" +
            "^A0N,51,51^FD规格:^FS\n" +
            "^FT328,317\n" +
            "^A0N,51,51^FD批次号:^FS\n" +
            "^FT328,388\n" +
            "^A0N,51,51^FD单位:^FS\n" +
            "^FT328,462\n" +
            "^A0N,51,51^FD生产日期:^FS\n" +
            "^FT470,242\n" +
            "^A0N,51,51^FD70/件^FS\n" +
            "^FT520,317\n" +
            "^A0N,51,51^FD2308254304^FS\n" +
            "^FT472,388\n" +
            "^A0N,51,51^FD件^FS\n" +
            "^FT570,462\n" +
            "^A0N,51,51^FD2023-10-12^FS\n" +
            "^FO60,217\n" +
            "^BQN,2,11^FDLA,W00000065^FS\n" +
            "^RFW,H,1,2,1^FD2400^FS\n" +
            "^RFW,H,2,8,1^FD1234567812345678^FS\n" +
            "^PQ1,0,1,Y\n" +
            "^XZ";
    public static String zplFinalStr = "^XA\n" +
            "^RS,,,3,N,,,2\n" +
            "^RR3\n" +
            "^CW0,E:SIMSUN.TTF^FS\n" +
            "^CI28\n" +
            "^SZ2^JMA\n" +
            "^MCY^PMN\n" +
            "^PW1242\n" +
            "~JSN\n" +
            "^JZY\n" +
            "^LH0,0^LRN\n" +
            "^FT107,161\n" +
            "^A0N,41,41^FDdescription^FS\n" +
            "^FT368,242\n" +
            "^A0N,51,51^FD物料编码:^FS\n" +
            "^FT368,317\n" +
            "^A0N,51,51^FD标签编码:^FS\n" +
            "^FT368,388\n" +
            "^A0N,51,51^FD批次号:^FS\n" +
            "^FT368,462\n" +
            "^A0N,51,51^FD规格单位:^FS\n" +
            "^FT600,242\n" +
            "^A0N,51,51^FDmaterialCode^FS\n" +
            "^FT600,317\n" +
            "^A0N,51,51^FDrfid^FS\n" +
            "^FT552,388\n" +
            "^A0N,51,51^FDbatchCode^FS\n" +
            "^FT620,462\n" +
            "^A0N,51,51^FDunitName^FS\n" +
            "^FO100,217\n" +
            "^BQN,2,11^FDLA,rfid^FS\n" +
            "^RFW,H,1,2,1^FD2400^FS\n" +
            "^RFW,H,2,8,1^FDrfid^FS\n" +
            "^PQ1,0,1,Y\n" +
            "^XZ";


    /**
     * @description: 执行打印机打印
     * @param ip
     * @param port
     * @param zplPrint
     * @author XMING
     * @date 2023-10-13
     * @return  void
     */
    public static void zplPrint(String ip, String port,ZplPrint zplPrint) throws IOException {
        String result = zplFinalStr.replace("description",zplPrint.getDescription())
                .replace("materialCode",zplPrint.getMaterialCode())
                .replace("unitName",zplPrint.getUnitName())
                .replace("rfid",zplPrint.getRfid())
                .replace("batchCode",zplPrint.getBatchCode());
        Map<String, Object> resultMap = sendSocket(ip, port, result);
        if(resultMap != null && "1".equals(resultMap.get("success"))){
            throw new ServiceException("打印失败："+resultMap.get("msg"));
        }
    }


    public static Map<String,Object> sendSocket(String ip, String port, String zpl) throws IOException {
        Map<String,Object> resultMap = new JSONObject();

        Socket socket = null;
        PrintWriter out = null;
        InputStream in = null;
        BufferedInputStream bufferedInputStream = null;
        String str = "";

        try  {
            socket = new Socket(ip, Integer.parseInt(port));
            socket.setSoTimeout(3000);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
            out.write(zpl);
            out.flush();
            in = socket.getInputStream();
            bufferedInputStream = new BufferedInputStream(in);
            while (true) {
                try {
                    str = readInputStream(bufferedInputStream);
                } catch (Exception var7) {
                    socket.close();
                    break;
                } finally {
                    socket.close();
                    System.out.println("finally");
                }
            }
        }
        catch(ConnectException e){
            resultMap.put("success","1");
            resultMap.put("msg",ip + ":" + port + " socket " + e.getMessage());
            System.err.println(ip + ":" + port + " socket " + e.getMessage());
            return resultMap;
        }
        catch (Exception e) {
            resultMap.put("success","1");
            resultMap.put("msg",ip + ":" + port + " socket " + e.getMessage());
            System.err.println(ip + ":" + port + " socket " + e.getMessage());
            return resultMap;
        }
        finally {
            if(socket != null) {
                socket.close();
            }
            if(in != null) {
                in.close();
            }
            if(bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if(out != null) {
                out.close();
            }
        }
        System.out.println(str);

        resultMap.put("success","0");
        resultMap.put("result", str);

        return resultMap;
    }

    private static String readInputStream(BufferedInputStream bufferedInputStream) throws IOException {
        String str = "";
        int read = bufferedInputStream.read();
        if (read == -1) {
            return null;
        } else {
            str = str + "" + (char)read;
            int available = bufferedInputStream.available();
            System.out.println("Len got : " + available);
            if (available > 0) {
                byte[] bytes = new byte[available];
                bufferedInputStream.read(bytes);
                str = str + new String(bytes);
                str = str.replaceAll("\\u0002", "");
                str = str.replaceAll("\\u0003", "");
                str = str.replaceAll(" ", "");
                str = str.replaceAll("\n", ";");
                str = str.replaceAll("\r", "");
            }
//            System.out.println(str);
            return str;
        }
    }


}
