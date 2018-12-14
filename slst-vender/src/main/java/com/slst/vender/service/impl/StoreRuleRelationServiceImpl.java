package com.slst.vender.service.impl;

import com.slst.common.enums.RuleExpEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.vender.dao.StoreRuleRelationDao;
import com.slst.vender.dao.model.StoreRuleRelation;
import com.slst.vender.service.StoreRuleRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 11:43
 */
@Service("storeRuleRelationService")
public class StoreRuleRelationServiceImpl extends BaseServiceImpl<StoreRuleRelation,Long> implements StoreRuleRelationService {
    @Resource
    private StoreRuleRelationDao storeRuleRelationDao;

    @Override
    public List<StoreRuleRelation> findByStoreIdAndRuleType(Long storeId, Integer ruleType) {
        return storeRuleRelationDao.findByStoreIdAndRuleTypeAndYn(storeId , ruleType , YNEnum.YES.getVal());
    }

    @Override
    public StoreRuleRelation save(Long storeId, Long storeRuleId, RuleExpEnum ruleExpEnum, String val) {
        StoreRuleRelation storeRuleRelation = new StoreRuleRelation();
        storeRuleRelation.setExp(ruleExpEnum.getVal());
        storeRuleRelation.setStoreId(storeId);
        storeRuleRelation.setStoreRuleId(storeRuleId);
        storeRuleRelation.setVal(val);
        storeRuleRelation.setCreator("sys");
        return storeRuleRelationDao.save(storeRuleRelation);
    }

}
