package com.xsrw.wms.base.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.file.FileUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TAppVersion;
import com.xsrw.wms.base.mapper.TAppVersionMapper;
import com.xsrw.wms.base.service.ITAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class TAppVersionServiceImpl extends ServiceImpl<TAppVersionMapper, TAppVersion> implements ITAppVersionService {

    @Autowired
    private TAppVersionMapper tAppVersionMapper;

    @Value("${app.version.download}")
    private String appUrl;


    /**
     * 查询最新app版本
     * @return
     */
    @Override
    public AjaxResult checkAppVersion() {
        TAppVersion tAppVersion = tAppVersionMapper.selectOne(
                new QueryWrapper<TAppVersion>().eq("del_flag", Constants.DEL_FLAG_NO));
        return AjaxResult.success(tAppVersion);
    }

    @Override
    public void downloadApp(HttpServletResponse response, HttpServletRequest request) {

        try {
            TAppVersion tAppVersion = tAppVersionMapper.selectOne(
                    new QueryWrapper<TAppVersion>().eq("del_flag", Constants.DEL_FLAG_NO));
            if (tAppVersion == null){
                throw new ServiceException("文件不存在");
            }

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, tAppVersion.getAppName());
            FileUtils.writeBytes(appUrl+"/"+tAppVersion.getAppName(), response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
