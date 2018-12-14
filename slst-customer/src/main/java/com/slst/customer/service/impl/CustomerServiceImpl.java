package com.slst.customer.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.NicService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.DateUtils;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Pager;
import com.slst.customer.dao.CustomerDao;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.customer.web.vo.CustomerConditionForSmsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
@Service("customerService")
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CustomerDao customerDao;

    @Resource
    private NicService nicService;

    @Override
    public List<Customer> findByMac(String mac) {
        return customerDao.findByMacAndYn(mac, YNEnum.YES.getVal());
    }

    @Override
    public List<Customer> findById(String ids) {
        List<Customer> customerList = new ArrayList<>();
        Customer findCustomer = null;
        if (StringUtils.isNullOrEmpty(ids)) {
            return null;
        }
        if (ids.contains(",")) {
            String[] idsArr = ids.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                try {
                    findCustomer = customerDao.findByIdAndYn(Long.parseLong(idsArr[i]), YNEnum.YES.getVal());
                    if (null != findCustomer) {
                        customerList.add(findCustomer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("查找customer，ID：" + idsArr[i] + "，数据库操作异常。" + e);
                }
            }
        } else {

            findCustomer = customerDao.findByIdAndYn(Long.parseLong(ids), YNEnum.YES.getVal());
            if (null != findCustomer) {
                customerList.add(findCustomer);
            }

        }
        if (customerList.size() == 0) {
            return null;
        }
        return customerList;
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> optional = customerDao.findById(id);

        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Customer save(Customer customer) {
        return customerDao.save(customer);
    }

    /**
     * 店铺发短信时，根据条件筛选顾客
     *
     * @param customerCondition
     * @param pager
     * @return
     */
    @Override
    public List getCustomerByConditionForSms(CustomerConditionForSmsVO customerCondition, Pager pager) {

        String endTime = customerCondition.getEndTime();

        if (!StringUtils.isNullOrEmpty(endTime)) {
            switch (customerCondition.getSearchType()) {

                case 1:
                    //本周
                    customerCondition.setStartTime(DateUtils.getWeekStart(endTime));
                    break;
                case 2:
                    //本月
                    customerCondition.setStartTime(DateUtils.getMonthStart(endTime));
                    break;
            }

        }else{
            //如果为本日的话,前端没有传结束时间,所以在这里给上结束时间
            String startTime=customerCondition.getStartTime();
            if (!StringUtils.isNullOrEmpty(startTime)){
                endTime=startTime;
            }
        }

        if (!StringUtils.isNullOrEmpty(endTime)) {
            //这里把结束时间+1天
            Date date=DateUtils.str2Date(endTime,DateUtils.DateTimeFormatters.YYYY_MM_DD);
            Date newDate=DateUtils.addOrSubDate(date,1);
            endTime= DateUtils.date2Str(newDate,DateUtils.DateTimeFormatters.YYYY_MM_DD);
            customerCondition.setEndTime(endTime);
        }
        return customerDao.getCustomerWithCondition(customerCondition, pager);
    }


    @Override
    public List getCustomerByMacOrMobile(Long venderId, Long storeId, String searchText) {

        if (!StringUtils.isNullOrEmpty(searchText)) {

            if (null != storeId && 0 != storeId) {
                return getCustomerByStoreIdAndMacOrMobile(storeId,searchText);
            }

            return getCustomerByVenderIdAndMacOrMobile(venderId,searchText);
        }

        return null;
    }

    private List getCustomerByStoreIdAndMacOrMobile(Long storeId, String searchText) {

        if (searchText.indexOf(":") > 0) {
            return customerDao.findCusByStoreIdAndMac(storeId, searchText);
        } else {
            return customerDao.findCusByStoreIdAndMobile(storeId, searchText);
        }
    }

    private List getCustomerByVenderIdAndMacOrMobile(Long venderId, String searchText) {

        if (searchText.indexOf(":") > 0) {
            return customerDao.findCusByVenderIdAndMac(venderId, searchText);
        } else {
            return customerDao.findCusByVenderIdAndMobile(venderId, searchText);
        }
    }


    @Override
    public List findAllMobileBrand() {
//        List brandCNList= customerDao.findAllMobileBrand();
//        List<String> brandENList=customerDao.findAllMobileBrandEn();
//        List mobileBrands=new ArrayList();
//
//        for (int i=0;i<brandCNList.size();i++) {
//            Object[] objects=(Object[])brandCNList.get(i);
//            String corporationEn=objects[0].toString().trim().toUpperCase();
//            String corporationCn=objects[1].toString();
//
//            for (int j = 0; j < brandENList.size(); j++) {
//                String brandEn=brandENList.get(j).trim().toUpperCase();
//                if (corporationEn.contains(brandEn)){
//                    Map<String,String> mobileBrand=new HashMap<>();
//                    mobileBrand.put("key",brandEn);
//                    mobileBrand.put("value",corporationCn);
//                    mobileBrands.add(mobileBrand);
//                }
//            }
//
//        }

//        for (int i=0;i<brandENList.size();i++) {
//
//            Map<String,String> mobileBrand=new HashMap<>();
//
//            for (Object object : brandCNList) {
//                String corporationEn=((Object [])object)[0].toString().toUpperCase();
//                String brandEN=brandENList.get(i).toUpperCase();
//
//                if (corporationEn.contains(brandEN)){
//                    mobileBrand.put("key",brandEN);
//                    mobileBrand.put("value",((Object [])object)[1].toString());
//                    mobileBrands.add(mobileBrand);
//                }
//
//
//            }
//
//
//
//        }


        return customerDao.findAllMobileBrandEn();


    }

    @Override
    public List<String> findAllAgeSlot() {
        return customerDao.findAllAgeSlot();
    }

    @Override
    public List<String> findAllCareer() {
        return customerDao.findAllCareer();
    }

    @Override
    public List<String> findAllInComeSlot() {
        return customerDao.findAllInComeSlot();
    }

    @Override
    public List<String> findAllHavingCars() {
        return customerDao.findAllHavingCars();
    }

    @Override
    public List<String> findAllEducations() {
        return customerDao.findAllEducations();
    }

    @Override
    public List<String> findAllChildren() {
        return customerDao.findAllChildren();
    }

    @Override
    public List<String> findAllHouseSlot() {
        return customerDao.findAllHouseSlot();
    }

    @Override
    public List<String> findAllInterest() {
        return customerDao.findAllInterest();
    }

    @Override
    public int listCustomerNum(CustomerConditionForSmsVO customerCondition) {
        //获取今天日期字符串
        String nowDateStr = DateUtils.getDateStr(new Date());
        //处理通过时间条件筛选到店客户
        switch (customerCondition.getSearchType()) {
            case 0:
                /*
                 * 筛选本日客户:
                 * 由于无限制与本日前端都用了0表示，但如果是本日是传了开始时间,所以如下处理
                 * 1、如果是本日就把结束时间给补上
                 * 2、如果是无限制就把查询类型设置为null
                 */
                if(!StringUtils.isNullOrEmpty(customerCondition.getStartTime())){
                    customerCondition.setEndTime(nowDateStr);
                }else{
                    customerCondition.setSearchType(null);
                }
                break;
            case 1:
                //筛选本周客户
                customerCondition.setStartTime(DateUtils.getWeekStart(nowDateStr));
                customerCondition.setEndTime(nowDateStr);
                break;
            case 2:
                //筛选本月客户
                customerCondition.setStartTime(DateUtils.getMonthStart(nowDateStr));
                customerCondition.setEndTime(nowDateStr);
                break;
            default: break;
        }
        return customerDao.listCustomerNum(customerCondition);
    }

    @Override
    public List<Customer> listCustomerBySql(String sql, Map<String, Object> mapParam) {
        return customerDao.listCustomerBySql(sql, mapParam);
    }

    @Override
    public Integer countCustomerBySql(String sql, Map<String, Object> mapParam) {
        return customerDao.countCustomerBySql(sql, mapParam);
    }

}
