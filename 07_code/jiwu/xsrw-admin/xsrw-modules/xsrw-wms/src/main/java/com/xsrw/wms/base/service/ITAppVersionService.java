package com.xsrw.wms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TAppVersion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ITAppVersionService extends IService<TAppVersion> {


    /**
     * 查询最新app版本
     * @return
     */
    AjaxResult checkAppVersion();


    /**
     * 下载app
     */
    void downloadApp(HttpServletResponse response, HttpServletRequest request);
}
