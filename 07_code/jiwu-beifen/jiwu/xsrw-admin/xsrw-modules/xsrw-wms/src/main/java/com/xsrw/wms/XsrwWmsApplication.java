package com.xsrw.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.xsrw.common.security.annotation.EnableCustomConfig;
import com.xsrw.common.security.annotation.EnableRyFeignClients;
import com.xsrw.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author zjj
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class XsrwWmsApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(XsrwWmsApplication.class, args);
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
