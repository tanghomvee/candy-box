package com.slst.report.dao.impl;

import com.google.common.collect.Maps;
import com.slst.common.dao.JpaDaoSupport;
import com.slst.common.enums.YNEnum;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.CustomerStoreStatsDaoExt;
import com.slst.report.dao.model.CustomerStoreStats;

import java.util.Date;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-06-12 18:52
 */
public class CustomerStoreStatsDaoExtImpl extends JpaDaoSupport<CustomerStoreStats , Long> implements CustomerStoreStatsDaoExt {
    @Override
    public Pager findByDateSlotAndStoreId(Long storeId, Date startTime, Date endTime, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT * FROM t_customer_store_stats where yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(startTime != null){
            sql.append(" AND UNIX_TIMESTAMP(createTime) >= :lowerCreateTime ");
            params.put("lowerCreateTime" , startTime.getTime() / 1000);
        }
        if(endTime != null){
            sql.append(" AND UNIX_TIMESTAMP(createTime) <= :upperCreateTime");
            params.put("upperCreateTime" , endTime.getTime() / 1000);
        }

        if(storeId != null){
            sql.append(" AND storeId= :storeId");
            params.put("storeId" , storeId);
        }
        sql.append(" order by mobile desc , createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , CustomerStoreStats.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定日期段到店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
