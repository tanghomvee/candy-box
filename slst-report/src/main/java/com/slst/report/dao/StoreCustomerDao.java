package com.slst.report.dao;

import com.slst.report.dao.model.StoreCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface StoreCustomerDao extends JpaRepository<StoreCustomer, Long>,StoreCustomerDaoExt {
    /**
     * 修改客户最近离店时间
     * @param mac
     * @param storeId
     * @return
     */
    @Modifying
    @Query(value = "update t_store_customer set leaveTime=NOW(),stayTime=timestampdiff(MINUTE,arriveTime , leaveTime) where mac=?1 and storeId=?2 and yn=1 and leaveTime is null" , nativeQuery = true)
    Integer modifyRecencyLeaveTimeByMacAndStoreId(String mac, Long storeId);

    /**
     * find
     * @param storeId
     * @param startTime
     * @return
     */
    @Query(value = "select count(customerId),avg(stayTime) FROM t_store_customer where DATE_FORMAT(createTime,'%Y-%m-%d') < DATE_FORMAT(?2,'%Y-%m-%d') and customerId not in(" +
            " select customerId from t_store_customer where DATE_FORMAT(createTime,'%Y-%m-%d')=DATE_FORMAT(?2,'%Y-%m-%d') and storeId=?1 and yn =1" +
            ")" , nativeQuery = true)
    Object findNewClientStatsByStoreIdAndCreateTime(Long storeId, Date startTime);
    /**
     * find
     * @param storeId
     * @param startTime
     * @return
     */
    @Query(value = "select count(customerId),avg(stayTime) FROM t_store_customer where DATE_FORMAT(createTime,'%Y-%m-%d') < DATE_FORMAT(?2,'%Y-%m-%d') and customerId in(" +
            " select customerId from t_store_customer where DATE_FORMAT(createTime,'%Y-%m-%d')=DATE_FORMAT(?2,'%Y-%m-%d') and storeId=?1 and yn =1" +
            ")" , nativeQuery = true)
    Object findOldClientStatsByStoreIdAndCreateTime(Long storeId, Date startTime);

    /**
     * 按小时统计当天门店到店的人数
     * @param createTime
     * @param storeId
     * @return
     */

    @Query(value = "SELECT DATE_FORMAT(createTime,'%k') as hours , count(id)  FROM t_store_customer where createTime >= ?1 and storeId=?2  and yn=1  group by hours order by hours+0" , nativeQuery = true)
    List<Object> countHourCustomersByCreateTimeAndStoreId(Date createTime, Long storeId);

    /**
     * 统计门店指定时间区间已到店的人数
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    @Query(value = "SELECT count(id) FROM t_store_customer where leaveTime >= ?1  and leaveTime <= ?2 and storeId=?3 and date_format(leaveTime, '%H') >= ?4 and date_format(leaveTime, '%H') <= ?5 and leaveTime is not null  and yn=1 " , nativeQuery = true)
    Integer countArrivedCustomerByDayAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour);

    /**
     * 统计门店指定时间区间已在店的人数
     * @param start
     * @param end
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    @Query(value = "SELECT count(id) FROM t_store_customer where arriveTime >= ?1  and arriveTime <= ?2 and storeId=?3 and date_format(arriveTime, '%H') >= ?4 and date_format(arriveTime, '%H') <= ?5 and leaveTime is  null  and yn=1 " , nativeQuery = true)
    Integer countStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour);

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
    @Query(value = "SELECT count(id) FROM t_store_customer where stayTime <= ?1 and  arriveTime >= ?2  and arriveTime <= ?3 and storeId=?4 and date_format(arriveTime, '%H') >= ?5 and date_format(arriveTime, '%H') <= ?6 and leaveTime is not null  and yn=1 " , nativeQuery = true)
    Integer countStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour);

    /**
     * 客户首次到店时间
     * @param storeId
     * @param customerId
     * @return
     */
    @Query(value = "SELECT * FROM t_store_customer where  yn=1 and storeId=?1 and customerId=?2 order by id ASC limit 0,1 " , nativeQuery = true)
    StoreCustomer findFirstTimeByStoreIdAndCustomerId(Long storeId, Long customerId);
    /**
     * 客户 最新到店时间
     * @param storeId
     * @param customerId
     * @return
     */
    @Query(value = "SELECT * FROM t_store_customer where  yn=1 and storeId=?1 and customerId=?2 order by id DESC limit 0,1 " , nativeQuery = true)
    StoreCustomer findRecencyTimeByStoreIdAndCustomerId(Long storeId, Long customerId);

    /**
     * 统计指定时间段的客户人数
     * @param startTime
     * @param endTime
     * @param storeId
     * @param startHour
     * @param endHour
     * @return
     */
    @Query(value = "SELECT count(id) FROM t_store_customer where createTime >= ?1  and createTime <= ?2 and storeId=?3 and date_format(createTime, '%H') >= ?4 and date_format(createTime, '%H') <= ?5 and yn=1 " , nativeQuery = true)
    Integer countByTimeSlotAndStoreId(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour);
}
