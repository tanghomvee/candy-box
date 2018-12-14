package com.slst.report.dao;

import com.slst.report.dao.model.StoreCustomerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface StoreCustomerStatsDao extends JpaRepository<StoreCustomerStats, Long> {

    /**
     * delete
     * @param statsTime
     * @return
     */
    @Modifying
    @Query(value = "delete from  t_store_customer_stats where DATE_FORMAT(statsTime,'%Y-%m-%d')=DATE_FORMAT(?1,'%Y-%m-%d')" , nativeQuery = true)
    Integer deleteByStatsTime(Date statsTime);

    /**
     * cnt
     * @param date
     * @param yn
     * @return
     */
    @Query(value = "select count(1) from  t_store_customer_stats where DATE_FORMAT(statsTime,'%Y-%m-%d')=DATE_FORMAT(?1,'%Y-%m-%d') AND yn=?2" , nativeQuery = true)
    Integer countByStatsTimeAndYn(Date date, Integer yn);
}
