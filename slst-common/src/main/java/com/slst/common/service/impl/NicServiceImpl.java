package com.slst.common.service.impl;

import com.google.common.collect.Lists;
import com.slst.common.dao.NicDao;
import com.slst.common.dao.model.Nic;
import com.slst.common.enums.MacTypeEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.NicService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-06-13 10:03
 */
@Service("deviceMacService")
public class NicServiceImpl extends BaseServiceImpl<Nic, Long> implements NicService {

    @Resource
    private NicDao nicDao;
    @Override
    public boolean isMobileMac(String mac) {
        if(StringUtils.isEmpty(mac) || mac.length() != MAC_LEN){
            LOGGER.warn("MAC地址错误:{}" , mac);
            return false;
        }
        mac = mac.substring(0 ,MAC_CORPORATION_LEN);
        List<Nic> nics = nicDao.findByMacAndMacTypeAndYn(mac, MacTypeEnum.MOBILE.getVal() , YNEnum.YES.getVal());
        return !CollectionUtils.isEmpty(nics);
    }

    @Override
    public List<Nic> findByMac(String mac) {
        if(StringUtils.isEmpty(mac) || mac.length() != MAC_LEN){
            LOGGER.warn("MAC地址错误:{}" , mac);
            return null;
        }
        mac = mac.substring(0 ,MAC_CORPORATION_LEN);
        return nicDao.findByMacAndYn(mac , YNEnum.YES.getVal());
    }

    @Override
    public List<Nic> save(Nic... nics) {
        if(nics == null || nics.length < 1){
            return null;
        }
        for (Nic nic : nics){
            String mac = nic.getMac();
            if(StringUtils.isEmpty(mac) || mac.length() != MAC_LEN){
                LOGGER.warn("MAC地址错误:{}" , mac);
                return null;
            }
            mac = mac.substring(0 ,MAC_CORPORATION_LEN);
            nic.setMac(mac);
        }

        return nicDao.saveAll(Lists.newArrayList(nics));
    }

    @Override
    public List<Nic> findByMacTypeAndYn(Integer macType, Integer yn) {
        return nicDao.findByMacTypeAndYn(macType,yn);
    }


}
