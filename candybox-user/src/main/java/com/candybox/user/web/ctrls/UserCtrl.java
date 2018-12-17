package com.candybox.user.web.ctrls;

import com.candybox.common.enums.EncryptionEnum;
import com.candybox.common.web.ctrls.BaseCtrl;
import com.candybox.common.web.vo.Msg;
import com.candybox.user.dao.model.User;
import com.candybox.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = "/user")
public class UserCtrl extends BaseCtrl {

    @Resource
    private UserService userService;

    @RequestMapping(path = "/reg" , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg reg(String mobile , String chkRand,Long referrer){
        if (StringUtils.isEmpty(mobile)){
            return Msg.error("用户名错误");
        }

        User user = userService.findByMobile(mobile);
        if (user != null){
            return Msg.error("手机号码已经存在");
        }

        if (StringUtils.isEmpty(chkRand)){
            return Msg.error("请输入验证码");
        }

        String rand = redisComponent.get(mobile);
        if (!chkRand.equals(rand)){
            return Msg.error("验证码错误");
        }
        user = new User();
        user.setMobile(mobile);
        user.setUserName(mobile);
        user.setRegTime(new Date());
        user.setReferrer(referrer);
        try {
            user.setPwd(EncryptionEnum.MD5_2_HEX.encrypt(System.currentTimeMillis() + ""));
        } catch (Exception e) {
            LOGGER.error("" ,e);
        }
        redisComponent.deleteByKey(mobile);


        return Msg.success();
    }

    @RequestMapping(path = "/login/u" , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg login(String userName , String pwd){
        if (StringUtils.isEmpty(userName)){
            return Msg.error("用户名错误");
        }
        User user = userService.findByUserName(userName);
        if (user == null){
            user = userService.findByMobile(userName);
            if (user == null){
                return Msg.error("用户名不存在");
            }
        }

        if (StringUtils.isEmpty(pwd)){
            return Msg.error("密码错误");
        }

        String encryptedPwd = null;
        try {
           encryptedPwd = EncryptionEnum.MD5_2_HEX.encrypt(pwd);
        } catch (Exception e) {
            LOGGER.error("用户登录加密异常pwd={}"  , pwd, e);
            return Msg.error("密码错误");
        }
        if (!user.getPwd().equals(encryptedPwd)){
            return Msg.error("密码错误");
        }

        return Msg.success();
    }

    @RequestMapping(path = "/login/m" , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg loginByMobile(String mobile , String chkRand){
        if (StringUtils.isEmpty(mobile)){
            return Msg.error("用户名错误");
        }

        User user = userService.findByMobile(mobile);
        if (user == null){
            return Msg.error("手机号码不存在");
        }

        if (StringUtils.isEmpty(chkRand)){
            return Msg.error("请输入验证码");
        }

        String rand = redisComponent.get(mobile);
        if (!chkRand.equals(rand)){
            return Msg.error("验证码错误");
        }
        redisComponent.deleteByKey(mobile);


        return Msg.success();
    }


}
