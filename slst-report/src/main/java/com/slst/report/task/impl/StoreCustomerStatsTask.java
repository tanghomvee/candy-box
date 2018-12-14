package com.slst.report.task.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SysCfgService;
import com.slst.common.utils.DateUtils;
import com.slst.common.web.vo.Pager;
import com.slst.report.dao.model.StoreCustomerStats;
import com.slst.report.service.StoreCustomerService;
import com.slst.report.service.StoreCustomerStatsService;
import com.slst.report.task.SlstTask;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description 统计每个门店每天的客户数据
 * @date 2018-05-21 10:05
 */
@Component("storeCustomerStatsTask")
public class StoreCustomerStatsTask implements SlstTask {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private StoreCustomerService storeCustomerService;

    @Resource
    private StoreCustomerStatsService storeCustomerStatsService;

    @Resource
    private SysCfgService sysCfgService;

    @Resource
    private RedisComponent redisComponent;

    @Value("${store.customer.stats.time}")
    private String statsTime;

    @Override
    public void execute() {

        DateTime dateTime = DateTime.now().minusDays(1);
        if(!StringUtils.isEmpty(statsTime)){
            dateTime = DateTime.parse(statsTime , DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS);
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SYS_TASK_RETRY_STORE_CUSTOMER_STATS.getVal());
        Integer runNum = sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal()) ? 1 : Integer.parseInt(sysCfg.getCodeVal().trim());
        boolean running = runNum < 0;
        Integer excutedNum = 1;
        while (runNum >= excutedNum || running){
            try{
                if(redisComponent.lock(RedisKey.LOCK_STORE_CUSTOMER_STATS_TASK , 3600L)){
                    Integer cnt = storeCustomerStatsService.countByStatsTime(dateTime.toDate());
                    if(cnt != null && cnt > 0){
                        return;
                    }
                    run(dateTime);
                    break;
                }
            }catch (Exception ex){
                LOGGER.error("日期:{},每个店铺的客户数据统计第{}次异常" , DateUtils.date2Str(dateTime.toDate(),DateUtils.DateTimeFormatters.YYYY_MM_DD) , excutedNum, ex);
            }finally {
                redisComponent.unLock(RedisKey.LOCK_STORE_CUSTOMER_STATS_TASK);
            }
            excutedNum ++;
        }

    }

    private void run(DateTime dateTime) throws Exception {


        Date startTime = dateTime.withTime(0,0,0,0).toDate();
        Date endTime = dateTime.withTime(23,59,59,59).toDate();
        Integer pageSize = 10,pageNum =1;

        storeCustomerStatsService.deleteByStatsTime(dateTime.toDate());

        while (true){
            Pager pager = storeCustomerService.findStoreCustomerStatsByCreateTime(startTime , endTime, pageNum, pageSize );
            if (pager == null){
                throw  new Exception("每个店铺的客户数据统计失败");
            }
            List<StoreCustomerStats> storeCustomerStats = pager.getData();
            if(CollectionUtils.isEmpty(storeCustomerStats)){
                break;
            }

            storeCustomerStatsService.save(Lists.transform(storeCustomerStats, new Function<StoreCustomerStats, StoreCustomerStats>() {
                @Override
                public StoreCustomerStats apply(StoreCustomerStats input) {
                    input.setId(null);
                    input.setCreateTime(DateTime.now().toDate());
                    input.setYn(YNEnum.YES.getVal());
                    input.setStatsTime(startTime);
                    Integer cnt = 0;
                    Double avg = 0D;
                    Object[] newer = storeCustomerService.findNewClientStatsByStoreIdAndCreateTime(input.getStoreId() , startTime);
                    if (newer != null && newer.length == 2){
                        cnt = ((BigInteger) newer[0]).intValue();
                        avg = ((BigDecimal) newer[1]).doubleValue();
                    }
                    input.setNewClientNum(cnt);
                    input.setNewClientStayAvgTime(avg);

                    cnt = 0;
                    avg = 0D;

                    Object[] older = storeCustomerService.findOldClientStatsByStoreIdAndCreateTime(input.getStoreId() , startTime );

                    if (older != null && older.length == 2){
                        cnt = ((BigInteger) older[0]).intValue();
                        avg = ((BigDecimal) older[1]).doubleValue();
                    }
                    input.setOldClientNum(cnt);
                    input.setOldClientStayAvgTime(avg);

                    return input;
                }
            }));
            pageNum ++;
        }
        LOGGER.info("日期:{},每个店铺的客户数据统计成功" , DateUtils.date2Str(dateTime.toDate(),DateUtils.DateTimeFormatters.YYYY_MM_DD));
    }
}
