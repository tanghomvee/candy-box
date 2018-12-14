package com.slst.customer.service;

import com.slst.common.service.BaseService;
import com.slst.customer.dao.model.CustomerInterest;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface CustomerInterestService extends BaseService <CustomerInterest, Long> {
    /**
     * save
     * @param customerInterest
     * @return
     */
    List<CustomerInterest> save(CustomerInterest... customerInterest);

    /**
     *
     * @param customerId
     * @param yn
     * @return
     */
    List<CustomerInterest> findByCustomerIdAndYn(Long customerId, Integer yn);
}
