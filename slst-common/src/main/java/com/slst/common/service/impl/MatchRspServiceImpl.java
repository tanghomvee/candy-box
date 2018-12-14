package com.slst.common.service.impl;

import com.slst.common.dao.MatchRspDao;
import com.slst.common.dao.model.MatchRsp;
import com.slst.common.service.MatchRspService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-07-03 10:09
 */
@Service("matchRspService")
public class MatchRspServiceImpl extends BaseServiceImpl<MatchRsp, Long> implements MatchRspService {

    @Resource
    private MatchRspDao matchRspDao;

    @Override
    public MatchRsp save(MatchRsp matchRsp) {
        return matchRspDao.save(matchRsp);
    }

    @Override
    public MatchRsp findByMac(String mac) {
        return matchRspDao.findByMac(mac);
    }

    @Override
    public MatchRsp findByImei(String imei) {
        return matchRspDao.findByImei(imei);
    }
}
