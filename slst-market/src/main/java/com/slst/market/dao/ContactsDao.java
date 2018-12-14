package com.slst.market.dao;

import com.slst.market.dao.model.Contacts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactsDao extends JpaRepository<Contacts, Long> {


    /**
     * 根据活动ID查询已添加联系人数量
     * @param activityId 活动id
     * @return 已创建联系人的数量
     */
    @Query(value = "select count(1) from t_contacts where activityId=:activityId", nativeQuery = true)
    Integer queryByActivityId(@Param("activityId") Long activityId);
    /**
     * 根据活动ID查询通话记录表
     * @param activityId
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityId(Long activityId, Pageable pageable);

    /**
     * 根据活动ID和商家员工ID查询已添加联系人数量
     * @param activityId 活动id
     * @param venderEmpId 商家员工id
     * @return 已创建联系人的数量
     */
    @Query(value = "select count(1) from t_contacts where venderEmpId=:venderEmpId and activityId=:activityId", nativeQuery = true)
    Integer queryByActivityIdAndVenderEmpId(@Param("activityId") Long activityId, @Param("venderEmpId") Long venderEmpId);

    /**
     * 根据活动ID和商家员工ID查询通话记录表
     * @param activityId
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndVenderEmpId(Long activityId,Long venderEmpId, Pageable pageable);

    /**
     * 根据手机号码后四位查找
     * @param activityId
     * @param toNum
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndToNumContaining(Long activityId,String toNum, Pageable pageable);

    /**
     * 根据活动ID，员工ID,手机号码后四位查找
     * @param activityId
     * @param toNum
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndVenderEmpIdAndToNumContaining(Long activityId,Long venderEmpId,String toNum, Pageable pageable);


    /**
     * 根据活动ID和意向等级查询通话记录表
     * @param activityId
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndIntention(Long activityId,Integer intention, Pageable pageable);


    /**
     * 根据活动ID,员工ID和意向等级查询通话记录表
     * @param activityId
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndVenderEmpIdAndIntention(Long activityId,Long venderEmpId,Integer intention, Pageable pageable);


    /**
     * 根据活动ID,意向等级和手机号码后四位查找
     * @param activityId
     * @param intention
     * @param toNum
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndIntentionAndToNumContaining(Long activityId,Integer intention,String toNum, Pageable pageable);


    /**
     * 根据活动ID,意向等级和手机号码后四位查找
     * @param activityId
     * @param intention
     * @param toNum
     * @param pageable
     * @return
     */
    Page<Contacts> findByActivityIdAndVenderEmpIdAndIntentionAndToNumContaining(Long activityId,Long venderEmpId,Integer intention,String toNum, Pageable pageable);

}
