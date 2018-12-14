package test.com.slst.mq;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.slst.common.mq.producer.DefaultProducerService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-27 15:26
 */
public class TestProducer extends BaseTest {
    @Resource
    private DefaultProducerService defaultProducerService;

    @Test
    public void sendMsg(){

        try {
           // defaultProducerService.sendMsg("test" , "hello slst");
            defaultProducerService.sendMsg("MY_CONTACTS_CALLBACK_TEMP1", JSON.toJSONString(Maps.newHashMap()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
