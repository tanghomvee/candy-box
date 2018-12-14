package com.slst.common.dao;

import com.slst.common.dao.model.MatchRsp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface MatchRspDao extends JpaRepository<MatchRsp, Long> {

    /**
     * find by mac
     * @param mac
     * @return
     */
    MatchRsp findByMac(String mac);

    /**
     * find by imei
     * @param imei
     * @return
     */
    MatchRsp findByImei(String imei);
}
