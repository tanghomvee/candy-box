package com.slst.market.mq.consumer;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.slst.common.constants.RedisKey;
import com.slst.common.enums.YNEnum;
import com.slst.common.mq.consumer.listeners.DefaultMessageListenerConcurrently;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.market.dao.model.SmsBox;
import com.slst.market.dao.model.SmsRecord;
import com.slst.market.service.SmsBoxService;
import com.slst.market.service.SmsRecordService;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Consts;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: daiyou.zhong
 * @description: 短信消息队列—消费
 * @create: 2018-09-07 11:13
 * @version: 2.0
 **/
@Component(value = "sMSSendListener")
public class SMSSendListener extends DefaultMessageListenerConcurrently {
    @Resource
    private SmsRecordService smsRecordService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CustomerService customerService;
    @Resource
    private SmsBoxService smsBoxService;


    @Transactional
    @Override
    public boolean consumeMessage(List<MessageExt> msgs) {
        if(CollectionUtils.isEmpty(msgs)){
            return true;
        }

//        if(true){
//            return true;
//        }
        for(MessageExt msg : msgs){
            //redisHash操作
            HashOperations customerHash = redisTemplate.opsForHash();
            //处理生产者传过来的参数
            String customerQueryJson = new String(msg.getBody(), Consts.UTF_8);
            Map<String, Object> customerQueryMap = (Map<String, Object>)JSONUtils.parse(customerQueryJson);
            //获取参数
            Long smsBoxId = Long.parseLong(customerQueryMap.get("smsBoxId").toString());
            LOGGER.info("MQ执行创建短信...，smsBoxId={}", smsBoxId);
            customerQueryMap.remove("smsBoxId");
            //商家短信单价
            Long smsRates = Long.parseLong(customerQueryMap.get("smsRates").toString());
            customerQueryMap.remove("smsRates");
            Map<String, Object> userMap = (Map<String, Object>)customerQueryMap.get("user");
            customerQueryMap.remove("user");
            UserVO userVO = JSON.parseObject(JSON.toJSONString(userMap), UserVO.class);
            Long storeId = userVO.getStoreId();
            String sql = customerQueryMap.get("sql").toString();
            customerQueryMap.remove("sql");
            String redisCustomerKey = RedisKey.LIST_CUSTOMER_ENTITY+smsBoxId;

            //定义与查询短信发件箱
            SmsBox smsBox = smsBoxService.findSmsBoxById(smsBoxId);
            if(ObjectUtils.isEmpty(smsBox)){
                LOGGER.error("执行MQ消费查询smsBox为空, id = {}", smsBoxId);
                return false;
            }

            //redis中取出客户数据
            Map<String, String> customerMap = customerHash.entries(redisCustomerKey);
            //查询短信记录
            Long smsRecordCount = smsRecordService.countBySmsBoxIdAndYn(smsBoxId, YNEnum.YES.getVal());
            //根据参数查询出客户数据
            List<Customer> customerList = customerService.listCustomerBySql(sql, customerQueryMap);
            if(CollectionUtils.isEmpty(customerList)){
                LOGGER.error("MQ没有查询到客户数据, sql = {}, param = {}", sql, customerQueryMap);
                return false;
            }
            LOGGER.info("判断是否第一次消费， smsBoxId = {}", smsBoxId);
            //如果redis中不存在客户记录，就判断短信记录中是否有记录。如果短信记录中没有记录说明是第一次消费，否则之前已经消费，是在结算的时候有异常
            if(MapUtils.isEmpty(customerMap)){
                //如果短信记录为空
                if(smsRecordCount == null || smsRecordCount == 0){
                    //把客户数据库为map放到redis中(用于防止客户丢失，当MQ成功消费一条数据，就从redis中删除这条数据)
                    customerMap = new HashMap<>(customerList.size());
                    for(Customer customer : customerList){
                        customerMap.put(customer.getId().toString(), JSON.toJSONString(customer));
                    }
                    customerHash.putAll(redisCustomerKey, customerMap);
                }
            //如果redis中存在客户记录，说明不是第一次消费。
            }else {
                //如果短信记录为空，说明是客户在存redis中的出错
                if(smsRecordCount == null || smsRecordCount == 0){
                    //判断redis中的客户是否与原客户一样，如果不一样，把redis中的客户删除，重新把客户插入redis中
                    if(customerMap.size() != customerList.size()){
                        //redis中删除所有的客户
                        redisTemplate.delete(redisCustomerKey);
                        //把客户数据库为map放到redis中(用于防止客户丢失，当MQ成功消费一条数据，就从redis中删除这条数据)
                        customerMap = new HashMap<>(customerList.size());
                        for(Customer customer : customerList){
                            customerMap.put(customer.getId().toString(), JSON.toJSONString(customer));
                        }
                        customerHash.putAll(redisCustomerKey, customerMap);
                    }
                }
            }
            LOGGER.info("开始短信发送， smsBoxId = {}", smsBoxId);
            //开始根据条件给客户发送短信
            if(!MapUtils.isEmpty(customerMap)){
                this.startSendSms(userVO, customerMap, smsBox, customerHash, redisCustomerKey);
            }
            LOGGER.info("开始短信费用结算， smsBoxId = {}", smsBoxId);
            //短信费用结算
            Msg endMsg = smsBoxService.endOfSendSmsByVenderOrStore(userVO, storeId, smsBoxId);
            if(!endMsg.isSuccess()) {
                LOGGER.error("短信发送完成，但endOfSendSmsByVenderOrStore()方法出错！");
            }
        }
        return true;
    }

    /**
     * 开始根据条件给客户发送短信
     * @param userVO 登陆信息
     * @param customerMap 客户数据Map（短信接收者）
     * @param smsBox 短信发件箱
     * @param customerHash redis操作
     * @param getCustomerKey redis中取客户的key值
     */
    protected void startSendSms(UserVO userVO, Map<String, String> customerMap, SmsBox smsBox, HashOperations customerHash, String getCustomerKey){
        //用于专门存放计算mq执行的进度
        Map<String, Integer> redisContactsNumMap = Maps.newHashMap();
        //定义总的联系人数量
        int totalContactsNum = customerMap.size();
        redisContactsNumMap.put("totalContactsNum", totalContactsNum);
        //模板ID
        Long smsBoxId = smsBox.getId();
        for (Map.Entry<String, String> mapEntry : customerMap.entrySet()){
            //获取客户数据
            Customer customer = JSON.parseObject(mapEntry.getValue(), Customer.class);
            try {
                //TODO 暂时注释测试数据
                //通过第三方发送短信
                Map<String, Object> sendSmsAutho = smsRecordService.sendSmsToThird(customer.getThirdPartyId(), smsBox.getThirdPartyId());
                Integer state = (Integer) sendSmsAutho.get("state");
                //如果发送短信没有成功，再次请求，如果5次没有成功就放弃
                if(state != 1){
                    for(int i=0; i<5; i++){
                        sendSmsAutho = smsRecordService.sendSmsToThird(customer.getThirdPartyId(), smsBox.getThirdPartyId());
                        Integer resultSendSms = (Integer) sendSmsAutho.get("state");
                        if(resultSendSms == 1){
                            state = resultSendSms;
                            break;
                        }
                    }
                }

//                //TODO 测试短信发送用
//                Map<String, Object> sendSmsAutho = new HashMap<>();
//                //随机取状态
//                int stateNum = Math.random()>0.5 ? 1 : -1;
//                sendSmsAutho.put("state", stateNum);
//                sendSmsAutho.put("errmsg", "短信测试");
//                Integer state = (Integer) sendSmsAutho.get("state");


                //发送短信后，第三方返回的信息
                String errmsg = (String) sendSmsAutho.get("errmsg");
                SmsRecord smsRecord = new SmsRecord();
                    smsRecord.setMobile(customer.getMobile() != null ? customer.getMobile() : "未知");
                    smsRecord.setMac(customer.getMac() != null ? customer.getMac() : "未知");
                    smsRecord.setThirdPartyId(customer.getThirdPartyId() != null ? customer.getThirdPartyId() : "未知");
                    smsRecord.setState(state);
                    smsRecord.setErrmsg(errmsg);
                    smsRecord.setCustomerId(customer.getId());
                    smsRecord.setSmsBoxId(smsBoxId);
                    smsRecord.setYn(YNEnum.YES.getVal());
                    smsRecord.setCreator(userVO.getUserName());
                    smsRecord.setCreateTime(new Date());

                //先从redis中删除数据然后及时提交数据
                Long resultDelete = customerHash.delete(getCustomerKey, customer.getId().toString());
                smsRecord = smsRecordService.modifySmsRecord(userVO, smsRecord);
                if (smsRecord == null) {
                    LOGGER.error("初始化短信记录保存失败，customerId：" + customer.getId());
                    continue;
                }
            }catch (Exception ex){
                LOGGER.error("初始化短信记录保存异常，customerId：" + customer.getId());
                continue;
            }

        }
        //删除临时存放发送短信的总条数放到redis
        redisTemplate.delete(RedisKey.MQ_TEMP_SEND_SMS_NUM+smsBoxId);
    }
}
