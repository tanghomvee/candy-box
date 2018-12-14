package test.com.slst.task;

import com.slst.report.task.SlstTask;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-21 11:13
 */
public class TestTask extends BaseTest {
    @Resource(name = "customerStoreStatsTask")
    private SlstTask customerStoreStatsTask;

    @Resource(name = "storeCustomerStatsTask")
    private SlstTask storeCustomerStatsTask;

    @Test
    public void testCustomerStoreStatsTask(){
        customerStoreStatsTask.execute();
    }

    @Test
    public void testStoreCustomerStatsTask(){
        storeCustomerStatsTask.execute();
    }
}
