package com.xsrw.common.datasource.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.model.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * MP注入处理器
 */
@Component
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        this.strictInsertFill(metaObject, "createBy", () ->"admin", String.class); // 起始版本 3.3.3(推荐)
        BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
        try {
            if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                LoginUser loginUser = getLoginUser();
                if (loginUser != null) {
                    //--创建人和创建者的自动填充
                    String username = StringUtils.isNotBlank(baseEntity.getCreateBy())
                            ? baseEntity.getCreateBy() : loginUser.getUsername();
                    if (StringUtils.isNotBlank(username)) {
                        baseEntity.setCreateBy(username);
                    }
                    //--部门的自动填充
                    Long dptId = ObjectUtils.isNotEmpty(baseEntity.getDeptId())
                            ? baseEntity.getDeptId() : loginUser.getSysUser().getDeptId();
                    baseEntity.setDeptId(dptId);
                    String dptname = StringUtils.isNotBlank(baseEntity.getDeptName())
                            ? baseEntity.getDeptName() :
                            ObjectUtils.isNotEmpty(loginUser.getSysUser().getDept()) ? loginUser.getSysUser().getDept().getDeptName() : "";
                    if (StringUtils.isNotBlank(dptname)) {
                        baseEntity.setDeptName(dptname);
                    }
                }
                Date current = Objects.nonNull(baseEntity.getCreateTime()) ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(current);
                //--delFlag的自动填充
                String delFlag = StringUtils.isNotBlank(baseEntity.getDelFlag())
                        ? baseEntity.getDelFlag() : "0";
                baseEntity.setDelFlag(delFlag);
            }
        } catch (Exception e) {
            baseEntity.setDelFlag("0");
            baseEntity.setCreateTime(new Date());
//            throw new ServiceException("insert表字段自动填充异常," + e.getMessage());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
        try {
            if (metaObject != null && metaObject.getOriginalObject() instanceof BaseEntity) {
                Date current = new Date();
                baseEntity.setUpdateTime(current);
                String username = getLoginUsername();
                if (StringUtils.isNotBlank(username)) {
                    baseEntity.setUpdateBy(username);
                }
            }
        } catch (Exception e) {
            baseEntity.setUpdateTime(new Date());
//            throw new ServiceException("update表字段自动填充异常, " + e.getMessage());
        }
    }

    /**
     * 获取登录用户名
     */
    private String getLoginUsername() {
        LoginUser loginUser;
        try {
            loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return loginUser.getUsername();
    }

    /**
     * 获取登录用户名
     */
    private LoginUser getLoginUser() {
        LoginUser loginUser;
        try {
            loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return loginUser;
    }

}
