package com.candybox.common.service;

import com.candybox.common.dao.model.SysCfg;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface SysCfgService extends BaseService <SysCfg, Long> {

    /**
     * find
     * @param code
     * @return
     */
    SysCfg findByCode(String code);


}
