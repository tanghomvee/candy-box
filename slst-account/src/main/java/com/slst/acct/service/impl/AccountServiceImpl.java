package com.slst.acct.service.impl;

import com.google.common.collect.Maps;
import com.slst.acct.dao.AccountDao;
import com.slst.acct.dao.model.Account;
import com.slst.acct.dao.model.AccountRecord;
import com.slst.acct.service.AccountRecordService;
import com.slst.acct.service.AccountService;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.enums.TransTypeAEnum;
import com.slst.common.enums.TransTypeBEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SysCfgService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl<Account, Long> implements AccountService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AccountDao accountDao;

    @Resource
    private AccountRecordService accountRecordService;

    @Resource
    private SysCfgService sysCfgService;

    /**
     * 创建账户
     *
     * @param account 账户实体
     * @return 账户实体
     */
    @Override
    public Account createAccount(Account account) {

        account.setLeftAgentAmt(0L);
        account.setLeftRechageAmt(0L);
        account.setBalance(0L);
        account.setTotalAgtAmt(0L);
        account.setTotalRchAmt(0L);
        account.setTotalAmt(0L);
        account.setTotalAgtCost(0L);
        account.setTotalRchCost(0L);
        account.setTotalCost(0L);
        account.setFrozen(0L);
        if (null == account.getStoreId()) {
            account.setStoreId(0L);
        }

        Account rtnAcct = accountDao.save(account);

        return rtnAcct;
    }

    @Transactional
    @Override
    public boolean deleteStoreAcct(Long storeId) {


        boolean isSuc = accountRecordService.deleteByStoreId(storeId);

        if (!isSuc) return false;

        try {
            accountDao.deleteByStoreId(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("删除门店账户信息错误,门店ID={},Msg={}", storeId, e);
            return false;
        }

        return true;
    }

    /**
     * 根据userId查找账户信息
     *
     * @param userId
     * @return
     */
    @Override
    public Account findAcctByUserId(Long userId) {
        Account rtnAcct = null;
        try {
            rtnAcct = accountDao.findByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找account，数据库操作异常。userId：" + userId);
            return null;
        }
        return rtnAcct;
    }

    /**
     * 根据storeId查找账户信息
     *
     * @param storeId
     * @return
     */
    @Override
    public Account findAcctByStoreId(Long storeId) {
        Account rtnAcct = null;
        try {
            rtnAcct = accountDao.findByStoreId(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找account，数据库操作异常。storeId：" + storeId);
            return null;
        }
        return rtnAcct;
    }

    /**
     * admin给顶级代理商充值（仅针对现金充值）
     *
     * @param curUser     当前用户（admin）
     * @param agentUserid 代理商的UserID
     * @param amount      new BigDecimal(x)中,x必须使用字符串
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer chargingForAgentByAdmin(UserVO curUser, Long agentUserid, BigDecimal amount) {

        Integer result = 0;   //代理商数据更新结果
        Integer adminResult = 0;    //admin数据更新结果
        Long adminUserId = curUser.getId();//测试admin的userId为1L
        Long saveAmount = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName();//操作者
        Date changeTime = new Date();//当前时间

        Account adminAcct = null;
        Account agentAcct = null;
        try {
            adminAcct = accountDao.findByUserId(adminUserId);//查找admin账户。用于acctId的取得
            agentAcct = accountDao.findByUserId(agentUserid);//查找需要充值的代理商账户。用于acctId的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找agentAcct/adminAcct，数据库操作异常！");
            return null;
        }
        if (null == agentAcct || null == adminAcct) return null;//未查到相关账户，返回null


        //在数据库更新账户信息
        try {
            result = accountDao.cashChargingByAdmin(agentUserid, saveAmount, changer, changeTime);//更新代理商账户数据
            adminResult = accountDao.cashChargingByAdmin(adminUserId, saveAmount, changer, changeTime);//更新admin账户数据
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常,admin/agent的acct表数据未修改！");
            return null;
        }
        if (null == result || result == 0 || null == adminResult || adminResult == 0) {    //代理商/admin 充值/入账 失败，acct表数据未修改
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，admin/agent的acct表数据未修改！");
            return null;
        }


        //此次充值,对于代理商的记录对象：rtnAgentAcctRecord
        Long tradeAcctId = adminAcct.getId();
        Integer transTypeA = TransTypeAEnum.CHARGING.getVal(); //充值
        Integer transTypeB = TransTypeBEnum.CASH.getVal();//现金
        String remark = "充值：代理商充值。现金交易";

        AccountRecord rtnAgentAcctRecord = createRecord(agentAcct, tradeAcctId, saveAmount, transTypeA, transTypeB, remark, changer);//在数据库,新建代理商账户交易记录
        if (null == rtnAgentAcctRecord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，记录表数据未修改！");
            return null;
        }

        //此次充值,对于admin的记录对象：rtnAdminAcctRecord
        tradeAcctId = agentAcct.getId();
        transTypeA = TransTypeAEnum.CHARGE_IN.getVal();  //入账
        transTypeB = TransTypeBEnum.CASH.getVal();//现金
        remark = "入账：代理商充值入账。现金交易";

        AccountRecord rtnAdminAcctRecord = createRecord(adminAcct, tradeAcctId, saveAmount, transTypeA, transTypeB, remark, changer);//在数据库,新建admin账户交易记录
        if (null == rtnAdminAcctRecord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，记录表数据未修改！");
            return null;
        }

        return result;
    }


    /**
     * 代理商给 子代理商或者商家充值（仅针对现金充值）
     *
     * @param curUser 当前用户（代理商）
     * @param userid  商家的UserID
     * @param amount  new BigDecimal(x)中,x必须使用字符串
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer chargingByAgent(UserVO curUser, Long userid, BigDecimal amount) {

        Integer result = 0;   //被充值账户（商家/子代理）数据更新结果
        Integer agentResult = 0;    //代理商数据更新结果
        Long agentUserId = curUser.getId();//代理商userId
        Long saveAmount = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        Long deductAmount = amount.multiply(new BigDecimal("-1000")).longValue();  //需要从代理商扣除的金额
        String changer = curUser.getUserName();//操作者
        Date changeTime = new Date();//当前时间

        Account agentAcct = null;
        Account resultAcct = null;
        try {
            agentAcct = accountDao.findByUserId(agentUserId);//查找代理商账户。用于acctId的取得
            resultAcct = accountDao.findByUserId(userid);//查找需要充值的商家账户。用于acctId的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找agentAcct/venderAcct，数据库操作异常！");
            return null;
        }
        if (null == agentAcct || null == resultAcct) return null;//未查到相关账户，返回null

        //代理商给 子代理商/商家充值，使用自定义modify
        Long oldBalance = agentAcct.getBalance();//原本的账户总余额
        if (saveAmount > oldBalance) {    //充值金额大于操作账户总余额
            LOGGER.error("操作账户余额不足！");
            return -1; //操作账户余额不足
        }

        Long oldLeftAgentAmt = agentAcct.getLeftAgentAmt();//原本的代理商划入账户余额
        Long oldleftRechageAmt = agentAcct.getLeftRechageAmt(); //原本的线上充值余额
        try {
            //修改操作账户信息
            if (oldleftRechageAmt == 0 && oldLeftAgentAmt == 0) {//顶级代理商
                agentResult = accountDao.costByTopAgentAndAdmin(agentUserId, saveAmount, changer, changeTime);
            } else {
                //非顶级代理商
                if (saveAmount <= oldLeftAgentAmt) {//代理商划入账户余额充足。线上充值信息不变
                    agentResult = accountDao.costByAgentAndVender(agentUserId, saveAmount, saveAmount, changer, changeTime);
                } else {//代理商划入账户余额不足
                    //先将代理商划入账户余额扣除完,差额部分再从线上充值余额中扣除
                    agentResult = accountDao.costByAgentAndVender(agentUserId, saveAmount, oldLeftAgentAmt, changer, changeTime);
                }
            }

            //修改被充值（子代理/商家）账户信息
            result = accountDao.cashChargingByAgent(userid, saveAmount, changer, changeTime);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常，acct表数据未修改！");
            return null;
        }
        if (null == agentResult || null == result || agentResult == 0 || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，acct表数据未修改！");
            return null;
        }

        //代理商给 子代理商/商家充值，使用saveAndFlush()。
        // 此方法有错，需要新 new 一个Account对象来存入数据操作，不能直接用agentAcct和resultAcct。否则事务可能会出错
//        //修改代理商账户数据（消费）
//        Long oldLeftAgentAmt = agentAcct.getLeftAgentAmt();//原本的代理商划入账户余额
//        Long oldTotalAgtCost = agentAcct.getTotalAgtCost();//原本的代划入累计消费
//        Long oldLeftRechageAmt = agentAcct.getLeftRechageAmt();//原本的线上充值余额
//        Long oldTotalRchCost = agentAcct.getTotalRchCost();//原本的线上充值累计消费
//        Long oldTotalCost = agentAcct.getTotalCost();   //原本的总累计消费

        //缺少一个顶级代理的判断

        //非顶级代理
//        if(saveAmount <= oldLeftAgentAmt){ //代理商划入账户余额充足。线上充值信息不变
//            agentAcct.setLeftAgentAmt(oldLeftAgentAmt-saveAmount); //代划入账户余额减少
//            agentAcct.setTotalAgtCost(oldTotalAgtCost+saveAmount);  //代划入累计消费增加
//        }else { //代理商划入账户余额不足
//            agentAcct.setLeftAgentAmt(0L);  //先将代理商划入账户余额扣除完
//            agentAcct.setLeftRechageAmt(oldLeftRechageAmt-saveAmount+oldLeftAgentAmt);   //差额部分再从线上充值余额中扣除
//            agentAcct.setTotalAgtCost(oldTotalAgtCost+oldLeftAgentAmt);  //代划入累计消费增加
//            agentAcct.setTotalRchCost(oldTotalRchCost+saveAmount-oldLeftAgentAmt); //线上充值累计消费增加
//        }
//        agentAcct.setBalance(oldBalance-saveAmount);  //总余额减少
//        agentAcct.setTotalCost(oldTotalCost+saveAmount); //总累计消费增加
//        agentAcct.setChanger(changer);
//        agentAcct.setChangeTime(changeTime);
//        resultAcct.setChanger(changer);
//        resultAcct.setChangeTime(changeTime);
//
//        //修改被充值账户数据（充值）
//        Long oldLeftAgentAmtForResult = resultAcct.getLeftAgentAmt();//被充值账户：原本的代划入余额
//        Long oldBalanceForResult = resultAcct.getBalance();//被充值账户：原本的总余额
//        Long oldTotalAgtAmtForResult = resultAcct.getTotalAgtAmt();//被充值账户：原本的代理商累计划入金额
//        Long oldTotalAmtForResult = resultAcct.getTotalAmt();//被充值账户：原本的累计充值金额
//
//        resultAcct.setLeftAgentAmt(oldLeftAgentAmtForResult+saveAmount);//代划入余额增加
//        resultAcct.setBalance(oldBalanceForResult+saveAmount);    //总余额增加
//        resultAcct.setTotalAgtAmt(oldTotalAgtAmtForResult+saveAmount);//代理商累计划入金额增加
//        resultAcct.setTotalAmt(oldTotalAmtForResult+saveAmount);//累计充值金额增加
//
//        Account rtnAgentAcc = null;
//        Account rtnResutAcct = null;
//        try{
//            rtnAgentAcc = accountDao.saveAndFlush(agentAcct);   //更新数据库中代理商acct数据
//            rtnResutAcct = accountDao.saveAndFlush(resultAcct);    //更新数据库中商家acct数据
//            result = 1;
//        }catch (Exception e){//acct更新失败
//            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
//            LOGGER.error("数据库操作异常，agent/vender的acct表数据未修改！");
//            return null;
//        }
//        if(null == rtnAgentAcc||null == rtnResutAcct){ //acct更新失败
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
//            LOGGER.error("数据库返回错误，agent/vender的acct表数据未修改！");
//            return null;
//        }


        //acct账户更新成功，开始创建Record记录
        //此次充值,对于被充值账户的记录对象：rtnResultAcctRecord
        Long tradeAcctId = agentAcct.getId();   //资金源头是代理商划给它的
        Integer transTypeA = TransTypeAEnum.GET_IN.getVal(); //划入
        Integer transTypeB = TransTypeBEnum.CASH.getVal();//现金
        String remark = "划入：充值划入。现金交易";

        AccountRecord rtnResultAcctRecord = createRecord(resultAcct, tradeAcctId, saveAmount, transTypeA, transTypeB, remark, changer);//在数据库,新建子代理/商家账户交易记录
        if (null == rtnResultAcctRecord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，记录表数据未修改！");
            return null;
        }

        //此次充值,对于代理商的记录对象：rtnagentAcctRecord
        tradeAcctId = resultAcct.getId();//资金源头是商家/子代理
        transTypeA = TransTypeAEnum.PAYOUT.getVal();//划出
        transTypeB = TransTypeBEnum.CASH.getVal();//现金
        remark = "划出：充值划出。现金交易";

        AccountRecord rtnagentAcctRecord = createRecord(agentAcct, tradeAcctId, saveAmount, transTypeA, transTypeB, remark, changer);//在数据库,代理商账户交易记录
        if (null == rtnagentAcctRecord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，记录表数据未修改！");
            return null;
        }

        return result;

    }


    /**
     * 给店铺充值（仅针对现金充值）
     *
     * @param curUser 当前用户（商家）
     * @param storeId 店铺的storeId
     * @param amount  new BigDecimal(x)中,x必须使用字符串
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer chargingForStore(UserVO curUser, Long storeId, BigDecimal amount) {

        Integer result = 0;   //店铺数据更新结果
        Integer venderResult = 0;    //商家数据更新结果
        Long venderUserId = curUser.getId();//商家userId
        Long saveAmount = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName();//操作者
        Date changeTime = new Date();//当前时间

        Account venderAcct = null;
        Account storeAcct = null;
        try {
            venderAcct = accountDao.findByUserId(venderUserId);//查找商家账户。用于acctId的取得
            storeAcct = accountDao.findByStoreId(storeId);//查找需要充值的店铺账户。用于acctId的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找storeAcct/venderAcct，数据库操作异常！");
            return null;
        }
        if (null == storeAcct || null == venderAcct) return null;//未查到相关账户，返回null


        Long oldBalance = venderAcct.getBalance();//原本的账户总余额
        if (saveAmount > oldBalance) {    //操作商家账户总余额不足
            LOGGER.error("操作账户余额不足！");
            return -1; //操作账户余额不足
        }

        Long oldLeftAgentAmt = venderAcct.getLeftAgentAmt();//原本的代理商划入账户余额
        //修改操作商家的账户数据
        try {
            if (saveAmount <= oldLeftAgentAmt) {  //代理商划入账户余额充足
                venderResult = accountDao.costByAgentAndVender(venderUserId, saveAmount, saveAmount, changer, changeTime);
            } else {
                venderResult = accountDao.costByAgentAndVender(venderUserId, saveAmount, oldLeftAgentAmt, changer, changeTime);
            }
            //更新店铺账户数据
            result = accountDao.cashChargingByVenderForStore(storeId, saveAmount, changer, changeTime);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常，acct表数据未修改！");
            return null;
        }
        if (null == result || null == venderResult || result == 0 || venderResult == 0) {    //商家/店铺 划出/划入 失败，acct表数据未修改
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，acct表数据未修改！");
            return null;
        }


        //此次充值,对于店铺的记录对象：rtnStoreAcctRecord
        //amount,typea,typeb,time,remark,acctId,tradeAcctId,userId,storeId,yn,creator,createTime
        Long tradeAcctId = venderAcct.getId();  //店铺充值，资金源头是商家划入
        Integer transTypeA = TransTypeAEnum.GET_IN.getVal();//划入
        Integer transTypeB = TransTypeBEnum.CASH.getVal();//现金
        String remark = "划入：店铺充值划入。现金交易";

        AccountRecord rtnStoreAcctRecord = createRecord(storeAcct, tradeAcctId, saveAmount, transTypeA, transTypeB, remark, changer);//在数据库，新建店铺交易数据
        if (null == rtnStoreAcctRecord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，记录表数据未修改！");
            return null;
        }

        //此次充值,对于商家的记录对象：rtnVenderAcctRecord
        tradeAcctId = storeAcct.getId();   //商家给店铺充值，资金源头是店铺
        transTypeA = TransTypeAEnum.PAYOUT.getVal();//划出
        transTypeB = TransTypeBEnum.CASH.getVal();//现金
        remark = "划出：给店铺充值划出。现金交易";

        AccountRecord rtnVenderAcctRecord = createRecord(venderAcct, tradeAcctId, saveAmount, transTypeA, transTypeB, remark, changer);//在数据库，新建商家交易数据
        if (null == rtnVenderAcctRecord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，记录表数据未修改！");
            return null;
        }

        return result;

    }


    /**
     * admin消费（电话匹配，标签匹配等）
     *
     * @param curUser
     * @param amount         new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @param transTypeBEnum 消费分类枚举：PHONE_MATCH(3,"电话匹配")；SMS(4,"短信")；TAG_MATCH(5,"标签匹配");
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer costOfAdmin(UserVO curUser, BigDecimal amount, TransTypeBEnum transTypeBEnum) {

        Integer result = 0;
        Long saveAmount = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName();
        Date curDate = new Date();

        Account adminAcct = null;
        try {
            String adminAcctName = sysCfgService.findByCode(SysCfgEnum.SYS_ACCT_NAME.getVal()).getCodeVal();
            adminAcct = accountDao.findByAcctName(adminAcctName);//查找账户，用于acct对应的userId和余额的获取
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找acct信息，数据库操作异常！");
            return null;
        }
        if (null == adminAcct) return null; //未找到相关用户，返回null

        Long acctId = adminAcct.getId();
        Long oldBalance = adminAcct.getBalance();   //原本的账户余额
//        if (saveAmount > oldBalance){   //账户余额不足
//            LOGGER.error("操作账户余额不足！");
//            return -1; //操作账户余额不足
//        }
        //更新acct信息
        try {
            result = accountDao.costByTopAgentAndAdmin(adminAcct.getUserId(), saveAmount, changer, curDate); //更新数据库中admin账户数据
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，更新acct数据失败！");
            return null;
        }
        if (null == result || result == 0) {
            LOGGER.error("数据库返回错误，更新acct数据失败！");
            return null;
        }
        //新建交易记录
        Integer transTypeA = TransTypeAEnum.COST.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        String remark = "消费：" + transTypeBEnum.getDesc() + "消费";

        AccountRecord rtnRcord = createRecord(adminAcct, acctId, saveAmount, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        return result;
    }


    /**
     * admin消费（电话匹配，标签匹配等）(设备开始匹配时专用)
     *
     * @param amount         new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @param transTypeBEnum 消费分类枚举：PHONE_MATCH(3,"电话匹配")；SMS(4,"短信")；TAG_MATCH(5,"标签匹配");
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer costOfAdmin(String changer, Long amount, TransTypeBEnum transTypeBEnum) throws Exception {
        Long saveAmount = amount;
        Date curDate = new Date();

        //查找账户，用于acct对应的userId和余额的获取
        String adminAcctName = sysCfgService.findByCode(SysCfgEnum.SYS_ACCT_NAME.getVal()).getCodeVal();
        Account adminAcct = accountDao.findByAcctName(adminAcctName);

        //未找到相关用户，返回null
        if (null == adminAcct) {
            throw new Exception("账户信息异常");
        }
        Long acctId = adminAcct.getId();
        //更新acct信息
        //更新数据库中admin账户数据
        Integer result = accountDao.costByTopAgentAndAdmin(adminAcct.getUserId(), saveAmount, changer, curDate);
        if (null == result || result == 0) {
            throw new Exception("更新Admin账户信息失败");
        }
        //新建交易记录
        Integer transTypeA = TransTypeAEnum.COST.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        String remark = "消费：" + transTypeBEnum.getDesc() + "消费";

        AccountRecord rtnRcord = createRecord(adminAcct, acctId, saveAmount, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            throw new Exception("更新Admin创建消费记录失败");
        }

        return result;
    }


    /**
     * 商家发短信消费
     *
     * @param curUser       当前消费的商家
     * @param smsCostAmount 商家短信消费金额，new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer smsCostByVender(UserVO curUser, Long venderUserId, BigDecimal smsCostAmount) {

        Integer result = 0;
        Long venderCost = smsCostAmount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        Long userId = venderUserId;  //商家userId
        String changer = curUser.getUserName(); //商家userName
        Date curDate = new Date();  //当前时间

        Account venderAcct = null;
        try {
            venderAcct = accountDao.findByUserId(userId);//查找商家账户。用于acctId，余额等的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找venderAcct，数据库操作异常！");
            return null;
        }
        if (null == venderAcct) return null;//未查到相关账户，返回null

        Long oldBalance = venderAcct.getBalance();//商家原本的账户总余额
        if (venderCost > oldBalance) {    //操作商家账户总余额不足
            LOGGER.error("操作账户余额不足！");
            return -1; //操作账户余额不足
        }

        //余额充足,修改商家账户数据
        Long oldLeftAgentAmt = venderAcct.getLeftAgentAmt();//原本的代理商划入账户余额
        try {
            if (venderCost <= oldLeftAgentAmt) {  //代理划入余额充足
                result = accountDao.costByAgentAndVender(userId, venderCost, venderCost, changer, curDate);//修改商家账户数据
            } else {//代理划入余额不足
                result = accountDao.costByAgentAndVender(userId, venderCost, oldLeftAgentAmt, changer, curDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，更新商家账户失败！");
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，更新商家账户失败！");
            return null;
        }

        //创建商家消费记录
        Integer transTypeA = TransTypeAEnum.COST.getVal();
        Integer transTypeB = TransTypeBEnum.SMS.getVal();
        String remark = "消费：发送短信消费";

        AccountRecord rtnRcord = createRecord(venderAcct, venderAcct.getId(), venderCost, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        return result;
    }

    /**
     * 店铺发短信消费
     *
     * @param curUser       当前操作的商家员工
     * @param storeId       当前消费的门店Id
     * @param smsCostAmount 短信消费金额，new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer smsCostByStore(UserVO curUser, Long storeId, BigDecimal smsCostAmount) {
        Integer result = 0;
        Long storeCost = smsCostAmount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //操作的商家员工的userName
        Date curDate = new Date();  //当前时间

        Account storeAcct = null;
        try {
            storeAcct = accountDao.findByStoreId(storeId);//查找门店账户。用于acctId，余额等的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找storeAcct，数据库操作异常！");
            return null;
        }
        if (null == storeAcct) return null;//未查到相关账户，返回null

        Long oldBalance = storeAcct.getBalance();//门店原本的账户总余额
        if (storeCost > oldBalance) {    //门店账户总余额不足
            LOGGER.error("操作账户余额不足！");
            return -1; //操作账户余额不足
        }
        //门店账户余额充足
        try {
            result = accountDao.costByStore(storeId, storeCost, changer, curDate);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，更新店铺账户失败！");
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，更新店铺账户失败！");
            return null;
        }

        //创建门店消费记录
        Integer transTypeA = TransTypeAEnum.COST.getVal();
        Integer transTypeB = TransTypeBEnum.SMS.getVal();
        String remark = "消费：发送短信消费";

        AccountRecord rtnRcord = createRecord(storeAcct, storeAcct.getId(), storeCost, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }


        return result;
    }

    /**
     * 子级代理短信收入
     *
     * @param curUser     当前短信消费的商家/商家员工
     * @param agentUserId 对应收入的子代理商ID
     * @param tradeAcctId 资金来源账户。发短信的商家/店铺账户Id
     * @param amount      代理商的收入,new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer smsIncomeofChildAgent(UserVO curUser, Long agentUserId, Long tradeAcctId, BigDecimal amount) {

        Integer result = 0;
        Long incomeAmount = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //发短信的商家/商家员工
        Date curDate = new Date();//当前时间

        Account agentAcct = null;
        try {
            agentAcct = accountDao.findByUserId(agentUserId);//查找代理商账户。用于acctId取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找子级代理Acct，数据库操作异常！");
            return null;
        }
        if (null == agentAcct) return null;//未查到相关账户，返回null

        try {
            result = accountDao.incomeOfChildAgent(agentUserId, incomeAmount, changer, curDate); //修改数据库代理商账户数据
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，更新子级代理商账户失败！");
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，更新子级代理商账户失败！");
            return null;
        }

//        账户数据修改成功，新建代理商收入记录
        Integer transTypeA = TransTypeAEnum.INCOME.getVal();
        Integer transTypeB = TransTypeBEnum.SMS.getVal();
        String remark = "收入：发送短信收入";
        AccountRecord rtnRcord = createRecord(agentAcct, tradeAcctId, incomeAmount, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        return result;
    }

    /**
     * 顶级代理短信收入
     *
     * @param curUser     当前短信消费的商家/商家员工
     * @param agentUserId 对应收入的顶级代理商ID
     * @param tradeAcctId 资金来源账户。发短信的商家/店铺账户Id
     * @param amount      代理商的收入,new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Integer smsIncomeofTopAgent(UserVO curUser, Long agentUserId, Long tradeAcctId, BigDecimal amount) {

        Integer result = 0;
        Long incomeAmount = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //发短信的商家/商家员工
        Date curDate = new Date();//当前时间

        Account agentAcct = null;
        try {
            agentAcct = accountDao.findByUserId(agentUserId);//查找代理商账户。用于acctId取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找顶级代理Acct，数据库操作异常！");
            return null;
        }
        if (null == agentAcct) return null;//未查到相关账户，返回null

        try {
            result = accountDao.incomeOfTopAgent(agentUserId, incomeAmount, changer, curDate); //修改数据库代理商账户数据
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，更新顶级代理商账户失败！");
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，更新顶级代理商账户失败！");
            return null;
        }

//        账户数据修改成功，新建代理商收入记录
        Integer transTypeA = TransTypeAEnum.INCOME.getVal();
        Integer transTypeB = TransTypeBEnum.SMS.getVal();
        String remark = "收入：发送短信收入";

        AccountRecord rtnRcord = createRecord(agentAcct, tradeAcctId, incomeAmount, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        return result;
    }

    /**
     * 冻结商家账户部分金额
     *
     * @param curUser        当前需要冻结操作的商家
     * @param venderUserId
     * @param amount         需要冻结的金额
     * @param transTypeBEnum 冻结类型B
     * @return map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：-1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Map<String, Object> frozenVenderAccount(UserVO curUser, Long venderUserId, BigDecimal amount, TransTypeBEnum transTypeBEnum) {
        Integer result = 0;
        Map<String, Object> resultAndRecord = Maps.newHashMap();
        Long venderCost = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //修改者userName
        Date curDate = new Date();  //当前时间

        Account venderAcct = null;
        try {
            venderAcct = accountDao.findByUserId(venderUserId);//查找商家账户。用于acctId，余额等的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找venderAcct，userId:" + venderUserId + "，数据库操作异常！" + e);
            return null;
        }
        if (null == venderAcct) return null;//未查到相关账户，返回null

        Long oldBalance = venderAcct.getBalance();//商家原本的账户总余额
        if (venderCost > oldBalance) {    //操作商家账户总余额不足
            LOGGER.error("操作账户余额不足！");
            resultAndRecord.put("updateCount", -1);
            resultAndRecord.put("acctRecordId", 0L);
            return resultAndRecord;
//            return -1; //操作账户余额不足
        }

        //余额充足,修改商家账户数据
        Long oldLeftAgentAmt = venderAcct.getLeftAgentAmt();//原本的代理商划入账户余额
        try {
//            if(venderCost <= oldLeftAgentAmt){  //代理划入余额充足
//                result = accountDao.frozenVenderAcct(userId,venderCost,venderCost,changer,curDate);//修改商家账户数据
//            }else {//代理划入余额不足
//                result = accountDao.frozenVenderAcct(userId,venderCost,oldLeftAgentAmt,changer,curDate);
//            }
            result = accountDao.frozenVenderAcct(venderUserId, venderCost, changer, curDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("数据库操作异常，冻结商家账户，UserId：" + venderUserId + "，失败！" + ex);
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，冻结商家账户，UserId：" + venderUserId + "，失败！");
            return null;
        }

        //创建商家消费记录
        Integer transTypeA = TransTypeAEnum.FROZEN.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        String remark = "冻结：" + transTypeBEnum.getDesc() + "消费";

        AccountRecord rtnRcord = createRecord(venderAcct, venderAcct.getId(), venderCost, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        resultAndRecord.put("updateCount", result);
        resultAndRecord.put("acctRecordId", rtnRcord.getId());
        return resultAndRecord;
    }

    /**
     * 冻结店铺账户部分金额
     *
     * @param curUser        当前操作者（员工/商家）
     * @param storeId        当前需要冻结操作的店铺
     * @param amount         需要冻结的金额
     * @param transTypeBEnum 冻结类型B
     * @return map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：-1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Map<String, Object> frozenStoreAccount(UserVO curUser, Long storeId, BigDecimal amount, TransTypeBEnum transTypeBEnum) {
        Integer result = 0;
        Map<String, Object> resultAndRecord = Maps.newHashMap();
        Long storeCost = amount.multiply(new BigDecimal("1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //操作的商家员工的userName
        Date curDate = new Date();  //当前时间

        Account storeAcct = null;
        try {
            storeAcct = accountDao.findByStoreId(storeId);//查找门店账户。用于acctId，余额等的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找storeAcct，storeID：" + storeId + "，数据库操作异常！");
            return null;
        }
        if (null == storeAcct) return null;//未查到相关账户，返回null

        Long oldBalance = storeAcct.getBalance();//门店原本的账户总余额
        if (storeCost > oldBalance) {    //门店账户总余额不足
            LOGGER.error("操作账户余额不足.");
            resultAndRecord.put("updateCount", -1);
            resultAndRecord.put("acctRecordId", 0L);
            return resultAndRecord;
//            return -1; //操作账户余额不足
        }
        //门店账户余额充足
        try {
            result = accountDao.frozenStoreAcct(storeId, storeCost, changer, curDate);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，冻结店铺账户，storeID：" + storeId + "，失败！");
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，冻结店铺账户失败！");
            return null;
        }

        //创建门店消费记录
        Integer transTypeA = TransTypeAEnum.FROZEN.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        String remark = "冻结：" + transTypeBEnum.getDesc() + "消费";

        AccountRecord rtnRcord = createRecord(storeAcct, storeAcct.getId(), storeCost, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        resultAndRecord.put("updateCount", result);
        resultAndRecord.put("acctRecordId", rtnRcord.getId());
        return resultAndRecord;
    }

    /**
     * 解冻商家账户部分金额
     *
     * @param curUser
     * @param venderUserId
     * @param amount
     * @param transTypeBEnum
     * @return map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Map<String, Object> thawVenderAccount(UserVO curUser, Long venderUserId, BigDecimal amount, TransTypeBEnum transTypeBEnum) {
        Integer result = 0;
        Long venderCost = amount.multiply(new BigDecimal("-1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //修改者userName
        Date curDate = new Date();  //当前时间

        Account venderAcct = null;
        try {
            venderAcct = accountDao.findByUserId(venderUserId);//查找商家账户。用于acctId的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找venderAcct。userId:" + venderUserId + "，数据库操作异常！" + e);
            return null;
        }
        if (null == venderAcct) return null;//未查到相关账户，返回null

        try {
            result = accountDao.frozenVenderAcct(venderUserId, venderCost, changer, curDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("数据库操作异常。解冻商家账户，UserId：" + venderUserId + "，失败！" + ex);
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，解冻商家账户，UserId：" + venderUserId + "，失败！");
            return null;
        }
        //创建商家消费记录
        Integer transTypeA = TransTypeAEnum.UNFROZEN.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        String remark = "解冻：" + transTypeBEnum.getDesc() + "消费结束";

        AccountRecord rtnRcord = createRecord(venderAcct, venderAcct.getId(), -venderCost, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }
        Map<String, Object> resultAndRecord = Maps.newHashMap();
        resultAndRecord.put("updateCount", result);
        resultAndRecord.put("acctRecordId", rtnRcord.getId());

        return resultAndRecord;
    }

    /**
     * 解冻店铺账户部分金额
     *
     * @param curUser        当前操作者（员工/商家）
     * @param storeId        当前需要冻结操作的店铺
     * @param amount         需要冻结的金额
     * @param transTypeBEnum 冻结类型B
     * @return result= map<String,Object> key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：>0：操作成功；null：操作失败，出现异常
     */
    @Transactional
    @Override
    public Map<String, Object> thawStoreAccount(UserVO curUser, Long storeId, BigDecimal amount, TransTypeBEnum transTypeBEnum) {
        Integer result = 0;
        Long storeCost = amount.multiply(new BigDecimal("-1000")).longValue();//数据库中金额单位为“厘”：0.001元。所以传入金额*1000
        String changer = curUser.getUserName(); //操作的商家员工的userName
        Date curDate = new Date();  //当前时间

        Account storeAcct = null;
        try {
            storeAcct = accountDao.findByStoreId(storeId);//查找门店账户。用于acctId的取得
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找storeAcct。storeID：" + storeId + "，数据库操作异常！" + e);
            return null;
        }
        if (null == storeAcct) return null;//未查到相关账户，返回null

        try {
            result = accountDao.frozenStoreAcct(storeId, storeCost, changer, curDate);//更新数据库
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("数据库操作异常，解冻店铺账户，storeID：" + storeId + "，失败！" + ex);
            return null;
        }
        if (null == result || result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库返回错误，解冻店铺账户失败！");
            return null;
        }

        //创建门店消费记录
        Integer transTypeA = TransTypeAEnum.UNFROZEN.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        String remark = "解冻：" + transTypeBEnum.getDesc() + "消费结束";

        AccountRecord rtnRcord = createRecord(storeAcct, storeAcct.getId(), -storeCost, transTypeA, transTypeB, remark, changer);
        if (null == rtnRcord) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建记录失败！");
            return null;
        }

        Map<String, Object> resultAndRecord = Maps.newHashMap();
        resultAndRecord.put("updateCount", result);
        resultAndRecord.put("acctRecordId", rtnRcord.getId());

        return resultAndRecord;
    }


    /**
     * admin给顶级代理商充值错误，admin申请退款
     *
     * @param curUser
     * @param topAgentUserId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional
    @Override
    public Msg chargingRefundFromTopAgToAdmin(UserVO curUser, Long topAgentUserId, BigDecimal amount, String reason) {
        if (StringUtils.isNullOrEmpty(reason) || reason.trim().length() < 5) {
            return Msg.error("退款理由字数不足，不能小于5个字！");
        }

        Date curDate = new Date(); //当前时间
        String curUserName = curUser.getUserName(); //当前操作者
        Long adminUserId = curUser.getId(); //admin的UserId
        Long transAmount = amount.multiply(new BigDecimal("1000")).longValue();
        Account adminAcct = null;

        Account agentAcount = null;
        Long agentBalance = 0L;  //顶级代理商余额
        try {
            agentAcount = accountDao.findByUserId(topAgentUserId);    //查找顶级代理商账户
            agentBalance = agentAcount.getBalance();

            adminAcct = accountDao.findByUserId(adminUserId);   //查找admin账户。用于后面的创建记录
            Long adminAcctId = adminAcct.getId();   //admin账户Id，用于判断是否查找账户出错

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("admin申请退款，查找admin/顶级代理商账户出错。adminUserId={}，topAgentUserId={}，Msg={}", adminUserId, topAgentUserId, e);
            return Msg.error("系统开了会小差...");
        }

        if (agentBalance < transAmount) {
            return Msg.error("顶级代理商账户余额不足，无法退款。");
        }

        Integer resultCount = 0;
        try {
            //从顶级代理商账户扣款
            resultCount = accountDao.chargingRefundFromAdminOrTopAgent(topAgentUserId, transAmount, curUserName, curDate);
            //退款给admin账户
            resultCount += accountDao.chargingRefundToAdminOrTopAgent(adminUserId, transAmount, curUserName, curDate);

            if (2 != resultCount) throw new RuntimeException("更改admin/TopAgent账户出错");

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("admin申请退款，更改账户信息出错。adminUserId={},topAgentUserId={}，Msg={}", adminUserId, topAgentUserId, e);
            return Msg.error("系统开了会小差...");
        }

        //创建admin记录
        Integer transTypeA = TransTypeAEnum.RETRIEVE.getVal();//回收
        Integer transTypeB = TransTypeBEnum.CASH.getVal();
        String remark = "退款申请：" + reason;
        AccountRecord rtnAdminRcord = createRecord(adminAcct, agentAcount.getId(), transAmount, transTypeA, transTypeB, remark, curUserName);

        //创建顶级代理商记录
        Integer transTypeA1 = TransTypeAEnum.RETREAT.getVal();//退回
        String remark1 = "退款扣费：" + reason;
        AccountRecord rtnTopAgentRcord = createRecord(agentAcount, agentAcount.getId(), transAmount, transTypeA1, transTypeB, remark1, curUserName);

        if (null == rtnAdminRcord || null == rtnTopAgentRcord) {  //创建记录失败
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建账户记录失败！adminUserId={}，topAgentUserId={}", adminUserId, topAgentUserId);
            return Msg.error("系统开了会小差...");
        }


        return Msg.success();   //退款成功
    }

    /**
     * 顶级代理给子级代理商充值错误，顶级代理申请退款
     *
     * @param curUser
     * @param childAgentUserId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional
    @Override
    public Msg chargingRefundFromChildAgToTopAg(UserVO curUser, Long childAgentUserId, BigDecimal amount, String reason) {
        if (StringUtils.isNullOrEmpty(reason) || reason.trim().length() < 5) {
            return Msg.error("退款理由字数不足，不能小于5个字！");
        }

        Date curDate = new Date(); //当前时间
        String curUserName = curUser.getUserName(); //当前操作者
        Long topUserId = curUser.getId(); //topAgent的UserId
        Long transAmount = amount.multiply(new BigDecimal("1000")).longValue();
        Account topAgentAcct = null;

        Account childAgentAcount = null;
        Long agentLeftAgentAmt = 0L;  //子级代理商的代理商划入余额
        try {
            childAgentAcount = accountDao.findByUserId(childAgentUserId);    //查找子级代理商账户
            agentLeftAgentAmt = childAgentAcount.getLeftAgentAmt();

            topAgentAcct = accountDao.findByUserId(topUserId);   //查找topAgent账户。用于后面的创建记录
            Long topAgentAcctId = topAgentAcct.getId();   //topAgent账户Id，用于判断是否查找账户出错

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("topAgent申请退款，查找子级/顶级代理商账户出错。topAgentUserId={}，childAgentUserId={}，Msg={}", topUserId, childAgentUserId, e);
            return Msg.error("系统开了会小差...");
        }

        if (agentLeftAgentAmt < transAmount) {
            return Msg.error("子代理商账户余额不足，无法退款。");
        }

        Integer resultCount = 0;
        try {
            //从子级代理商账户扣款
            resultCount = accountDao.chargingRefundFromChildAgentOrVender(childAgentUserId, transAmount, curUserName, curDate);
            //退款给顶级代理商账户
            resultCount += accountDao.chargingRefundToAdminOrTopAgent(topUserId, transAmount, curUserName, curDate);

            if (2 != resultCount) throw new RuntimeException("更改topAgent/childAgent账户出错");

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("topAgent申请退款，更改账户信息出错。topAgentUserId={},childAgentUserId={}，Msg={}", topUserId, childAgentUserId, e);
            return Msg.error("系统开了会小差...");
        }

        //创建topAgent记录
        Integer transTypeA = TransTypeAEnum.RETRIEVE.getVal();//回收
        Integer transTypeB = TransTypeBEnum.CASH.getVal();
        String remark = "退款申请：" + reason;
        AccountRecord rtnAdminRcord = createRecord(topAgentAcct, childAgentAcount.getId(), transAmount, transTypeA, transTypeB, remark, curUserName);

        //创建childAgent记录
        Integer transTypeA1 = TransTypeAEnum.RETREAT.getVal();//退回
        String remark1 = "退款扣费：" + reason;
        AccountRecord rtnTopAgentRcord = createRecord(childAgentAcount, childAgentAcount.getId(), transAmount, transTypeA1, transTypeB, remark1, curUserName);

        if (null == rtnAdminRcord || null == rtnTopAgentRcord) {  //创建记录失败
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建账户记录失败！topAgentUserId={}，childAgentUserId={}", topUserId, childAgentUserId);
            return Msg.error("系统开了会小差...");
        }

        return Msg.success();   //退款成功
    }


    /**
     * 顶级代理给商家充值错误，顶级代理申请退款
     *
     * @param curUser
     * @param venderUserId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional
    @Override
    public Msg chargingRefundFromVenderToTopAg(UserVO curUser, Long venderUserId, BigDecimal amount, String reason) {
        if (StringUtils.isNullOrEmpty(reason) || reason.trim().length() < 5) {
            return Msg.error("退款理由字数不足，不能小于5个字!");
        }

        Date curDate = new Date(); //当前时间
        String curUserName = curUser.getUserName(); //当前操作者
        Long topUserId = curUser.getId(); //topAgent的UserId
        Long transAmount = amount.multiply(new BigDecimal("1000")).longValue();
        Account topAgentAcct = null;

        Account venderAcount = null;
        Long venderLeftAgentAmt = 0L;  //商家的代理商划入余额
        try {
            venderAcount = accountDao.findByUserId(venderUserId);    //查找商家账户
            venderLeftAgentAmt = venderAcount.getLeftAgentAmt();

            topAgentAcct = accountDao.findByUserId(topUserId);   //查找topAgent账户。用于后面的创建记录
            Long topAgentAcctId = topAgentAcct.getId();   //topAgent账户Id，用于判断是否查找账户出错

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("topAgent申请退款，查找顶级代理商/商家账户出错。topAgentUserId={}，venderUserId={}，Msg={}", topUserId, venderUserId, e);
            return Msg.error("系统开了会小差...");
        }

        if (venderLeftAgentAmt < transAmount) {
            return Msg.error("商家账户余额不足，无法退款。");
        }

        Integer resultCount = 0;
        try {
            //从商家账户扣款
            resultCount = accountDao.chargingRefundFromChildAgentOrVender(venderUserId, transAmount, curUserName, curDate);
            //退款给顶级代理商账户
            resultCount += accountDao.chargingRefundToAdminOrTopAgent(topUserId, transAmount, curUserName, curDate);

            if (2 != resultCount) throw new RuntimeException("更改topAgent/vender账户出错");

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("topAgent申请退款，更改账户信息出错。topAgentUserId={},venderUserId={}，Msg={}", topUserId, venderUserId, e);
            return Msg.error("系统开了会小差...");
        }

        //创建topAgent记录
        Integer transTypeA = TransTypeAEnum.RETRIEVE.getVal();//回收
        Integer transTypeB = TransTypeBEnum.CASH.getVal();
        String remark = "退款申请：" + reason;
        AccountRecord rtnAdminRcord = createRecord(topAgentAcct, venderAcount.getId(), transAmount, transTypeA, transTypeB, remark, curUserName);

        //创建商家记录
        Integer transTypeA1 = TransTypeAEnum.RETREAT.getVal();//退回
        String remark1 = "退款扣费：" + reason;
        AccountRecord rtnTopAgentRcord = createRecord(venderAcount, venderAcount.getId(), transAmount, transTypeA1, transTypeB, remark1, curUserName);

        if (null == rtnAdminRcord || null == rtnTopAgentRcord) {  //创建记录失败
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建账户记录失败！topAgentUserId={}，venderUserId={}", topUserId, venderUserId);
            return Msg.error("系统开了会小差...");
        }

        return Msg.success();   //退款成功
    }


    /**
     * 子级代理给商家充值错误，子级代理申请退款
     *
     * @param curUser
     * @param venderUserId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional
    @Override
    public Msg chargingRefundFromVenderToChildAg(UserVO curUser, Long venderUserId, BigDecimal amount, String reason) {
        if (StringUtils.isNullOrEmpty(reason) || reason.trim().length() < 5) {
            return Msg.error("退款理由字数不足，不能小于5个字 !");
        }

        Date curDate = new Date(); //当前时间
        String curUserName = curUser.getUserName(); //当前操作者
        Long childUserId = curUser.getId(); //childAgent的UserId
        Long transAmount = amount.multiply(new BigDecimal("1000")).longValue();
        Account childAgentAcct = null;

        Account venderAcount = null;
        Long venderLeftAgentAmt = 0L;  //商家的代理商划入余额
        try {
            venderAcount = accountDao.findByUserId(venderUserId);    //查找商家账户
            venderLeftAgentAmt = venderAcount.getLeftAgentAmt();

            childAgentAcct = accountDao.findByUserId(childUserId);   //查找子级代理账户。用于后面的创建记录
            Long topAgentAcctId = childAgentAcct.getId();   //子级代理账户Id，用于判断是否查找账户出错

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("childAgent申请退款，查找子级代理商/商家账户出错。childAgentUserId={}，venderUserId={}，Msg={}", childUserId, venderUserId, e);
            return Msg.error("系统开了会小差...");
        }

        if (venderLeftAgentAmt < transAmount) {
            return Msg.error("商家账户余额不足，无法退款。");
        }

        Integer resultCount = 0;
        try {
            //从商家账户扣款
            resultCount = accountDao.chargingRefundFromChildAgentOrVender(venderUserId, transAmount, curUserName, curDate);
            //退款给子级代理商账户
            resultCount += accountDao.chargingRefundToChildAgentOrVender(childUserId, transAmount, curUserName, curDate);

            if (2 != resultCount) throw new RuntimeException("更改childAgent/vender账户出错");

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("childAgent申请退款，更改账户信息出错。childAgentUserId={},venderUserId={}，Msg={}", childUserId, venderUserId, e);
            return Msg.error("系统开了会小差...");
        }

        //创建childAgent记录
        Integer transTypeA = TransTypeAEnum.RETRIEVE.getVal();//回收
        Integer transTypeB = TransTypeBEnum.CASH.getVal();
        String remark = "退款申请：" + reason;
        AccountRecord rtnAdminRcord = createRecord(childAgentAcct, venderAcount.getId(), transAmount, transTypeA, transTypeB, remark, curUserName);

        //创建vender记录
        Integer transTypeA1 = TransTypeAEnum.RETREAT.getVal();//退回
        String remark1 = "退款扣费：" + reason;
        AccountRecord rtnTopAgentRcord = createRecord(venderAcount, venderAcount.getId(), transAmount, transTypeA1, transTypeB, remark1, curUserName);

        if (null == rtnAdminRcord || null == rtnTopAgentRcord) {  //创建记录失败
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建账户记录失败！childAgentUserId={}，venderUserId={}", childUserId, venderUserId);
            return Msg.error("系统开了会小差...");
        }

        return Msg.success();   //退款成功
    }


    /**
     * 商家给门店充值错误，商家申请退款
     *
     * @param curUser
     * @param storeId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional
    @Override
    public Msg chargingRefundFromStoreToVender(UserVO curUser, Long storeId, BigDecimal amount, String reason) {
        if (StringUtils.isNullOrEmpty(reason) || reason.trim().length() < 5) {
            return Msg.error("退款理由字数不足，不能小于5个字!");
        }

        Date curDate = new Date(); //当前时间
        String curUserName = curUser.getUserName(); //当前操作者
        Long venderUserId = curUser.getId(); //vender的UserId
        Long transAmount = amount.multiply(new BigDecimal("1000")).longValue();
        Account venderAcct = null;

        Account storeAcount = null;
        Long storeBalance = 0L;  //门店余额
        try {
            storeAcount = accountDao.findByStoreId(storeId);    //查找门店账户
            storeBalance = storeAcount.getBalance();

            venderAcct = accountDao.findByUserId(venderUserId);   //查找商家账户。用于后面的创建记录
            Long venderAcctId = venderAcct.getId();   //商家账户Id，用于判断是否查找账户出错

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("vender申请退款，查找商家/门店账户出错。venderUserId={}，storeId={}，Msg={}", venderUserId, storeId, e);
            return Msg.error("系统开了会小差...");
        }

        if (storeBalance < transAmount) {
            return Msg.error("门店账户余额不足，无法退款。");
        }

        Integer resultCount = 0;
        try {
            //从门店账户扣款
            resultCount = accountDao.chargingRefundFromStore(storeId, transAmount, curUserName, curDate);
            //退款给商家账户
            resultCount += accountDao.chargingRefundToChildAgentOrVender(venderUserId, transAmount, curUserName, curDate);

            if (2 != resultCount) throw new RuntimeException("更改vender/store账户出错");

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("vender申请退款，更改账户信息出错。venderUserId={}，storeId={}，Msg={}", venderUserId, storeId, e);
            return Msg.error("系统开了会小差...");
        }

        //创建vender记录
        Integer transTypeA = TransTypeAEnum.RETRIEVE.getVal();//回收
        Integer transTypeB = TransTypeBEnum.CASH.getVal();
        String remark = "退款申请：" + reason;
        AccountRecord rtnAdminRcord = createRecord(venderAcct, storeAcount.getId(), transAmount, transTypeA, transTypeB, remark, curUserName);

        //创建store记录
        Integer transTypeA1 = TransTypeAEnum.RETREAT.getVal();//退回
        String remark1 = "退款扣费：" + reason;
        AccountRecord rtnTopAgentRcord = createRecord(storeAcount, storeAcount.getId(), transAmount, transTypeA1, transTypeB, remark1, curUserName);

        if (null == rtnAdminRcord || null == rtnTopAgentRcord) {  //创建记录失败
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
            LOGGER.error("数据库操作异常/返回错误，新建账户记录失败！venderUserId={}，storeId={}", venderUserId, storeId);
            return Msg.error("系统开了会小差...");
        }

        return Msg.success();   //退款成功
    }

    /**-----------------------------(商家和门店皆可使用)通话功能扣费模块,以后可复用于其他模块--------------------------------------*/
    /**
     * 商家通开通语音服务扣费操作(恢复语音,线下消费等皆可用)
     *
     * @param curUser 当前用户
     * @param amount  金额
     * @return
     */
    @Transactional
    @Override
    public Integer venderCallSrvCost(UserVO curUser, Long amount) {

        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setTypea(TransTypeAEnum.COST.getVal());
        accountRecord.setTypeb(TransTypeBEnum.PHONE_CALL.getVal());
        accountRecord.setRemark("开通语音服务、恢复语音服务或月租费用扣费");

        return this.venderCost(curUser, amount, accountRecord);
    }

    @Transactional
    @Override
    public Integer venderCost(UserVO curUser, Long amount, AccountRecord accountRecord) {
        Long userId = curUser.getId();
        Long storeId = curUser.getStoreId();
        String userName = curUser.getUserName();
        Date curDate = new Date();

        Account account = null;

        if (null == storeId || storeId.equals(0L)) {
            account = findAcctByUserId(userId);
        } else {
            account = findAcctByUserId(storeId);
        }

        if (null == account) return -1;

        //账户扣费
        Integer result = accountDao.venderCost(userId, amount, userName, curDate);

        if (result <= 0) return result;

        accountRecord.setAmount(amount);
        accountRecord.setTime(curDate);
        accountRecord.setAcctId(account.getId());
        accountRecord.setTradeAcctId(account.getId());
        if (null == storeId || storeId.equals(0L)) {
            accountRecord.setUserId(userId);
            accountRecord.setStoreId(0L);
        } else {
            accountRecord.setUserId(0L);
            accountRecord.setStoreId(storeId);
        }
        accountRecord.setYn(YNEnum.YES.getVal());
        accountRecord.setCreator(userName);
        accountRecord.setCreateTime(curDate);

        //生成消费记录
        AccountRecord rtnAcctRcd = accountRecordService.createAcctRecord(accountRecord);

        if (null != rtnAcctRcd) {
            return 1;
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚,如果不手动回滚会导致UnexpectedRollbackException
            return -1;
        }
    }

    /**
     * 打电话时冻结账户
     *
     * @param curUser
     * @param amount
     * @return 如果返回结果大于0, 代表是账户记录表的ID
     */
    @Transactional
    @Override
    public Long venderCallSrvFrozen(UserVO curUser, Long amount) {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setTypea(TransTypeAEnum.FROZEN.getVal());
        accountRecord.setTypeb(TransTypeBEnum.PHONE_CALL.getVal());
        accountRecord.setRemark("用户:" + curUser.getUserName() + "," + TransTypeAEnum.FROZEN.getDesc() + ":" + TransTypeBEnum.PHONE_CALL.getDesc());

        return this.venderFrozen(curUser, amount, accountRecord);
    }

    /**
     * 冻结账户
     *
     * @param curUser
     * @param amount
     * @return 如果返回结果大于0, 代表是账户记录表的ID
     */
    @Transactional
    @Override
    public Long venderFrozen(UserVO curUser, Long amount, AccountRecord accountRecord) {

        Long userId = curUser.getId();
        Long storeId = curUser.getStoreId();
        String userName = curUser.getUserName();
        Date curDate = new Date();

        Account account = null;

        if (null == storeId || storeId.equals(0L)) {
            account = findAcctByUserId(userId);
        } else {
            account = findAcctByUserId(storeId);
        }

        if (null == account) return -1L;

        //冻结金额
        Integer result = accountDao.venderFrozen(userId, amount, userName, curDate);


        if (result <= 0) return (long) result;


        accountRecord.setAmount(amount);
        accountRecord.setTime(curDate);
        accountRecord.setAcctId(account.getId());
        accountRecord.setTradeAcctId(account.getId());
        if (null == storeId || storeId.equals(0L)) {
            accountRecord.setUserId(userId);
            accountRecord.setStoreId(0L);
        } else {
            accountRecord.setStoreId(storeId);
            accountRecord.setUserId(0L);
        }
        accountRecord.setYn(YNEnum.YES.getVal());
        accountRecord.setCreator(userName);
        accountRecord.setCreateTime(curDate);

        //生成冻结金额记录
        AccountRecord rtnAcctRcd = accountRecordService.createAcctRecord(accountRecord);

        if (null != rtnAcctRcd) {
            return rtnAcctRcd.getId();
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚,如果不手动回滚会导致UnexpectedRollbackException
            return -1L;
        }
    }

    /**
     * 解除冻结
     *
     * @param curUser  当前用户
     * @param acctRcId 账户记录ID用于找到冻结记录
     * @param amount   消费金额,用于计算应该解冻的金额
     * @return 返回账户记录中解冻记录的ID
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Long venderUnfreeze(UserVO curUser, Long acctRcId, Long amount) throws Exception {

        Long userId = curUser.getId();
        Long storeId = curUser.getStoreId();
        String userName = curUser.getUserName();
        Date curDate = new Date();

        //获取账户信息
        Account account = null;
        if (null == storeId || storeId.equals(0L)) {
            account = findAcctByUserId(userId);
        } else {
            account = findAcctByUserId(storeId);
        }
        if (null == account) {
            return -1L;
        }

        //获取冻结记录
        AccountRecord accountRecord = accountRecordService.findAcctRecordById(acctRcId);
        if (null == accountRecord) {
            return -1L;
        }

        Long frozenAmt = accountRecord.getAmount();//冻结金额

        Long balance = frozenAmt - amount;//应该退回到余额的钱

        Integer typeB = accountRecord.getTypeb();//交易类型B

        String typeBDesc = TransTypeBEnum.getByVal(typeB).getDesc();//交易类型B的描述

        StringBuilder remark = new StringBuilder("解冻账户");
        remark.append(account.getAcctName());
        remark.append("于");
        remark.append(accountRecord.getTime());
        remark.append("创建的冻结记录.");
        remark.append("交易类型为:");
        remark.append(typeBDesc);
        remark.append(".冻结记录ID为");
        remark.append(accountRecord.getId());

        //账户解冻

        Integer result = accountDao.venderUnFreeze(userId, balance, amount, frozenAmt, userName, curDate);
        if (result <= 0) {
            return (long) result;
        }

        //生成解冻记录
        AccountRecord unfreezeAcctRcd = new AccountRecord();
        unfreezeAcctRcd.setAmount(frozenAmt);
        unfreezeAcctRcd.setTypea(TransTypeAEnum.UNFROZEN.getVal());
        unfreezeAcctRcd.setTypeb(typeB);
        unfreezeAcctRcd.setRemark(remark.toString());
        unfreezeAcctRcd.setTime(curDate);
        unfreezeAcctRcd.setAcctId(account.getId());
        unfreezeAcctRcd.setTradeAcctId(account.getId());
        if (null == storeId || storeId.equals(0L)) {
            unfreezeAcctRcd.setStoreId(0L);
            unfreezeAcctRcd.setUserId(userId);
        } else {
            unfreezeAcctRcd.setStoreId(storeId);
            unfreezeAcctRcd.setUserId(0L);
        }
        unfreezeAcctRcd.setYn(YNEnum.YES.getVal());
        unfreezeAcctRcd.setCreator(userName);
        unfreezeAcctRcd.setCreateTime(curDate);

        AccountRecord rtnAcctRcd = accountRecordService.createAcctRecord(unfreezeAcctRcd);
        //如果创建记录失败则返回-1
        if (null == rtnAcctRcd) {
            throw new Exception("创建解冻记录失败");
        }

        Long unfreezeRcId = rtnAcctRcd.getId();//解冻记录ID

        if (amount > 0L) {
            //生成消费记录
            AccountRecord costAcctRcd = new AccountRecord();
            costAcctRcd.setAmount(amount);
            costAcctRcd.setTypea(TransTypeAEnum.COST.getVal());
            costAcctRcd.setTypeb(typeB);
            costAcctRcd.setRemark("用户账户:" + account.getAcctName() + "," + TransTypeAEnum.COST.getDesc() + ":" + typeBDesc);
            costAcctRcd.setTime(curDate);
            costAcctRcd.setAcctId(account.getId());
            costAcctRcd.setTradeAcctId(account.getId());
            if (null == storeId || storeId.equals(0L)) {
                costAcctRcd.setStoreId(0L);
                costAcctRcd.setUserId(userId);
            } else {
                costAcctRcd.setUserId(0L);
                costAcctRcd.setStoreId(storeId);
            }
            costAcctRcd.setYn(YNEnum.YES.getVal());
            costAcctRcd.setCreator(userName);
            costAcctRcd.setCreateTime(curDate);

            AccountRecord rtnAcctRcdOfCost = accountRecordService.createAcctRecord(costAcctRcd);
            //如果创建记录失败则返回-1
            if (null == rtnAcctRcdOfCost) {
                throw new Exception("创建消费记录失败");
            }
        }

        return unfreezeRcId;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Long unfreezeWithCallSrvCost(UserVO curUser, Long acctRcId, Long agentUserId, Long platformCost, Long amount, Long earning) throws Exception {
        return this.unfreezeWithCost(curUser, acctRcId, agentUserId, platformCost, amount, earning, TransTypeBEnum.PHONE_CALL);
    }

    /**
     * 解冻金额时产生了消费
     *
     * @param curUser        当前用户(商家或者门店,目前只在商家或门店会有冻结账户操作)
     * @param acctRcId       账户冻结记录ID
     * @param agentUserId    代理商用户ID
     * @param platformCost   平台成本花费
     * @param amount         商家或商家门店产生的消费
     * @param earning        代理商提成金额
     * @param transTypeBEnum 细分交易类型
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Long unfreezeWithCost(UserVO curUser, Long acctRcId, Long agentUserId, Long platformCost, Long amount, Long earning, TransTypeBEnum transTypeBEnum) throws Exception {

        //商家解冻并产生消费
        Long unfreezeAcctRcdId = 0L;//解冻之后的记录ID
        Integer result = 0;

        //admin产生的消费
        result = this.costOfAdmin("sys", platformCost, transTypeBEnum);


        if (result == null || result <= 0) {
            LOGGER.error("用户ID={},解除冻结并产生消费时系统创建成本记录时异常", curUser.getId());
        }

        unfreezeAcctRcdId = this.venderUnfreeze(curUser, acctRcId, amount);

        if (unfreezeAcctRcdId == null || unfreezeAcctRcdId <= 0) {
            throw new Exception("解除冻结并产生消费失败");
        }

        //代理商提成
        result = this.inComeOfTopAgent(curUser, agentUserId, earning, transTypeBEnum);
        if (result == null || result <= 0) {
            throw new Exception("解除冻结并产生消费时,代理商提成失败");
        }

        return unfreezeAcctRcdId;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer callSrvCostWithEarning(UserVO curUser, Long agentUserId, Long platformCost, Long amount, Long earning) throws Exception {
        return costWithEarning(curUser, agentUserId, platformCost, amount, earning, TransTypeBEnum.PHONE_CALL);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer costWithEarning(UserVO curUser, Long agentUserId, Long platformCost, Long amount, Long earning, TransTypeBEnum transTypeBEnum) throws Exception {
        Integer result = 0;

        //admin产生的消费
        result = this.costOfAdmin("sys", platformCost, transTypeBEnum);
        if (result == null || result < 1) {
            throw new Exception("Admin扣费失败");
        }


        result = callSrvCostAllowNegative(curUser, amount, transTypeBEnum);

        if (result == null || result < 1) {
            throw new Exception("商家扣费失败");
        }


        //代理商提成
        result = this.inComeOfTopAgent(curUser, agentUserId, earning, transTypeBEnum);
        if (result == null || result < 1) {
            throw new Exception("代理商提成失败");
        }

        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer callSrvCostAllowNegative(UserVO curUser, Long amount, TransTypeBEnum transTypeBEnum) throws Exception {

        Long storeId = curUser.getStoreId();
        Long userId = curUser.getId();

        //获取账户信息
        Account account = null;
        if (null == storeId || storeId.equals(0L)) {
            account = findAcctByUserId(userId);
        } else {
            account = findAcctByUserId(storeId);
        }
        if (null == account) return -1;

        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setRemark("账户:" + account.getAcctName() + transTypeBEnum.getDesc() + "消费");
        return costAllowNegative(curUser, amount, transTypeBEnum, accountRecord);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer costAllowNegative(UserVO curUser, Long amount, TransTypeBEnum transTypeBEnum, AccountRecord accountRecord) throws Exception {
        Long userId = curUser.getId();
        Long storeId = curUser.getStoreId();
        String userName = curUser.getUserName();
        Date curDate = new Date();

        Account account = null;

        if (null == storeId || storeId.equals(0L)) {
            account = findAcctByUserId(userId);
        } else {
            account = findAcctByUserId(storeId);
        }

        if (null == account) return -1;

        //账户扣费
        Integer result = accountDao.venderCostAllowNega(userId, amount, userName, curDate);

        if (result == null || result <= 0) {
            throw new Exception("商家账户扣费失败");
        }
        accountRecord.setTypea(TransTypeAEnum.COST.getVal());
        accountRecord.setTypeb(transTypeBEnum.getVal());
        accountRecord.setAmount(amount);
        accountRecord.setTime(curDate);
        accountRecord.setAcctId(account.getId());
        accountRecord.setTradeAcctId(account.getId());
        if (null != storeId && !storeId.equals(0L)) {
            accountRecord.setUserId(0L);
            accountRecord.setStoreId(storeId);
        } else {
            accountRecord.setStoreId(0L);
            accountRecord.setUserId(userId);
        }
        accountRecord.setYn(YNEnum.YES.getVal());
        accountRecord.setCreator(userName);
        accountRecord.setCreateTime(curDate);

        //生成消费记录
        AccountRecord rtnAcctRcd = accountRecordService.createAcctRecord(accountRecord);

        if (null == rtnAcctRcd) {
            throw new Exception("创建商家消费记录失败");
        }
        return 1;
    }


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer inComeOfTopAgent(UserVO curUser, Long agentUserId, Long amount, TransTypeBEnum transTypeBEnum) throws Exception {
        Integer result = 0;
        String changer = "sys";
        Date curDate = new Date();//当前时间
        Long userId = curUser.getId();
        Long storeId = curUser.getStoreId();
        //设置记录交易类型和生成收入记录备注
        Integer transTypeA = TransTypeAEnum.INCOME.getVal();
        Integer transTypeB = transTypeBEnum.getVal();
        StringBuilder remark = new StringBuilder();
        remark.append("商家");
        remark.append(curUser.getVenderName());

        //获得交易账户信息
        Account tradeAcct = null;
        if (null == storeId || storeId.equals(0L)) {
            tradeAcct = findAcctByUserId(userId);
        } else {
            tradeAcct = findAcctByUserId(storeId);
            remark.append("旗下门店");
            remark.append(curUser.getStoreName());
        }
        remark.append("通过");
        remark.append(transTypeBEnum.getDesc());
        remark.append("产生消费带来的收入.");

        if (null == tradeAcct) {
            return -1;
        }

        Long tradeAcctId = tradeAcct.getId();

        //查找代理商账户。用于acctId取得
        Account agentAcct = accountDao.findByUserId(agentUserId);
        if (null == agentAcct) {
            LOGGER.error("查找顶级代理Acct，数据库操作异常！代理商用户ID={}", agentUserId);
            return null;//未查到相关账户，返回null
        }

        //修改数据库代理商账户数据
        result = accountDao.incomeOfTopAgent(agentUserId, amount, changer, curDate);
        if (null == result || result == 0) {
            throw new Exception("代理商获得收入时,修改信息失败");
        }

//        账户数据修改成功，新建代理商收入记录
        AccountRecord rtnRcord = createRecord(agentAcct, tradeAcctId, amount, transTypeA, transTypeB, remark.toString(), changer);
        if (null == rtnRcord) {
            throw new Exception("创建代理商获得收入时记录");
        }

        return result;
    }


    /**
     * 创建交易记录
     *
     * @param account     此条记录的对应账户
     * @param tradeAcctId 资金源头的账户ID
     * @param saveAmount  交易金额
     * @param transTypeA  交易类型A:TransTypeAEnum
     * @param transTypeB  交易类型B:TransTypeBEnum
     * @param remark      备注信息
     * @return
     */
    @Transactional
    public AccountRecord createRecord(Account account, Long tradeAcctId, Long saveAmount, Integer transTypeA, Integer transTypeB, String remark, String changer) {
        Integer yes = YNEnum.YES.getVal();//该条数据是否有效
        Date curDate = new Date();
        AccountRecord accountRecord = new AccountRecord();

        accountRecord.setAmount(saveAmount);    //交易金额
        accountRecord.setTypea(transTypeA);   //划入等
        accountRecord.setTypeb(transTypeB); //现金等
        accountRecord.setTime(curDate);  //交易时间
        accountRecord.setRemark(remark);    //备注
        accountRecord.setAcctId(account.getId());   //此条记录的acctId
        accountRecord.setTradeAcctId(tradeAcctId);  //资金源头的acctId
        accountRecord.setUserId(account.getUserId());   //此条记录的userId
        accountRecord.setStoreId(account.getStoreId()); //此条记录的storeId
        accountRecord.setYn(yes);   //此条数据是否有效
        accountRecord.setCreator(changer);  //记录的创建者
        accountRecord.setCreateTime(curDate);    //记录的创建时间

        AccountRecord rtnAcctRecord = null;
        try {
            rtnAcctRecord = accountRecordService.createAcctRecord(accountRecord);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("数据库操作异常，创建Record失败。accountId={}，Msg={}", account.getId(), e);
            return null;
        }
        if (null == rtnAcctRecord) {
            LOGGER.error("数据库返回错误，创建Record失败accountId={}，Msg={}", account.getId(), "创建账户记录返回为空。");
        }

        return rtnAcctRecord;
    }


}

