package com.slst.market.service.impl;

import com.google.common.collect.Maps;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SoundToothService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.customer.service.CustomerService;
import com.slst.market.dao.SmsRecordDao;
import com.slst.market.dao.model.SmsRecord;
import com.slst.market.service.SmsRecordService;
import com.slst.market.service.SmsTplService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-23 14:32
 */
@Service("smsRecordService")
public class SmsRecordServiceImpl extends BaseServiceImpl<SmsRecord, Long> implements SmsRecordService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SmsRecordDao smsRecordDao;

    @Resource
    private SoundToothService soundToothService;

    @Resource
    private CustomerService customerService;

    @Resource
    private SmsTplService smsTplService;

    /**
     * 发送给短信。返回Map<String,Object>：state - 发送状态（Integer），errmsg - 状态描述（String）
     * @param soundToothPhoneId
     * @param soundToothSmsTemplateId
     * @return 返回Map<String,Object>：state - 发送状态（Integer），errmsg - 状态描述（String）
     */
    public Map<String, Object> sendSmsToThird(String soundToothPhoneId,String soundToothSmsTemplateId){
        Map<String,Object> rtnMap = Maps.newHashMap();
        Integer state = null;
        String errmsg = null;
        Msg rtnThirdMsg = soundToothService.sendSMSMsg(soundToothPhoneId,soundToothSmsTemplateId);
        //成功：发送成功 | error | null。失败：查无手机号信息 | error | null等
        if(null == rtnThirdMsg ||  Msg.error().getMsg().equals(rtnThirdMsg.getMsg())){
            state = -1;
            errmsg = "发送短信请求失败。";
        }else if(rtnThirdMsg.getMsg().contains("发送成功")){
            state = 1;
            errmsg = rtnThirdMsg.getMsg();
        }else {
            state = -1;
            errmsg = rtnThirdMsg.getMsg();
        }
        rtnMap.put("state",state);
        rtnMap.put("errmsg",errmsg);
        return rtnMap;
    }

    /**
     * 新建（顾客）短信记录
     *
     * @param userVO
     * @param smsRecord
     * @return
     */
    @Override
    public SmsRecord createSmsRecord(UserVO userVO, SmsRecord smsRecord) {
        String creator = userVO.getUserName();
        Date curDate = new Date();
        Integer yes = YNEnum.YES.getVal();

        //mobile;mac;customerId;thirdPartyId;state;errmsg;smsBoxId
        smsRecord.setState(0);  //初始化状态
        smsRecord.setErrmsg("正在发送中。");
        smsRecord.setYn(yes);
        smsRecord.setCreator(creator);
        smsRecord.setCreateTime(curDate);

        SmsRecord rtnSmsRecord = null;
        try {
            rtnSmsRecord = smsRecordDao.save(smsRecord);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("新建SmsRecord，数据库操作异常。");
            return null;
        }

        return rtnSmsRecord;
    }

    /**
     * 更新短信记录
     * @param userVO
     * @param smsRecord
     * @return
     */
    public SmsRecord modifySmsRecord(UserVO userVO,SmsRecord smsRecord){
        String changer = userVO.getUserName();
        Date curDate = new Date();

        smsRecord.setChanger(changer);
        smsRecord.setChangeTime(curDate);
        SmsRecord rtnSmsRecord = null;
        try {
            rtnSmsRecord = smsRecordDao.saveAndFlush(smsRecord);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("更新短信模板，ID：："+smsRecord.getId()+"，数据库操作异常。"+e);
            return null;
        }
        return rtnSmsRecord;
    }

    /**
     * 批量删除（顾客）短信记录
     *
     * @param ids
     * @return
     */
    @Override
    public Msg deleteSmsRecordById(String ids) {
        if (StringUtils.isNullOrEmpty(ids)){
            return Msg.error("未选择需要删除的短信记录。");
        }
        if(ids.contains(",")){
            String[] idsArr = ids.split(",");
            for (int i = 0; i <idsArr.length ; i++) {
                try {
                    smsRecordDao.deleteById(Long.parseLong(idsArr[i].trim()));
                }catch (Exception e){
                    e.printStackTrace();
                    LOGGER.error("根据ID删除短信记录，ID："+idsArr[i]+"，数据库操作异常。"+e);
                    return Msg.error("删除出错，请重试。");
                }
            }
        }else {

            try {
                smsRecordDao.deleteById(Long.parseLong(ids.trim()));
            }catch (Exception ex){
                ex.printStackTrace();
                LOGGER.error("根据ID删除短信记录，ID："+ids+"，数据库操作异常。"+ex);
                return Msg.error("删除出错，请重试。");
            }
        }
        return Msg.success();
    }

    /**
     * 根据短信箱ID，批量删除（顾客）短信记录
     *
     * @param smsBoxId
     * @return
     */
    @Override
    public Msg deleteSmsRecordBySmsBoxId(String smsBoxId) {

        if (StringUtils.isNullOrEmpty(smsBoxId)){
            return Msg.error("未选择需要删除的短信记录。");
        }
        if(smsBoxId.contains(",")){
            String[] idsArr = smsBoxId.split(",");
            for (int i = 0; i <idsArr.length ; i++) {
                try {
                    smsRecordDao.deleteBySmsBoxId(Long.parseLong(idsArr[i].trim()));
                }catch (Exception e){
                    e.printStackTrace();
                    LOGGER.error("根据SmsBoxID删除短信记录，ID："+idsArr[i]+"，数据库操作异常。"+e);
                    return Msg.error("删除出错，请重试。");
                }
            }
        }else {

            try {
                smsRecordDao.deleteBySmsBoxId(Long.parseLong(smsBoxId.trim()));
            }catch (Exception ex){
                ex.printStackTrace();
                LOGGER.error("根据SmsBoxID删除短信记录，ID："+smsBoxId+"，数据库操作异常。"+ex);
                return Msg.error("删除出错，请重试。");
            }
        }
        return Msg.success();
    }

    /**
     * 分页查询指定smsBoxId中，所有的短信记录
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    public Page<SmsRecord> findSmsRecordBySmsBoxId(Long smsBoxId, int pageNum,int pageSize,String sortKey,String orderKey){
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,sortKey,orderKey);
        Page<SmsRecord> rtnPage = null;
        try {
            rtnPage = smsRecordDao.findBySmsBoxId(smsBoxId,pageable);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找SmsRecord，smsBoxId："+smsBoxId+"，数据库操作异常。"+e);
            return null;
        }
        if(null == rtnPage||rtnPage.getContent().size() == 0){
            return null;
        }
        return rtnPage;
    }

    /**
     * 不分页，查询指定smsBoxId中，所有的短信记录
     * @param smsBoxId
     * @return
     */
    @Override
    public List<SmsRecord> findSmsRecordBySmsBoxId(Long smsBoxId){
        List<SmsRecord> rtnList = null;
        try {
            rtnList = smsRecordDao.findBySmsBoxId(smsBoxId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找SmsRecord，smsBoxId："+smsBoxId+"，数据库操作异常。"+e);
            return null;
        }
         return rtnList;
    }

    /**
     * 根据SmsBoxId和发送状态分页查看（顾客）短信记录
     *
     * @param smsBoxId
     * @param state
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsRecord> findSmsRecoredBySmsBoxIdAndState(Long smsBoxId, Integer state, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,sortKey,orderKey);
        Page<SmsRecord> rtnPage = null;
        try {
            rtnPage = smsRecordDao.findBySmsBoxIdAndState(smsBoxId,state,pageable);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找SmsRecord，smsBoxId："+smsBoxId+"，state："+state+"，数据库操作异常。");
            return null;
        }
        if (null == rtnPage||rtnPage.getContent().size() == 0) return null;

        return rtnPage;
    }

    /**
     * 根据smsBoxId和state统计条数
     * @param smsBoxId
     * @param state
     * @return
     */
    public Long countSmsRecordBySmsBoxIdAndState(Long smsBoxId,Integer state){

        Long count = 0L;
        try {
            count = smsRecordDao.countBySmsBoxIdAndState(smsBoxId,state);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("统计短信记录，smsBoxId："+smsBoxId+"，state"+state+"，数据库操作异常。"+e);
            return null;
        }
        return count;
    }

    /**
     * 某个smsBoxId下，短信详情（电话，发送状态）展示。按照state降序
     * @param smsBoxId
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    @Override
    public Page<Object[]> findMobileAndErrmsgByBoxId(Long smsBoxId,int pageNum,int pageSize){
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,"state",null);
        Page<Object[]> rtnSmsRecord = null;
        try {
            rtnSmsRecord = smsRecordDao.findMobileAndErrmsgByBoxId(smsBoxId,pageable);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("本地查询短信发送记录出错，smsBoxId：" + smsBoxId);
            return null;
        }

        return rtnSmsRecord;
    }

    /**
     * 根据smsBoxId和状态state，查询发送结果，展示mobile和errmsg
     * @param smsBoxId
     * @param state
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    @Override
    public Page<Object[]> findMobileAndErrmsgByBoxIdAndState(Long smsBoxId,Integer state,int pageNum,int pageSize){
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,"createTime",null);

        Page<Object[]> rtnSmsRecord = null;
        try {
            rtnSmsRecord = smsRecordDao.findMobileAndErrmsgByBoxIdAndState(smsBoxId,state,pageable);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据state，本地查询短信发送记录出错，smsBoxId：" + smsBoxId);
            return null;
        }

        return rtnSmsRecord;
    }


    /**
     * 根据smsBoxId和mobile（电话后四位），查询发送结果，展示mobile和errmsg
     * @param smsBoxId
     * @param mobileLike
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    @Override
    public Page<Object[]> findMobileAndErrmsgByBoxIdAndMobile(Long smsBoxId,String mobileLike,int pageNum,int pageSize){
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,"createTime",null);

        Page<Object[]> rtnSmsRecord = null;
        try {
            rtnSmsRecord = smsRecordDao.findMobileAndErrmsgByBoxIdAndMobile(smsBoxId,mobileLike,pageable);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据mobile，本地查询短信发送记录出错，smsBoxId：" + smsBoxId);
            return null;
        }

        return rtnSmsRecord;
    }

    /**
     * 根据smsBoxId和state（发送状态）和mobile（电话后四位），查询发送结果，展示mobile和errmsg
     * @param smsBoxId
     * @param state
     * @param mobileLike
     * @param pageNum
     * @param pageSize
     * @return Page<Object[]>:[0]:mobile;[1]:errmsg
     */
    @Override
    public Page<Object[]> findMobileAndErrmsgByBoxIdAndMobileAndState(Long smsBoxId,Integer state,String mobileLike,int pageNum,int pageSize){
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,"createTime",null);

        Page<Object[]> rtnSmsRecord = null;
        try {
            rtnSmsRecord = smsRecordDao.findMobileAndErrmsgByBoxIdAndMobileAndState(smsBoxId,state,mobileLike,pageable);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据state和mobile，本地查询短信发送记录出错，smsBoxId：" + smsBoxId);
            return null;
        }

        return rtnSmsRecord;
    }

    @Override
    public Long countBySmsBoxIdAndYn(Long smsBoxId, Integer yn) {
        return smsRecordDao.countBySmsBoxIdAndYn(smsBoxId, yn);
    }
}
