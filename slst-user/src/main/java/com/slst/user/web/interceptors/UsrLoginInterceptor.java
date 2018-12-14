package com.slst.user.web.interceptors;

import com.alibaba.fastjson.JSON;
import com.slst.common.components.RedisComponent;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UsrLoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsrLoginInterceptor.class);

    private static final String MEDIA_JSON = "application/json;charset=UTF-8";

    @Resource
    private RedisComponent redisComponent;

    @Value("${out.of.service.time}")
    private Long expire;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getServletPath();

        Object operator = null;
        String token = request.getHeader("authorization");
        String userVOStr = null;
        if (!StringUtils.isEmpty(token)) {
            userVOStr = redisComponent.get(token);
            if(!StringUtils.isEmpty(userVOStr)){

                operator = JSON.parseObject(userVOStr, UserVO.class);
            }
        }

        if (operator == null) {
            response.setContentType(MEDIA_JSON);
            String msg = JSON.toJSONString(Msg.login());
            LOGGER.info("请求路径{}, 当前用户未登录，请先登录", uri);
            PrintWriter printWriter = response.getWriter();
            printWriter.write(msg);
            printWriter.flush();
            printWriter.close();
            return false;
        }
        redisComponent.set(token,userVOStr,expire);

        return super.preHandle(request, response, handler);

    }

}
