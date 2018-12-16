package com.candybox.common.dao;

import com.candybox.common.dao.model.Nic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface NicDao extends JpaRepository<Nic, Long> {

    /**
     * find
     * @param mac
     * @param yn
     * @return
     */
    List<Nic> findByMacAndYn(String mac, Integer yn);

    /**
     * find
     * @param mac
     * @param macType
     * @param yn
     * @return
     */
    List<Nic> findByMacAndMacTypeAndYn(String mac, Integer macType, Integer yn);

    /**
     * find
     * @param macType
     * @param yn
     * @return
     */
    List<Nic> findByMacTypeAndYn(Integer macType, Integer yn);
}
