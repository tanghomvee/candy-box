package com.slst.acct.dao;

import com.slst.acct.dao.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface AccountDao extends JpaRepository<Account, Long>, AccountDaoExt {

    /**
     * 根据用户ID查询账户信息
     *
     * @param userId
     * @return
     */
    Account findByUserId(Long userId);


    /**
     * 根据店铺ID（storeId）查询
     * @param storeId
     * @return
     */
    Account findByStoreId(Long storeId);

    /**
     * 根据账户名查找acct
     * @param acctName
     * @return
     */
    Account findByAcctName(String acctName);



    /**
     * 新开账户
     *
     * @param account
     * @return
     */
    Account save(Account account);


    /**
     * 根据门店ID删除账户
     * @param storeId
     */
    void deleteByStoreId(Long storeId);

    /**
     * 更新账户信息
     * @param account
     * @return
     */
    Account saveAndFlush(Account account);

    /**
     * admin给代理商充值（现金）
     *
     * @param userId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance+:amount,a.totalAmt=a.totalAmt+:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer cashChargingByAdmin(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 顶级代理给子代理/商家充值（现金）
     *
     * @param userId
     * @param amount
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance+:amount,a.totalAmt=a.totalAmt+:amount,a.totalAgtAmt=a.totalAgtAmt+:amount,a.leftAgentAmt=a.leftAgentAmt+:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer cashChargingByAgent(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 商家给门店充值（现金）
     * @param storeId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance+:amount,a.totalAmt=a.totalAmt+:amount,a.changer=:userName,a.changeTime=:curDate where a.storeId=:storeId")
    Integer cashChargingByVenderForStore(@Param("storeId") Long storeId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * admin/顶级代理消费
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance-:amount,a.totalCost=a.totalCost+:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer costByTopAgentAndAdmin(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 子代理商/商家 消费（现金）
     * @param userId
     * @param amount 消费总金额
     * @param agentAmtPay 代理商划入余额中扣除的金额
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.totalAgtCost=a.totalAgtCost+:agentAmtPay,a.totalRchCost=a.totalRchCost+:amount-:agentAmtPay,a.totalCost=a.totalCost+:amount,a.leftRechageAmt=a.leftRechageAmt-:amount+:agentAmtPay,a.leftAgentAmt=a.leftAgentAmt-:agentAmtPay,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer costByAgentAndVender(@Param("userId") Long userId, @Param("amount") Long amount, @Param("agentAmtPay") Long agentAmtPay, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 门店消费
     * @param storeId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance-:amount,a.totalCost=a.totalCost+:amount,a.changer=:userName,a.changeTime=:curDate where a.storeId=:storeId")
    Integer costByStore(@Param("storeId") Long storeId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 子级代理（短信）收入
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance+:amount,a.totalAmt=a.totalAmt+:amount,a.leftAgentAmt=a.leftAgentAmt+:amount,a.totalAgtAmt=a.totalAgtAmt+:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer incomeOfChildAgent(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 顶级代理（短信）收入
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance+:amount,a.totalAmt=a.totalAmt+:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer incomeOfTopAgent(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 冻结商家账户资金
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.frozen=a.frozen+:amount,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer frozenVenderAcct(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 冻结店铺账户资金
     * @param storeId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.frozen=a.frozen+:amount,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.storeId=:storeId")
    Integer frozenStoreAcct(@Param("storeId") Long storeId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 充值错误，退款给admin/顶级代理商（金额增加方）
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance+:amount,a.totalCost=a.totalCost-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer chargingRefundToAdminOrTopAgent(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 充值错误，退款给子级代理商/商家（金额增加方）
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.leftAgentAmt=a.leftAgentAmt+:amount,a.balance=a.balance+:amount,a.totalAgtCost=a.totalAgtCost-:amount,a.totalCost=a.totalCost-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer chargingRefundToChildAgentOrVender(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);




    /**
     * 充值错误，从admin/顶级代理商 处退款（金额减少方）
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance-:amount,a.totalAmt=a.totalAmt-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer chargingRefundFromAdminOrTopAgent(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 充值错误，从子级代理商/商家 处退款（金额减少方）
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance-:amount,a.leftAgentAmt=a.leftAgentAmt-:amount,a.totalAgtAmt=a.totalAgtAmt-:amount,a.totalAmt=a.totalAmt-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer chargingRefundFromChildAgentOrVender(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 充值错误，从门店 处退款（金额减少方）
     * @param storeId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.balance=a.balance-:amount,a.totalAmt=a.totalAmt-:amount,a.changer=:userName,a.changeTime=:curDate where a.storeId=:storeId")
    Integer chargingRefundFromStore(@Param("storeId") Long storeId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);




    /**-----------------------------通话功能扣费模块,以后可复用于其他模块--------------------------------------*/
    /**
     * 商家线下消费,若该商家代理商划入金额充足才消费
     * @param userId 用户Id
     * @param amount 金额
     * @param userName 修改者名字
     * @param curDate 当前时间
     * @return
     */
    @Modifying
    @Query("update Account a set a.totalAgtCost=a.totalAgtCost+:amount,a.totalCost=a.totalCost+:amount,a.leftAgentAmt=a.leftAgentAmt-:amount,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId and a.leftAgentAmt>=:amount and a.balance>=:amount")
    Integer venderCost(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 商家线下消费,允许为负数
     * @param userId 用户Id
     * @param amount 金额
     * @param userName 修改者名字
     * @param curDate 当前时间
     * @return
     */
    @Modifying
    @Query("update Account a set a.totalAgtCost=a.totalAgtCost+:amount,a.totalCost=a.totalCost+:amount,a.leftAgentAmt=a.leftAgentAmt-:amount,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer venderCostAllowNega(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 商家线下消费前,冻结金额,若该商家代理商划入金额充足才冻结
     * @param userId
     * @param amount
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.leftAgentAmt=a.leftAgentAmt-:amount,a.balance=a.balance-:amount,a.frozen=a.frozen+:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId and a.leftAgentAmt>=:amount and a.balance>=:amount")
    Integer venderFrozen(@Param("userId") Long userId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 商家金额解冻
     * @param userId
     * @param balance
     * @param amount
     * @param frozenAmt
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Account a set a.leftAgentAmt=a.leftAgentAmt+:balance,a.balance=a.balance+:balance,a.totalAgtCost=a.totalAgtCost+:amount,a.totalCost=a.totalCost+:amount,a.frozen=a.frozen-:frozenAmt,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
    Integer venderUnFreeze(@Param("userId") Long userId, @Param("balance") Long balance,@Param("amount") Long amount,@Param("frozenAmt") Long frozenAmt, @Param("userName") String userName, @Param("curDate") Date curDate);


    //    /**
//     * 冻结商家账户资金
//     * @param userId
//     * @param amount
//     * @param agentAmtPay
//     * @param userName
//     * @param curDate
//     * @return
//     */
//    @Modifying
//    @Query("update Account a set a.frozen=a.frozen+:amount,a.leftRechageAmt=a.leftRechageAmt-:amount+:agentAmtPay,a.leftAgentAmt=a.leftAgentAmt-:agentAmtPay,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.userId=:userId")
//    Integer frozenVenderAcct(@Param("userId") Long userId, @Param("amount") Long amount, @Param("agentAmtPay") Long agentAmtPay, @Param("userName") String userName, @Param("curDate") Date curDate);
//
//    /**
//     * 冻结店铺账户资金
//     * @param storeId
//     * @param amount
//     * @param userName
//     * @param curDate
//     * @return
//     */
//    @Modifying
//    @Query("update Account a set a.frozen=a.frozen+:amount,a.balance=a.balance-:amount,a.changer=:userName,a.changeTime=:curDate where a.storeId=:storeId")
//    Integer frozenStoreAcct(@Param("storeId") Long storeId, @Param("amount") Long amount, @Param("userName") String userName, @Param("curDate") Date curDate);

}
