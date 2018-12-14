package com.slst.market.web.ctrls;

import com.google.common.collect.Maps;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.service.SysCfgService;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.CallFee;
import com.slst.market.service.CallFeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
@RequestMapping(path = "/callfee")
public class CallFeeCtrl extends BaseCtrl {

    @Resource
    private CallFeeService callFeeService;

    @Resource
    private SysCfgService sysCfgService;

    @RequestMapping(path = {"/getstate"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg getState(String mobile, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        CallFee callFee= callFeeService.findByVenderId(curUser.getVenderId());

        return Msg.success("查询成功",callFee);
    }


    @RequestMapping(path = {"/getcriterion"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg getCriterion(HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        String rentStr = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_RENT_FEE.getVal()).getCodeVal();
        String cardFeeStr = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_CARD_FEE.getVal()).getCodeVal();
        String callFeeStr = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_CALL_FEE.getVal()).getCodeVal();


        Map<String,Object> map= Maps.newHashMap();
        map.put("rentStr",rentStr);
        map.put("cardFeeStr",cardFeeStr);
        map.put("callFeeStr",callFeeStr);
        return Msg.success("查询成功",map);
    }

    @RequestMapping(path = {"/createcallsrv"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg createCallSrv(String city,HttpSession session) {
        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return callFeeService.createCallSrv(curUser,city);
    }

    @RequestMapping(path = {"/closecallsrv"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg closeCallSrv(HttpSession session) {
        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return callFeeService.closeCallSrv(curUser);
    }

    @RequestMapping(path = {"/recoverycallsrv"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg recoveryCallSrv(HttpSession session) {
        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return callFeeService.recoveryCallSrv(curUser);
    }

}
