package com.slst.customer.web.ctrls;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.slst.common.dao.model.MatchRsp;
import com.slst.common.enums.GenderEnum;
import com.slst.common.enums.SeparatorEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.MatchRspService;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.dao.model.CustomerApp;
import com.slst.customer.dao.model.CustomerInterest;
import com.slst.customer.service.CustomerAppService;
import com.slst.customer.service.CustomerInterestService;
import com.slst.customer.service.CustomerService;
import com.slst.customer.web.vo.CustomerConditionForSmsVO;
import com.slst.customer.web.vo.CustomerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = "/customer")
public class CustomerCtrl extends BaseCtrl {
    @Resource
    private CustomerInterestService customerInterestService;
    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerAppService customerAppService;
    @Resource
    private MatchRspService matchRspService;


    @RequestMapping(path = {"/one"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg one(Long id) {
        if (ObjectUtils.isEmpty(id) || id < 1) {
            return Msg.error("参数错误");
        }

        Customer customer = customerService.findById(id);
        if (customer == null) {
            return Msg.error("客户不存在");
        }
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customer, customerVO);
        if (null != customer.getSex()) {
            GenderEnum genderEnum = GenderEnum.getByVal(customer.getSex());
            if (genderEnum != null) {
                customerVO.setGender(genderEnum.getDesc());
            }
        }
        List<CustomerApp> apps = customerAppService.findByCustomerIdAndYn(customer.getId(), YNEnum.YES.getVal());
        if (!CollectionUtils.isEmpty(apps)) {
            List<String> appNames = Lists.transform(apps, new Function<CustomerApp, String>() {
                @Override
                public String apply(CustomerApp customerApp) {
                    return customerApp.getAppName();
                }
            });
            customerVO.setApps(String.join(SeparatorEnum.COMMA.getVal(), appNames));

        }
        List<CustomerInterest> interests = customerInterestService.findByCustomerIdAndYn(customer.getId(), YNEnum.YES.getVal());

        if (!CollectionUtils.isEmpty(interests)) {
            List<String> strings = Lists.transform(interests, new Function<CustomerInterest, String>() {
                @Override
                public String apply(CustomerInterest customerInterest) {
                    return customerInterest.getInterest();
                }
            });
            customerVO.setInterests(String.join(SeparatorEnum.COMMA.getVal(), strings));

        }
        MatchRsp matchRsp = matchRspService.findByMac(customer.getMac());
        if (matchRsp != null) {
            customerVO.setTags(matchRsp.getResult());
        }
        return Msg.success(customerVO);
    }

    /**
     * 根据条件筛选客户
     * @param customerConditionForSmsVO 筛选条件请求对象
     * @param session
     * @return
     */
    @RequestMapping(path = {"/selectcustomer"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg selectCustomer(CustomerConditionForSmsVO customerConditionForSmsVO, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

//        if (curUser.getUserType() == UserTypeEnum.VENDER.getVal()) {
//            if (null == customerConditionForSmsVO.getStoreId()) {
//                customerConditionForSmsVO.setStoreId(0L);
//            }
//            customerConditionForSmsVO.setVenderId(curUser.getVenderId());
//        }
//
//        if (curUser.getUserType() == UserTypeEnum.VENDER_EMP.getVal()) {
//            customerConditionForSmsVO.setStoreId(curUser.getStoreId());
//            customerConditionForSmsVO.setVenderId(0L);
//        }

        if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {
            customerConditionForSmsVO.setStoreId(curUser.getStoreId());
            customerConditionForSmsVO.setVenderId(0L);
        } else {
            customerConditionForSmsVO.setStoreId(0L);
            customerConditionForSmsVO.setVenderId(curUser.getVenderId());
        }

        List rtnList = customerService.getCustomerByConditionForSms(customerConditionForSmsVO, null);

        return Msg.success("查询成功", rtnList);
    }


    /**
     * 短信精准筛选
     *
     * @param storeId    门店ID没有就不传
     * @param searchText 搜索关键字
     * @param session    sessionselectcustomer
     * @return
     */
    @RequestMapping(path = {"/selectfuccustomer"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg selectFucCustomer(Long storeId, String searchText, HttpSession session) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Long venderId = 0L;

//        if (curUser.getUserType() == UserTypeEnum.VENDER.getVal()) {
//            venderId = curUser.getVenderId();
//        }
//
//        if (curUser.getUserType() == UserTypeEnum.VENDER_EMP.getVal()) {
//            storeId = curUser.getStoreId();
//        }

        if (null!=curUser.getStoreId() && curUser.getStoreId()>0){
            storeId = curUser.getStoreId();
        }else{
            venderId = curUser.getVenderId();
        }

        List storeCustomers = customerService.getCustomerByMacOrMobile(venderId, storeId, searchText);

        if (null != storeCustomers) {

            return Msg.success("查询成功", storeCustomers);
        }

        return Msg.error("查询失败");
    }


    @RequestMapping(path = {"/getmobilebrand"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getMobileBrand(HttpSession session) {


        List mobileBrands = customerService.findAllMobileBrand();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallagealot"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllAgeSlot(HttpSession session) {


        List<String> mobileBrands = customerService.findAllAgeSlot();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallcareer"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllCareer(HttpSession session) {


        List<String> mobileBrands = customerService.findAllCareer();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallincomeslot"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllInComeSlot(HttpSession session) {


        List<String> mobileBrands = customerService.findAllInComeSlot();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallhavingcars"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getallhavingcars(HttpSession session) {

        List<String> mobileBrands = customerService.findAllHavingCars();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getalleducations"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllEducations(HttpSession session) {

        List<String> mobileBrands = customerService.findAllEducations();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallchildren"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllChildren(HttpSession session) {

        List<String> mobileBrands = customerService.findAllChildren();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallhouseslot"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllHouseSlot(HttpSession session) {

        List<String> mobileBrands = customerService.findAllHouseSlot();

        return Msg.success("查询成功", mobileBrands);

    }

    @RequestMapping(path = {"/getallinterest"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAllInterest(HttpSession session) {

        List<String> mobileBrands = customerService.findAllInterest();

        return Msg.success("查询成功", mobileBrands);

    }

    /**
     * 发短信，根据条件筛选顾客(注：此接口代替selectcustomer，如果此接口稳定，就可以删除selectcustomer)
     * @author: daiyou.zhong
     * @param customerConditionForSmsVO 筛选条件请求对象
     * @param session
     * @return
     */
    @RequestMapping(path = {"/list/customerNum"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg listCustomerNum(CustomerConditionForSmsVO customerConditionForSmsVO, HttpSession session) {
        UserVO curUser = getUser();
        if (curUser == null) {
            return Msg.error("请先登录后操作!");
        }
        if (curUser.getStoreId() != null && curUser.getStoreId() > 0L) {
            customerConditionForSmsVO.setStoreId(curUser.getStoreId());
            customerConditionForSmsVO.setVenderId(0L);
        } else {
            customerConditionForSmsVO.setStoreId(0L);
            customerConditionForSmsVO.setVenderId(curUser.getVenderId());
        }
        if(customerConditionForSmsVO.getActivityId() == null){
            return Msg.error("活动id [必填]");
        }
        return Msg.success("查询成功", customerService.listCustomerNum(customerConditionForSmsVO));
    }


}
