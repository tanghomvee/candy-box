package com.slst.acct.service;

import com.slst.acct.dao.model.Account;
import com.slst.acct.dao.model.AccountRecord;
import com.slst.common.enums.TransTypeBEnum;
import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 10:53
 */
public interface AccountService extends BaseService<Account , Long> {

    /**
     * 创建账户
     * @param account
     * @return
     */
    Account createAccount(Account account);

    boolean deleteStoreAcct(Long storeId);

    /**
     * 根据userId查找账户信息
     * @param userId
     * @return
     */
    Account findAcctByUserId(Long userId);

    /**
     * 根据storeId查找账户信息
     * @param storeId
     * @return
     */
    Account findAcctByStoreId(Long storeId);


    /**
     * admin给顶级代理商充值（仅针对现金充值）
     * @param curUser 当前用户（admin）
     * @param agentUserid 代理商的UserID
     * @param amount new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return  >0：操作成功；null：操作失败，出现异常
     */
    Integer chargingForAgentByAdmin(UserVO curUser, Long agentUserid, BigDecimal amount);

    /**
     * 代理商给 子代理商或者商家充值（仅针对现金充值）
     * @param curUser 当前用户（代理商）
     * @param userid 商家的UserID
     * @param amount new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return  -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Integer chargingByAgent(UserVO curUser, Long userid, BigDecimal amount);

    /**
     * 给店铺充值（仅针对现金充值）
     * @param curUser 当前用户（商家）
     * @param storeId 店铺的storeId
     * @param amount new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Integer chargingForStore(UserVO curUser, Long storeId, BigDecimal amount);

    /**
     * admin消费（电话匹配，标签匹配等）
     * @param curUser 当前操作者
     * @param amount new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @param transTypeBEnum 消费分类枚举：PHONE_MATCH(3,"电话匹配")；SMS(4,"短信")；TAG_MATCH(5,"标签匹配");
     * @return
     */
    Integer costOfAdmin(UserVO curUser, BigDecimal amount, TransTypeBEnum transTypeBEnum);

    /**
     * admin消费（电话匹配，标签匹配等）(设备开始匹配时专用)
     * @param amount new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @param transTypeBEnum 消费分类枚举：PHONE_MATCH(3,"电话匹配")；SMS(4,"短信")；TAG_MATCH(5,"标签匹配");
     * @return
     */
    Integer costOfAdmin(String changer, Long amount, TransTypeBEnum transTypeBEnum) throws Exception;

    /**
     * 商家发短信消费
     * @param curUser 当前消费的商家
     * @param smsCostAmount 商家短信消费金额，new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Integer smsCostByVender(UserVO curUser,Long venderUserId, BigDecimal smsCostAmount);

    /**
     * 店铺发短信消费
     * @param curUser 当前操作的商家员工
     * @param storeId 当前消费的门店Id
     * @param smsCostAmount 短信消费金额，new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Integer smsCostByStore(UserVO curUser, Long storeId, BigDecimal smsCostAmount);

    /**
     * 子级代理短信收入
     * @param curUser 当前短信消费的商家/商家员工
     * @param agentUserId 对应收入的子代理商ID
     * @param tradeAcctId 资金来源账户。发短信的商家/店铺账户Id
     * @param amount 代理商的收入,new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Integer smsIncomeofChildAgent(UserVO curUser, Long agentUserId, Long tradeAcctId, BigDecimal amount);

    /**
     * 顶级代理短信收入
     * @param curUser 当前短信消费的商家/商家员工
     * @param agentUserId 对应收入的顶级代理商ID
     * @param tradeAcctId 资金来源账户。发短信的商家/店铺账户Id
     * @param amount 代理商的收入,new BigDecimal(x)中,x必须使用字符串，大于0且小于BigDecimal最大值
     * @return -1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Integer smsIncomeofTopAgent(UserVO curUser, Long agentUserId, Long tradeAcctId, BigDecimal amount);

    /**
     * 发短信时，冻结商家账户部分金额
     * @param curUser
     * @param venderUserId 需要冻结操作的商家userId
     * @param amount 需要冻结的金额
     * @param transTypeBEnum 冻结类型B
     * @return map<String,Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：-1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Map<String,Object> frozenVenderAccount(UserVO curUser, Long venderUserId, BigDecimal amount, TransTypeBEnum transTypeBEnum);

    /**
     * 发短信时，冻结店铺账户部分金额
     * @param curUser 当前操作者（员工/商家）
     * @param storeId 当前需要冻结操作的店铺
     * @param amount 需要冻结的金额
     * @param transTypeBEnum 冻结类型B
     * @return map<String,Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：-1：操作账户余额不足；>0：操作成功；null：操作失败，出现异常
     */
    Map<String,Object> frozenStoreAccount(UserVO curUser, Long storeId, BigDecimal amount, TransTypeBEnum transTypeBEnum);

    /**
     * 解冻商家账户部分金额
     * @param curUser
     * @param venderUserId
     * @param amount
     * @param transTypeBEnum
     * @return map<String,Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：>0：操作成功；null：操作失败，出现异常
     */
    Map<String,Object> thawVenderAccount(UserVO curUser, Long venderUserId, BigDecimal amount, TransTypeBEnum transTypeBEnum);

    /**
     * 解冻店铺账户部分金额
     * @param curUser 当前操作者（员工/商家）
     * @param storeId 当前需要冻结操作的店铺
     * @param amount 需要冻结的金额
     * @param transTypeBEnum 冻结类型B
     * @return map<String,Object>。key:acctRecordId - 创建的记录ID（Long）；updateCount - 修改条数（Integer）：>0：操作成功；null：操作失败，出现异常
     */
    Map<String,Object> thawStoreAccount(UserVO curUser, Long storeId, BigDecimal amount, TransTypeBEnum transTypeBEnum);

    /**
     * admin给顶级代理商充值错误，admin申请退款
     * @param curUser
     * @param topAgentUserId
     * @param amount
     * @param reason
     * @return
     */
    Msg chargingRefundFromTopAgToAdmin(UserVO curUser, Long topAgentUserId, BigDecimal amount, String reason);

    /**
     * 顶级代理给子级代理商充值错误，顶级代理申请退款
     * @param curUser
     * @param childAgentUserId
     * @param amount
     * @param reason
     * @return
     */
    Msg chargingRefundFromChildAgToTopAg(UserVO curUser, Long childAgentUserId, BigDecimal amount, String reason);

    /**
     * 顶级代理给商家充值错误，顶级代理申请退款
     * @param curUser
     * @param venderUserId
     * @param amount
     * @param reason
     * @return
     */
    Msg chargingRefundFromVenderToTopAg(UserVO curUser, Long venderUserId, BigDecimal amount, String reason);

    /**
     * 子级代理给商家充值错误，子级代理申请退款
     * @param curUser
     * @param venderUserId
     * @param amount
     * @param reason
     * @return
     */
    Msg chargingRefundFromVenderToChildAg(UserVO curUser, Long venderUserId, BigDecimal amount, String reason);

    /**
     * 商家给门店充值错误，商家申请退款
     * @param curUser
     * @param storeId
     * @param amount
     * @param reason
     * @return
     */
    Msg chargingRefundFromStoreToVender(UserVO curUser, Long storeId, BigDecimal amount, String reason);


    /**-----------------------------(商家和门店皆可使用)通话功能扣费模块,以后可复用于其他模块--------------------------------------*/
    /**
     * 商家通开通语音服务扣费操作(恢复语音,线下消费等皆可用)
     * @param curUser 当前用户
     * @param amount 金额
     * @return
     */
    Integer venderCallSrvCost(UserVO curUser,Long amount);

    /**
     * 商家线下消费
     * @param curUser
     * @param amount
     * @param accountRecord
     * @return
     */
    Integer venderCost(UserVO curUser, Long amount, AccountRecord accountRecord);

    /**
     * 打电话时冻结账户
     * @param curUser
     * @param amount
     * @return
     */
    Long venderCallSrvFrozen(UserVO curUser,Long amount);

    /**
     * 冻结账户
     * @param curUser
     * @param amount
     * @param accountRecord
     * @return
     */
    Long venderFrozen(UserVO curUser,Long amount,AccountRecord accountRecord);

    /**
     * 解除冻结
     * @param curUser 当前用户
     * @param acctRcId 账户记录ID用于找到冻结记录
     * @param amount 消费金额,用于计算应该解冻的金额
     * @return 返回账户记录中解冻记录的ID
     */
    Long venderUnfreeze(UserVO curUser,Long acctRcId,Long amount) throws Exception;

    /**
     * 语音业务解冻金额时产生了消费
     * @param curUser 当前用户(商家或者门店,目前只在商家或门店会有冻结账户操作)
     * @param acctRcId 账户冻结记录ID
     * @param agentUserId 代理商用户ID
     * @param platformCost 平台成本花费
     * @param amount 商家或商家门店产生的消费
     * @return 返回值为解冻之后的记录ID
     */
    Long unfreezeWithCallSrvCost(UserVO curUser,Long acctRcId, Long agentUserId,Long platformCost, Long amount,Long earning) throws Exception;

    /**
     * 解冻金额时产生了消费
     * @param curUser 当前用户(商家或者门店,目前只在商家或门店会有冻结账户操作)
     * @param acctRcId 账户冻结记录ID
     * @param agentUserId 代理商用户ID
     * @param platformCost 平台成本花费
     * @param amount 商家或商家门店产生的消费
     * @param transTypeBEnum 细分交易类型
     * @return 返回值为解冻之后的记录ID
     */
    Long unfreezeWithCost(UserVO curUser,Long acctRcId, Long agentUserId,Long platformCost, Long amount,Long earning,TransTypeBEnum transTypeBEnum) throws Exception;


    Integer callSrvCostWithEarning(UserVO curUser,Long agentUserId,Long platformCost, Long amount,Long earning) throws Exception;

    Integer costWithEarning(UserVO curUser,Long agentUserId,Long platformCost, Long amount,Long earning,TransTypeBEnum transTypeBEnum) throws Exception;


    Integer callSrvCostAllowNegative(UserVO curUser,Long amount,TransTypeBEnum transTypeBEnum) throws Exception;

    Integer costAllowNegative(UserVO curUser,Long amount,TransTypeBEnum transTypeBEnum,AccountRecord accountRecord) throws Exception;

    /**
     * 总代理商收入
     * @param curUser 当前用户(商家或者门店,目前只在商家或门店产生消费时会为代理商带来收入)
     * @param agentUserId 代理商用户ID
     * @param amount 金额
     * @param transTypeBEnum 交易类型B
     * @return
     */
    Integer inComeOfTopAgent(UserVO curUser, Long agentUserId, Long amount,TransTypeBEnum transTypeBEnum) throws Exception;


}
