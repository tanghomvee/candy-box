package com.slst.vender.service;

import com.slst.common.enums.RuleExpEnum;
import com.slst.common.service.BaseService;
import com.slst.vender.dao.model.StoreRuleRelation;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface StoreRuleRelationService extends BaseService <StoreRuleRelation, Long>{

    /**
     * find By StoreIdAndRuleType
     * @param storeId
     * @param ruleType
     * @return
     */
    List<StoreRuleRelation> findByStoreIdAndRuleType(Long storeId, Integer ruleType);

    /**
     *
     * @param storeId 门店ID
     * @param storeRuleId 门店 storeRuleId
     * @see com.slst.common.enums.RuleExpEnum
     * @param ruleExpEnum
     *
     * @param val
     * @return
     */
    StoreRuleRelation save(Long storeId , Long storeRuleId, RuleExpEnum ruleExpEnum , String val);

}
