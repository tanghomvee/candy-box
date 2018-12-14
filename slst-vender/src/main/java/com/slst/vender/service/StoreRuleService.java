package com.slst.vender.service;

import com.slst.common.service.BaseService;
import com.slst.vender.dao.model.StoreRule;

import java.util.List;
import java.util.function.Function;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface StoreRuleService extends BaseService <StoreRule, Long>{

    /**
     * find by storeId
     * @param storeId
     * @return
     */
    List<StoreRule> findByStoreId(Long storeId);

    /**
     * execute rule
     * @param storeId
     * @param ruleType
     * @param specifiedVal
     * @param typeConvertFun
     * @param <T>
     * @return
     */
    <T extends Comparable> boolean execute(Long storeId, Integer ruleType, T specifiedVal, Function<String, T> typeConvertFun);
}
