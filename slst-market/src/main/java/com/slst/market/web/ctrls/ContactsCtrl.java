package com.slst.market.web.ctrls;

import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.Contacts;
import com.slst.market.service.ContactsService;
import com.slst.market.web.vo.ContactsSaveByActivityIdVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
//@RequestMapping(path = "/callrecord")
@RequestMapping(path = "/contacts")
public class ContactsCtrl extends BaseCtrl {

    @Resource
    private ContactsService contactsService;

    //@RequestMapping(path = {"/addrecord"} , method = {RequestMethod.GET , RequestMethod.POST})
    @RequestMapping(path = {"/addcontacts"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg addContacts(Long activityId, String customerIds, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        if (StringUtils.isEmpty(customerIds)) {
            return Msg.error("请先选择需要发送的人群");
        }

        return contactsService.createContacts(curUser, activityId, customerIds);
    }

    /**
     *
     * @param activityId 活动ID
     * @param intention 查询全部不传
     * @param toNum 不查手机号码不传
     * @param pageNum 页号
     * @param pageSize 页数大小
     * @return 返回通话联系人列表
     */
    //@RequestMapping(path = {"/getrecord"}, method = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(path = {"/getcontacts"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getContacts(Long activityId,Integer intention, String toNum,int pageNum, int pageSize) {
        //创建返回参数
        Page<Contacts> page = null;
        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        if (curUser.getUserType().equals(UserTypeEnum.VENDER_EMP.getVal())){
            page=contactsService.findByActivityIdAndVenderEmpId(activityId,curUser.getVenderEmpId(),intention,toNum, pageNum, pageSize);
            if(page.getTotalElements()<1){
                page = contactsService.findByActivityId(activityId,intention,toNum, pageNum, pageSize);
            }
            return Msg.success("查询成功", page);
        }
        page = contactsService.findByActivityId(activityId,intention,toNum, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    @RequestMapping(path = {"/remark"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg remark(Long recordId, Integer intention, String remark, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        return contactsService.callRemark(curUser, intention, remark, recordId);
    }

    /**
     * 商家给员工分配联系人
     * @param req 请求参数
     * @param session
     * @return
     */
    @RequestMapping(path = {"/add/contacts"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg addContacts(ContactsSaveByActivityIdVO req, HttpSession session) {

        UserVO curUser = getUser();
        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }
        return contactsService.addContacts(curUser, req);
    }

    /**
     * 通过活动id查询添加联系人状态
     * @param activityId 活动id
     * @return
     */
    @RequestMapping(path = {"/getSave/contactsStatus"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getSaveContactsStatus(Long activityId){
        UserVO userVO = getUser();
        if (null == userVO) {
            return Msg.error("请先登录后操作!");
        }
        if(activityId == null){
            return Msg.error("activityId [必填]");
        }
        return contactsService.getSaveContactsStatus(userVO, activityId);
    }


}
