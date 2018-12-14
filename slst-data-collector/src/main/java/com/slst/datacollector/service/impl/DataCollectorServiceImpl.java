package com.slst.datacollector.service.impl;

import com.slst.acct.service.AccountService;
import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.dao.model.Nic;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.*;
import com.slst.common.service.NicService;
import com.slst.common.service.RedisMsgDelegate;
import com.slst.common.service.SoundToothService;
import com.slst.common.service.SysCfgService;
import com.slst.common.web.vo.Msg;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.dao.model.CustomerInterest;
import com.slst.customer.dao.model.MobileDevice;
import com.slst.customer.service.CustomerInterestService;
import com.slst.customer.service.CustomerService;
import com.slst.customer.service.MobileDeviceService;
import com.slst.datacollector.service.DataCollectorService;
import com.slst.datacollector.web.vo.WIFIReportVO;
import com.slst.device.dao.model.Device;
import com.slst.device.service.DeviceService;
import com.slst.report.dao.model.StoreCustomer;
import com.slst.report.service.StoreCustomerService;
import com.slst.vender.dao.model.Store;
import com.slst.vender.dao.model.StoreRuleRelation;
import com.slst.vender.service.StoreRuleRelationService;
import com.slst.vender.service.StoreRuleService;
import com.slst.vender.service.StoreService;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
@Service("dataCollectorService")
public class DataCollectorServiceImpl implements DataCollectorService , RedisMsgDelegate {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisComponent redisComponent;
    @Resource
    private CustomerService customerService;

    @Resource
    private StoreCustomerService storeCustomerService;

    @Resource
    private StoreRuleService storeRuleService;

    @Resource
    private StoreRuleRelationService storeRuleRelationService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private StoreService storeService;

    @Resource
    private SoundToothService soundToothService;

    @Resource
    private MobileDeviceService customerDeviceService;

    @Resource
    private SysCfgService sysCfgService;

    @Resource
    private NicService nicService;

    @Resource
    private CustomerInterestService customerInterestService;

    @Resource
    private AccountService accountService;

    @Resource(name = "threadPoolTaskExecutor")
    private AsyncTaskExecutor asyncTaskExecutor;

    @Override
    public void handleMessage(String message) {
        LOGGER.info("expire key type String========================{}" , message);
        if(StringUtils.isEmpty(message)){
            return;
        }
        if (!message.startsWith(RedisKey.STORE_CUSTOMER)){
            return;
        }

        String[] strs = message.split(SeparatorEnum.UNDERLINE.getVal());
        if(strs == null || strs.length != 4){
            LOGGER.error("客户到店默认过期时间KEY错误:{}",message);
            return;
        }
        String storeId = strs[2];
        String mac = strs[3];

        try{

            Integer rs = storeCustomerService.modifyRecencyLeaveTimeByMacAndStoreId(mac , Long.valueOf(storeId));
            if(rs < 1){
                LOGGER.warn("修改客户离店时间失败,storeId={},mac={},rs={}" , storeId , mac ,rs);
            }
        }catch (Exception ex){
            LOGGER.error("修改客户离店时间异常,storeId={},mac={}" , storeId , mac ,ex);
        }

    }

    @Override
    public void handleMessage(Map message) {
        LOGGER.info("expire key type Map========================{}" , message);
    }

    @Override
    public void handleMessage(byte[] message) {
        LOGGER.info("expire key type byte[]========================{}" , message);
    }

    @Override
    public void handleMessage(Serializable message) {
        LOGGER.info("expire key type Serializable========================{}" , message);
        this.handleMessage(message.toString());
    }

    @Override
    public void handleMessage(Serializable message, String channel) {
        LOGGER.info("expire key type Serializable & channel========================{}" , message);
        this.handleMessage(message.toString());
    }

    @Override
    public Msg doCheck(WIFIReportVO wifiReportVO) {
        String deviceMac = wifiReportVO.getSta();
        String redisKey = RedisKey.DEVICE  + SeparatorEnum.UNDERLINE.getVal() + deviceMac;
        Long expire = 3600L;
        redisComponent.set(redisKey , (System.currentTimeMillis() / 1000) + "" , expire);
        return Msg.success();
    }

    @Override
    public Msg doCfgWIFI(WIFIReportVO wifiReportVO) {
        return Msg.success();
    }

    @Override
    public Msg doAP(WIFIReportVO wifiReportVO) {
        return Msg.success();
    }

    @Override
    public Msg doWifiReportedData(WIFIReportVO wifiReportVO) {

        String deviceMac = wifiReportVO.getSta();

        Device device = deviceService.findByMac(deviceMac);

        if(null== device){
            LOGGER.warn("设备mac={},storeId={}不可使用:{}" , device,deviceMac);
            return Msg.error("未找到设备");
        }
        Long storeId=device.getStoreId();

        Double A = Double.valueOf(device.getAVal());
        Double N = Double.valueOf(device.getNVal());

        Map<String ,ArrayList<Integer>>  macRssi =  wifiReportVO.getMacRssi();

        //获取门店的限制规则
        //门店默认客户逗留时间单位分钟
        SysCfg sysCfg =  sysCfgService.findByCode(SysCfgEnum.SYS_DEFAULT_CUSTOMER_STAY_TIME.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("系统未配置默认客户逗留时间");
            return  Msg.error();
        }
        Integer storeCustomerExpire = Integer.valueOf(sysCfg.getCodeVal().trim());
        List<StoreRuleRelation> storeRuleRelations = storeRuleRelationService.findByStoreIdAndRuleType(Long.valueOf(storeId) , RuleTypeEnum.RULE_TYPE_ST_CUST_STAY_TIME.getVal());
        if(CollectionUtils.isNotEmpty(storeRuleRelations)){
            StoreRuleRelation storeRuleRelation = storeRuleRelations.get(0);
            if(StringUtils.isEmpty(storeRuleRelation.getVal())){
                LOGGER.warn("门店客户默认已离开的时间限制规则未配置值,storeId={},ruleType={}" , storeId , RuleTypeEnum.RULE_TYPE_ST_CUST_STAY_TIME);
            }else{
                storeCustomerExpire = Integer.valueOf(storeRuleRelation.getVal());
            }
        }
        Store store = storeService.findStoreById(Long.valueOf(storeId));

        //门店调用外部接口获取手机号的次数
        //1.入客户信息
        for (String mac : macRssi.keySet()){

            if(!nicService.isMobileMac(mac)){
                LOGGER.warn("门店设备采集的mac是非移动设备:storeId={},deviceMac={},clientMac={}" , storeId , deviceMac , mac);
                continue;
            }
            List<Integer> rssi = macRssi.get(mac);
            //平均数
            double argv = rssi.stream().mapToInt(Integer :: intValue).average().getAsDouble();
            //幂值
            double pow = (argv - A) / N;
            //距离
            double distance=Math.pow(10, pow);
            LOGGER.info("门店:{},收集设备:{},客户设备:{},平均信号值:{},幂值:{},距离:{}" ,storeId , deviceMac, mac ,argv , pow ,distance);
            if(device.getDistance() == null ||  distance > device.getDistance()){
                LOGGER.warn("门店:{},收集设备:{},客户设备:{},平均信号值:{},幂值:{},距离:{},大于门店给定阈值:{}" ,storeId , deviceMac, mac ,argv , pow ,distance,device.getDistance());
                continue;
            }


            String redisKey = RedisKey.STORE_CUSTOMER + SeparatorEnum.UNDERLINE.getVal() + storeId + SeparatorEnum.UNDERLINE.getVal() + mac;


            String val = redisComponent.get(redisKey);

            if(StringUtils.isEmpty(val)){
                doWifiReportedData(mac  , store);
                //TODO 发送MQ有新用户到此门店
            }
            redisComponent.set(redisKey , (System.currentTimeMillis() / 1000) + "", storeCustomerExpire * 60 );
        }

        return Msg.success();
    }


    void buildWifiReportedData(String clientMac , Store store){

        try{
            //1.先判断是否已达到限制
            String redisKey = RedisKey.STORE_RULE_ST_PH_LIMIT + SeparatorEnum.UNDERLINE.getVal() + store.getId() ;
            Long limit = redisComponent.getAtomicLong(redisKey);
            if(limit == null || limit < 0){
                limit = 0L;
            }

            boolean rs = storeRuleService.execute(store.getId(), RuleTypeEnum.RULE_TYPE_ST_PH_LIMIT.getVal(), limit.intValue(), (String param) -> Integer.valueOf(param));
            if(!rs){
                LOGGER.warn("门店调用外部接口获取手机号码的的次数限制规则已达上限,storeId={}" , store.getId());
                return;
            }

            //构建storeCustomer信息
            CompletableFuture<StoreCustomer> storeCustomerCompletableFuture = CompletableFuture.supplyAsync(() ->{
                StoreCustomer storeCustomer = new StoreCustomer();
                storeCustomer.setStoreId(store.getId());
                storeCustomer.setVenderId(store.getVenderId());
                storeCustomer.setStoreName(store.getStoreName());
                storeCustomer.setVenderId(store.getVenderId());
                storeCustomer.setVenderName(store.getVenderName());
                storeCustomer.setCreator("sys");
                storeCustomer.setArriveTime(new Date());
                storeCustomer.setMac(clientMac);
                return  storeCustomerService.save(storeCustomer);
            } , asyncTaskExecutor);


            //构建设备信息
            CompletableFuture<MobileDevice> mobileDeviceCompletableFuture = CompletableFuture.supplyAsync(() ->{
                return this.buildDeviceInfo(redisKey ,clientMac);
            } , asyncTaskExecutor);

            //构建客户信息
            CompletableFuture<Customer> customerCompletableFuture =  mobileDeviceCompletableFuture.thenApplyAsync(mobileDevice -> {
                Customer customer = new Customer();
                if (StringUtils.isEmpty(mobileDevice.getMobile())){
                    customer.setMac(clientMac);
                    customer.setCreator("sys");
                }else {
                    //手机号存在才调用客户信息匹配
                    customer = this.buildCustomerBaseInfo(redisKey, clientMac);
                    if (customer != null && !StringUtils.isEmpty(customer.getInterest())) {
                        CustomerInterest customerInterest = new CustomerInterest();
                        customerInterest.setInterest(customer.getInterest());
                        customerInterest.setCustomerId(customer.getId());
                        customerInterest.setMac(customer.getMac());
                        customerInterest.setMobile(customer.getMobile());
                        customerInterest.setCreator("sys");
                        customerInterestService.save(customerInterest);
                    }
                    customer.setMobile(mobileDevice.getMobile());
                }
                if (!StringUtils.isEmpty(mobileDevice.getThirdPartyId())){
                    customer.setThirdPartyId(mobileDevice.getThirdPartyId());
                }
                customer.setDeviceId(mobileDevice.getId());
                customer = customerService.save(customer);
                return customer;
            }, asyncTaskExecutor);




            CompletableFuture<Boolean> resultFuture = customerCompletableFuture.thenCombineAsync(storeCustomerCompletableFuture , (customer,storeCustomer) ->{
                if (customer == null){
                    LOGGER.error("处理客户信息失败:mac={}" , clientMac );
                    return Boolean.FALSE;
                }
                if (storeCustomer == null){
                    LOGGER.error("处理客户记录信息失败:mac={}" , clientMac);
                    return Boolean.FALSE;
                }

                storeCustomer.setCustomerId(customer.getId());
                storeCustomer.setMobile(customer.getMobile());
                storeCustomerService.save(storeCustomer);
                return Boolean.TRUE;
            },asyncTaskExecutor).exceptionally(throwable -> {
                LOGGER.error("处理客户、设备、客户记录信息异常:{}" , clientMac , throwable);
                return Boolean.FALSE;
            }).whenComplete((result, throwable) -> {
                if (result){
                    LOGGER.info("处理客户、设备、客户记录信息成功:mac={},store={}" , clientMac , store.getId());
                }else {
                    LOGGER.info("处理客户、设备、客户记录信息失败:mac={},store={}" , clientMac , store.getId());
                }
            });

        }catch (Exception ex){
            LOGGER.error("处理客户、设备、客户记录信息异常:mac={},store={}" , clientMac , store.getId() , ex);
        }

    }

    private MobileDevice  buildDeviceInfo(String redisKey ,String clientMac){

        MobileDevice mobileDevice = null;
        List<MobileDevice> mobileDevices = customerDeviceService.findByMac(clientMac);
        boolean callDeviceApi = false;
        if (CollectionUtils.isEmpty(mobileDevices)){
            callDeviceApi = true;
            mobileDevice = new MobileDevice();
            mobileDevice.setMac(clientMac);
            mobileDevice.setCreator("sys");
        }else{
            mobileDevice = mobileDevices.get(0);
            if (!StringUtils.isEmpty(mobileDevice.getMobile())){
                return mobileDevice;
            }
            mobileDevice.setChanger("sys");
            mobileDevice.setChangeTime(new Date());
            callDeviceApi = true;
        }

        if (!callDeviceApi){
            return customerDeviceService.save(mobileDevice);
        }


        Long expire = Long.valueOf(24*3600 - DateTime.now().getSecondOfDay());
        expire = expire >=0 ? expire : 0L;
        //2.如果未达到限制,先将redis对应的值自增1,再调用外部接口
        Map<String,Object> data = null;
        String pid = null;
        try{
            redisComponent.incr(redisKey , expire);
            Msg msg = soundToothService.findCustomerDeviceByMac(clientMac);
            if(msg.isSuccess()){
                data = (Map<String, Object>) msg.getData();
            }else{
                LOGGER.warn("获取手机设备信息失败:{},msg={}",clientMac , msg);
            }
            if (data == null){
                LOGGER.warn("获取手机设备信息失败:{}",clientMac);
            } else {
                Boolean charge = (Boolean) data.get(SoundToothService.SOUND_TOOTH_CHARGE);
                if(charge != null && charge){
                    //收费
                    //获取admin手机匹配费用
                    SysCfg sysCfg= sysCfgService.findByCode(SysCfgEnum.SYS_SOUND_TOOTH_SMS_FEE.getVal());

                    Long amount=Long.parseLong(sysCfg.getCodeVal());

                    accountService.costOfAdmin("sys",amount,TransTypeBEnum.PHONE_MATCH);
                }
                String imeiMd5 = (String) data.get(SoundToothService.SOUND_TOOTH_RESULT_IMEI);
                if (StringUtils.isEmpty(imeiMd5)){
                    LOGGER.warn("非手机设备:{}",clientMac);
                }
                String phone = (String) data.get(SoundToothService.SOUND_TOOTH_RESULT_PHONE);
                pid = (String) data.get(SoundToothService.SOUND_TOOTH_RESULT_PHONE_ID);
                mobileDevice.setImeiMd5(imeiMd5);
                mobileDevice.setMobile(phone);
                if (StringUtils.isEmpty(phone)){
                    LOGGER.warn("获取手机设备信息无是手机号码:{}" , clientMac);
                }
            }
        }catch (Exception ex){
            LOGGER.error("获取客户手机信息异常,mac={}" , clientMac , ex);
        }finally {
            //3.如果调用失败，将redis对应的值减1 ,然后 return
            if (data == null){
                redisComponent.dncr(redisKey , expire);
            }
        }


        mobileDevice = customerDeviceService.save(mobileDevice);
        mobileDevice.setThirdPartyId(pid);
        return mobileDevice;
    }

    private Customer buildCustomerBaseInfo(String redisKey ,String clientMac){

        //1.判断客户信息是否存在
        List<Customer> customers = customerService.findByMac(clientMac);
        Customer customer = new Customer();
        boolean callCustomerApi = false;
        //如果此用户不存在
        if (CollectionUtils.isEmpty(customers)){
            callCustomerApi = true;
            customer.setMac(clientMac);
            customer.setCreator("sys");
            List<Nic> nics = nicService.findByMac(clientMac);
            if(!CollectionUtils.isEmpty(nics)){
                String brand = nics.get(0).getCorporationCn();
                customer.setMobileBrand(brand);
            }
        }else {
            customer = customers.get(0);
            //如果此用户不存在并未获取客户信息
            if(!StringUtils.isEmpty(customer.getThirdPartyId())){
                return customer;
            }
            customer.setChanger("sys");
            customer.setChangeTime(new Date());
            callCustomerApi = true;
        }
        if (!callCustomerApi){
            return customerService.save(customer);
        }

        Long expire = Long.valueOf(24*3600 - DateTime.now().getSecondOfDay());
        expire = expire >=0 ? expire : 0L;
        //2.如果未达到限制,先将redis对应的值自增1,再调用外部接口

        Map<String ,String> baseInfo = null;
        String interest = null;
        try{
            redisComponent.incr(redisKey , expire);
            Msg msg = soundToothService.findCustomerBaseInfoByMac(clientMac);
            if(msg.isSuccess()){
                baseInfo = (Map<String, String>) msg.getData();
                customer.setAgeSlot(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_AGE , null));
                customer.setCar(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CAR , null));
                customer.setCareer(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CAREER , null));
                customer.setEducation(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_EDUCATION , null));
                customer.setIncomeSlot(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_INCOME , null));
                customer.setChildren(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CHILDREN , null));
                //customer.setMobileBrand(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_BRAND , null));
                String gender = baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_GENDER , null);
                GenderEnum genderEnum = GenderEnum.getByDesc(gender);
                customer.setSex(genderEnum == null ? null : genderEnum.getVal());
                customer.setPermCity(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CITY , null));
                customer.setMdLevel(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_MODEL_LEVEL , null));
                customer.setMarried(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_MARRIED, null));
                customer.setHouse(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_HOUSE, null));
                interest = baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_SEGMENT , null);
                Boolean charge = Boolean.valueOf(baseInfo.get(SoundToothService.SOUND_TOOTH_CHARGE));
                if(charge != null && charge){
                    //收费
                    //获取admin手机匹配费用
                    SysCfg sysCfg= sysCfgService.findByCode(SysCfgEnum.SYS_SOUND_TOOTH_MATCH_PORT_FEE.getVal());
                    Long amount=Long.parseLong(sysCfg.getCodeVal());
                    accountService.costOfAdmin("sys",amount,TransTypeBEnum.TAG_MATCH);
                }


            }else{
                LOGGER.error("获取客户画像信息失败:mac={},msg={}" , customer.getMac() , msg);
            }

        }catch (Exception ex){
            LOGGER.error("获取客户画像信息异常:mac = {}" , customer.getMac() , ex);
        }finally {
            if (baseInfo == null){
                redisComponent.dncr(redisKey , expire);
            }
        }
        customer = customerService.save(customer);
        customer.setInterest(interest);
        return customer;
    }

/*************************************************************老版本****************************************************************************************/
    @Async
    StoreCustomer doWifiReportedData(String clientMac , Store store){

        //1.判断客户信息是否存在
        List<Customer> customers = customerService.findByMac(clientMac);
        Customer customer = new Customer();
        MobileDevice mobileDevice = new MobileDevice();
        StoreCustomer storeCustomer = new StoreCustomer();

        boolean callApi = false;
        //如果此用户不存在
        if (!CollectionUtils.isEmpty(customers)){
            customer = customers.get(0);
            //如果此用户不存在并未获取客户信息
            if(StringUtils.isEmpty(customer.getThirdPartyId())){
                customer.setChanger("sys");
                customer.setChangeTime(new Date());
                callApi = true;
                mobileDevice = customerDeviceService.findById(customer.getDeviceId());
                if (mobileDevice != null){
                    mobileDevice.setChanger("sys");
                    mobileDevice.setChangeTime(new Date());
                }else {
                    mobileDevice = new MobileDevice();
                }
            }
        }else {
            callApi = true;
        }



        if (callApi){
            mobileDevice.setMac(clientMac);
            mobileDevice.setCreator("sys");

            customer.setMac(clientMac);
            customer.setCreator("sys");

            //调用外部接口获取客户信息
            //1.先判断是否已达到限制
            String redisKey = RedisKey.STORE_RULE_ST_PH_LIMIT + SeparatorEnum.UNDERLINE.getVal() + store.getId() ;
            String interest = null;
            Long limit = redisComponent.getAtomicLong(redisKey);
            if(limit == null || limit < 0){
                limit = 0L;
            }

            boolean rs = storeRuleService.execute(store.getId(), RuleTypeEnum.RULE_TYPE_ST_PH_LIMIT.getVal(), limit.intValue(), (String param) -> Integer.valueOf(param));
            if(!rs){
                LOGGER.warn("门店调用外部接口获取手机号码的的次数限制规则已达上限,storeId={}" , store.getId());
            }else{
                //2.如果未达到限制,先将redis对应的值自增1,再调用外部接口
                //3.如果调用失败，将redis对应的值减1 ,然后 return
                interest = this.buildDeviceInfo(redisKey , customer , mobileDevice);

            }
            mobileDevice = customerDeviceService.save(mobileDevice);
            customer.setDeviceId(mobileDevice.getId());
            customer = customerService.save(customer);
            if(!StringUtils.isEmpty(interest)){
                CustomerInterest customerInterest = new CustomerInterest();
                customerInterest.setInterest(interest);
                customerInterest.setCustomerId(customer.getId());
                customerInterest.setMac(customer.getMac());
                customerInterest.setMobile(customer.getMobile());
                customerInterest.setCreator("sys");
                customerInterestService.save(customerInterest);
            }

        }
        //构建storeCustomer信息
        storeCustomer.setCustomerId(customer.getId());
        storeCustomer.setStoreId(store.getId());
        storeCustomer.setVenderId(store.getVenderId());
        storeCustomer.setStoreName(store.getStoreName());
        storeCustomer.setVenderId(store.getVenderId());
        storeCustomer.setVenderName(store.getVenderName());

        storeCustomer.setArriveTime(new Date());
        storeCustomer.setMobile(customer.getMobile());
        storeCustomer.setMac(clientMac);
        storeCustomer.setCreator("sys");
        return storeCustomerService.save(storeCustomer);

  }


  private String  buildDeviceInfo(String redisKey ,Customer customer , MobileDevice mobileDevice){

        String clientMac = customer.getMac();
        Long expire = Long.valueOf(24*3600 - DateTime.now().getSecondOfDay());
        expire = expire >=0 ? expire : 0L;
       //2.如果未达到限制,先将redis对应的值自增1,再调用外部接口
        redisComponent.incr(redisKey , expire);
        Map<String,Object> data = null;
        try{
            Msg msg = soundToothService.findCustomerDeviceByMac(clientMac);
            if(msg.isSuccess()){
                data = (Map<String, Object>) msg.getData();
            }else{
                LOGGER.warn("获取手机设备信息失败:{},msg={}",clientMac , msg);
            }
            if (data == null){
                LOGGER.warn("获取手机设备信息失败:{}",clientMac);
            } else {
                Boolean charge = (Boolean) data.get(SoundToothService.SOUND_TOOTH_CHARGE);
                if(charge != null && charge){
                    //TODO 收费
                    //获取admin手机匹配费用
                    SysCfg sysCfg= sysCfgService.findByCode(SysCfgEnum.SYS_SOUND_TOOTH_SMS_FEE.getVal());

                    Long amount=Long.parseLong(sysCfg.getCodeVal());

                    accountService.costOfAdmin("sys",amount,TransTypeBEnum.PHONE_MATCH);
                }
                String imeiMd5 = (String) data.get(SoundToothService.SOUND_TOOTH_RESULT_IMEI);
                if (StringUtils.isEmpty(imeiMd5)){
                    LOGGER.warn("非手机设备:{}",clientMac);
                }
                String phone = (String) data.get(SoundToothService.SOUND_TOOTH_RESULT_PHONE);
                String pid = (String) data.get(SoundToothService.SOUND_TOOTH_RESULT_PHONE_ID);
                mobileDevice.setImeiMd5(imeiMd5);
                mobileDevice.setMobile(phone);
                customer.setMobile(phone);
                customer.setThirdPartyId(pid);
                if (StringUtils.isEmpty(phone)){
                    LOGGER.warn("获取手机设备信息无是手机号码:{}" , clientMac);
                   return null;
                }
                return  this.buildCustomerBaseInfo(customer);
            }
        }catch (Exception ex){
            LOGGER.error("获取客户手机信息异常,mac={}" , clientMac , ex);
        }


       //3.如果调用失败，将redis对应的值减1 ,然后 return
        if (data == null){
            redisComponent.dncr(redisKey , expire);
        }
        return null;
  }
  private String  buildCustomerBaseInfo(Customer customer){
        try{
            Msg msg = soundToothService.findCustomerBaseInfoByMac(customer.getMac());
            if(msg.isSuccess()){
                Map<String ,String> baseInfo = (Map<String, String>) msg.getData();
                customer.setAgeSlot(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_AGE , null));
                customer.setCar(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CAR , null));
                customer.setCareer(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CAREER , null));
                customer.setEducation(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_EDUCATION , null));
                customer.setIncomeSlot(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_INCOME , null));
                customer.setChildren(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CHILDREN , null));
                List<Nic> nics = nicService.findByMac(customer.getMac());
                if(!CollectionUtils.isEmpty(nics)){
                    String brand = nics.get(0).getCorporationCn();
                    //customer.setMobileBrand(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_BRAND , null));
                    customer.setMobileBrand(brand);
                }
                String gender = baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_GENDER , null);
                GenderEnum genderEnum = GenderEnum.getByDesc(gender);
                customer.setSex(genderEnum == null ? null : genderEnum.getVal());
                customer.setPermCity(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_CITY , null));
                customer.setMdLevel(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_MODEL_LEVEL , null));
                customer.setMarried(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_MARRIED, null));
                customer.setHouse(baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_HOUSE, null));
                Boolean charge = Boolean.valueOf(baseInfo.get(SoundToothService.SOUND_TOOTH_CHARGE));
                if(charge != null && charge){
                    //TODO 收费

                    //获取admin手机匹配费用
                    SysCfg sysCfg= sysCfgService.findByCode(SysCfgEnum.SYS_SOUND_TOOTH_MATCH_PORT_FEE.getVal());

                    Long amount=Long.parseLong(sysCfg.getCodeVal());

                    accountService.costOfAdmin("sys",amount,TransTypeBEnum.TAG_MATCH);
                }
                String interest = baseInfo.getOrDefault(SoundToothService.SOUND_TOOTH_MATCH_PORT_RESULT_SEGMENT , null);
                return interest;
            }else{
                LOGGER.error("获取客户画像信息失败:mac={},msg={}" , customer.getMac() , msg);
            }

        }catch (Exception ex){
            LOGGER.error("获取客户画像信息异常:mac = {}" , customer.getMac() , ex);
        }
        return null;
  }

}
