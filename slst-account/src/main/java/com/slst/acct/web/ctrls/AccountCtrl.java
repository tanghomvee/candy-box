package com.slst.acct.web.ctrls;

import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = "/acct")
public class AccountCtrl extends BaseCtrl {

    @Resource
    private AccountService accountService;

    @RequestMapping(path = {"/list"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(){
        return Msg.success();
    }


    @RequestMapping(path = {"/getacctbyloginer"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg getAcctByLoginer(HttpSession session){

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Account account=null;

        if(curUser.getUserType()==UserTypeEnum.VENDER_EMP.getVal()){
            account=accountService.findAcctByStoreId(curUser.getStoreId());
        }else{
            account=accountService.findAcctByUserId(curUser.getId());
        }

        if (null!=account){
            return Msg.success("查询成功",account);
        }

        return Msg.error("查询失败");
    }





}
