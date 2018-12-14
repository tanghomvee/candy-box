package com.slst.report.service;

import com.slst.common.enums.DimensionEnum;
import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.model.StoreCustomer;

import java.util.Date;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface StoreCustomerService extends BaseService <StoreCustomer, Long>{

    /**
     * save
     * @param storeCustomer
     * @return
     */
    StoreCustomer save(StoreCustomer storeCustomer);

    /**
     * 修改客户最近离店时间
     * @param mac
     * @param storeId
     * @return
     */
    Integer modifyRecencyLeaveTimeByMacAndStoreId(String mac , Long storeId);

    /**
     * find By CreateTime
     * @param startTime
     * @param endTime
     * @param pageNum
     *@param pageSize @return
     */
    Pager findCustomerStoreStatsByCreateTime(Date startTime, Date endTime, Integer pageNum, Integer pageSize);

    /**
     * find Store CustomerStatsByCreateTime
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    Pager findStoreCustomerStatsByCreateTime(Date startTime, Date endTime, Integer pageNum, Integer pageSize);

    /**
     * find New Client Stats
     * @param storeId
     * @param startTime
     * @return
     */
    Object[] findNewClientStatsByStoreIdAndCreateTime(Long storeId, Date startTime);

    /**
     * find old client stats
     * @param storeId
     * @param startTime
     * @return
     */
    Object[] findOldClientStatsByStoreIdAndCreateTime(Long storeId, Date startTime);

    /**
     * 按小时统计当天门店到店的人数
     * @param createTime
     * @param storeId
     * @return
     */
    Map<Integer, Integer> countArrivedByHourAndStoreId(Date createTime ,Long storeId);

    /**
     * 统计门店指定时间区间已到过店的人数
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Integer countArrivedByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour);

    /**
     * find arrived customer
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Pager findArrivedByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager);

    /**
     * 统计门店指定时间区间已在店的人数
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Integer countStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour);

    /**
     * find staying customer
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Pager findStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager);

    /**
     * 统计门店指定时间区间和小于指定停留时间的人数
     * @param stayTime
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Integer countStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour);

    /**
     * find stranger
     * @param stayTime
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Pager findStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour , Pager pager);

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
     * find first arrive store time
     * @param storeId
     * @param customerId
     * @return
     */
    StoreCustomer findFirstTimeByStoreIdAndCustomerId(Long storeId, Long customerId);
    /**
     * find Recency arrive store time
     * @param storeId
     * @param customerId
     * @return
     */
    StoreCustomer findRecencyTimeByStoreIdAndCustomerId(Long storeId, Long customerId);

    /**
     * find
     * @param customerId
     * @param storeId
     * @param pager
     * @return
     */
    Pager findByCustomerAndStoreId(Long customerId, Long storeId, Pager pager);

    /**
     * 统计指定时间段的总人数
     * @param startTime
     * @param endTime
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    Integer countByTimeSlotAndStoreId(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour);

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
    Map<String,Integer> countStayingByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum byVal);

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
    Map<String,Integer> countArrivedByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension);

    /**
     * 安维度统计到店陌生客户
     * @param stayTime
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @param dimension
     * @return
     */
    Map<String, Integer> countStrangerByTimeSlotAndStoreIdAndDimension(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension);
}
