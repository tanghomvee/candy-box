package com.slst.report.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.report.dao.StoreCustomerStatsDao;
import com.slst.report.dao.model.StoreCustomerStats;
import com.slst.report.service.StoreCustomerStatsService;
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
@Service("storeCustomerStatsService")
public class StoreCustomerStatsServiceImpl extends BaseServiceImpl<StoreCustomerStats,Long> implements StoreCustomerStatsService {

    @Resource
    private StoreCustomerStatsDao storeCustomerStatsDao;

    @Override
    public List<StoreCustomerStats> save(List<StoreCustomerStats> storeCustomerStats) {
        return storeCustomerStatsDao.saveAll(storeCustomerStats);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteByStatsTime(Date statsTime) {
        return storeCustomerStatsDao.deleteByStatsTime(statsTime);
    }

    @Override
    public Integer countByStatsTime(Date date) {
        return storeCustomerStatsDao.countByStatsTimeAndYn(date , YNEnum.YES.getVal());
    }
}
