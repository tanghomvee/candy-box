package com.slst.common.service.impl;

import com.slst.common.dao.CallRspDao;
import com.slst.common.dao.model.CallRsp;
import com.slst.common.service.CallRspService;
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
@Service("callRspService")
public class CallRspServiceImpl extends BaseServiceImpl<CallRsp , Long> implements CallRspService {

    @Resource
    private CallRspDao callRspDao;

    @Override
    public CallRsp findByBillId(String billId) {
        return callRspDao.findByBillId(billId);
    }

    @Override
    public CallRsp save(CallRsp callRsp) {
        return callRspDao.save(callRsp);
    }
}
