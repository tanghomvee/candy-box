package com.slst.vender.dao;

import com.slst.vender.dao.model.StoreRuleRelation;
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
public interface StoreRuleRelationDao extends JpaRepository<StoreRuleRelation, Long> {



    /**
     * find exp
     * @param storeId
     * @param ruleType
     * @param yn
     * @return
     */
    @Query(value = "select srr.* from t_store_rule sr , t_store_rule_relation srr  " +
            "where srr.storeRuleId = sr.id and srr.yn = ?3 and sr.yn=?3 and srr.storeId=?1 and sr.ruleType=?2" , nativeQuery = true)
    List<StoreRuleRelation> findByStoreIdAndRuleTypeAndYn(Long storeId, Integer ruleType, Integer yn);

}
