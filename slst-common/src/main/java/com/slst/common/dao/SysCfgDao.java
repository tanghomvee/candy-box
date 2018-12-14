package com.slst.common.dao;

import com.slst.common.dao.model.SysCfg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:42
 */
public interface SysCfgDao extends JpaRepository<SysCfg, Long> {

    /**
     * find
     * @param code
     * @param yn
     * @return
     */
    SysCfg findByCodeAndYn(String code, Integer yn);
}
