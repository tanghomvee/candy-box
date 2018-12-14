package com.slst.report.web.ctrls;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slst.common.enums.GenderEnum;
import com.slst.common.enums.SeparatorEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.customer.web.vo.CustomerVO;
import com.slst.report.dao.model.CustomerStoreStats;
import com.slst.report.dao.model.StoreCustomer;
import com.slst.report.service.CustomerStoreStatsService;
import com.slst.report.service.StoreCustomerService;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
@RequestMapping(path = "/report/customer/store")
public class CustomerStoreStatsCtrl extends BaseCtrl {

    @Resource
    private CustomerStoreStatsService customerStoreStatsService;

    @Resource
    private CustomerService customerService;

    @Resource
    private StoreCustomerService storeCustomerService;


    @RequestMapping(path = {"/listByDateSlot"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Msg list(Long  storeId , Date startTime , Date endTime , Pager pager){
        if(storeId == null || storeId < 1){
            return Msg.error("数据错误");
        }

        if (startTime == null){
            startTime = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        }
        if (endTime == null){
            endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
        }

        pager =  customerStoreStatsService.findByDateSlotAndStoreId(storeId , startTime , endTime ,pager);
        if (pager != null && !CollectionUtils.isEmpty(pager.getData())){
            List<CustomerVO> customerVOS = Lists.newArrayList();
            Map<String , Date> arriveTime = Maps.newHashMap();
            for (Object obj : pager.getData()){
                CustomerStoreStats customerStoreStats = (CustomerStoreStats) obj;
                Long customerId = customerStoreStats.getCustomerId();
                CustomerVO customerVO = new CustomerVO();
                BeanUtils.copyProperties(customerStoreStats , customerVO);
                customerVO.setId(customerId);
                customerVO.setNums(customerStoreStats.getComeTimes() == null ? 0 : customerStoreStats.getComeTimes().intValue());
                customerVO.setAvgStayTime(customerStoreStats.getStayAvgTime());
                customerVOS.add(customerVO);

                ///////////////////////////////////////////
                Customer customer = customerService.findById(customerId);
                if(customer == null){
                    continue;
                }
                customerVO.setAgeSlot(customer.getAgeSlot());

                GenderEnum genderEnum = GenderEnum.getByVal(customer.getSex());
                if(genderEnum != null){
                    customerVO.setGender(genderEnum.getDesc());
                }

                customerVO.setMobileBrand(customer.getMobileBrand());

                //获取首次到店时间&最近一次到店时间
                String minKey = "MIN" + SeparatorEnum.UNDERLINE.getVal() + storeId + SeparatorEnum.UNDERLINE.getVal() + customerId;
                String maxKey = "MAX" + SeparatorEnum.UNDERLINE.getVal() +storeId + SeparatorEnum.UNDERLINE.getVal() + customerId;
                Date firstArriveTime = arriveTime.get(minKey) , recencyArriveTime = arriveTime.get(maxKey);
                StoreCustomer storeCustomer = null;
                if(firstArriveTime == null){
                    storeCustomer = storeCustomerService.findFirstTimeByStoreIdAndCustomerId(storeId , customerId);
                    firstArriveTime = storeCustomer == null ? null : storeCustomer.getArriveTime();
                    arriveTime.put(minKey , firstArriveTime);
                }
                if(recencyArriveTime == null){
                    storeCustomer = storeCustomerService.findRecencyTimeByStoreIdAndCustomerId(storeId , customerId);
                    recencyArriveTime = storeCustomer == null ? null : storeCustomer.getArriveTime();
                    arriveTime.put(maxKey , recencyArriveTime);
                }
                customerVO.setFirstArriveTime(firstArriveTime);
                customerVO.setRecencyArriveTime(recencyArriveTime);
            }
            pager.setData(customerVOS);
        }

        return Msg.success(pager);
    }





}
