package com.slst.market.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.market.dao.SmsTplRecordDao;
import com.slst.market.dao.model.SmsTplRecord;
import com.slst.market.service.SmsTplRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-23 14:33
 */
@Service("smsTplRecordService")
public class SmsTplRecordServiceImpl extends BaseServiceImpl<SmsTplRecord ,Long> implements SmsTplRecordService {
    @Resource
    private SmsTplRecordDao smsTplRecordDao;
}
