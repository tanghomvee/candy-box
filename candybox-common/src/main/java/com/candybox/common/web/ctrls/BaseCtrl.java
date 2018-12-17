package com.candybox.common.web.ctrls;

import com.candybox.common.components.RedisComponent;
import com.candybox.common.constants.SessionKey;
import com.candybox.common.service.SysCfgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseCtrl {
    protected Logger LOGGER = null;

    @Resource
    private HttpSession session;

    @Resource
    private HttpServletRequest request;

    @Resource
    protected SysCfgService sysCfgService;

    @Resource
    protected RedisComponent redisComponent;


    public BaseCtrl() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }

    public Object getUser(){
        return  session.getAttribute(SessionKey.USER);
    }

}
