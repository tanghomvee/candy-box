package com.slst.vender.service.impl;

import com.slst.common.enums.RuleExpEnum;
import com.slst.common.enums.RuleTypeEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.vender.dao.StoreRuleDao;
import com.slst.vender.dao.model.StoreRule;
import com.slst.vender.dao.model.StoreRuleRelation;
import com.slst.vender.service.StoreRuleRelationService;
import com.slst.vender.service.StoreRuleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 10:16
 */
@Service("storeRuleService")
public class StoreRuleServiceImpl extends BaseServiceImpl<StoreRule ,Long> implements StoreRuleService {
    @Resource
    private StoreRuleDao storeRuleDao;

    @Resource
    private StoreRuleRelationService storeRuleRelationService;

    @Override
    public List<StoreRule> findByStoreId(Long storeId) {
        return storeRuleDao.findByStoreId(storeId , YNEnum.YES.getVal());
    }


    @Override
    public <T extends Comparable> boolean execute(Long storeId , Integer ruleType , T specifiedVal , Function<String,T> typeConvertFun) {
        if(storeId == null || storeId < 1 || ruleType == null || ruleType < 1 || specifiedVal == null){
            LOGGER.error("参数错误storeId={},ruleType={},specifiedVal={}" , storeId ,ruleType,specifiedVal);
            return false;
        }

        RuleTypeEnum ruleTypeEnum = RuleTypeEnum.getByVal(ruleType);
        if(ruleTypeEnum == null){
            LOGGER.error("规则类型不存在,ruleType={}" , ruleType);
            return false;
        }

        List<StoreRuleRelation>  storeRuleRelations = storeRuleRelationService.findByStoreIdAndRuleType(storeId , ruleType);

        if(CollectionUtils.isEmpty(storeRuleRelations)){
            return true;
        }

        for (StoreRuleRelation storeRuleRelation : storeRuleRelations){
            String exp = storeRuleRelation.getExp();
            if (StringUtils.isEmpty(exp)){
                LOGGER.error("门店规则表达式为空:{}" , storeRuleRelation);
                return false;
            }

            RuleExpEnum ruleExpEnum = RuleExpEnum.getByVal(exp);
            if(ruleExpEnum == null){
                LOGGER.error("门店规则表达式不存在:{}" , exp);
                return false;
            }


            String val = storeRuleRelation.getVal();
            T convertedVal = typeConvertFun.apply(val);
            return  ruleExpEnum.execute(specifiedVal , convertedVal);

        }
        return false;
    }


}
