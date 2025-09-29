package com.xsrw.common.core.exception.user;

import com.xsrw.common.core.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author zjj
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
