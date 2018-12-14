package com.slst.market.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;
import com.slst.acct.dao.model.Account;
import com.slst.acct.dao.model.AccountRecord;
import com.slst.acct.service.AccountRecordService;
import com.slst.acct.service.AccountService;
import com.slst.agent.dao.model.Agent;
import com.slst.agent.service.AgentService;
import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.enums.AgentLevelEnum;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.enums.TransTypeBEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.mq.producer.DefaultProducerService;
import com.slst.common.service.SysCfgService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.BeanMapUtil;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.market.dao.ActivityDao;
import com.slst.market.dao.SmsBoxDao;
import com.slst.market.dao.model.*;
import com.slst.market.service.SmsBoxService;
import com.slst.market.service.SmsFeeService;
import com.slst.market.service.SmsRecordService;
import com.slst.market.service.SmsTplService;
import com.slst.market.web.vo.SmsSendReqVO;
import com.slst.vender.dao.model.Store;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.service.StoreService;
import com.slst.vender.service.VenderEmpService;
import com.slst.vender.service.VenderService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-23 14:30
 */
@Service("smsBoxService")
public class SmsBoxServiceImpl extends BaseServiceImpl<SmsBox, Long> implements SmsBoxService {
    @Resource
    private SysCfgService sysCfgService;
    @Resource
    private SmsBoxDao smsBoxDao;
    @Resource
    private SmsTplService smsTplService;
    @Resource
    private SmsFeeService smsFeeService;
    @Resource
    private AccountService accountService;
    @Resource
    private AccountRecordService accountRecordService;
    @Resource
    private AgentService agentService;
    @Resource
    private VenderService venderService;
    @Resource
    private VenderEmpService venderEmpService;
//    @Resource
//    private ActivityService activityService;
    @Resource
    private ActivityDao activityDao;
    @Resource
    private StoreService storeService;
    @Resource
    private CustomerService customerService;
    @Resource
    private SmsRecordService smsRecordService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private DefaultProducerService defaultProducerService;
    @Value(value = "${mq.send.producer.topic.SMS}")
    private String topic;

    /**
     * 短信发送前，新建并初始化短信箱记录（price;count;acctRecordId为0L）
     *
     * @param curUser
     * @param smsBox
     * @return
     */
    @Override
    public SmsBox createSmsBox(UserVO curUser, SmsBox smsBox) {

        String creator = curUser.getUserName();
        Date curDate = new Date();
        Integer yes = YNEnum.YES.getVal();

        smsBox.setCreator(creator);
        smsBox.setCreateTime(curDate);
        smsBox.setYn(yes);
        //price;count为0
        smsBox.setPrice(0L);
        smsBox.setCount(0L);

        SmsBox rtnSmsBox = null;
        try {
            rtnSmsBox = smsBoxDao.save(smsBox);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("新建SmsBox，数据库操作异常。");
            return null;
        }

        return rtnSmsBox;
    }

    /**
     * 更新短信箱记录
     *
     * @param curUser
     * @param smsBox
     * @return
     */
    @Override
    public SmsBox modifySmsBox(UserVO curUser, SmsBox smsBox) {

        String changer = curUser.getUserName();
        Date curDate = new Date();  //当前时间
        Long smsBoxId = smsBox.getId();

        SmsBox findSmsBox = null;
        try {
            findSmsBox = smsBoxDao.findById(smsBoxId).get();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据ID：" + smsBoxId + "查找SmsBox，数据库操作异常。" + e);
            return null;
        }

        findSmsBox.setChanger(changer);
        findSmsBox.setChangeTime(curDate);
        //更新最终结果：price;count;acctRecordId
        findSmsBox.setPrice(smsBox.getPrice());
        findSmsBox.setCount(smsBox.getCount());
//        findSmsBox.setAcctRecordId(smsBox.getAcctRecordId());
        SmsBox rtnSmsBox = null;
        try {
            rtnSmsBox = smsBoxDao.saveAndFlush(findSmsBox);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("更新SmsBox，ID：" + smsBoxId + "，数据库操作异常。" + ex);
            return null;
        }

        return rtnSmsBox;
    }

    /**
     * 删除短信箱记录
     *
     * @param ids
     * @return
     */
    @Override
    public Msg deleteSmsBox(String ids) {

        if (StringUtils.isNullOrEmpty(ids)) {
            return Msg.error("未选择要删除的短信箱记录。");
        }

        if (ids.contains(",")) {

            String[] idsArr = ids.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                try {
                    smsBoxDao.deleteById(Long.parseLong(idsArr[i].trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("删除SmsBox，ID：" + idsArr[i] + "，数据库操作异常。" + e);
                    return Msg.error("删除短信箱记录失败，请重试。");
                }
            }
        } else {

            try {
                smsBoxDao.deleteById(Long.parseLong(ids.trim()));
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("删除SmsBox，ID：" + ids + "，数据库操作异常。" + ex);
                return Msg.error("删除短信箱记录失败，请重试。");
            }
        }

        return Msg.success();
    }

    /**
     * 根据ID查找SmsBox
     *
     * @param id
     * @return
     */
    @Override
    public SmsBox findSmsBoxById(Long id) {
        Optional<SmsBox> findSmsBox = smsBoxDao.findById(id);
        if(!findSmsBox.isPresent()){
            return null;
        }
        return findSmsBox.get();
    }

    /**
     * 根据userId分页查找短信箱记录
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsBox> findSmsBoxByUserId(Long userId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<SmsBox> smsBoxPage = null;
        try {
            smsBoxPage = smsBoxDao.findByUserId(userId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据userId：" + userId + "查找SmsBox，数据库操作异常。" + e);
            return null;
        }
        if (null == smsBoxPage || smsBoxPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return smsBoxPage;
    }

    /**
     * 根据venderId分页查找短信箱记录
     *
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsBox> findSmsBoxByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<SmsBox> smsBoxPage = null;
        try {
            smsBoxPage = smsBoxDao.findByVenderId(venderId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据venderId：" + venderId + "查找SmsBox，数据库操作异常。" + e);
            return null;
        }
        if (null == smsBoxPage || smsBoxPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return smsBoxPage;
    }

    /**
     * 根据storeId分页查找短信箱记录
     *
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey  排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsBox> findSmsBoxByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<SmsBox> smsBoxPage = null;
        try {
            smsBoxPage = smsBoxDao.findByStoreId(storeId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据storeId：" + storeId + "查找SmsBox，数据库操作异常。" + e);
            return null;
        }
        if (null == smsBoxPage || smsBoxPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return smsBoxPage;
    }

    /**
     * 根据activityId分页查找短信箱记录
     *
     * @param activityId
     * @param pageNum
     * @param pageSize
     * @param sortKey    排序关键字。默认createTime
     * @param orderKey   升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsBox> findSmsBoxByActivityId(Long activityId, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<SmsBox> smsBoxPage = null;
        try {
            smsBoxPage = smsBoxDao.findByActivityId(activityId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据activityId：" + activityId + "查找SmsBox，数据库操作异常。" + e);
            return null;
        }
        if (null == smsBoxPage || smsBoxPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return smsBoxPage;
    }

    /**
     * 根据venderId和title模糊分页查找短信箱记录
     *
     * @param venderId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey   排序关键字。默认createTime
     * @param orderKey  升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsBox> findSmsBoxByVenderIdAndTitle(Long venderId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<SmsBox> smsBoxPage = null;
        try {
            smsBoxPage = smsBoxDao.findByVenderIdAndTitleContaining(venderId, titleLike, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据venderId：" + venderId + "查找名称包含：" + titleLike + "的SmsBox，数据库操作异常。" + e);
            return null;
        }
        if (null == smsBoxPage || smsBoxPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return smsBoxPage;
    }

    /**
     * 根据storeId和title模糊分页查找短信箱记录
     *
     * @param storeId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey   排序关键字。默认createTime
     * @param orderKey  升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    @Override
    public Page<SmsBox> findSmsBoxByStoreIdAndTitle(Long storeId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<SmsBox> smsBoxPage = null;
        try {
            smsBoxPage = smsBoxDao.findByStoreIdAndTitleContaining(storeId, titleLike, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据storeId：" + storeId + "查找名称包含：" + titleLike + "的SmsBox，数据库操作异常。" + e);
            return null;
        }
        if (null == smsBoxPage || smsBoxPage.getContent().size() == 0) {  //如果没有匹配的内容
            return null;
        }

        return smsBoxPage;
    }

    /**
     * 商家/店铺发送短信的前期准备工作（冻结账户，初始化smsBox，初始化smsRecord）
     *
     * @param curUser       当前操作者
     * @param targetStoreId 需要发短信的店铺ID。如果是商家活动，没有具体店铺，则为0L。
     * @param activityId    活动ID
     * @param smsTplId      短信模板ID
     * @param customerIds   选择的发送短信的顾客们的ID
     * @return
     */
    @Transactional
    @Override
    public Msg prepareToSendSmsByVenderOrStore(UserVO curUser, Long targetStoreId, Long activityId, Long smsTplId, String customerIds) {
//        Long realSmsCount = 0L;  //发送成功的条数
        Date curDate = new Date();  //当前时间
        Long curVenderId = curUser.getVenderId();  //商家ID
        Long smsCount = null;//选择发送的总条数

        if (StringUtils.isNullOrEmpty(customerIds)) { //根据customerIds取得选择发送的总条数
            LOGGER.error("未选择需要发送信息的顾客。");
            return Msg.error("未选择需要发送信息的顾客。");
        }
        if (customerIds.contains(",")) {
            String[] custIdArr = customerIds.split(",");
            smsCount = (long) custIdArr.length;
        } else {
            smsCount = 1L;
        }

        SmsFee smsFeeObject = smsFeeService.findSmsFeeByVenderId(curVenderId);    //查找商家对应的费率信息
        if (null == smsFeeObject || smsFeeObject.getFee() == 0) {
            LOGGER.error("查找商家短信费率出错，venderId：" + curVenderId);
            return Msg.error("系统开了个小差...");
        }

        String stringFee = String.valueOf(smsFeeObject.getFee());   //转为String传入构造，保证BigDecimal的准确性
        String totalCount = String.valueOf(smsCount); //转为String传入构造，保证BigDecimal的准确性
        BigDecimal totalSmsFee = new BigDecimal(stringFee).multiply(new BigDecimal("0.001")).multiply(new BigDecimal(totalCount));//预估总金额，用于冻结。数据库中费用单位为：厘

        //1.冻结账户对应金额，新建账户记录，并查找对应短信模板信息
        Integer userType = curUser.getUserType();//当前用户类型
        Long rtnFrozenAcctRecord = null;// 冻结账户返回的recordId，后面更新SmsBox使用
        Integer rtnFrozenAcctCount = null;   //冻结账户：modify操作返回的条数，后面判断冻结操作是否成功使用

        if (null == curUser.getStoreId() || curUser.getStoreId() <= 0L) {   //商家操作或商家员工操作
            Vender vender = venderService.findVenderById(curVenderId);

            if (null == vender) {
                return Msg.error("查询商家信息失败");
            }
            Long venderUserId = vender.getUserId();
            Map<String, Object> rtnFrozenAcctMap = accountService.frozenVenderAccount(curUser, venderUserId, totalSmsFee, TransTypeBEnum.SMS);//冻结账户对应金额
            rtnFrozenAcctCount = (Integer) rtnFrozenAcctMap.get("updateCount");
            rtnFrozenAcctRecord = (Long) rtnFrozenAcctMap.get("acctRecordId");

            if (null == rtnFrozenAcctCount || rtnFrozenAcctCount == 0) {
                LOGGER.error("冻结商家资金出错，venderId：" + curVenderId);
                return Msg.error("系统开了个小差...");
            }
            if (rtnFrozenAcctCount == -1) {
                LOGGER.error("商家余额不足，冻结商家资金出错，venderId：" + curVenderId);
                return Msg.error("商家账户余额不足。");
            }

        } else if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {//门店操作
            Map<String, Object> rtnFrozenAcctMap = accountService.frozenStoreAccount(curUser, targetStoreId, totalSmsFee, TransTypeBEnum.SMS);//冻结账户对应金额
            rtnFrozenAcctCount = (Integer) rtnFrozenAcctMap.get("updateCount");
            rtnFrozenAcctRecord = (Long) rtnFrozenAcctMap.get("acctRecordId");

            if (null == rtnFrozenAcctCount || rtnFrozenAcctCount == 0) {
                LOGGER.error("冻结店铺资金出错，storeId：" + targetStoreId);
                return Msg.error("系统开了个小差...");
            }
            if (rtnFrozenAcctCount == -1) {
                LOGGER.error("店铺余额不足，冻结店铺资金出错，storeId：" + targetStoreId);
                return Msg.error("店铺余额不足。");
            }

        }
//        else {//非商家/商家员工，无权操作
//            LOGGER.error("当前用户非商家或商家员工。无权操作");
//            return Msg.error("当前用户没有发送短信的权限。");
//
//        }

        //查找对应短信模板信息
        SmsTpl rtnSmsTpl = smsTplService.findSmsTplById(smsTplId);//查找返回的短信模板

        if (null == rtnSmsTpl) {  //查找短信模板
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("查找SmsTpl出错。");
            return Msg.error("系统开了个小差...");
        }
        if (1 != rtnSmsTpl.getState()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("选择的短信模板未通过。smsTplId：" + rtnSmsTpl.getId());
            return Msg.error("选择的短信模板未通过。");
        }

        //2.初始化短信箱
        SmsBox initSmsBox = new SmsBox();   //初始化短信箱使用
        Store rtnStore = null;

        if (null == targetStoreId || targetStoreId == 0) {//如果商家直接发送短信
            targetStoreId = 0L;
            initSmsBox.setStoreName("无");

        } else {
            rtnStore = storeService.findStoreById(targetStoreId);  //查找返回的店铺信息
            if (null == rtnStore) {  //查找店铺信息出错
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                LOGGER.error("查找Store出错，storeId={}", targetStoreId);
                return Msg.error("系统开了个小差...");
            }

            initSmsBox.setStoreName(rtnStore.getStoreName());
        }
        initSmsBox.setStoreId(targetStoreId);
        initSmsBox.setTitle(rtnSmsTpl.getTitle());
        initSmsBox.setContent(rtnSmsTpl.getContent());
        initSmsBox.setThirdPartyId(rtnSmsTpl.getThirdPartyId());
        initSmsBox.setSmsTmpId(rtnSmsTpl.getId());
        initSmsBox.setSendTime(curDate);
        initSmsBox.setActivityId(activityId);
        initSmsBox.setAcctRecordId(rtnFrozenAcctRecord);
        initSmsBox.setUserId(curUser.getId());
        initSmsBox.setVenderId(curVenderId);
        initSmsBox.setVenderName(curUser.getVenderName());

        SmsBox rtnInitSmsBox = createSmsBox(curUser, initSmsBox); //初始化smsBox并存入数据库

        if (null == rtnInitSmsBox) {  //初始化SmsBox出错
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("查找初始化SmsBox出错");
            return Msg.error("系统开了个小差...");
        }

        //3.查找顾客信息
        List<Customer> customerList = customerService.findById(customerIds);

        if (null == customerList || 0 == customerList.size()) {  //查找顾客信息出错
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("查找Customer信息出错。");
            return Msg.error("系统开了个小差...");
        }

        //4.初始化短信记录
        for (int i = 0; i < customerList.size(); i++) {
            SmsRecord smsRecord = new SmsRecord();
            SmsRecord rtnSmsRecord = null;
            String custMobile = customerList.get(i).getMobile();
            String custMac = customerList.get(i).getMac();
            String custThirdPartyId = customerList.get(i).getThirdPartyId();

            if (null == custMobile) custMobile = "未知";
            if (null == custMac) custMac = "未知";
            if (null == custThirdPartyId) custThirdPartyId = "未知";

            smsRecord.setMobile(custMobile);
            smsRecord.setMac(custMac);
            smsRecord.setThirdPartyId(custThirdPartyId);
            smsRecord.setCustomerId(customerList.get(i).getId());
            smsRecord.setSmsBoxId(rtnInitSmsBox.getId());
            rtnSmsRecord = smsRecordService.createSmsRecord(curUser, smsRecord);

            if (null == rtnSmsRecord) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                LOGGER.error("初始化短信记录出错，customerId：" + customerList.get(i).getId());
//                return Msg.error("系统开了个小差...");
            }
        }

        return Msg.success("开始发送，预估价格：" + totalSmsFee + "。", rtnInitSmsBox);
    }


    /**
     * 商家/店铺发送短信（向第三方发送短信请求，更改短信发送状态记录）
     *
     * @param curUser
     * @param smsBoxId
     * @return
     */
    @Transactional
    @Override
    @Async
    public Future<Msg> startToSendSmsByVenderOrStore(UserVO curUser, Long targetStoreId, Long smsBoxId) {   //具体的返回结果类型为AsyncResult
        //1、异步方法和调用类不要在同一个类中
        //2、注解扫描时，要注意过滤，避免重复实例化，因为存在覆盖问题，@Async就失效了
        // -> 2、的解决办法：
        //1.删除beanDefines.xml的<context:component-scan base-package="com" />和<task:annotation-driven/>；
        //2.springMVC.xml添加配置<task:annotation-driven/>。
        //@Autowired


        //查找SmsRecord信息，用于发送短信时，ThirdPartyId()的取得
        List<SmsRecord> smsRecordList = smsRecordService.findSmsRecordBySmsBoxId(smsBoxId);
        SmsBox rtnSmsBox = findSmsBoxById(smsBoxId);

        if (null == smsRecordList || 0 == smsRecordList.size() || null == rtnSmsBox) {  //查找smsBox/smsRecord信息出错
            LOGGER.error("查找smsBox/smsRecord信息出错。");
            return new AsyncResult<>(Msg.error("系统开了个小差..."));
        }

//        5.发送短信以及更改短信记录
        Integer realSmsCount = 0;//发送成功的条数

        for (int i = 0; i < smsRecordList.size(); i++) {
            SmsRecord modifySmsRecord = new SmsRecord();
            SmsRecord rtnModifySmsRecord = null;
            Map<String, Object> sendSmsAutho = smsRecordService.sendSmsToThird(smsRecordList.get(i).getThirdPartyId(), String.valueOf(rtnSmsBox.getThirdPartyId()));

            //模拟短信通过第三方发送。测试用
//            Map<String,Object> sendSmsAutho = Maps.newHashMap();
//            sendSmsAutho.put("state",1);
//            sendSmsAutho.put("errmsg","发送成功");

            Integer state = (Integer) sendSmsAutho.get("state");    //发送短信后，第三方返回的状态
            String errmsg = (String) sendSmsAutho.get("errmsg");    //发送短信后，第三方返回的信息

            SmsRecord tempSmsRecord = smsRecordList.get(i);
            modifySmsRecord.setId(tempSmsRecord.getId());
            modifySmsRecord.setMobile(tempSmsRecord.getMobile());
            modifySmsRecord.setMac(tempSmsRecord.getMac());
            modifySmsRecord.setCustomerId(tempSmsRecord.getCustomerId());
            modifySmsRecord.setThirdPartyId(tempSmsRecord.getThirdPartyId());
            modifySmsRecord.setState(state);
            modifySmsRecord.setErrmsg(errmsg);
            modifySmsRecord.setSmsBoxId(rtnSmsBox.getId());
            modifySmsRecord.setYn(tempSmsRecord.getYn());
            modifySmsRecord.setCreator(tempSmsRecord.getCreator());
            modifySmsRecord.setCreateTime(tempSmsRecord.getCreateTime());
            rtnModifySmsRecord = smsRecordService.modifySmsRecord(curUser, modifySmsRecord);
            if (null == rtnModifySmsRecord) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                LOGGER.error("更新SmsRecord出错。ID：" + tempSmsRecord.getId());
                //为防止第三方已发送出去，但是记录修改没有成功导致的扣费失败，当出现一条修改记录异常就停止发送
//                return new AsyncResult<>(Msg.error("系统开了个小差..."));
                break;
                //如果让它继续发送，则发送成功条数为“发送成功”+“正在发送”的条数总和

            }
            if (state.equals(1)) {
                realSmsCount++; //记录发送成功条数
            }
        }


        Msg endMsg = endOfSendSmsByVenderOrStore(curUser, targetStoreId, smsBoxId);
        if (!(Msg.success().getMsg().equals(endMsg.getMsg()))) {
            LOGGER.error("短信发送完成，但endOfSendSmsByVenderOrStore()方法出错！");
            return new AsyncResult<>(Msg.error("系统开了个小差..."));
        }
        return new AsyncResult<>(Msg.success("发送结束。发送成功：" + realSmsCount + "条"));// .get()取得Msg
    }


    /**
     * 商家/店铺发送短信完成后的更新操作（解冻账户金额，商家/门店扣费，更新SmsBox信息，admin消费）
     * --在startToSendSmsByVenderOrStore()方法完成后会自动调用
     *
     * @param curUser
     * @param targetStoreId
     * @param smsBoxId
     * @return
     */
    @Transactional
    @Override
    public Msg endOfSendSmsByVenderOrStore(UserVO curUser, Long targetStoreId, Long smsBoxId) {

        //6.计算实际花费的费用
        Long curVenderId = curUser.getVenderId();
        Vender vender = venderService.findVenderById(curVenderId);

        if (null == vender) {
            return Msg.error("查询商家信息失败");
        }
        Long venderUserId = vender.getUserId();

        SmsFee smsFeeObject = smsFeeService.findSmsFeeByVenderId(curVenderId);    //查找商家对应的费率信息
        if (null == smsFeeObject || smsFeeObject.getFee() == 0) {
            LOGGER.error("查找商家短信费率出错，venderId：" + curVenderId);
            return Msg.error("系统开了个小差...");
        }

        Long recordSuccess = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, 1);//记录中发送成功的
        Long recordPending = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, 0);//记录中正在发送的
        if (null == recordSuccess || null == recordPending) {
            LOGGER.error("统计发送成功条数出错：" + curVenderId);
            return Msg.error("系统开了个小差...");
        }
        //计算商家真实消费
        Long realSmsCount = recordSuccess + recordPending;
        String stringFee = String.valueOf(smsFeeObject.getFee());   //转为String传入构造，保证BigDecimal的准确性
        BigDecimal realSmsFee = new BigDecimal(stringFee).multiply(new BigDecimal("0.001")).multiply(new BigDecimal(String.valueOf(realSmsCount)));//实际发送价格

        //7.解冻账户金额，新建解冻记录
        //根据smsBoxId查找accountRecord，取得被冻结金额，用于解冻
        SmsBox rtnSmsBox = findSmsBoxById(smsBoxId);
        if (null == rtnSmsBox) {
            LOGGER.error("查找SmsBox出错，ID：：" + smsBoxId);
            return Msg.error("系统开了个小差...");
        }
        AccountRecord rtnAccountRecord = accountRecordService.findAcctRecordById(rtnSmsBox.getAcctRecordId());
        if (null == rtnAccountRecord) {
            LOGGER.error("查找AccountRecord出错，ID：：" + rtnSmsBox.getAcctRecordId());
            return Msg.error("系统开了个小差...");
        }
        BigDecimal frozenAmount = new BigDecimal(String.valueOf(rtnAccountRecord.getAmount())).multiply(new BigDecimal("0.001"));//需要解冻的金额

        Integer rtnThawCount = null;//更新条数
        Long rtnThawAcctRecordId = null;//解冻的账户记录
        Map<String, Object> rtnMap = null;//rtnMap中包含rtnThawCount和rtnThawAcctRecordId
        if (null == curUser.getStoreId() || curUser.getStoreId() <= 0L) {   // 商家操作或商家员工操作
            rtnMap = accountService.thawVenderAccount(curUser, venderUserId, frozenAmount, TransTypeBEnum.SMS);
        } else if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {//门店操作
            rtnMap = accountService.thawStoreAccount(curUser, targetStoreId, frozenAmount, TransTypeBEnum.SMS);
        }

        if (null == rtnMap) {
            LOGGER.error("解冻账户出错。");
            return Msg.error("系统开了个小差...");
        }

        rtnThawCount = (Integer) rtnMap.get("updateCount");
        rtnThawAcctRecordId = (Long) rtnMap.get("acctRecordId");
        if (null == rtnThawCount || 0 == rtnThawCount) {
            LOGGER.error("解冻账户出错。");
            return Msg.error("系统开了个小差...");
        }

        //8.vender/store扣费，更新账户，新建记录
        Integer rtnReduce = null;
        if (null == curUser.getStoreId() || curUser.getStoreId() <= 0L) {   //商家操作或商家员工
            rtnReduce = accountService.smsCostByVender(curUser, venderUserId, realSmsFee);
        } else if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {//门店操作
            rtnReduce = accountService.smsCostByStore(curUser, targetStoreId, realSmsFee);
        }

        if (null == rtnReduce || 0 == rtnReduce || -1 == rtnReduce) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("扣费，更新账户出错。");
            return Msg.error("系统开了个小差...");
        }

        //9.修改SmsBox
        //初始化时：price;count为0
        Long realPrice = new BigDecimal(String.valueOf(realSmsFee)).multiply(new BigDecimal("1000")).longValue();
        Integer modifyBoxCount = modifyBoxOnPCA(curUser, smsBoxId, realPrice, realSmsCount, rtnThawAcctRecordId);

        if (null == modifyBoxCount || modifyBoxCount == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("更新SmsBox出错，ID：" + smsBoxId);
            return Msg.error("系统开了个小差...");
        }

        //查找资金源头账户（商家/门店），后面修改admin和agent账户创建记录时使用
        Account tradeAcct = null;//资金来源账户
        Long directAgentId = null;  //直接代理商的agentId
        if (null == curUser.getStoreId() || curUser.getStoreId() <= 0L) {   //商家操作或商家员工操作
            directAgentId = vender.getAgentId();
            tradeAcct = accountService.findAcctByUserId(venderUserId);

        } else if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {//门店操作
            directAgentId = venderService.findVenderById(curVenderId).getAgentId();
            tradeAcct = accountService.findAcctByStoreId(targetStoreId);

        }
        if (null == tradeAcct) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("查找源头账户出错。");
            return Msg.error("系统开了个小差...");
        }
        Long tradeAcctId = tradeAcct.getId(); //资金来源账户ID

        //10.admin扣费，更新账户，新建记录
        String stringAdminFee = sysCfgService.findByCode(SysCfgEnum.SYS_SOUND_TOOTH_SMS_FEE.getVal()).getCodeVal();//admin短信成本
        //admin总成本
        BigDecimal adminAmount = new BigDecimal(stringAdminFee).multiply(new BigDecimal("0.001")).multiply(new BigDecimal(String.valueOf(realSmsCount)));
        Integer rtnAdminCost = accountService.costOfAdmin(curUser, adminAmount, TransTypeBEnum.SMS);
        if (null == rtnAdminCost || 0 == rtnAdminCost) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("admin扣费出错");
            return Msg.error("系统开了个小差...");
        }

        //11.代理商提成
        //查找相关的代理商（顶级，一级，二级...）
        List<Agent> agentList = new ArrayList<>();
        Long theAgentId = directAgentId;
//        int agentCount = 0; //记录agent数量，用于防止死循环
        do {
            Agent findAgent = agentService.findAgentById(theAgentId);//代理商的agent信息
            if (null == findAgent) {
                LOGGER.error("查找相关代理商信息出错。");
                return Msg.error("系统开了个小差...");
            }

            agentList.add(findAgent);
            theAgentId = findAgent.getParentId();//上一级代理
//            agentCount++;
        } while (0L != theAgentId);


        for (int i = 0; i < agentList.size(); i++) {    //提成，依次更改代理商账户信息
            Long agentUserId = agentList.get(i).getUserId();
            Integer earningsRate = agentList.get(i).getEarningsRate();//eg:30
            BigDecimal earning = realSmsFee.multiply(new BigDecimal(String.valueOf(earningsRate))).multiply(new BigDecimal("0.01"));
            Integer rtnIncomeOfAgent = null;
            if (AgentLevelEnum.TOP_LEVEL.getVal().equals(agentList.get(i).getLevel())) { //顶级代理
                rtnIncomeOfAgent = accountService.smsIncomeofTopAgent(curUser, agentUserId, tradeAcctId, earning);

            } else {//非顶级代理
                rtnIncomeOfAgent = accountService.smsIncomeofChildAgent(curUser, agentUserId, tradeAcctId, earning);

            }
            if (null == rtnIncomeOfAgent || 0 == rtnIncomeOfAgent) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                LOGGER.error("代理商提成出错。agentUserId：" + agentUserId);
                return Msg.error("系统开了个小差...");
            }
        }
        return Msg.success();
    }

    /**
     * SmsBox短信箱展示状态：Map<String,Object>。Key：title,totalCount,currentSendState,sendTime,currentAmount
     * title(主题,String),totalCount(发送人数,Long),currentSendState(状态描述,String),sendTime(发件时间,Date),currentAmount(总计价格,BigDecimal)
     *
     * @param venderId
     * @param smsBoxId
     * @return
     */
    @Override
    public Map<String, Object> showSendResult(Long venderId, Long smsBoxId) {

        HashOperations customerHash = redisTemplate.opsForHash();
        Long curVenderId = venderId;
        Map<String, Object> showMap = Maps.newHashMap();

        SmsBox rtnSmsBox = findSmsBoxById(smsBoxId);//查找对应smsBox信息
        if (null == rtnSmsBox) {
            LOGGER.error("查找SmsBox，ID：" + smsBoxId + "，数据库操作异常。");
            return null;
        }
        String title = rtnSmsBox.getTitle();//1.主题
        Date sendTime = rtnSmsBox.getSendTime();//2.发送时间

        Long failedCount = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, -1);//失败条数
//        Long pendingCount = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, 0);//正在发送条数
        Long successCount = smsRecordService.countSmsRecordBySmsBoxIdAndState(smsBoxId, 1);//成功条数


        //根据key查询MQ已发送的数量
        String totalCountStr = redisComponent.get(RedisKey.MQ_TEMP_SEND_SMS_NUM + smsBoxId);
//        Long totalCount = failedCount + pendingCount + successCount;//3.发送总条数
        Long totalCount = StringUtils.isNullOrEmpty(totalCountStr) ? 0 : Long.valueOf(totalCountStr);
        Long pendingCount = totalCount != 0 ? totalCount-successCount-failedCount : totalCount;
        String currentSendState = pendingCount + "人正在发送," + successCount + "人成功," + failedCount + "人失败";//4.状态描述

        SmsFee smsFeeObject = smsFeeService.findSmsFeeByVenderId(curVenderId);    //查找商家对应的费率信息
        if (null == smsFeeObject || smsFeeObject.getFee() == 0) {
            LOGGER.error("查找商家短信费率出错，venderId：" + curVenderId);
            return null;
        }
        String stringFee = String.valueOf(smsFeeObject.getFee());   //转为String传入构造，保证BigDecimal的准确性
        String StringSuccessCount = String.valueOf(successCount); //转为String传入构造，保证BigDecimal的准确性
        //5.当前消费金额
        BigDecimal currentAmount = new BigDecimal(stringFee).multiply(new BigDecimal("0.001")).multiply(new BigDecimal(StringSuccessCount));//预估总金额，用于冻结。数据库中费用单位为：厘
        //主题;发送人数;状态;发件时间;总计价格
        //title,totalCount,currentSendState,sendTime,currentAmount
        showMap.put("id", smsBoxId);
        showMap.put("venderId", curVenderId);
        showMap.put("title", title);
        showMap.put("totalCount", totalCount);
        showMap.put("currentSendState", currentSendState);
        showMap.put("sendTime", sendTime);
        showMap.put("currentAmount", currentAmount);

        return showMap;

    }

    @Override
    public Map<String, Object> initSendBox(Long activityId, String title, int pageNum, int pageSize, String sortKey, String order) {
        //定义活动对象
        Activity activity = null;

        sortKey = "sendTime";
        order = "desc";
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);
        Page<SmsBox> smsBoxes = null;
        if (StringUtils.isNullOrEmpty(title)) {
            smsBoxes = smsBoxDao.findByActivityId(activityId, pageable);
        } else {
            smsBoxes = smsBoxDao.findByActivityIdAndTitleContaining(activityId, title, pageable);
        }

        List<SmsBox> list = smsBoxes.getContent();
        List sendBoxes = new ArrayList();
        if (list.size() > 0) {
            Optional<Activity> findActivity = activityDao.findById(activityId);
            if(findActivity == null || !findActivity.isPresent()){
                return null;
            }else {
                activity = findActivity.get();
            }
            Long venderId = activity.getVenderId();

            Long storeId = activity.getVenderId();

            if (null == venderId || 0 == venderId) {
                venderId = storeService.findStoreById(storeId).getVenderId();
            }
            for (SmsBox smsBox : list) {
                Long boxId = smsBox.getId();
                Map<String, Object> map = showSendResult(venderId, boxId);
                sendBoxes.add(map);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("content", sendBoxes);
        map.put("first", smsBoxes.isFirst());
        map.put("last", smsBoxes.isLast());
        map.put("number", smsBoxes.getNumber());
        map.put("numberOfElements", smsBoxes.getNumberOfElements());
        map.put("pageable", smsBoxes.getPageable());
        map.put("size", smsBoxes.getSize());
        map.put("sort", smsBoxes.getSort());
        map.put("totalElements", smsBoxes.getTotalElements());
        map.put("totalPages", smsBoxes.getTotalPages());
        return map;
    }

    @Override
    @Transactional
    public Msg sendSms(SmsSendReqVO smsSendReqVO, UserVO userVO) {
        //定义活动对象
        Activity activity = null;
        //定义redis客户key
        String redisCustomerKey = RedisKey.LIST_CUSTOMER_NUM + smsSendReqVO.getActivityId();
        //定义查询客户的sql与参数
        Map<String, Object> smsMap = new HashMap<>();
        //通过key在redis中取得查询客户的sql与参数
        Map<Object, Object> redisMap = redisComponent.getRedisMapByKey(redisCustomerKey);
        if(MapUtils.isEmpty(redisMap)){
            return Msg.error("请重新筛选客户");
        }
        //Map<Object, Object> 转换为 Map<String, Object>
        redisMap.entrySet().forEach(item -> smsMap.put((String) item.getKey(), item.getValue()));
        //获取参数
        Long activityId = smsSendReqVO.getActivityId();
        String sql = smsMap.get("sql").toString();
        smsMap.remove("sql");
        Long smsTplId = smsSendReqVO.getSmsTplId();
        Long venderId = userVO.getVenderId();
        Long storeId = userVO.getStoreId();
        Long acctRecordId = null;
        //客户输入的要发送的条数
        Integer customerCount = smsSendReqVO.getCustomerCount();
        //验证活动Id
        Optional<Activity> findActivity = activityDao.findById(activityId);
        if(findActivity == null || !findActivity.isPresent()){
            return null;
        }else {
            activity = findActivity.get();
        }
        if(activity == null){
            LOGGER.error("没有此活动，activityId = {}" , activityId);
            return Msg.error("没有此活动");
        }else if(activity.getState() == 2){
            LOGGER.error("活动已完结，activityId = {}" , activityId);
            return Msg.error("活动已完结");
        }
        //验证商家
        Vender vender = venderService.findVenderById(venderId);
        if(ObjectUtils.isEmpty(vender)){
            LOGGER.error("没有此商家，venderId = {}" , venderId);
            return Msg.error("没有此商家");
        }
        //查询发送短信的客户有多少条
        Integer resultNum = customerService.countCustomerBySql(sql, smsMap);
        if(resultNum == 0){
            LOGGER.warn("没有客户信息 sql = {}，sqlParam = {}", sql, smsMap);
            return Msg.error("没有客户信息");
        }
        //处理客户要发送的数据条数
        if(customerCount != null && customerCount > 0){
            if(customerCount < resultNum){
                sql = sql + " limit 0, "+customerCount;
                //覆盖sql
                redisMap.put("sql", sql);
                resultNum = customerCount;
            }
        }

        //处理发送信息所须费用
        //查询商家的费率
        SmsFee venderSmsRates = smsFeeService.findSmsFeeByVenderId(venderId);
        if (venderSmsRates == null || venderSmsRates.getFee() < 1) {
            LOGGER.error("没有此商家短信费率，venderId = {}", venderId);
            return Msg.error("没有此商家短信费率");
        }
        //计算此次发送短信所须费用
        BigDecimal smsFee = new BigDecimal(venderSmsRates.getFee()).multiply(new BigDecimal("0.001")).multiply(new BigDecimal(resultNum));
        //根据参数冻结商家或者门店此次发送信息所须的额度
        Msg resultFrozenAccount = this.frozenVenderOrStoreAccount(userVO, smsFee, TransTypeBEnum.SMS);
        if(resultFrozenAccount.isSuccess()){
            acctRecordId = (Long) resultFrozenAccount.getData();
        }else {
            return resultFrozenAccount;
        }
        //验证短信模板
        SmsTpl rtnSmsTpl = smsTplService.findSmsTplById(smsTplId);
        if (rtnSmsTpl == null) {
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("没有此短信模板，smsTplId = {}", smsTplId);
            return Msg.error("没有此短信模板");
        }else if(1 != rtnSmsTpl.getState()){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("此短信模板未通过或在审核中，smsTplId ={}", smsTplId);
            return Msg.error("此短信模板未通过或在审核中。");
        }
        //初始化短信箱
        Msg resultSmsBox = this.initSmsBox(userVO, rtnSmsTpl, storeId, activityId, acctRecordId);
        if(resultSmsBox.isSuccess()){
            redisMap.put("smsBoxId", resultSmsBox.getData());
        }else {
            return resultSmsBox;
        }
        //把发送短信的总条数放到redis（用于时时计算短信发送情况，MQ执行完会删除）
        redisComponent.set(RedisKey.MQ_TEMP_SEND_SMS_NUM+resultSmsBox.getData(), resultNum.toString());
        //设置MQ消费须要的参数
        redisMap.put("user", BeanMapUtil.bean2Map(userVO));
        //短信费率
        redisMap.put("smsRates", venderSmsRates.getFee());
        //将查询客户数据的SQL与参数转成json放到MQ中,等待消费
        String smsJson = JSONUtils.toJSONString(redisMap);
        try{
            defaultProducerService.sendMsg(topic, smsJson);
            redisComponent.deleteByKey(redisCustomerKey);
        }catch (Exception ex){
            LOGGER.error("短信消息队列生产异常，topic={}, jsonStr={}", topic, smsJson, ex);
        }
        return Msg.success("创建成功");
    }

    /**
     * 初始化短信箱
     * @param userVO 登陆信息
     * @param smsTpl 短信模板
     * @param storeId 门店id
     * @param activityId 活动id
     * @param acctRecordId 账户流水id
     * @return
     */
    protected Msg initSmsBox(UserVO userVO, SmsTpl smsTpl, Long storeId, Long activityId, Long acctRecordId){
        //初始化短信箱
        SmsBox initSmsBox = new SmsBox();
        //如果商家直接发送短信
        if (storeId == null || storeId == 0) {
            initSmsBox.setStoreId(0L);
            initSmsBox.setStoreName("无");
        } else {
            //验证门店信息
            Store store = storeService.findStoreById(storeId);
            if (ObjectUtils.isEmpty(store)) {
                //手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                LOGGER.error("没有此门店，storeId={}", storeId);
                return Msg.error("没有此门店");
            }
            initSmsBox.setStoreId(storeId);
            initSmsBox.setStoreName(store.getStoreName());
        }
        initSmsBox.setTitle(smsTpl.getTitle());
        initSmsBox.setContent(smsTpl.getContent());
        initSmsBox.setThirdPartyId(smsTpl.getThirdPartyId());
        initSmsBox.setSmsTmpId(smsTpl.getId());
        initSmsBox.setSendTime(new Date());
        initSmsBox.setActivityId(activityId);
        initSmsBox.setAcctRecordId(acctRecordId);
        initSmsBox.setUserId(userVO.getId());
        initSmsBox.setVenderId(userVO.getVenderId());
        initSmsBox.setVenderName(userVO.getVenderName());
        SmsBox rtnInitSmsBox = createSmsBox(userVO, initSmsBox); //初始化smsBox并存入数据库
        //初始化SmsBox出错
        if (rtnInitSmsBox == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("新建并初始化短信箱记录失败");
            return Msg.error("新建并初始化短信箱记录失败");
        }
        return Msg.success(rtnInitSmsBox.getId());
    }

    /**
     * 根据参数冻结商家或者门店此次发送信息所须的额度
     * @param userVO 登陆信息
     * @param smsFee 冻结额度
     * @param transTypeBEnum 冻结状态
     * @return
     */
    protected Msg frozenVenderOrStoreAccount(UserVO userVO, BigDecimal smsFee, TransTypeBEnum transTypeBEnum){
        Long venderId = userVO.getVenderId();
        Long storeId = userVO.getStoreId();
        // 冻结账户返回的recordId，后面更新SmsBox使用
        Long rtnFrozenAcctRecord = null;
        //冻结账户：modify操作返回的条数，后面判断冻结操作是否成功使用
        Integer rtnFrozenAcctCount = null;
        //如果门店id为空或者为0说明是商家
        if(storeId == null || storeId == 0L){
            Vender vender = venderService.findVenderById(venderId);
            Map<String, Object> rtnFrozenAcctMap = accountService.frozenVenderAccount(userVO, vender.getUserId(), smsFee, transTypeBEnum);//冻结账户对应金额
            rtnFrozenAcctCount = (Integer) rtnFrozenAcctMap.get("updateCount");
            rtnFrozenAcctRecord = (Long) rtnFrozenAcctMap.get("acctRecordId");
            if (null == rtnFrozenAcctCount || rtnFrozenAcctCount == 0) {
                LOGGER.error("冻结商家资金出错，venderId：" + venderId);
                return Msg.error("冻结商家资金出错");
            }else if(rtnFrozenAcctCount == -1){
                LOGGER.error("商家余额不足，冻结商家资金出错，venderId：" + venderId);
                return Msg.error("商家账户余额不足。");
            }
            //门店操作
        }else {
            Map<String, Object> rtnFrozenAcctMap = accountService.frozenStoreAccount(userVO, storeId, smsFee, transTypeBEnum);//冻结账户对应金额
            rtnFrozenAcctCount = (Integer) rtnFrozenAcctMap.get("updateCount");
            rtnFrozenAcctRecord = (Long) rtnFrozenAcctMap.get("acctRecordId");
            if (null == rtnFrozenAcctCount || rtnFrozenAcctCount == 0) {
                LOGGER.error("冻结店铺资金出错，storeId：" + storeId);
                return Msg.error("系统开了个小差...");
            }else if(rtnFrozenAcctCount == -1){
                LOGGER.error("店铺余额不足，冻结店铺资金出错，storeId：" + storeId);
                return Msg.error("店铺余额不足。");
            }
        }
        return Msg.success(rtnFrozenAcctRecord);
    }

    /**
     * 发送短信时，最后的扣费使用，修改部分为SmsBox中的price;count;acctRecordId
     *
     * @param userVO
     * @param id
     * @param price
     * @param count
     * @param acctRecordId
     * @return
     */
    private Integer modifyBoxOnPCA(UserVO userVO, Long id, Long price, Long count, Long acctRecordId) {
        String changer = userVO.getUserName();
        Date curDate = new Date();
        Integer result = null;
        try {
            result = smsBoxDao.modifySmsBoxOnPCA(id, price, count, acctRecordId, changer, curDate);
        } catch (Exception e) {
            LOGGER.error("扣费，修改SmsBox中的price;count;acctRecordId，数据库操作异常。");
            return null;
        }
        return result;
    }

}
