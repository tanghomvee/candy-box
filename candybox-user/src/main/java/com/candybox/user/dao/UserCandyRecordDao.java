package com.candybox.user.dao;

import com.candybox.user.dao.model.UserCandyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 10:50
 */
public interface UserCandyRecordDao extends JpaRepository<UserCandyRecord, Long> , UserCandyRecordDaoExt {


    @Query(value = "select * from t_user_candy_record where userId=?1 and candyId=?2 and kind=?3 and operateTime >= (?4)/1000 and operateTime <= (?5)/1000 AND YN=?6" , nativeQuery=true)
    List<UserCandyRecord> findByUserIdAndCandyIdAndKindAndTimesAndYn(Long userId, Long candyId, Integer kind, Date fromDate, Date toDate, Integer yn);
}
