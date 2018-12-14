package com.slst.market.web.ctrls;

import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsTpl;
import com.slst.market.service.SmsTplService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
 * @date 2018-05-23 14:37
 */
@Controller
@RequestMapping(path = "/sms/tpl")
public class SmsTplCtrl extends BaseCtrl {

    @Resource
    private SmsTplService smsTplService;

    /**
     * 所有短信模板
     * @param venderId
     * @param storeId
     * @param title
     * @param pageNum
     * @param pageSize
     * @param sortKey
     * @param orderKey
     * @param session
     * @return
     */
    @RequestMapping(path = {"/list"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(Long venderId,Long storeId,String title,int pageNum, int pageSize, String sortKey, String orderKey, HttpSession session){

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<SmsTpl> pages=null;

        if (null!=curUser.getStoreId() && curUser.getStoreId()>0L){
            if (StringUtils.isEmpty(title)){
                pages= smsTplService.findSmsTplByStoreId(curUser.getStoreId(),pageNum,pageSize,sortKey,orderKey);
            }else{
                pages=smsTplService.findSmsTplByStoreIdAndTitle(curUser.getStoreId(),title,pageNum,pageSize,sortKey,orderKey);
            }
        }else{

            if (StringUtils.isEmpty(title)){
                pages= smsTplService.findSmsTplByVenderId(curUser.getVenderId(),pageNum,pageSize,sortKey,orderKey);
            }else{
                pages=smsTplService.findSmsTplByVenderIdAndTitle(curUser.getVenderId(),title,pageNum,pageSize,sortKey,orderKey);
            }
        }


        if(null!= venderId && 0!=venderId){
            if (StringUtils.isEmpty(title)){
                pages= smsTplService.findSmsTplByVenderId(venderId,pageNum,pageSize,sortKey,orderKey);
            }else{
                pages=smsTplService.findSmsTplByVenderIdAndTitle(venderId,title,pageNum,pageSize,sortKey,orderKey);
            }
        }

        if(null!= storeId && 0!=storeId){
            if (StringUtils.isEmpty(title)){
                pages= smsTplService.findSmsTplByStoreId(storeId,pageNum,pageSize,sortKey,orderKey);
            }else{
                pages=smsTplService.findSmsTplByStoreIdAndTitle(storeId,title,pageNum,pageSize,sortKey,orderKey);
            }
        }



        if(null!= pages){
            return Msg.success("查询成功",pages);
        }

        return Msg.error("查询失败");
    }

    /**
     * 通过的短信模板
     * @param storeId
     * @param title
     * @param pageNum
     * @param pageSize
     * @param sortKey
     * @param orderKey
     * @param session
     * @return
     */
    @RequestMapping(path = {"/acceslist"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg acceslist(Long storeId,String title,int pageNum, int pageSize, String sortKey, String orderKey, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<SmsTpl> page=null;

        if (null!=curUser.getStoreId() && curUser.getStoreId()>0L){
            page=smsTplService.findSmsTplByStateAndTitle(0L,curUser.getStoreId(),1,title,pageNum,pageSize,sortKey,orderKey);
        }else{
            page=smsTplService.findSmsTplByStateAndTitle(curUser.getVenderId(),storeId,1,title,pageNum,pageSize,sortKey,orderKey);
        }

        if(null!= page){
            return Msg.success("查询成功",page);
        }

        return Msg.error("查询失败");
    }



    @RequestMapping(path = {"/create"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg create(SmsTpl smsTpl, HttpSession session){

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        SmsTpl rtnSmsTpl= smsTplService.createSmsTpl(curUser,smsTpl);

        if(null!= rtnSmsTpl){
            return Msg.success("创建成功",rtnSmsTpl);
        }

        return Msg.error("查询失败");
    }

    @RequestMapping(path = {"/detail"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg create(Long smsTplId, HttpSession session){

        SmsTpl smsTpl= smsTplService.findSmsTplById(smsTplId);

        if(null!= smsTpl){
            return Msg.success("查询成功",smsTpl);
        }

        return Msg.error("查询失败");
    }


    @RequestMapping(path = {"/delsmstpl"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg delSmsTpl(String ids){

        return smsTplService.deleteSmsTpl(ids);

    }


}
