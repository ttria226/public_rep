package com.xsrw.wms;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xsrw.common.security.annotation.EnableCustomConfig;
import com.xsrw.common.security.annotation.EnableRyFeignClients;
import com.xsrw.common.swagger.annotation.EnableCustomSwagger2;
import com.xsrw.wms.webservice.service.ErpService;
import com.xsrw.wms.webservice.service.impl.ErpServiceImpl;

/**
 * 系统模块
 * 
 * @author zjj
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class XsrwWmsApplication{
    public static void main(String[] args)
    {
        SpringApplication.run(XsrwWmsApplication.class, args);
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(ErpServiceImpl.class);
        factory.setAddress("http://10.132.1.75:51100/ws/erp-server");
        factory.create().getEndpoint().getEndpointInfo().setProperty("wsdl_location", "http://10.132.1.75:51100/ws/erp-server?wsdl");
        System.out.println("(♥◠‿◠)ﾉﾞ  wms模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
