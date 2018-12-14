package test.com.slst.service;

import com.slst.common.service.SoundToothService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 18:25
 */
public class SoundToothServiceImplTest extends BaseTest{
    @Resource
    private SoundToothService soundToothService;

    @Test
    public void findConsume() throws Exception {
        System.out.println(soundToothService.findAcct());

    } @Test
    public void findCustomerDeviceByMac() throws Exception {
        System.out.println(soundToothService.findCustomerDeviceByMac("a4:ca:a0:ab:e6:31"));

    }

    @Test
    public void createSMSTemplate() throws Exception {
        System.out.println(soundToothService.createSMSTemplate("测试标题" , "遇见黑科技" , "XX公司"));
    }

    @Test
    public void findSMSTemplateAuthStatus() throws Exception {
        System.out.println(soundToothService.findSMSTemplateAuthStatus("2644"));
    }

    @Test
    public void sendSMSMsg() throws Exception {
        System.out.println(soundToothService.sendSMSMsg("c45c47392e1a8fc86676839dae4270cf" , "2644"));
    }
    @Test
    public void findCustomerBaseInfoByMac() throws Exception {
        System.out.println(soundToothService.findCustomerBaseInfoByMac("aa:bb:cc:dd:ee:ff"));
    }
    @Test
    public void findSmsToken() throws Exception {
        System.out.println(soundToothService.findSmsToken());
    }
    @Test
    public void listPhoneNumByToken() throws Exception {
        System.out.println(soundToothService.listPhoneNumByToken());

    }
    @Test
    public void call() throws Exception {
        String to = "2c960586a866eb2082f3822fbac2bd1f";
        String from = "18123238902";
        System.out.println(soundToothService.call("028",to , from).getData());
    }

    @Test
    public void testMap(){
        Map<String,String> map=new HashMap<String,String>();
        map.put("pl_id","dddd");

        System.out.println(map.get("pl_id"));
    }

}