package com.slst.report.web.ctrls;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.*;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.customer.web.vo.CustomerVO;
import com.slst.report.dao.model.StoreCustomer;
import com.slst.report.service.StoreCustomerService;
import com.slst.vender.dao.model.StoreRuleRelation;
import com.slst.vender.service.StoreRuleRelationService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-06-05 11:23
 */
@Controller
@RequestMapping(path = "/report/store/customer")
public class StoreCustomerCtrl extends BaseCtrl {

    @Resource
    private StoreCustomerService storeCustomerService;

    @Resource
    private StoreRuleRelationService storeRuleRelationService;

    @Resource
    private CustomerService customerService;

    @RequestMapping(path = {"/storeCustomer"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg findCustomer(Long customerId  , Long storeId, Pager pager){
        if(ObjectUtils.isEmpty(customerId) || customerId < 1 || ObjectUtils.isEmpty(storeId) || storeId < 1){
            return Msg.error("参数错误");
        }
        pager =  storeCustomerService.findByCustomerAndStoreId(customerId , storeId , pager);

        return Msg.success(pager);
    }


    @RequestMapping(path = {"/staying"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg staying(Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour, Pager pager){
        if(ObjectUtils.isEmpty(storeId) || storeId < 1){
            return Msg.error("参数错误");
        }

        if (startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if (endHour == null || endHour < 0 || endHour > 23){
            endHour = 23;
        }
        if (startTime == null){
            startTime = DateTime.now().toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().toDate();
        }

        startTime = new DateTime(startTime).withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        endTime = new DateTime(endTime).withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();

        pager =  storeCustomerService.findStayingByTimeSlotAndStoreId(startTime , endTime ,storeId ,startHour , endHour ,pager);
        pager = buildBaseInfo(pager, null);
        return Msg.success(pager);
    }

    @RequestMapping(path = {"/pieStaying"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg statsStayingCustomerByTime(Integer dimension , Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour){
        if(ObjectUtils.isEmpty(storeId) || storeId < 1 || DimensionEnum.getByVal(dimension) == null){
            return Msg.error("参数错误");
        }

        if (startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if (endHour == null || endHour < 0 || endHour > 23){
            endHour = 23;
        }
        if (startTime == null){
            startTime = DateTime.now().toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().toDate();
        }

        startTime = new DateTime(startTime).withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        endTime = new DateTime(endTime).withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();

        Map<String , Integer>  data = storeCustomerService.countStayingByTimeSlotAndStoreIdAndDimension(startTime , endTime ,storeId ,startHour , endHour , DimensionEnum.getByVal(dimension));

        return Msg.success(data);

    }


    @RequestMapping(path = {"/arrived"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg arrived(Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour, Pager pager){
        if(ObjectUtils.isEmpty(storeId) || storeId < 1){
            return Msg.error("参数错误");
        }

        if (startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if (endHour == null || endHour < 0 || endHour > 23){
            endHour = 23;
        }

        if (startTime == null){
            startTime = DateTime.now().toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().toDate();
        }

        startTime = new DateTime(startTime).withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        endTime = new DateTime(endTime).withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();

        pager =  storeCustomerService.findArrivedByTimeSlotAndStoreId(startTime , endTime ,storeId ,startHour , endHour ,pager);

        pager = buildBaseInfo(pager, null);

        return Msg.success(pager);
    }

    @RequestMapping(path = {"/pieArrived"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg statsArrivedCustomerByTime(Integer dimension , Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour){
        if(ObjectUtils.isEmpty(storeId) || storeId < 1 || DimensionEnum.getByVal(dimension) == null){
            return Msg.error("参数错误");
        }

        if (startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if (endHour == null || endHour < 0 || endHour > 23){
            endHour = 23;
        }
        if (startTime == null){
            startTime = DateTime.now().toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().toDate();
        }

        startTime = new DateTime(startTime).withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        endTime = new DateTime(endTime).withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();

        Map<String , Integer>  data = storeCustomerService.countArrivedByTimeSlotAndStoreIdAndDimension(startTime , endTime ,storeId ,startHour , endHour , DimensionEnum.getByVal(dimension));

        return Msg.success(data);

    }

    @RequestMapping(path = {"/stranger"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg stranger(Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour, Pager pager){

        if(ObjectUtils.isEmpty(storeId) || storeId < 1 ){
            return Msg.error("参数错误");
        }
        Msg msg = getStrangerStayTime(storeId);
        if(!msg.isSuccess()){
            return msg;
        }
        Integer strangerStayTime = (Integer) msg.getData();

        if (startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if (endHour == null || endHour < 0 || endHour > 23){
            endHour = 23;
        }

        if (startTime == null){
            startTime = DateTime.now().toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().toDate();
        }

        startTime = new DateTime(startTime).withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        endTime = new DateTime(endTime).withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();


        pager =  storeCustomerService.findStrangerByTimeSlotAndStoreIdAndStayTime(strangerStayTime , startTime , endTime ,storeId ,startHour , endHour ,pager);

        pager = buildBaseInfo(pager, strangerStayTime);

        return Msg.success(pager);
    }

    @RequestMapping(path = {"/pieStranger"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg statsStrangerCustomerByTime(Integer dimension ,Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour){

        if(ObjectUtils.isEmpty(storeId) || storeId < 1 || DimensionEnum.getByVal(dimension) == null){
            return Msg.error("参数错误");
        }
        Msg msg = getStrangerStayTime(storeId);
        if(!msg.isSuccess()){
            return msg;
        }
        Integer strangerStayTime = (Integer) msg.getData();

        if (startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if (endHour == null || endHour < 0 || endHour > 23){
            endHour = 23;
        }

        if (startTime == null){
            startTime = DateTime.now().toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().toDate();
        }

        startTime = new DateTime(startTime).withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        endTime = new DateTime(endTime).withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();


        Map<String , Integer>  data =  storeCustomerService.countStrangerByTimeSlotAndStoreIdAndDimension(strangerStayTime , startTime , endTime ,storeId ,startHour , endHour ,DimensionEnum.getByVal(dimension));


        return Msg.success(data);
    }


    @RequestMapping(path = {"/listByTimeSlot"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(String mom ,Long  storeId , Date startTime , Date endTime , Pager pager){
        if(storeId == null || storeId < 1){
            return Msg.error("数据错误");
        }
        Msg msg = getStrangerStayTime(storeId);
        if(!msg.isSuccess()){
            return msg;
        }
        Integer strangerStayTime = (Integer) msg.getData();
        if (startTime == null){
            startTime = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
        }

        pager =  storeCustomerService.findByTimeSlotAndStoreId(mom, storeId, startTime, endTime, pager);
        pager = buildBaseInfo(pager, strangerStayTime);

        return Msg.success(pager);
    }



    @RequestMapping(path = {"/line"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg statsLineCustomers(Long  storeId){
        if(storeId == null || storeId < 1){
            return Msg.error("数据错误");
        }
        Map<Integer , Integer> data = storeCustomerService.countArrivedByHourAndStoreId(new Date(), storeId);

        Integer min = getExpVal(storeId  , RuleTypeEnum.RULE_TYPE_ST_COLLECT_TIME_LIMIT , RuleExpEnum.GE) ,
                max = getExpVal(storeId  , RuleTypeEnum.RULE_TYPE_ST_COLLECT_TIME_LIMIT , RuleExpEnum.LE);

        if (min == null){
            min = getExpVal(storeId  , RuleTypeEnum.RULE_TYPE_ST_COLLECT_TIME_LIMIT , RuleExpEnum.GT);
            min = min == null ? 0 : min + 1;

        }
        if (max == null){
            max = getExpVal(storeId  , RuleTypeEnum.RULE_TYPE_ST_COLLECT_TIME_LIMIT , RuleExpEnum.LT);
            max = max == null ? 23 : max - 1;
        }

        Map<String , Integer> retData = Maps.newTreeMap(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });
        for (int hour = min ; hour <= max ; hour ++){
            retData.put(hour + "" , data != null ? data.getOrDefault(hour , 0) : 0);
        }


        return Msg.success(retData);
    }
    @RequestMapping(path = {"/pie"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg statsPieCustomer(Long  storeId , Date startTime , Date endTime){

        return statsPieCustomerByTime(storeId , startTime , endTime , 0 ,23);
    }

    @RequestMapping(path = {"/percent"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg showPercent(Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour){
        Msg msg = this.statsPieCustomerByTime(storeId , startTime ,endTime ,startHour ,endHour);
        if(!msg.isSuccess()){
            return msg;
        }
        Map<String , Integer> data = (Map<String, Integer>) msg.getData();

        Integer arrived = data.getOrDefault("arrived" , 0);
        Integer staying = data.getOrDefault("staying" , 0);
        Integer stranger = data.getOrDefault("stranger" , 0);
        DateTime deltaEndDateTime = null;

        if(startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if(endHour == null || endHour > 23 || endHour < 1){
            endHour = 23;
        }

        if (startTime == null){
            startTime = DateTime.now().withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        }
        if (endTime == null){
            deltaEndDateTime = DateTime.now().minusHours(1);
            endTime = DateTime.now().withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
        }else{
            deltaEndDateTime = new DateTime(endTime).minusHours(1);
        }

        Integer total = storeCustomerService.countByTimeSlotAndStoreId(startTime, endTime, storeId, startHour,endHour);
        Integer deltaOneHour = storeCustomerService.countByTimeSlotAndStoreId(startTime, deltaEndDateTime.toDate(), storeId, startHour,endHour);

        Double pa = 0D , psa = 0D , psr = 0D , pd = 0D;
        if(total != null && total > 0){
            pa = arrived / (total+0.0);
            psa = staying / (total+0.0);
            psr = stranger / (total+0.0);
            pd = deltaOneHour / (total+0.0);
        }
        Map<String , String> retMap = Maps.newHashMap();
        retMap.put("arrived" , String.format("%.2f", pa * 100));
        retMap.put("staying" , String.format("%.2f", psa * 100));
        retMap.put("stranger" , String.format("%.2f", psr * 100));
        retMap.put("delta" , String.format("%.2f", pd * 100));

        return Msg.success(retMap);
    }

    @RequestMapping(path = {"/pieTime"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg statsPieCustomerByTime(Long  storeId , Date startTime , Date endTime , Integer startHour , Integer endHour){
        if(storeId == null || storeId < 1){
            return Msg.error("数据错误");
        }
        if(startHour == null || startHour < 0 || startHour > 23){
            startHour = 0;
        }
        if(endHour == null || endHour > 23 || endHour < 1){
            endHour = 23;
        }

        Msg msg = getStrangerStayTime(storeId);
        if(!msg.isSuccess()){
            return msg;
        }


        Integer strangerStayTime = (Integer) msg.getData();

        if (startTime == null){
            startTime = DateTime.now().withHourOfDay(startHour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().withHourOfDay(endHour).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
        }

        Integer arrived = storeCustomerService.countArrivedByTimeSlotAndStoreId(startTime, endTime, storeId, startHour, endHour );
        Integer staying = storeCustomerService.countStayingByTimeSlotAndStoreId(startTime, endTime, storeId, startHour,endHour );
        Integer stranger = storeCustomerService.countStrangerByTimeSlotAndStoreIdAndStayTime(strangerStayTime ,startTime, endTime, storeId, startHour,endHour );

        Map<String , Integer> data = Maps.newHashMap();
        data.put("arrived" , arrived);
        data.put("staying" , staying);
        data.put("stranger" , stranger);

        return Msg.success(data);
    }



    private Msg getStrangerStayTime(Long storeId){

        Integer storeDefaultCustomerStayTime= getExpVal(storeId , RuleTypeEnum.RULE_TYPE_ST_CUST_STAY_TIME, RuleExpEnum.EQ);
        if(storeDefaultCustomerStayTime == null){
            SysCfg sysCfg =  sysCfgService.findByCode(SysCfgEnum.SYS_DEFAULT_CUSTOMER_STAY_TIME.getVal());
            if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
                LOGGER.error("系统未配置默认客户逗留时间");
                return  Msg.error();
            }
            storeDefaultCustomerStayTime = Integer.valueOf(sysCfg.getCodeVal().trim());
        }

        Integer strangerStayTime= getExpVal(storeId , RuleTypeEnum.RULE_TYPE_ST_STRANGER_STAY_TIME, RuleExpEnum.LE);
        if(strangerStayTime == null){
            strangerStayTime= getExpVal(storeId , RuleTypeEnum.RULE_TYPE_ST_STRANGER_STAY_TIME, RuleExpEnum.LT);
            strangerStayTime = strangerStayTime == null ? null : strangerStayTime -1;
        }
        strangerStayTime = strangerStayTime == null ? storeDefaultCustomerStayTime : strangerStayTime + storeDefaultCustomerStayTime;
        return Msg.success(strangerStayTime);
    }

    private Integer getExpVal(Long storeId , RuleTypeEnum typeEnum , RuleExpEnum expEnum){

        List<StoreRuleRelation> storeRuleRelations = storeRuleRelationService.findByStoreIdAndRuleType(storeId , typeEnum.getVal());
        if (CollectionUtils.isEmpty(storeRuleRelations)){
           return null;
        }

        for (StoreRuleRelation storeRuleRelation : storeRuleRelations){
            String exp = storeRuleRelation.getExp();
            if (StringUtils.isEmpty(exp)){
                LOGGER.warn("门店{},规则表达式为空" , storeId);
                continue;
            }

            RuleExpEnum ruleExpEnum = RuleExpEnum.getByVal(exp);
            if(ruleExpEnum == null){
                LOGGER.warn("门店{},规则表达式不存在:{}"  ,storeId, exp);
                continue;
            }
            if(ruleExpEnum.equals(expEnum)){
                return Integer.valueOf(storeRuleRelation.getVal());
            }

        }
        return null;
    }

    private Pager buildBaseInfo(Pager pager, Integer strangerStayTime) {
        if (pager != null && !CollectionUtils.isEmpty(pager.getData())){
            List<CustomerVO> customerVOS = Lists.newArrayList();
            for (Object obj : pager.getData()){
                StoreCustomer storeCustomer = (StoreCustomer) obj;
                Long customerId = storeCustomer.getCustomerId();
                CustomerVO customerVO = new CustomerVO();
                BeanUtils.copyProperties(storeCustomer , customerVO);
                customerVOS.add(customerVO);
                customerVO.setId(customerId);

                ///////////////////////////////////////////
                Customer customer = customerService.findById(customerId);
                if(customer == null){
                    continue;
                }
                customerVO.setAgeSlot(customer.getAgeSlot());
                GenderEnum genderEnum = GenderEnum.getByVal(customer.getSex());
                if (genderEnum != null){
                    customerVO.setGender(genderEnum.getDesc());
                }
                if (storeCustomer.getArriveTime() != null && storeCustomer.getLeaveTime() != null && strangerStayTime != null){
                    customerVO.setStayState(StayStateEnum.STAID.getDesc());
                    if(storeCustomer.getStayTime() <= strangerStayTime){
                        customerVO.setStayState(StayStateEnum.STRANGER.getDesc());
                    }
                }else if (storeCustomer.getArriveTime() != null && storeCustomer.getLeaveTime() == null){
                    customerVO.setStayState(StayStateEnum.STAYING.getDesc());
                }
            }
            pager.setData(customerVOS);
        }
        return pager;
    }
}
