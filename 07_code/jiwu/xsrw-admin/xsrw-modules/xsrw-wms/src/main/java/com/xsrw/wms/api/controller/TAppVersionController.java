package com.xsrw.wms.api.controller;

import com.xsrw.common.core.utils.file.FileUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TAppVersion;
import com.xsrw.wms.base.service.ITAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: APP版本更新相关
 * @Author XMING
 * @Date 2024-03-28
 */
@RestController
@RequestMapping("/app")
public class TAppVersionController {

    @Autowired
    private ITAppVersionService tAppVersionService;


    /**
     * 查询最新app版本
     * @return
     */
    @GetMapping("/version")
    public AjaxResult getVersion(){
        return tAppVersionService.checkAppVersion();
    }


    /**
     * 下载app
     */
    @GetMapping("/version/download")
    public void downloadApp(HttpServletResponse response, HttpServletRequest request){
        tAppVersionService.downloadApp(response,request);
    }



}
