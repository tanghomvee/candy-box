package com.slst.customer.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.customer.dao.MobileDeviceDao;
import com.slst.customer.dao.model.MobileDevice;
import com.slst.customer.service.MobileDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 15:01
 */
@Service("customerDeviceService")
public class MobileDeviceServiceImpl extends BaseServiceImpl<MobileDevice, Long> implements MobileDeviceService {

    @Resource
    private MobileDeviceDao mobileDeviceDao;

    @Override
    public MobileDevice save(MobileDevice mobileDevice) {
        return mobileDeviceDao.save(mobileDevice);
    }

    @Override
    public List<MobileDevice> findByMac(String mac) {
        return mobileDeviceDao.findByMacAndYn(mac , YNEnum.YES.getVal());
    }

    @Override
    public MobileDevice findById(Long id) {
         Optional<MobileDevice> optional = mobileDeviceDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }
}
