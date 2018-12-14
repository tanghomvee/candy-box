package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsBox;
import com.slst.market.web.vo.SmsSendReqVO;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface SmsBoxService extends BaseService <SmsBox, Long>{

    /**
     * 短信发送前，新建并初始化短信箱记录（price;count;acctRecordId为0L）
     * @param curUser
     * @param smsBox
     * @return
     */
    SmsBox createSmsBox(UserVO curUser, SmsBox smsBox);

    /**
     * 更新短信箱记录
     * @param curUser
     * @param smsBox
     * @return
     */
    SmsBox modifySmsBox(UserVO curUser, SmsBox smsBox);

    /**
     * 删除短信箱记录
     * @param ids
     * @return
     */
    Msg deleteSmsBox(String ids);

    /**
     * 根据ID查找SmsBox
     * @param id
     * @return
     */
    SmsBox findSmsBoxById(Long id);

    /**
     * 根据userId分页查找短信箱记录
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsBox> findSmsBoxByUserId(Long userId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据venderId分页查找短信箱记录
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsBox> findSmsBoxByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据storeId分页查找短信箱记录
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsBox> findSmsBoxByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据activityId分页查找短信箱记录
     * @param activityId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsBox> findSmsBoxByActivityId(Long activityId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据venderId和title模糊分页查找短信箱记录
     * @param venderId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsBox> findSmsBoxByVenderIdAndTitle(Long venderId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据storeId和title模糊分页查找短信箱记录
     * @param storeId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsBox> findSmsBoxByStoreIdAndTitle(Long storeId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 商家/店铺发送短信的前期准备工作（冻结账户，初始化smsBox，初始化smsRecord）
     * @param curUser 当前操作者
     * @param targetStoreId 需要发短信的店铺ID。如果是商家活动，没有具体店铺，则为0L。
     * @param activityId 活动ID
     * @param smsTplId 短信模板ID
     * @param customerIds 选择的发送短信的顾客们的ID
     * @return
     */
    Msg prepareToSendSmsByVenderOrStore(UserVO curUser, Long targetStoreId, Long activityId, Long smsTplId, String customerIds);

    /**
     * 商家/店铺发送短信（向第三方发送短信请求，更改短信发送状态记录），完成后会字段调用end方法结算以及更新记录
     * @param curUser
     * @param smsBoxId
     * @return
     */
    Future<Msg> startToSendSmsByVenderOrStore(UserVO curUser, Long targetStoreId, Long smsBoxId);

//    /**
//     * 商家/店铺发送短信完成后的更新操作（解冻账户金额，扣费，更新SmsBox信息）
//     * @param curUser
//     * @param targetStoreId
//     * @param smsBoxId
//     * @return
//     */
//    Msg endOfSendSmsByVenderOrStore(UserVO curUser,Long targetStoreId,Long smsBoxId);

    /**
     * SmsBox短信箱展示状态：Map<String,Object>。Key：title,totalCount,currentSendState,sendTime,currentAmount
     * title(主题,String),totalCount(发送人数,Long),currentSendState(状态描述,String),sendTime(发件时间,Date),currentAmount(总计价格,BigDecimal)
     * @param venderId
     * @param smsBoxId
     * @return
     */
    Map<String,Object> showSendResult(Long venderId, Long smsBoxId);

    /**
     * 生成发件箱
     * @param activityId
     * @return
     */
    Map<String,Object> initSendBox(Long activityId,String title,int pageNum,int pageSize,String sortKey,String order);

    /**
     * 确定发送短信
     * @param smsSendReqVO 发送短信请求参数
     * @param userVO 登陆信息
     * @return
     */
    Msg sendSms(SmsSendReqVO smsSendReqVO, UserVO userVO);

    /**
     * 费用结算
     * @param curUser 登陆用户
     * @param targetStoreId 门店Id
     * @param smsBoxId 发件箱id
     * @return
     */
    Msg endOfSendSmsByVenderOrStore(UserVO curUser, Long targetStoreId, Long smsBoxId);

}
