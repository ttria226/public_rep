//package com.xsrw.common.core.print;
//
//import com.zebra.sdk.comm.Connection;
//import com.zebra.sdk.graphics.ZebraImageFactory;
//import com.zebra.sdk.graphics.ZebraImageI;
//import com.zebra.sdk.printer.ZebraPrinter;
//import com.zebra.sdk.printer.ZebraPrinterFactory;
//import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
//import com.zebra.sdk.printer.discovery.UsbDiscoverer;
//import com.zebra.sdk.printer.discovery.ZebraPrinterFilter;
//
//import javax.imageio.ImageIO;
//import javax.imageio.stream.ImageOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//
///**
// * @author wxr
// * @date 2023/10/915:25
// */
//public class PrintHandler {
//    private static ZebraPrinter zebraPrinter = null;
//    //单次打印只给一个对象，防止打印机线程堵塞
//    public static void initPrinter()throws Exception{
//        if(null==zebraPrinter){
//            DiscoveredPrinter[] printers = null;
//            Connection connection = null;
//
//            ByteArrayOutputStream baos = null;
//            printers = UsbDiscoverer.getZebraUsbPrinters(new ZebraPrinterFilter());
//            connection = printers[0].getConnection();
//            System.out.println(connection.toString());
//            connection.open();
//            zebraPrinter = ZebraPrinterFactory.getInstance(connection);
//            System.out.println("获取到实例---"+zebraPrinter.toString());
//        }
//    }
//
//    public static void main(String[] args) {
//        try{
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
////            ImageIO.write(image, "jpg", imageOutput);
////这里的image需要是BufferedImage类型的，需要自己构造
//            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//            ZebraImageI image2 = ZebraImageFactory.getImage((inputStream));
//            System.out.println("--开始打印--");
//            zebraPrinter.printImage(image2, 0, 0, 450,320, false);
//            System.out.println("--打印完成--");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//}
