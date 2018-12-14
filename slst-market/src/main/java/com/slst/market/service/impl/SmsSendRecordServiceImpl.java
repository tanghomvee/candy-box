package com.slst.market.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.market.dao.SmsSendRecordDao;
import com.slst.market.dao.model.SmsSendRecord;
import com.slst.market.service.SmsSendRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-23 14:35
 */
@Service("smsSendRecordService")
public class SmsSendRecordServiceImpl extends BaseServiceImpl<SmsSendRecord ,Long> implements SmsSendRecordService {

    @Resource
    private SmsSendRecordDao smsSendRecordDao;
}
