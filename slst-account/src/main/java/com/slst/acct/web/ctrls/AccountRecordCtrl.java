package com.slst.acct.web.ctrls;


import com.slst.acct.service.AccountRecordService;
import com.slst.acct.web.vo.AcctRecordVO;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.common.web.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(path = "/acctrecord")
public class AccountRecordCtrl extends BaseCtrl {

    @Resource
    private AccountRecordService accountRecordService;

    @RequestMapping(path = {"/gettypea"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg getTypeA(HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        UserTypeEnum userTypeEnum=UserTypeEnum.getByVal(curUser.getUserType());

        Map<String,String> map=accountRecordService.initTypeAList(userTypeEnum);

        return Msg.success("查询成功",map);
    }


    @RequestMapping(path = {"/list"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(AcctRecordVO acctRecordVO, Pager pager, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        acctRecordVO.setUserId(curUser.getId());

        if (curUser.getUserType()==UserTypeEnum.VENDER_EMP.getVal()){
            acctRecordVO.setStoreId(curUser.getStoreId());
        }




        return Msg.success("查询成功",accountRecordService.findAcctRecord(acctRecordVO,pager));
    }


    /**
     * 代理商首页折线图
     * @param session
     * @return
     */
    @RequestMapping(path = {"/line"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg line(HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return Msg.success("查询成功",accountRecordService.sumIncomeOfAgentForThisMonth(curUser));
    }


}
