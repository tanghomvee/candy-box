package com.slst.report.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.model.CustomerStoreStats;

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
public interface CustomerStoreStatsService extends BaseService <CustomerStoreStats, Long>{

    /**
     * save
     * @param customerStoreStats
     * @return
     */
    List<CustomerStoreStats> save(List<CustomerStoreStats> customerStoreStats);

    /**
     * del
     * @param createTime
     * @return
     */
    Integer deleteByStatsTime(Date createTime);

    /**
     * find by date-slot
     * @param storeId
     * @param startTime
     * @param endTime
     * @param pager
     * @return
     */
    Pager findByDateSlotAndStoreId(Long storeId, Date startTime, Date endTime, Pager pager);

    /**
     * count by date-slot
     * @param date
     * @return
     */
    Integer countByStatsTime(Date date);
}
