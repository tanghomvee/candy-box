package com.candybox.user.service.impl;

import com.candybox.common.enums.YNEnum;
import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.user.dao.UserCandyRecordDao;
import com.candybox.user.dao.model.UserCandyRecord;
import com.candybox.user.service.UserCandyRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:15
 */
@Service("userCandyRecordService")
public class UserCandyRecordServiceImpl extends BaseServiceImpl<UserCandyRecord, Long> implements UserCandyRecordService {

    @Resource
    private UserCandyRecordDao userCandyRecordDao;


    @Override
    public List<UserCandyRecord> findByUserIdAndCandyIdAndKindAndTimes(Long userId, Long candyId, Integer kind, Date fromDate, Date toDate) {
        return userCandyRecordDao.findByUserIdAndCandyIdAndKindAndTimesAndYn( userId,  candyId,  kind,  fromDate, toDate , YNEnum.YES.getVal()) ;
    }

    @Override
    public UserCandyRecord save(UserCandyRecord userCandyRecord) {
        return userCandyRecordDao.save(userCandyRecord);
    }
}
