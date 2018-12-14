package com.slst.market.web.ctrls;

import com.slst.common.enums.ActivityTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.Activity;
import com.slst.market.service.ActivityService;
import com.slst.market.service.ActivityVenderEmpRelationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(path = "/sms/activity")
public class ActivityCtrl extends BaseCtrl {

    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityVenderEmpRelationService activityVenderEmpRelationService;

    @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg list(Long venderId, Long storeId, Integer type, String activName, int pageNum, int pageSize, String sortKey, String orderKey, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Page<Activity> pages = null;

        if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {

            if (null!=type){
                pages=activityService.findByStoreIdAndTypeAndActivName(curUser.getStoreId(),type, activName, pageNum, pageSize, sortKey, orderKey);
                return Msg.success("查询成功", pages);
            }

            if (StringUtils.isEmpty(activName)) {
                pages = activityService.findActivByStoreId(curUser.getStoreId(), pageNum, pageSize, sortKey, orderKey);
            } else {
                pages = activityService.findActivByStoreIdAndActivName(curUser.getStoreId(), activName, pageNum, pageSize, sortKey, orderKey);
            }
        } else if(null == curUser.getStoreId() || curUser.getStoreId() <= 0L) {
            if(curUser.getVenderEmpId() != null && curUser.getVenderEmpId() > 0L){
                pages = activityService.findActivByEmpId(curUser.getVenderEmpId(), type, activName, pageNum, pageSize, sortKey, orderKey);
                return Msg.success("查询成功", pages);
            }

            if (null!=type){

                pages=activityService.findByVenderIdAndTypeAndActivName(curUser.getVenderId(),type, activName, pageNum, pageSize, sortKey, orderKey);
                return Msg.success("查询成功", pages);
            }

            if (StringUtils.isEmpty(activName)) {
                pages = activityService.findActivByVenderId(curUser.getVenderId(), pageNum, pageSize, sortKey, orderKey);
            } else {
                pages = activityService.findActivByVenderIdAndActivName(curUser.getVenderId(), activName, pageNum, pageSize, sortKey, orderKey);
            }
        }

        if (null != venderId && 0 != venderId) {

            if (null!=type){
                pages=activityService.findByVenderIdAndTypeAndActivName(venderId,type, activName, pageNum, pageSize, sortKey, orderKey);
                return Msg.success("查询成功", pages);
            }

            if (StringUtils.isEmpty(activName)) {
                pages = activityService.findActivByVenderId(venderId, pageNum, pageSize, sortKey, orderKey);
            } else {
                pages = activityService.findActivByVenderIdAndActivName(venderId, activName, pageNum, pageSize, sortKey, orderKey);
            }
        }

        if (null != storeId && 0 != storeId) {

            if (null!=type){
                pages=activityService.findByStoreIdAndTypeAndActivName(storeId,type, activName, pageNum, pageSize, sortKey, orderKey);
                return Msg.success("查询成功", pages);
            }

            if (StringUtils.isEmpty(activName)) {
                pages = activityService.findActivByStoreId(storeId, pageNum, pageSize, sortKey, orderKey);
            } else {
                pages = activityService.findActivByStoreIdAndActivName(storeId, activName, pageNum, pageSize, sortKey, orderKey);
            }
        }


        return Msg.success("查询成功", pages);


    }

    /**
     * 通过参数查询商家员工所有活动
     * @param type 活动类型1短信,2电话
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(path = {"/empActivityList"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg listVenderEmpActivity(Integer type, String activName, Integer pageNum, Integer pageSize){
        UserVO curUser = getUser();
        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }
        if(pageNum == null){
            return Msg.error("pageNum [必填]");
        }
        if(pageSize == null){
            return Msg.error("pageSize [必填]!");
        }

        if(pageNum>=0){
            pageNum+=1;
        }

        Pager result = null;
        try {
            result = activityVenderEmpRelationService.listVenderEmpActivity(type, curUser.getVenderEmpId(), activName, pageNum, pageSize);
        }catch (Exception ex){
            LOGGER.error("通过参数查询商家员工所有活动异常, type={}, venderEmpId={}, activNameLike={}", type, curUser.getVenderEmpId(), activName, ex);
            return Msg.error("通过参数查询商家员工所有活动异常");
        }
        return Msg.success(result);
    }


    @RequestMapping(path = {"/activitytype"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getActivityType(HttpSession session) {

        List types = new ArrayList();
        ActivityTypeEnum[] activityTypeEnum = ActivityTypeEnum.values();

        for (int i = 0; i < activityTypeEnum.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", activityTypeEnum[i].getVal());
            map.put("value", activityTypeEnum[i].getDesc());
            types.add(map);
        }


        return Msg.success("查询成功", types);
    }

    @RequestMapping(path = {"/create"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg create(Activity activity, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Activity rtnActivity = activityService.createActivity(curUser, activity);

        if (null != rtnActivity) {
            return Msg.success("创建成功", rtnActivity);
        }

        return Msg.error("查询失败");
    }


    @RequestMapping(path = {"/delete"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg delete(String ids, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        return activityService.deleteActivityByIds(ids);
    }



    @RequestMapping(path = {"/effectparttop"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg effectPartTop(Long venderId, Long smsBoxId, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Map<String, Object> map = activityService.showSummaryOfSmsBox(venderId, smsBoxId);

        return Msg.success("查询成功", map);
    }

    @RequestMapping(path = {"/piesendresult"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg pieSendResult(Long smsBoxId, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Map<String, Object> map = activityService.resultOfSendingSms(smsBoxId);

        return Msg.success("查询成功", map);
    }


    @RequestMapping(path = {"/piearrive"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg pieArrive(Long venderId, Long smsBoxId, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Map<String, Object> map = activityService.resultOfArrive(venderId, smsBoxId);

        return Msg.success("查询成功", map);
    }


    @RequestMapping(path = {"/effectpartbottom"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg effectPartBottom(Long venderId, Long smsBoxId, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Map<String, Object> map = activityService.summaryOfSmsAndArrive(venderId, smsBoxId);

        return Msg.success("查询成功", map);
    }

    @RequestMapping(path = {"/smsstatedetail"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg smsStateDetail(Long smsBoxId, Integer state, String mobile, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Page<Object[]> pages = activityService.showDetailOfSms(smsBoxId, pageNum, pageSize);

        if (-2 != state) {

            if (StringUtils.isEmpty(mobile)) {
                pages = activityService.filterSmsRecordByState(smsBoxId, state, pageNum, pageSize);
            } else {
                pages = activityService.filterSmsRecordByMobileAndState(smsBoxId, state, mobile, pageNum, pageSize);
            }

        } else {
            if (!StringUtils.isEmpty(mobile)) {
                pages = activityService.filterSmsRecordByMobile(smsBoxId, mobile, pageNum, pageSize);
            }
        }


        return Msg.success("查询成功", pages);
    }

    @RequestMapping(path = {"/arrivedetail"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg arrivedetail(Long venderId, Long smsBoxId, String mobile, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Page<Object[]> pages = activityService.showDetailOfArriveCust(venderId, smsBoxId, mobile, pageNum, pageSize);

        return Msg.success("查询成功", pages);
    }

}
