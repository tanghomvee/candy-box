package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsRecord;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface SmsRecordService extends BaseService <SmsRecord, Long>{


    /**
     * 发送给短信。返回Map<String,Object>：state - 发送状态（Integer），errmsg - 状态描述（String）
     * @param soundToothPhoneId
     * @param soundToothSmsTemplateId
     * @return 返回Map<String,Object>：state - 发送状态（Integer），errmsg - 状态描述（String）
     */
    Map<String, Object> sendSmsToThird(String soundToothPhoneId, String soundToothSmsTemplateId);

    /**
     * 新建并初始化（顾客）短信记录
     * @param userVO
     * @param smsRecord
     * @return
     */
    SmsRecord createSmsRecord(UserVO userVO, SmsRecord smsRecord);

    /**
     * 更新短信记录
     * @param userVO
     * @param smsRecord
     * @return
     */
    SmsRecord modifySmsRecord(UserVO userVO, SmsRecord smsRecord);

    /**
     * 批量删除（顾客）短信记录
     * @param ids
     * @return
     */
    Msg deleteSmsRecordById(String ids);

    /**
     * 根据短信箱ID。批量删除（顾客）短信记录
     * @param smsBoxId
     * @return
     */
    Msg deleteSmsRecordBySmsBoxId(String smsBoxId);

    /**
     * 分页查询指定smsBoxId中，所有的短信记录
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsRecord> findSmsRecordBySmsBoxId(Long smsBoxId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 不分页，查询指定smsBoxId中，所有的短信记录
     * @param smsBoxId
     * @return
     */
    List<SmsRecord> findSmsRecordBySmsBoxId(Long smsBoxId);

    /**
     * 根据SmsBoxId和发送状态分页查看（顾客）短信记录
     * @param smsBoxId
     * @param state
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsRecord> findSmsRecoredBySmsBoxIdAndState(Long smsBoxId, Integer state, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据smsBoxId和state统计条数
     * @param smsBoxId
     * @param state
     * @return
     */
    Long countSmsRecordBySmsBoxIdAndState(Long smsBoxId, Integer state);

    /**
     * 某个smsBoxId下，短信详情（电话，发送状态）展示。按照state降序
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    Page<Object[]> findMobileAndErrmsgByBoxId(Long smsBoxId,int pageNum,int pageSize);

    /**
     * 根据smsBoxId和状态state，查询发送结果，展示mobile和errmsg
     * @param smsBoxId
     * @param state
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    Page<Object[]> findMobileAndErrmsgByBoxIdAndState(Long smsBoxId,Integer state,int pageNum,int pageSize);

    /**
     * 根据smsBoxId和mobile（电话后四位），查询发送结果，展示mobile和errmsg
     * @param smsBoxId
     * @param mobileLike
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    Page<Object[]> findMobileAndErrmsgByBoxIdAndMobile(Long smsBoxId,String mobileLike,int pageNum,int pageSize);

    /**
     * 根据smsBoxId和state（发送状态）和mobile（电话后四位），查询发送结果，展示mobile和errmsg
     * @param smsBoxId
     * @param state
     * @param mobileLike
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    Page<Object[]> findMobileAndErrmsgByBoxIdAndMobileAndState(Long smsBoxId,Integer state,String mobileLike,int pageNum,int pageSize);

    /**
     * 根据smsBoxId和yn统计条数
     * @param smsBoxId 发件箱Id
     * @param yn 是否有效
     * @return
     */
    Long countBySmsBoxIdAndYn(Long smsBoxId, Integer yn);

}
