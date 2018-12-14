package com.slst.report.service;

import com.slst.common.service.BaseService;
import com.slst.report.dao.model.StoreCustomerStats;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface StoreCustomerStatsService extends BaseService <StoreCustomerStats, Long>{

    /**
     * save
     * @param storeCustomerStats
     * @return
     */
    List<StoreCustomerStats> save(List<StoreCustomerStats> storeCustomerStats);

    /**
     * del
     * @param createTime
     * @return
     */
    Integer deleteByStatsTime(Date createTime);

    /**
     * cnt by stats-date
     * @param date
     * @return
     */
    Integer countByStatsTime(Date date);
}
