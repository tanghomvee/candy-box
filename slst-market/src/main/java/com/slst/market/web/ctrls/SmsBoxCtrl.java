package com.slst.market.web.ctrls;

import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.Activity;
import com.slst.market.dao.model.SmsBox;
import com.slst.market.dao.model.SmsFee;
import com.slst.market.service.ActivityService;
import com.slst.market.service.SmsBoxService;
import com.slst.market.service.SmsFeeService;
import com.slst.market.web.vo.SmsSendReqVO;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/6 10:21
 */
@Controller
@RequestMapping(path = "/sms/smsbox")
public class SmsBoxCtrl extends BaseCtrl {

    @Resource
    private ActivityService activityService;

    @Resource
    private SmsBoxService smsBoxService;

    @Resource
    private SmsFeeService smsFeeService;

    @RequestMapping(path = {"/send"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg sendSms(Long targetStoreId,Long activityId,Long smsTplId,String cusIds, HttpSession session) {

        UserVO curUser = getUser();
        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Activity activity= activityService.findActivityById(activityId);

        if (null==activity){
            return Msg.error("不存在的活动");
        }

        if (2==activity.getState()){
            return Msg.error("已经完结的活动");
        }

        Msg rtnPrepareMag = smsBoxService.prepareToSendSmsByVenderOrStore(curUser,targetStoreId,activityId,smsTplId,cusIds);

        if(rtnPrepareMag.getFlag().equals("error")){
            return rtnPrepareMag;
        }

        if(null!= rtnPrepareMag){
            SmsBox initBox = (SmsBox) rtnPrepareMag.getData();
            smsBoxService.startToSendSmsByVenderOrStore(curUser,targetStoreId,initBox.getId());

            return Msg.success("创建短信成功");
        }

        return Msg.error("创建短信失败");
    }

    @RequestMapping(path = {"/sendBox"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg sendBox(Long activityId,String title,int pageNum,int pageSize,String sortKey,String order, HttpSession session) {


        Map<String,Object> map=smsBoxService.initSendBox(activityId,title,pageNum,pageSize,sortKey,order);

        return Msg.success("查询成功",map);
    }

    @RequestMapping(path = {"/smsfee"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg venderFee(Long venderId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (UserTypeEnum.VENDER.getVal()==curUser.getUserType()){
            venderId=curUser.getVenderId();
        }

        if (UserTypeEnum.VENDER_EMP.getVal()==curUser.getUserType()){
            venderId=curUser.getVenderId();
        }
        SmsFee smsFee= smsFeeService.findSmsFeeByVenderId(venderId);

        if(null!= smsFee){
            return Msg.success("查询成功",smsFee.getFee());
        }

        return Msg.error("查询失败");
    }


    /**
     * 确定发送短信(接口是没有传客户的，
     * 客户在redis中取的sql，然后查询出来的)
     * @param req 短信发送请求参数
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/sendSms", method = RequestMethod.POST)
    public Msg sendSms(SmsSendReqVO req){
        UserVO userVO = super.getUser();
        if(ObjectUtils.isEmpty(userVO)){
            return Msg.error("请登陆");
        }
        if(req.getSmsTplId() == null){
            return Msg.error("smsTplId [必填]");
        }
        if(req.getActivityId() == null){
            return Msg.error("activityId [必填]");
        }
        return smsBoxService.sendSms(req, userVO);
    }
}
