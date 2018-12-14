package com.slst.market.dao;

import com.slst.market.dao.model.SmsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface SmsRecordDao extends JpaRepository<SmsRecord, Long>,SmsRecordDaoExt {

    /**
     * 新建（顾客）短信记录
     * @param smsRecord
     * @return
     */
    SmsRecord save(SmsRecord smsRecord);

    /**
     * 更新短信记录
     * @param smsRecord
     * @return
     */
    SmsRecord saveAndFlush(SmsRecord smsRecord);

    /**
     * 根据ID删除（顾客）短信记录
     * @param id
     */
    void deleteById(Long id);

    /**
     *根据SmsBoxId删除短信记录
     * @param smsBoxId
     */
    void deleteBySmsBoxId(Long smsBoxId);

    /**
     * 根据smsBoxId分页查询短信记录
     * @param smsBoxId
     * @param pageable
     * @return
     */
    Page<SmsRecord> findBySmsBoxId(Long smsBoxId, Pageable pageable);

    /**
     * 根据smsBoxId查询短信记录，不分页
     * @param smsBoxId
     * @return
     */
    List<SmsRecord> findBySmsBoxId(Long smsBoxId);

    /**
     * 根据SmsBoxId和发送状态分页查看（顾客）短信记录
     * @param smsBoxId
     * @param state
     * @param pageable
     * @return
     */
    Page<SmsRecord> findBySmsBoxIdAndState(Long smsBoxId, Integer state, Pageable pageable);

    /**
     * 根据smsBoxId和state统计条数
     * @param smsBoxId
     * @param state
     * @return
     */
    Long countBySmsBoxIdAndState(Long smsBoxId, Integer state);


    /**
     * 根据SmsBoxId，查找mobile和errmsg
     * @param smsBoxId
     * @return ORDER BY ?#{#pageable}
     */
    @Query(value = "SELECT r.mobile,r.errmsg FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId",
            countQuery = "SELECT COUNT(*) FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId",
            nativeQuery = true)
    Page<Object[]> findMobileAndErrmsgByBoxId(@Param("smsBoxId") Long smsBoxId,Pageable pageable);

    /**
     * 根据smsBoxId和state，查询mobile和errmsg
     * @param smsBoxId
     * @param state
     * @param pageable
     * @return
     */
    @Query(value = "SELECT r.mobile,r.errmsg FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND r.state =:state",
            countQuery = "SELECT COUNT(*) FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND r.state =:state",
            nativeQuery = true)
    Page<Object[]> findMobileAndErrmsgByBoxIdAndState(@Param("smsBoxId") Long smsBoxId,@Param("state") Integer state,Pageable pageable);

    /**
     * 根据smsBoxId和state，查询mobile和errmsg
     * @param smsBoxId
     * @param mobile
     * @param pageable
     * @return
     */
    @Query(value = "SELECT r.mobile,r.errmsg FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND locate(:mobile,r.mobile)>0",
            countQuery = "SELECT COUNT(*) FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND locate(:mobile,r.mobile)>0",
            nativeQuery = true)
    Page<Object[]> findMobileAndErrmsgByBoxIdAndMobile(@Param("smsBoxId") Long smsBoxId, @Param("mobile") String mobile, Pageable pageable);


    /**
     * 根据smsBoxId和state和部分mobile，查询mobile和errmsg
     * @param smsBoxId
     * @param mobile
     * @param pageable
     * @return
     */
    @Query(value = "SELECT r.mobile,r.errmsg FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND r.state =:state AND locate(:mobile,r.mobile)>0",
            countQuery = "SELECT COUNT(*) FROM t_sms_record r WHERE r.smsBoxId =:smsBoxId AND r.state =:state AND locate(:mobile,r.mobile)>0",
            nativeQuery = true)
    Page<Object[]> findMobileAndErrmsgByBoxIdAndMobileAndState(@Param("smsBoxId") Long smsBoxId,@Param("state") Integer state,@Param("mobile") String mobile,Pageable pageable);

    /**
     * 根据smsBoxId和yn统计条数
     * @param smsBoxId 发件箱Id
     * @param yn 是否有效
     * @return
     */
    Long countBySmsBoxIdAndYn(Long smsBoxId, Integer yn);

}
