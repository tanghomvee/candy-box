package com.candybox.user.service;

import com.candybox.common.service.BaseService;
import com.candybox.user.dao.model.UserCandy;
import com.candybox.user.dao.model.UserCandyRecord;

import java.util.Date;
import java.util.List;


public interface UserCandyRecordService extends BaseService<UserCandyRecord, Long> {

    /**
     * find
     * @param userId
     * @param candyId
     * @param kind
     * @param fromDate
     * @param toDate
     * @return
     */
    List<UserCandyRecord> findByUserIdAndCandyIdAndKindAndTimes(Long userId, Long candyId, Integer kind, Date fromDate, Date toDate);

    /**
     * add
     * @param userCandyRecord
     * @return
     */
    UserCandyRecord save(UserCandyRecord userCandyRecord);
}
