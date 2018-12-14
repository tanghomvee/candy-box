package com.slst.customer.dao;


import com.slst.customer.dao.model.CustomerInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:42
 */
public interface CustomerInterestDao extends JpaRepository<CustomerInterest, Long> {

    /**
     * find
     * @param customerId
     * @param yn
     * @return
     */
    List<CustomerInterest> findByCustomerIdAndYn(Long customerId, Integer yn);
}
