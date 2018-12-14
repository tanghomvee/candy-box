package com.slst.report.dao;

import com.slst.common.enums.DimensionEnum;
import com.slst.common.web.vo.Pager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface StoreCustomerDaoExt  {

    /**
     * findCustomerStoreStatsByCreateTime
     * @param startTime
     * @param endTime
     * @param yn
     * @param pageNum
     * @param pageSize
     * @return
     */
    Pager findCustomerStoreStatsByCreateTime(Date startTime, Date endTime, Integer yn, Integer pageNum, Integer pageSize);

    /**
     * findStoreCustomerStatsByCreateTime
     * @param startTime
     * @param endTime
     * @param yn
     * @param pageNum
     * @param pageSize
     * @return
     */
    Pager findStoreCustomerStatsByCreateTime(Date startTime, Date endTime, Integer yn, Integer pageNum, Integer pageSize);

    /**
     * 分页查询特定时间段到店的客户
     *
     * @param mom
     * @param storeId
     * @param startTime
     * @param endTime
     * @param pager
     * @return
     */
    Pager findByTimeSlotAndStoreId(String mom, Long storeId, Date startTime, Date endTime, Pager pager);

    /**
     * 分页查询
     * @param customerId
     * @param storeId
     * @param pager
     * @return
     */
    Pager findByCustomerAndStore(Long customerId, Long storeId, Pager pager);

    /**
     * 查询门店指定时间区间已到店的客户
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @param pager
     * @return
     */
    Pager findArrivedCustomerByDayAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager);

    /**
     * 查询门店指定时间区间已在店的客户
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @param pager
     * @return
     */
    Pager findStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager);

    /**
     * 查询门店指定时间区间和小于指定停留时间的客户
     * @param stayTime
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @param pager
     * @return
     */
    Pager findStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager);

    /**
     * 安维度统计住店客户
     * @param startTime
     * @param endTime
     * @param storeId
     * @param startHour
     * @param endHour
     * @param byVal
     * @return
     */
    List<HashMap<String, String>> countStayingByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum byVal);

    /**
     * 安维度统计到店客户
     * @param startTime
     * @param endTime
     * @param storeId
     * @param startHour
     * @param endHour
     * @param dimension
     * @return
     */
    List<HashMap<String,String>> countArrivedByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension);


    /**
     * 安维度统计到店的陌生客户
     * @param stayTime
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @param dimension
     * @return
     */
    List<HashMap<String, String>> findStrangerByTimeSlotAndStoreIdAndStayTimeAndDimension(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension);
}
