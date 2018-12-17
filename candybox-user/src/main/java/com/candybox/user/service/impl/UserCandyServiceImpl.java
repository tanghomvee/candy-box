package com.candybox.user.service.impl;

import com.candybox.common.components.RedisComponent;
import com.candybox.common.constants.RedisKey;
import com.candybox.common.enums.OperateKindEnum;
import com.candybox.common.enums.SeparatorEnum;
import com.candybox.common.enums.YNEnum;
import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.common.utils.DateUtils;
import com.candybox.user.dao.UserCandyDao;
import com.candybox.user.dao.model.UserCandy;
import com.candybox.user.dao.model.UserCandyRecord;
import com.candybox.user.service.UserCandyRecordService;
import com.candybox.user.service.UserCandyService;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
@Service("userCandyService")
public class UserCandyServiceImpl extends BaseServiceImpl<UserCandy, Long> implements UserCandyService {

    @Resource
    private UserCandyDao userCandyDao;

    @Resource
    private UserCandyRecordService userCandyRecordService;

    @Resource
    private RedisComponent redisComponent;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int received(Long candyId, Long userId, Long amt) throws Exception{
        String redisLockKey = RedisKey.LOCK_USER_CANDY + SeparatorEnum.UNDERLINE.getVal() +  OperateKindEnum.SIGN.getVal() + SeparatorEnum.UNDERLINE.getVal() +userId + SeparatorEnum.UNDERLINE.getVal() + candyId;

        Long expire = 20L;

        try{
            if (!redisComponent.lock(redisLockKey , expire , 3)){
                LOGGER.error("redis加锁失败：{}" , redisLockKey);
                return -1;
            }
            DateTime end = new DateTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);

            List<UserCandyRecord> userCandyRecords = userCandyRecordService.findByUserIdAndCandyIdAndKindAndTimes(userId , candyId , OperateKindEnum.SIGN.getVal() , DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate(),end.toDate());

            if (!CollectionUtils.isEmpty(userCandyRecords)){
                LOGGER.warn("用户已经领取糖果userId={},candyId={}"  , userId , candyId);
                return 0;
            }

            UserCandy userCandy =  userCandyDao.findByUserIdAndCandyIdAndYn(userId , candyId , YNEnum.YES.getVal());
            if (userCandy == null){
                userCandy = new UserCandy();
                userCandy.setUserId(userId);
                userCandy.setCandyId(candyId);
                userCandy.setAmt(0L);
            }else{
                userCandy.setAmt(userCandy.getAmt() + amt);
            }
            userCandy = userCandyDao.save(userCandy);
            UserCandyRecord  userCandyRecord = new UserCandyRecord();
            userCandyRecord.setUserId(userId);
            userCandyRecord.setCandyId(candyId);
            userCandyRecord.setAmt(amt);
            userCandyRecord.setBeforAmt(userCandy.getAmt());
            userCandyRecord.setAfterAmt(userCandy.getAmt() + amt);
            DateTime operateTime = new DateTime();
            userCandyRecord.setOperateTime(operateTime.toDate());
            userCandyRecord.setKind(OperateKindEnum.SIGN.getVal());
            userCandyRecord.setRemark(OperateKindEnum.SIGN.getDesc());

            userCandyRecord =  userCandyRecordService.save(userCandyRecord);
            String userCandyVal = userId + SeparatorEnum.UNDERLINE.getVal() + candyId;
            String userCandyKey = RedisKey.USER_CANDY + SeparatorEnum.UNDERLINE.getVal() + userCandyVal;

            Integer leftSeconds =  Seconds.secondsBetween(operateTime, end).getSeconds();
            redisComponent.set(userCandyKey,  userCandyVal ,leftSeconds.longValue());

        }catch (Exception ex){
            throw  ex;
        }finally {
            redisComponent.unLock(redisLockKey);
        }

        return 1;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int reward(Long candyId, Long userId, Long amt , OperateKindEnum operateKindEnum) {
        String redisLockKey = RedisKey.LOCK_USER_CANDY + SeparatorEnum.UNDERLINE.getVal() +userId + SeparatorEnum.UNDERLINE.getVal() + candyId;

        Long expire = 20L;

        try{
            if (!redisComponent.lock(redisLockKey , expire , 3)){
                LOGGER.error("redis加锁失败：{}" , redisLockKey);
                return -1;
            }
            UserCandy userCandy =  userCandyDao.findByUserIdAndCandyIdAndYn(userId , candyId , YNEnum.YES.getVal());
            if (userCandy == null){
                userCandy = new UserCandy();
                userCandy.setUserId(userId);
                userCandy.setAmt(0L);
                userCandy.setCandyId(candyId);
            }else{
                userCandy.setAmt(userCandy.getAmt() + amt);
            }
            userCandy = userCandyDao.save(userCandy);
            UserCandyRecord  userCandyRecord = new UserCandyRecord();
            userCandyRecord.setUserId(userId);
            userCandyRecord.setCandyId(candyId);
            userCandyRecord.setAmt(amt);
            userCandyRecord.setBeforAmt(userCandy.getAmt());
            userCandyRecord.setAfterAmt(userCandy.getAmt() + amt);
            DateTime operateTime = new DateTime();
            userCandyRecord.setOperateTime(operateTime.toDate());
            userCandyRecord.setKind(operateKindEnum.getVal());
            userCandyRecord.setRemark(operateKindEnum.getDesc());
            userCandyRecord =  userCandyRecordService.save(userCandyRecord);

        }catch (Exception ex){
            throw  ex;
        }finally {
            redisComponent.unLock(redisLockKey);
        }

        return 1;
    }
}
