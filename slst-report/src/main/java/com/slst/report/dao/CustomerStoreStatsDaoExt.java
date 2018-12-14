package com.slst.report.dao;

import com.slst.common.web.vo.Pager;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface CustomerStoreStatsDaoExt {



    /**
     * find By date-slot
     * @param storeId
     * @param startTime
     * @param endTime
     * @param pager
     * @return
     */
    Pager findByDateSlotAndStoreId(Long storeId, Date startTime, Date endTime, Pager pager);
}
