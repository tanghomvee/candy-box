package com.slst.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.slst.common.enums.DimensionEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.StoreCustomerDao;
import com.slst.report.dao.model.StoreCustomer;
import com.slst.report.service.StoreCustomerService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
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
 * @date 2018-05-19 15:17
 */
@Service("storeCustomerService")
public class StoreCustomerServiceImpl extends BaseServiceImpl<StoreCustomer , Long> implements StoreCustomerService {
    @Resource
    private StoreCustomerDao storeCustomerDao;
    @Override
    public StoreCustomer save(StoreCustomer storeCustomer) {
        return storeCustomerDao.save(storeCustomer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer modifyRecencyLeaveTimeByMacAndStoreId(String mac, Long storeId) {
        return storeCustomerDao.modifyRecencyLeaveTimeByMacAndStoreId(mac , storeId);
    }

    @Override
    public Pager findCustomerStoreStatsByCreateTime(Date startTime, Date endTime, Integer pageNum, Integer pageSize) {
        return storeCustomerDao.findCustomerStoreStatsByCreateTime(startTime,endTime, YNEnum.YES.getVal(), pageNum, pageSize );
    }

    @Override
    public Pager findStoreCustomerStatsByCreateTime(Date startTime, Date endTime, Integer pageNum, Integer pageSize) {
        return storeCustomerDao.findStoreCustomerStatsByCreateTime(startTime,endTime, YNEnum.YES.getVal(), pageNum, pageSize );
    }

    @Override
    public Object[] findNewClientStatsByStoreIdAndCreateTime(Long storeId, Date startTime) {
          return (Object[]) storeCustomerDao.findNewClientStatsByStoreIdAndCreateTime(storeId , startTime );
    }

    @Override
    public Object[] findOldClientStatsByStoreIdAndCreateTime(Long storeId, Date startTime) {
        return (Object[]) storeCustomerDao.findOldClientStatsByStoreIdAndCreateTime(storeId , startTime );
    }

    @Override
    public Map<Integer, Integer> countArrivedByHourAndStoreId(Date createTime ,Long storeId) {

        DateTime dateTime = new DateTime(createTime);
        dateTime = dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        List<Object> objects =  storeCustomerDao.countHourCustomersByCreateTimeAndStoreId(dateTime.toDate() , storeId);

        if(CollectionUtils.isEmpty(objects)){
            return null;
        }

        Map<Integer, Integer> retData = Maps.newHashMap();
        for (Object obj: objects){
            Object[] objs = (Object[]) obj;
            String hour = (String) objs[0];
            BigInteger val = (BigInteger) objs[1];
            retData.put(Integer.valueOf(hour) , val.intValue());
        }
        return retData;
    }

    @Override
    public Integer countArrivedByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour) {
        return storeCustomerDao.countArrivedCustomerByDayAndStoreId(start  , end , storeId, startHour,endHour );
    }

    @Override
    public Pager findArrivedByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager) {
        return storeCustomerDao.findArrivedCustomerByDayAndStoreId(start , end ,storeId ,startHour , endHour , pager);
    }

    @Override
    public Integer countStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour) {
        return storeCustomerDao.countStayingByTimeSlotAndStoreId(start  , end , storeId, startHour,endHour );
    }

    @Override
    public Pager findStayingByTimeSlotAndStoreId(Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager) {
        return storeCustomerDao.findStayingByTimeSlotAndStoreId(start , end ,storeId ,startHour ,endHour ,pager);
    }

    @Override
    public Integer countStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour) {
        return storeCustomerDao.countStrangerByTimeSlotAndStoreIdAndStayTime(stayTime , start  , end , storeId, startHour,endHour );
    }

    @Override
    public Pager findStrangerByTimeSlotAndStoreIdAndStayTime(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, Pager pager) {
        return storeCustomerDao.findStrangerByTimeSlotAndStoreIdAndStayTime(stayTime ,start ,end ,storeId ,startHour ,endHour , pager);
    }

    @Override
    public Pager findByTimeSlotAndStoreId(String mom, Long storeId, Date startTime, Date endTime, Pager pager) {
        Pager retPager = storeCustomerDao.findByTimeSlotAndStoreId(mom, storeId, startTime, endTime, pager);

        if(retPager != null && !CollectionUtils.isEmpty(retPager.getData())){
            String jsonStr = JSONObject.toJSONString(retPager.getData());
            List data = JSON.parseArray(jsonStr , StoreCustomer.class);
            retPager.setData(data);
        }

        return retPager;
    }

    @Override
    public StoreCustomer findFirstTimeByStoreIdAndCustomerId(Long storeId, Long customerId) {
        return storeCustomerDao.findFirstTimeByStoreIdAndCustomerId(storeId , customerId);
    }

    @Override
    public StoreCustomer findRecencyTimeByStoreIdAndCustomerId(Long storeId, Long customerId) {
        return storeCustomerDao.findRecencyTimeByStoreIdAndCustomerId(storeId , customerId);
    }

    @Override
    public Pager findByCustomerAndStoreId(Long customerId, Long storeId, Pager pager) {
        return storeCustomerDao.findByCustomerAndStore(customerId , storeId , pager);
    }

    @Override
    public Integer countByTimeSlotAndStoreId(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour) {
        return storeCustomerDao.countByTimeSlotAndStoreId(startTime  , endTime , storeId, startHour,endHour );
    }

    @Override
    public Map<String, Integer> countStayingByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum byVal) {

        Map<String , Integer> retMap = Maps.newHashMap();

        List<HashMap<String , String>> objects = storeCustomerDao.countStayingByTimeSlotAndStoreIdAndDimension(startTime  , endTime , storeId, startHour,endHour , byVal);
        if(CollectionUtils.isEmpty(objects)){
            return retMap;
        }

        for (HashMap<String , ?> obj : objects){
            BigInteger cnt = (BigInteger) obj.get("cnt");
            Object key = obj.get(byVal.toString());
            if(key == null){
                key = "未知";
            }
            retMap.put(key + "" , Integer.valueOf(cnt.intValue()));
        }

        return retMap;

    }

    @Override
    public Map<String, Integer> countArrivedByTimeSlotAndStoreIdAndDimension(Date startTime, Date endTime, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension) {
        Map<String , Integer> retMap = Maps.newHashMap();

        List<HashMap<String , String>> objects = storeCustomerDao.countArrivedByTimeSlotAndStoreIdAndDimension(startTime  , endTime , storeId, startHour,endHour , dimension);
        if(CollectionUtils.isEmpty(objects)){
            return retMap;
        }

        for (HashMap<String , ?> obj : objects){
            Object key = obj.get(dimension.toString());
            BigInteger cnt = (BigInteger) obj.get("cnt");
            if(key == null){
                key = "未知";
            }
            retMap.put(key + "" , Integer.valueOf(cnt.intValue()));
        }

        return retMap;
    }

    @Override
    public Map<String, Integer> countStrangerByTimeSlotAndStoreIdAndDimension(Integer stayTime, Date start, Date end, Long storeId, Integer startHour, Integer endHour, DimensionEnum dimension) {
        Map<String , Integer> retMap = Maps.newHashMap();

        List<HashMap<String , String>> objects = storeCustomerDao.findStrangerByTimeSlotAndStoreIdAndStayTimeAndDimension(stayTime , start  , end , storeId, startHour,endHour , dimension);
        if(CollectionUtils.isEmpty(objects)){
            return retMap;
        }

        for (HashMap<String , ?> obj : objects){

            Object key = obj.get(dimension.toString());
            if(key == null){
                key = "未知";
            }

            BigInteger cnt = (BigInteger) obj.get("cnt");

            retMap.put(key + "" , Integer.valueOf(cnt.intValue()));
        }

        return retMap;
    }
}
