package com.slst.market.dao;

import com.slst.market.dao.model.SmsBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface SmsBoxDao extends JpaRepository<SmsBox, Long> {

    /**
     * 保存短信箱记录
     * @param smsBox
     * @return
     */
    SmsBox save(SmsBox smsBox);

    /**
     * 更新短信箱记录
     * @param smsBox
     * @return
     */
    SmsBox saveAndFlush(SmsBox smsBox);

    /**
     * 更新短信箱price，count，acctRecordId
     * @param id
     * @param price
     * @param count
     * @param acctRecordId
     * @return
     */
    @Modifying
    @Query("update SmsBox s set s.price=:price,s.count=:count,s.acctRecordId=:acctRecordId,s.changer=:changer,s.changeTime=:changeTime where s.id=:id")
    Integer modifySmsBoxOnPCA(@Param("id") Long id, @Param("price") Long price, @Param("count") Long count, @Param("acctRecordId") Long acctRecordId, @Param("changer") String changer, @Param("changeTime") Date changeTime);

    /**
     * 根据ID删除短信箱记录
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据userId，分页查找短信箱记录
     * @param userId
     * @param pageable
     * @return
     */
    Page<SmsBox> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据venderId，分页查找短信箱记录
     * @param venderId
     * @param pageable
     * @return
     */
    Page<SmsBox> findByVenderId(Long venderId, Pageable pageable);

    /**
     * 根据storeId，分页查找短信箱记录
     * @param storeId
     * @param pageable
     * @return
     */
    Page<SmsBox> findByStoreId(Long storeId, Pageable pageable);

    /**
     * 根据activityId，分页查找短信箱记录
     * @param activityId
     * @param pageable
     * @return
     */
    Page<SmsBox> findByActivityId(Long activityId, Pageable pageable);


    /**
     * 根据activityId，分页查找短信箱记录
     * @param activityId
     * @param pageable
     * @return
     */
    Page<SmsBox> findByActivityIdAndTitleContaining(Long activityId,String title, Pageable pageable);

    /**
     * 根据venderId和titleLike模糊分页查询
     * @param venderId
     * @param titleLike
     * @param pageable
     * @return
     */
    Page<SmsBox> findByVenderIdAndTitleContaining(Long venderId, String titleLike, Pageable pageable);

    /**
     * 根据storeId和titleLike模糊分页查询
     * @param storeId
     * @param titleLike
     * @param pageable
     * @return
     */
    Page<SmsBox> findByStoreIdAndTitleContaining(Long storeId, String titleLike, Pageable pageable);


}
