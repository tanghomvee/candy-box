package com.slst.report.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.CustomerStoreStatsDao;
import com.slst.report.dao.model.CustomerStoreStats;
import com.slst.report.service.CustomerStoreStatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-21 16:31
 */
@Service("customerStoreStatsService")
public class CustomerStoreStatsServiceImpl extends BaseServiceImpl<CustomerStoreStats ,Long> implements CustomerStoreStatsService {

    @Resource
    private CustomerStoreStatsDao customerStoreStatsDao;

    @Override
    public List<CustomerStoreStats> save(List<CustomerStoreStats> customerStoreStats) {
        return customerStoreStatsDao.saveAll(customerStoreStats);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteByStatsTime(Date createTime) {
        return customerStoreStatsDao.deleteByStatsTime(createTime);
    }

    @Override
    public Pager findByDateSlotAndStoreId(Long storeId, Date startTime, Date endTime, Pager pager) {
        return customerStoreStatsDao.findByDateSlotAndStoreId(storeId ,startTime , endTime , pager);
    }

    @Override
    public Integer countByStatsTime(Date date) {
        return customerStoreStatsDao.countByStatsTimeAndYn(date , YNEnum.YES.getVal());
    }
}
