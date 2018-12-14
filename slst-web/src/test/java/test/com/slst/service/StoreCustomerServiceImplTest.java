package test.com.slst.service;

import com.slst.common.enums.DimensionEnum;
import com.slst.common.utils.DateUtils;
import com.slst.common.web.vo.Pager;
import com.slst.customer.web.vo.CustomerVO;
import com.slst.report.service.StoreCustomerService;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 19:08
 */
public class StoreCustomerServiceImplTest extends BaseTest {

    @Resource
    private StoreCustomerService storeCustomerService;
    @Test
    public void modifyRecencyLeaveTimeByMacAndStoreId() throws Exception {
        System.out.println(storeCustomerService.modifyRecencyLeaveTimeByMacAndStoreId("mac" , 1L));
    }

    @Test
    public void save() {
        storeCustomerService.countStayingByTimeSlotAndStoreIdAndDimension(null , null , 12L , null , null ,DimensionEnum.AGE);
    }
    @Test
    public void findCustomerStoreStatsByCreateTime() {
    }

    @Test
    public void findStoreCustomerStatsByCreateTime() {
    }

    @Test
    public void findNewClientStatsByStoreIdAndCreateTime() {
    }

    @Test
    public void findOldClientStatsByStoreIdAndCreateTime() {
    }

    @Test
    public void countArrivedByHourAndStoreId() {
        Object obj = storeCustomerService.countArrivedByHourAndStoreId(new Date() , 12L);
        System.out.println(obj);
    }

    @Test
    public void countArrivedByTimeSlotAndStoreId() {
    }

    @Test
    public void countStayingByTimeSlotAndStoreId() {
    }
    @Test
    public void countStayingByTimeSlotAndStoreIdAndDimension() {
        Map<String , Integer>  data =  storeCustomerService.countStayingByTimeSlotAndStoreIdAndDimension(
                 DateUtils.str2DateTime("2018-06-20" ,DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS),
                        DateUtils.str2DateTime("2018-07-20" ,DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS) ,116L ,0 , 23 ,DimensionEnum.getByVal(1));

        System.out.println(data);
    }
    @Test
    public void countArrivedByTimeSlotAndStoreIdAndDimension() {
        Map<String , Integer>  data =  storeCustomerService.countArrivedByTimeSlotAndStoreIdAndDimension(
                 DateUtils.str2DateTime("2018-06-20" ,DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS),
                        DateUtils.str2DateTime("2018-07-20" ,DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS) ,116L ,0 , 23 ,DimensionEnum.getByVal(1));

        System.out.println(data);
    }

    @Test
    public void countStrangerByTimeSlotAndStoreIdAndStayTime() {
        Map<String , Integer>  data =  storeCustomerService.countStrangerByTimeSlotAndStoreIdAndDimension
                (5 , DateUtils.str2DateTime("2018-06-20" ,DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS),
                        DateUtils.str2DateTime("2018-07-20" ,DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS) ,116L ,0 , 23 ,DimensionEnum.getByVal(1));

        System.out.println(data);
    }

    @Test
    public void findByTimeSlotAndStoreId() {
        Pager pager = new Pager();
        pager =  storeCustomerService.findByTimeSlotAndStoreId(null, 7L, null, null, pager);
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(pager.getData().get(0) , customerVO);
        System.out.println(pager);
    }

    @Test
    public void findFirstTimeByStoreIdAndCustomerId() {
    }

    @Test
    public void findRecencyTimeByStoreIdAndCustomerId() {
    }

    @Test
    public void findByCustomerAndStoreId() {
    }
}