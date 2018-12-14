package com.slst.customer.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Pager;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.web.vo.CustomerConditionForSmsVO;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface CustomerService extends BaseService <Customer , Long> {

    /**
     * find
     * @param mac
     * @return
     */
    List<Customer> findByMac(String mac);

    /**
     * 根据ID批量查找顾客
     * @param ids
     * @return
     */
    List<Customer> findById(String ids);

    Customer findById(Long id);



    /**
     * save
     * @param customer
     * @return
     */
    Customer save(Customer customer);

    /**
     * 店铺发短信时，根据条件筛选顾客
     * @param customerCondition
     * @param pager
     * @return
     */
    List getCustomerByConditionForSms(CustomerConditionForSmsVO customerCondition, Pager pager);

    /**
     * 根据Mac或Mobile查询到店客户信息(客户ID,手机,mac)
     * @param searchText
     * @return
     */
    List getCustomerByMacOrMobile(Long venderId,Long storeId,String searchText);


    /**----------------------------------短信查询条件-----------------------------------------*/
    List findAllMobileBrand();

    /**
     * 查询所有年龄阶段
     * @return
     */
    List<String> findAllAgeSlot();

    /**
     * 查询所有职业
     * @return
     */
    List<String> findAllCareer();

    /**
     * 查询所有收入标准
     * @return
     */
    List<String> findAllInComeSlot();

    /**
     * 查询有无车辆
     * @return
     */
    List<String> findAllHavingCars();

    /**
     * 查询所有教育程度
     * @return
     */
    List<String> findAllEducations();

    /**
     * 查询所有子女
     * @return
     */
    List<String> findAllChildren();

    /**
     * 查询有房,无房
     * @return
     */
    List<String> findAllHouseSlot();

    /**
     * 查询所有兴趣标签
     * @return
     */
    List<String> findAllInterest();

    /**
     * 店铺发短信或打电话，根据条件筛选顾客
     * @param customerCondition 筛选条件
     * @return 筛选出多少个客户
     */
    int listCustomerNum(CustomerConditionForSmsVO customerCondition);

    /**
     * 通过sql与sql参数查询客户信息
     * @param sql sql语句
     * @param mapParam 参数
     * @return 客户信息
     */
    List<Customer> listCustomerBySql(String sql, Map<String, Object> mapParam);

    /**
     * 通过sql与sql参数查询客户数量
     * @param sql sql语句
     * @param mapParam 参数
     * @return 客户数量
     */
    Integer countCustomerBySql(String sql, Map<String, Object> mapParam);


}
