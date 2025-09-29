package com.xsrw.wms.webservice;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KL8XSTLZHCC2ERPHCZCSQXX;

public class WSClient {
    public static void main(String[] args) throws Exception {
        URL url = new URL("file:/C:/863/2K_SmartWarehousing/07_code/jiwu/xsrw-admin/xsrw-modules/xsrw-wms/src/main/resources/wsdl/SI_03KL8_XSTLZHCC2ERP_HCZCSQXX.wsdl");
        QName qname = new QName("http://www.shenhuagroup.com.cn/XSTLZHCC", "SI_03KL8_XSTLZHCC2ERP_HCZCSQXX");

        Service service = Service.create(url, qname);
        SI03KL8XSTLZHCC2ERPHCZCSQXX inOutService = service.getPort(SI03KL8XSTLZHCC2ERPHCZCSQXX.class);
    }
}
