package com.slst.market.dao;

import com.slst.market.dao.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ActivityDao extends JpaRepository<Activity, Long>,ActivityExtDao {

    /**
     * 新建并保存活动
     * @param activity
     * @return
     */
    Activity save(Activity activity);

    /**
     * 更新活动
     * @param activity
     * @return
     */
    Activity saveAndFlush(Activity activity);

    /**
     * 根据ID删除活动
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据UserId分页查找活动
     * @param userId
     * @param pageable
     * @return
     */
    Page<Activity> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据venderId分页查找活动
     * @param venderId
     * @param pageable
     * @return
     */
    Page<Activity> findByVenderId(Long venderId, Pageable pageable);

    /**
     * 根据empId分页查找活动
     * @param empId
     * @param pageable
     * @return
     */
    Page<Activity> findByEmpId(Long empId, Pageable pageable);
    /**
     * 根据empId和活动名称分页查找活动
     * @param empId 员工id
     * @param activNameLike 活动名称
     * @param pageable
     * @return
     */
    Page<Activity> findByEmpIdAndActivNameContaining (Long empId,String activNameLike, Pageable pageable);
    /**
     * 根据empId和活动类型分页查找活动
     * @param empId 员工id
     * @param type 活动类型
     * @param pageable
     * @return
     */
    Page<Activity> findByEmpIdAndType (Long empId, Integer type, Pageable pageable);

    /**
     * 根据empId和活动类型和活动名称分页查找活动
     * @param empId 员工id
     * @param type 活动类型
     * @param activNameLike 活动名称
     * @param pageable
     * @return
     */
    Page<Activity> findByEmpIdAndTypeAndActivNameContaining (Long empId, Integer type, String activNameLike, Pageable pageable);

    /**
     * 根据storeId分页查找活动
     * @param storeId
     * @param pageable
     * @return
     */
    Page<Activity> findByStoreId(Long storeId, Pageable pageable);

    /**
     * 根据venderId和titleLike模糊分页查询
     * @param venderId
     * @param activNameLike
     * @param pageable
     * @return
     */
    Page<Activity> findByVenderIdAndActivNameContaining(Long venderId, String activNameLike, Pageable pageable);

    /**
     * 根据storeId和titleLike模糊分页查询
     * @param storeId
     * @param activNameLike
     * @param pageable
     * @return
     */
    Page<Activity> findByStoreIdAndActivNameContaining(Long storeId, String activNameLike, Pageable pageable);


    Page<Activity> findByVenderIdAndType(Long venderId, Integer type, Pageable pageable);

    Page<Activity> findByStoreIdAndType(Long storeId, Integer type, Pageable pageable);

    Page<Activity> findByVenderIdAndTypeAndActivNameContaining(Long venderId, Integer type,String activNameLike, Pageable pageable);

    Page<Activity> findByStoreIdAndTypeAndActivNameContaining(Long storeId, Integer type,String activNameLike, Pageable pageable);

    /**
     * 查询指定smsBoxId下，具体某个店铺的,活动的短信到店人数
     * @param smsBoxId
     * @param startTime
     * @param endTime
     * @param storeId
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT customerId) FROM t_sms_record WHERE smsBoxId =:smsBoxId AND state=1 AND  customerId IN(SELECT customerId FROM t_store_customer WHERE arriveTime >=:startTime AND arriveTime <=:endTime AND storeId =:storeId)",nativeQuery = true)
    Long countSmsArriveCustomerOfStore(@Param("smsBoxId") Long smsBoxId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("storeId") Long storeId);

    /**
     * 查询指定smsBoxId下，具体某个商家所有店铺的，活动的短信到店人数
     * @param smsBoxId
     * @param startTime
     * @param endTime
     * @param venderId
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT customerId) FROM t_sms_record WHERE smsBoxId =:smsBoxId AND state=1 AND customerId IN(SELECT customerId FROM t_store_customer WHERE arriveTime >=:startTime AND arriveTime <=:endTime AND venderId =:venderId)",nativeQuery = true)
    Long countSmsArriveCustomerOfVender(@Param("smsBoxId") Long smsBoxId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("venderId") Long venderId);


    /**
     * 查询指定smsBoxId下，具体某个店铺的,活动的短信到店顾客的电话，到店离店时间
     * @param smsBoxId
     * @param startTime
     * @param endTime
     * @param storeId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT c.mobile,c.arriveTime,c.leaveTime FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.storeId =:storeId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1)",
            countQuery ="SELECT COUNT(*) FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.storeId =:storeId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1)",
            nativeQuery = true)
    Page<Object[]> findSmsArriveMobileAndTimeOfStoreId(@Param("smsBoxId") Long smsBoxId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("storeId") Long storeId,Pageable pageable);

    /**
     * 查询指定smsBoxId下，具体某个商家所有店铺的,活动的短信到店顾客的电话，到店离店时间
     * @param smsBoxId
     * @param startTime
     * @param endTime
     * @param venderId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT c.mobile,c.arriveTime,c.leaveTime FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.venderId =:venderId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1)",
            countQuery ="SELECT COUNT(*) FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.venderId =:venderId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1)",
            nativeQuery = true)
    Page<Object[]> findSmsArriveMobileAndTimeOfVender(@Param("smsBoxId") Long smsBoxId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("venderId") Long venderId, Pageable pageable);

    /**
     * 查询指定smsBoxId和mobile下，某个店铺的,活动的短信到店顾客的电话，到店离店时间
     * @param smsBoxId
     * @param startTime
     * @param endTime
     * @param storeId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT c.mobile,c.arriveTime,c.leaveTime FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.storeId =:storeId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1) AND locate(:mobile,c.mobile)>0",
            countQuery ="SELECT COUNT(*) FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.storeId =:storeId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1) AND locate(:mobile,c.mobile)>0",
            nativeQuery = true)
    Page<Object[]> findSmsArriveMobileAndTimeOfStoreByMobile(@Param("smsBoxId") Long smsBoxId, @Param("mobile") String mobile,@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("storeId") Long storeId,Pageable pageable);


    /**
     * 查询指定smsBoxId和mobile下，某个店铺的,活动的短信到店顾客的电话，到店离店时间
     * @param smsBoxId
     * @param startTime
     * @param endTime
     * @param venderId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT c.mobile,c.arriveTime,c.leaveTime FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.venderId =:venderId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1) AND locate(:mobile,c.mobile)>0",
            countQuery ="SELECT COUNT(*) FROM t_store_customer c WHERE c.arriveTime >=:startTime AND c.arriveTime <=:endTime AND c.venderId =:venderId AND c.customerId IN(SELECT r.customerId FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND state=1) AND locate(:mobile,c.mobile)>0",
            nativeQuery = true)
    Page<Object[]> findSmsArriveMobileAndTimeOfvenderByMobile(@Param("smsBoxId") Long smsBoxId, @Param("mobile") String mobile,@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("venderId") Long venderId,Pageable pageable);

}
