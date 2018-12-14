package com.slst.market.web.ctrls;

import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.CallRecord;
import com.slst.market.service.CallRecordService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/callrecord")
public class CallRecordCtrl extends BaseCtrl {

    @Resource
    private CallRecordService callRecordService;

    @RequestMapping(path = {"/call"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg call(Long recordId, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        return callRecordService.call(curUser, recordId);
    }

//    @RequestMapping(path = {"/testEx"} , method = {RequestMethod.GET , RequestMethod.POST})
//    @ResponseBody
//    public Msg testEx(String billId, String callId, Long duration) {
//
//        try {
//            boolean isTrue=callRecordService.execute(billId,callId,duration);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return Msg.error("sss");
//    }
}
