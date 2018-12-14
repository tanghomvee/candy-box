package com.slst.market.web.ctrls;

import com.slst.common.utils.RegexUtils;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.service.DialNumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(path = "/dialnum")
public class DialNumCtrl extends BaseCtrl {

    @Resource
    private DialNumService dialNumService;

    @RequestMapping(path = {"/setdialnum"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(String mobile, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        boolean isMobile= RegexUtils.isMobile(mobile);

        if (!isMobile){
            return Msg.error("请输入正确的手机号");
        }

        Integer result= dialNumService.modifyDialNum(curUser,mobile);

        if (result>0){
            return Msg.success("设置成功");
        }

        return Msg.error("设置失败");
    }


    @RequestMapping(path = {"/getdialnum"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        String dailNum= dialNumService.getDialNumByUserId(curUser);



        return Msg.success("查询成功",dailNum);
    }
}
