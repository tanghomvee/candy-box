package com.slst.market.web.ctrls;

import com.google.common.collect.Lists;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.NumberPool;
import com.slst.market.service.NumberPoolService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(path = "/numberPool")
public class NumberPoolCtrl extends BaseCtrl {

    @Resource
    private NumberPoolService numberPoolService;

    @RequestMapping(path = {"/list"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(int pageNum, int pageSize, String sortKey, String orderKey,HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (curUser.getUserType()==UserTypeEnum.ADMIN.getVal()){
            return numberPoolService.pageNumberPool(pageNum,pageSize,sortKey,orderKey);
        }

        return Msg.error();
    }

    @RequestMapping(path = {"/buylist"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(int pageNum,int pageSize, HttpSession session) {

        Page<NumberPool> page= numberPoolService.findNotUsedFromAdmin(pageNum,pageSize);

        return Msg.success("购买列表",page);
    }


    @RequestMapping(path = {"/buyrelaynum"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(Long[] list, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return numberPoolService.buyRelayCard(curUser, Lists.newArrayList(list));
    }
}
