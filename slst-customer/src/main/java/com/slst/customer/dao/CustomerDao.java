package com.slst.customer.dao;

import com.slst.customer.dao.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:42
 */
public interface CustomerDao extends JpaRepository<Customer , Long>,CustomerDaoExt {

    /**
     * find
     * @param mac
     * @param yn
     * @return
     */
    List<Customer> findByMacAndYn(String mac, Integer yn);

    /**
     *
     * @param id
     * @param yn
     * @return
     */
    Customer findByIdAndYn(Long id,Integer yn);

    /**
     * 根据mobile查询店铺内客户
     * @return
     */
    @Query(value = "SELECT DISTINCT customerId,mobile,mac FROM `t_store_customer` WHERE mobile is not null AND mobile<>'' AND venderId=:venderId AND LOCATE(:mobile,mobile)>0",nativeQuery = true)
    List findCusByVenderIdAndMobile(@Param("venderId") Long venderId,@Param("mobile") String mobile);

    @Query(value = "SELECT DISTINCT customerId,mobile,mac FROM `t_store_customer` WHERE mobile is not null AND mobile<>'' AND storeId=:storeId AND LOCATE(:mobile,mobile)>0",nativeQuery = true)
    List findCusByStoreIdAndMobile(@Param("storeId") Long storeId,@Param("mobile") String mobile);

    @Query(value = "SELECT DISTINCT customerId,mobile,mac FROM `t_store_customer` WHERE mobile is not null AND mobile<>'' AND venderId=:venderId AND LOCATE(:mac,mac)>0",nativeQuery = true)
    List findCusByVenderIdAndMac(@Param("venderId") Long venderId,@Param("mac") String mac);

    @Query(value = "SELECT DISTINCT customerId,mobile,mac FROM `t_store_customer` WHERE mobile is not null AND mobile<>'' AND storeId=:storeId AND LOCATE(:mac,mac)>0",nativeQuery = true)
    List findCusByStoreIdAndMac(@Param("storeId") Long storeId,@Param("mac") String mac);

    /**
     * 查询所有手机品牌
     * @return
     */
    @Query(value = "select corporationEn,corporationCn,count(distinct corporationCn) from `t_nic` where macType=1 and yn=1 group by corporationCn",nativeQuery = true)
    List findAllMobileBrand();

    /**
     * 查询所有年龄阶段
     * @return
     */
    @Query(value = "SELECT distinct mobileBrand FROM `t_customer` where mobileBrand is not null and mobileBrand<>'未知'",nativeQuery = true)
    List<String> findAllMobileBrandEn();

    /**
     * 查询所有年龄阶段
     * @return
     */
    @Query(value = "SELECT distinct ageSlot FROM `t_customer` where ageSlot is not null and ageSlot<>'未知'",nativeQuery = true)
    List<String> findAllAgeSlot();

    /**
     * 查询所有职业
     * @return
     */
    @Query(value = "SELECT distinct career FROM `t_customer` where career is not null and career<>'未知'",nativeQuery = true)
    List<String> findAllCareer();

    /**
     * 查询所有收入标准
     * @return
     */
    @Query(value = "SELECT distinct incomeSlot FROM `t_customer` where incomeSlot is not null and incomeSlot<>'未知'",nativeQuery = true)
    List<String> findAllInComeSlot();

    /**
     * 查询有无车辆
     * @return
     */
    @Query(value = "SELECT distinct car FROM `t_customer` where car is not null and car<>'未知'",nativeQuery = true)
    List<String> findAllHavingCars();

    /**
     * 查询所有教育程度
     * @return
     */
    @Query(value = "SELECT distinct education FROM `t_customer` where education is not null and education<>'未知'",nativeQuery = true)
    List<String> findAllEducations();

    /**
     * 查询所有子女
     * @return
     */
    @Query(value = "SELECT distinct children FROM `t_customer` where children is not null and children<>'未知'",nativeQuery = true)
    List<String> findAllChildren();

    /**
     * 查询所有子女
     * @return
     */
    @Query(value = "SELECT distinct house FROM `t_customer` where house is not null and house<>'未知'",nativeQuery = true)
    List<String> findAllHouseSlot();

    /**
     * 查询所有兴趣标签
     * @return
     */
    @Query(value = "select distinct interest FROM `t_customer_interest` where interest is not null and interest<>'未知'",nativeQuery = true)
    List<String> findAllInterest();




}
