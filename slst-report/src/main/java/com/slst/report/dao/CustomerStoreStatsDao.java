package com.slst.report.dao;

import com.slst.report.dao.model.CustomerStoreStats;
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
public interface CustomerStoreStatsDao extends JpaRepository<CustomerStoreStats, Long>  , CustomerStoreStatsDaoExt{

    /**
     * delete
     * @param statsTime
     * @return
     */
    @Modifying
    @Query(value = "delete from  t_customer_store_stats where DATE_FORMAT(statsTime,'%Y-%m-%d')=DATE_FORMAT(?1,'%Y-%m-%d')" , nativeQuery = true)
    Integer deleteByStatsTime(Date statsTime);

    /**
     * count By Stats-Time And Yn
     * @param date
     * @param yn
     * @return
     */
    @Query(value = "select count(1) from  t_customer_store_stats where DATE_FORMAT(statsTime,'%Y-%m-%d')=DATE_FORMAT(?1,'%Y-%m-%d') and yn=?2" , nativeQuery = true)
    Integer countByStatsTimeAndYn(Date date, Integer yn);
}
