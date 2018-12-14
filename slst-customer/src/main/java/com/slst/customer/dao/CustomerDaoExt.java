package com.slst.customer.dao;

import com.slst.common.web.vo.Pager;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.web.vo.CustomerConditionForSmsVO;

import java.util.List;
import java.util.Map;

public interface CustomerDaoExt {
    List getCustomerWithCondition(CustomerConditionForSmsVO customerCondition, Pager pager);

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