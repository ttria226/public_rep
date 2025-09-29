package com.xsrw.wms.webservice.util;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KL8XSTLZHCC2ERPHCZCSQXX;
import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KL8XSTLZHCC2ERPHCZCSQXXService;
import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KL9XSTLZHCC2ERPHCCWYDXX;
import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KL9XSTLZHCC2ERPHCCWYDXXService;
import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KM2XSTLZHCC2ERPRECEIVE;
import com.xsrw.wms.webservice.cn.com.shenhuagroup.xstlzhcc.SI03KM2XSTLZHCC2ERPRECEIVEService;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJW2ERPRECEIVE;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJW2ERPRECEIVEResponse;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJWHCCWYDXX;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJWHCCWYDXXResponse;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJWHCZCSQXX;
import com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions.ZWMXSJWHCZCSQXXResponse;

/**
 * WebService工具类
 */

public class WebServiceUtils {

	/**
	  * 上下架信息传输接口
	 */
    public static ZWMXSJWHCZCSQXXResponse SI03KL8XSTLZHCC2ERPHCZCSQXX(ZWMXSJWHCZCSQXX parameters) throws Exception {
    	SI03KL8XSTLZHCC2ERPHCZCSQXXService service = new SI03KL8XSTLZHCC2ERPHCZCSQXXService();
        SI03KL8XSTLZHCC2ERPHCZCSQXX ws = service.getSI03KL8XSTLZHCC2ERPHCZCSQXXPort();

        BindingProvider bp = (BindingProvider) ws;
        check(bp);

        ZWMXSJWHCZCSQXXResponse res = ws.si03KL8XSTLZHCC2ERPHCZCSQXX(parameters);
        return res;
    }

    /**
	  * 仓位移动传输接口
	 */
    public static ZWMXSJWHCCWYDXXResponse SI03KL9XSTLZHCC2ERPHCCWYDXX(ZWMXSJWHCCWYDXX parameters) throws Exception {
    	SI03KL9XSTLZHCC2ERPHCCWYDXXService service = new SI03KL9XSTLZHCC2ERPHCCWYDXXService();
    	SI03KL9XSTLZHCC2ERPHCCWYDXX ws = service.getSI03KL9XSTLZHCC2ERPHCCWYDXXPort();

    	BindingProvider bp = (BindingProvider) ws;
        check(bp);

    	ZWMXSJWHCCWYDXXResponse res = ws.si03KL9XSTLZHCC2ERPHCCWYDXX(parameters);
        return res;
    }

    /**
	  * 盘点结果上传接口
	 */
    public static ZWMXSJW2ERPRECEIVEResponse SI03KM2XSTLZHCC2ERPRECEIVE(ZWMXSJW2ERPRECEIVE parameters) throws Exception {
    	SI03KM2XSTLZHCC2ERPRECEIVEService service = new SI03KM2XSTLZHCC2ERPRECEIVEService();
    	SI03KM2XSTLZHCC2ERPRECEIVE ws = service.getSI03KM2XSTLZHCC2ERPRECEIVEPort();

    	BindingProvider bp = (BindingProvider) ws;
        check(bp);

    	ZWMXSJW2ERPRECEIVEResponse res = ws.si03KM2XSTLZHCC2ERPRECEIVE(parameters);
    	return res;
    }

    private static void check(BindingProvider bp) {
    	Map<String,Object> context = bp.getRequestContext();
        context.put(BindingProvider.USERNAME_PROPERTY, "ZZSOAPXSTL");
        context.put(BindingProvider.PASSWORD_PROPERTY, "h*2.@xS9kl");
    }
}
