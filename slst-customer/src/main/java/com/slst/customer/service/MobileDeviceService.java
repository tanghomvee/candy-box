package com.slst.customer.service;

import com.slst.common.service.BaseService;
import com.slst.customer.dao.model.MobileDevice;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface MobileDeviceService extends BaseService <MobileDevice, Long> {

    /**
     * save
     * @param mobileDevice
     * @return
     */
      MobileDevice save(MobileDevice mobileDevice);

    /**
     * find by mac
     * @param mac
     * @return
     */
    List<MobileDevice> findByMac(String mac);

    /**
     * find one
     * @param id
     * @return
     */
    MobileDevice findById(Long id);
}
