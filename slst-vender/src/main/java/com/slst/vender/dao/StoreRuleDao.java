package com.slst.vender.dao;

import com.slst.vender.dao.model.StoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface StoreRuleDao extends JpaRepository<StoreRule, Long> {

    /**
     * find by storeId
     * @param storeId
     * @param yn
     * @return
     */
    @Query(value = "select sr.* from t_store_rule sr left join t_store_rule_relation srr on srr.ruleId = sr.id " +
            "where srr.yn = ?2 and sr.yn=?2 and srr.storeId=?1" , nativeQuery = true)
    List<StoreRule> findByStoreId(Long storeId, Integer yn);

    /**
     * find exp
     * @param storeId
     * @param ruleType
     * @param yn
     * @return
     */
    @Query(value = "select srr.exp,srr.val from t_store_rule sr , t_store_rule_relation srr  " +
            "where srr.ruleId = sr.id and srr.yn = ?3 and sr.yn=?3 and srr.storeId=?1 and sr.ruleType=?2" , nativeQuery = true)
    List<String[]> findExpByStoreIdAndRuleTypeAndYn(Long storeId, Integer ruleType, Integer yn);

}
