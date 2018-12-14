package com.slst.common.dao;

import com.slst.common.dao.model.AreaCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface AreaCodeDao extends JpaRepository<AreaCode, Long> {

    /**
     * 根据城市名字查询区号
     * @param city
     * @return
     */
   AreaCode findByCityContaining(String city);


}
