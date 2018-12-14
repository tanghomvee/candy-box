package com.slst.report.dao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slst.common.dao.JpaDaoSupport;
import com.slst.common.enums.DimensionEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.StoreCustomerDaoExt;
import com.slst.report.dao.model.CustomerStoreStats;
import com.slst.report.dao.model.StoreCustomer;
import com.slst.report.dao.model.StoreCustomerStats;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-21 10:13
 */
public class StoreCustomerDaoExtImpl extends JpaDaoSupport<StoreCustomer , Long> implements StoreCustomerDaoExt {
    @Override
    public Pager findCustomerStoreStatsByCreateTime(Date startTime, Date endTime, Integer yn, Integer pageNum, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql
        .append("SELECT ")
        .append("id ,")
        .append("yn,")
        .append("creator,")
        .append("createTime,")
        .append("changer,")
        .append("changeTime,")
        .append("customerId ,")
        .append("storeId ,")
        .append("venderId,")
        .append("storeName , ")
        .append("venderName , ")
        .append("mobile,")
        .append("mac,")
        .append("NULL AS statsTime , ")
        .append("count(id) AS comeTimes , ")
        .append("ifnull(sum(stayTime),0) AS stayTime , ")
        .append("ifnull(avg(stayTime),0) AS stayAvgTime ")
        .append(" FROM t_store_customer where 1=1 ");

        Map<String , Object> params = Maps.newHashMap();
        if(yn != null){
            sql.append(" AND yn=:yn");
            params.put("yn" , yn);
        }
        if(startTime != null){
            sql.append(" AND UNIX_TIMESTAMP(createTime) >= :lowerCreateTime ");
            params.put("lowerCreateTime" , startTime.getTime() / 1000);
        }
        if(endTime != null){
            sql.append(" AND UNIX_TIMESTAMP(createTime) <= :upperCreateTime");
            params.put("upperCreateTime" , endTime.getTime() / 1000);
        }
        sql.append(" group by storeId,customerId");

        try{

            Pager pager = super.doSQLPage(sql.toString() , params , CustomerStoreStats.class , pageNum ,pageSize);
            return pager;
        }catch (Exception ex){
            LOGGER.error("统计每个客户每天到每个店铺的数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public Pager findStoreCustomerStatsByCreateTime(Date startTime, Date endTime, Integer yn, Integer pageNum, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql
                .append("SELECT ")
                .append("id ,")
                .append("yn,")
                .append("creator,")
                .append("createTime,")
                .append("changer,")
                .append("changeTime,")

                .append("storeId ,")
                .append("venderId,")
                .append("storeName , ")
                .append("venderName , ")

                .append("NULL AS statsTime , ")
                .append("count(id) AS clientNum , ")
                .append("NULL AS newClientNum , ")
                .append("NULL AS oldClientNum , ")
                .append("NULL AS newClientStayAvgTime , ")
                .append("NULL AS oldClientStayAvgTime , ")
                .append("ifnull(avg(stayTime) ,0) AS stayAvgTime ")

                .append(" FROM t_store_customer where 1=1 ");

        Map<String , Object> params = Maps.newHashMap();
        if(yn != null){
            sql.append(" AND yn=:yn");
            params.put("yn" , yn);
        }
        if(startTime != null){
            sql.append(" AND UNIX_TIMESTAMP(createTime) >= :lowerCreateTime ");
            params.put("lowerCreateTime" , startTime.getTime() / 1000);
        }
        if(endTime != null){
            sql.append(" AND UNIX_TIMESTAMP(createTime) <= :upperCreateTime");
            params.put("upperCreateTime" , endTime.getTime() / 1000);
        }
        sql.append(" group by storeId");

        try{

            Pager pager = super.doSQLPage(sql.toString() , params , StoreCustomerStats.class , pageNum ,pageSize);
            return pager;
        }catch (Exception ex){
            LOGGER.error("统计各个门店的客户数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public Pager findByTimeSlotAndStoreId(String mom, Long storeId, Date startTime, Date endTime, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT *,count(customerId) AS nums FROM t_store_customer where yn=:yn ");

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

        if(!StringUtils.isEmpty(mom)){
            if(NumberUtils.isNumber(mom)){

                if(mom.length() < 3){
                    sql.append("  AND (mobile like :mobile OR mac like :mac) ");
                    params.put("mobile" , "%" + mom + "%");
                    params.put("mac" , "%" + mom + "%");
                }else{
                    sql.append(" AND mobile like :mobile");
                    params.put("mobile" , "%" + mom + "%");
                }
            }else{
                sql.append(" AND mac like :mac");
                params.put("mac" , "%" + mom + "%");
            }
        }

        sql.append(" group by customerId order by mobile desc , createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , HashMap.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定时间段到店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }


    @Override
    public Pager findByCustomerAndStore(Long customerId, Long storeId, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT * FROM t_store_customer where yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(!ObjectUtils.isEmpty(customerId)){
            sql.append(" AND customerId= :customerId ");
            params.put("customerId" , customerId);
        }
        if(!ObjectUtils.isEmpty(storeId)){
            sql.append(" AND storeId= :storeId ");
            params.put("storeId" , storeId);
        }

        sql.append(" order by id desc ");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , StoreCustomer.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定时间段到店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public Pager findArrivedCustomerByDayAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager) {

        StringBuffer sql = new StringBuffer("SELECT * FROM t_store_customer where leaveTime is not null  and yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(start != null){
            sql.append(" AND UNIX_TIMESTAMP(leaveTime) >= :start ");
            params.put("start" , start.getTime()/1000);
        }
        if(end != null){
            sql.append(" AND UNIX_TIMESTAMP(leaveTime) <= :end ");
            params.put("end" , end.getTime()/1000);
        }
        if (storeId != null && storeId > 0){
            sql.append(" AND storeId=:storeId ");
            params.put("storeId" , storeId);
        }

        if (startHour != null){
            sql.append(" AND date_format(leaveTime, '%H') >= :startHour ");
            params.put("startHour" , startHour);
        }

        if(endHour != null){
            sql.append(" AND date_format(leaveTime, '%H') <= :endHour ");
            params.put("endHour" , endHour);
        }


        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , StoreCustomer.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定时间段到过店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public Pager findStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager) {

        StringBuffer sql = new StringBuffer("SELECT * FROM t_store_customer where leaveTime is  null  and yn=:yn ");
        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(start != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) >= :start ");
            params.put("start" , start.getTime()/1000);
        }
        if(end != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) <= :end ");
            params.put("end" , end.getTime()/1000);
        }
        if (storeId != null && storeId > 0){
            sql.append(" AND storeId=:storeId ");
            params.put("storeId" , storeId);
        }

        if (startHour != null){
            sql.append(" AND date_format(arriveTime, '%H') >= :startHour ");
            params.put("startHour" , startHour);
        }

        if(endHour != null){
            sql.append(" AND date_format(arriveTime, '%H') <= :endHour ");
            params.put("endHour" , endHour);
        }

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , StoreCustomer.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定时间段正在门店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public Pager findStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT * FROM t_store_customer where leaveTime is not null  and yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(start != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) >= :start ");
            params.put("start" , start.getTime()/1000);
        }
        if(end != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) <= :end ");
            params.put("end" , end.getTime()/1000);
        }
        if (storeId != null && storeId > 0){
            sql.append(" AND storeId=:storeId ");
            params.put("storeId" , storeId);
        }

        if (startHour != null){
            sql.append(" AND date_format(arriveTime, '%H') >= :startHour ");
            params.put("startHour" , startHour);
        }

        if(endHour != null){
            sql.append(" AND date_format(arriveTime, '%H') <= :endHour ");
            params.put("endHour" , endHour);
        }
        if(stayTime != null){
            sql.append(" AND stayTime <= :stayTime ");
            params.put("stayTime" , stayTime);
        }

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , StoreCustomer.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定时间段路过的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public List<HashMap<String, String>> countStayingByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum byVal) {

        String dimension =  getDimension(byVal);

        if(StringUtils.isEmpty(dimension)){
            return Lists.newArrayList();
        }

        StringBuffer sql = new StringBuffer("select ");
        sql.append(dimension + " AS " + byVal.toString()).append(" , count(1) AS cnt from t_customer where yn = 1 and id in (SELECT customerId FROM t_store_customer where leaveTime is  null  and yn=:yn");


        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(startTime != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) >= :start ");
            params.put("start" , startTime.getTime()/1000);
        }
        if(endTime != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) <= :end ");
            params.put("end" , endTime.getTime()/1000);
        }
        if (storeId != null && storeId > 0){
            sql.append(" AND storeId=:storeId ");
            params.put("storeId" , storeId);
        }

        if (startHour != null){
            sql.append(" AND date_format(arriveTime, '%H') >= :startHour ");
            params.put("startHour" , startHour);
        }

        if(endHour != null){
            sql.append(" AND date_format(arriveTime, '%H') <= :endHour ");
            params.put("endHour" , endHour);
        }
        sql.append(" )");

        sql.append(" group by ").append(dimension);

        try{


            Query query = createLocalQuery(HashMap.class, sql.toString());

            if(!CollectionUtils.isEmpty(params)){
                for (String paramName : params.keySet()){
                    query.setParameter(paramName , params.get(paramName));
                }
            }
            return query.getResultList();
        }catch (Exception ex){
            LOGGER.error("统计特定时间段在店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    @Override
    public List<HashMap<String, String>> countArrivedByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension) {
        String dimensionStr =  getDimension(dimension);

        if(StringUtils.isEmpty(dimensionStr)){
            return Lists.newArrayList();
        }

        StringBuffer sql = new StringBuffer("select ");
        sql.append(dimensionStr + " AS " + dimension.toString()).append(" , count(1) AS cnt from t_customer where yn = 1 and id in (SELECT customerId FROM t_store_customer where leaveTime is not null  and yn=:yn ");


        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(startTime != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) >= :start ");
            params.put("start" , startTime.getTime()/1000);
        }
        if(endTime != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) <= :end ");
            params.put("end" , endTime.getTime()/1000);
        }
        if (storeId != null && storeId > 0){
            sql.append(" AND storeId=:storeId ");
            params.put("storeId" , storeId);
        }

        if (startHour != null){
            sql.append(" AND date_format(arriveTime, '%H') >= :startHour ");
            params.put("startHour" , startHour);
        }

        if(endHour != null){
            sql.append(" AND date_format(arriveTime, '%H') <= :endHour ");
            params.put("endHour" , endHour);
        }
        sql.append(" )");

        sql.append(" group by ").append(dimensionStr);

        try{


            Query query = createLocalQuery(HashMap.class, sql.toString());

            if(!CollectionUtils.isEmpty(params)){
                for (String paramName : params.keySet()){
                    query.setParameter(paramName , params.get(paramName));
                }
            }
            return query.getResultList();
        }catch (Exception ex){
            LOGGER.error("统计特定时间段路过到店的客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }


    @Override
    public List<HashMap<String, String>> findStrangerByTimeSlotAndStoreIdAndStayTimeAndDimension(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension) {

        String dimensionStr =  getDimension(dimension);

        if(StringUtils.isEmpty(dimensionStr)){
            return Lists.newArrayList();
        }

        StringBuffer sql = new StringBuffer("select ");
        sql.append(dimensionStr + " AS " + dimension.toString()).append(" , count(1) AS cnt from t_customer where yn = 1 and id in (SELECT customerId FROM t_store_customer where leaveTime is not null  and yn=:yn ");


        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if(start != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) >= :start ");
            params.put("start" , start.getTime()/1000);
        }
        if(end != null){
            sql.append(" AND UNIX_TIMESTAMP(arriveTime) <= :end ");
            params.put("end" , end.getTime()/1000);
        }
        if (storeId != null && storeId > 0){
            sql.append(" AND storeId=:storeId ");
            params.put("storeId" , storeId);
        }

        if (startHour != null){
            sql.append(" AND date_format(arriveTime, '%H') >= :startHour ");
            params.put("startHour" , startHour);
        }

        if(endHour != null){
            sql.append(" AND date_format(arriveTime, '%H') <= :endHour ");
            params.put("endHour" , endHour);
        }
        if(stayTime != null){
            sql.append(" AND stayTime <= :stayTime ");
            params.put("stayTime" , stayTime);
        }

        sql.append(" )");

        sql.append(" group by ").append(dimensionStr);

        try{

            Query query = createLocalQuery(HashMap.class, sql.toString());

            if(!CollectionUtils.isEmpty(params)){
                for (String paramName : params.keySet()){
                    query.setParameter(paramName , params.get(paramName));
                }
            }
            return query.getResultList();
        }catch (Exception ex){
            LOGGER.error("统计查询特定时间段路过的陌生客户异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }

    private String getDimension(DimensionEnum dimension){
        switch (dimension){
            case AGE:
                return  " ageSlot ";
            case CAREER:
                return " career ";
            case GENDER:
                return " sex ";
            default:
                return null;
        }
    }

}
