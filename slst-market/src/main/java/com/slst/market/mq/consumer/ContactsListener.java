package com.slst.market.mq.consumer;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.enums.YNEnum;
import com.slst.common.mq.consumer.listeners.DefaultMessageListenerConcurrently;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.market.dao.ContactsDao;
import com.slst.market.dao.model.Contacts;
import com.slst.market.web.vo.ContactsSaveByActivityIdReq;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Consts;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: daiyou.zhong
 * @description: 联系人MQ消费
 * @create: 2018-09-04 11:28
 * @version: 2.0
 **/
@Component(value = "contactsListener")
public class ContactsListener extends DefaultMessageListenerConcurrently {
    @Resource
    private CustomerService customerService;
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public boolean consumeMessage(List<MessageExt> msgs) {
        if (CollectionUtils.isEmpty(msgs)){
            return true;
        }
//        if(true){
//            return true;
//        }

        for(MessageExt msg : msgs){
                //redisHash操作
                HashOperations customerHash = redisTemplate.opsForHash();
                //获取队列中的数据
                String strJson = new String(msg.getBody(), Consts.UTF_8);
                Map<String, Object> mapParam = (Map<String, Object>)JSONUtils.parse(strJson);
                //从Map中取出sql，然后把Map中的sql删除
                String sql = mapParam.get("sql").toString();
                mapParam.remove("sql");
                //获取请求参数(此参数相当于ContactsSaveByActivityIdReq.class)
                Map<String, Object> req = (Map<String, Object>)mapParam.get("req");
                mapParam.remove("req");
                Long activityId = Long.parseLong(req.get("activityId").toString());
                LOGGER.info("MQ执行创建联系人...，activityId={}", activityId);
                //redisKey
                String redisCustomerKey = RedisKey.LIST_CUSTOMER_ENTITY_CALL+activityId;
                //是否分给员工的状态：0-不分; 1-分给员工
                Integer status = (Integer) req.get("status");
                List<Map<String, Object>> contactsCountReqs = (List<Map<String, Object>>)req.get("contactsCountReqs");
                //获取登陆信息
                Map<String, Object> userVo = (Map<String, Object>)mapParam.get("user");
                mapParam.remove("user");
                //获取登陆的基本信息
                Long userId = Long.parseLong(userVo.get("id").toString());
                Long venderId = Long.parseLong(userVo.get("venderId").toString());
                Object storeObj = userVo.get("storeId");
                Long storeId = storeObj==null ? 0L : Long.parseLong(storeObj.toString());
                String userName = userVo.get("userName").toString();
                //避免重复消费，加锁当前活动id
                if(!redisComponent.lock(RedisKey.MQ_LOCK_ACTIVITY_ID+activityId, 120L)){
                    LOGGER.warn("MQ_联系人活动ID已加锁 key={}", RedisKey.MQ_LOCK_ACTIVITY_ID+activityId);
                    return false;
                }
                try {
                    //redis中取出联系人
                    Map<String, String> redisCustomerMap = customerHash.entries(redisCustomerKey);
                    //判断redis中是否存在联系人，如果不存在，说明是第一次消费
                    if(MapUtils.isEmpty(redisCustomerMap)){
                        //查询筛选到的客户
                        List<Customer> customerList = customerService.listCustomerBySql(sql, mapParam);
                        if(CollectionUtils.isEmpty(customerList)){
                            return true;
                        }
                        //把联系客户放到redis中
                        redisCustomerMap = new HashMap<>(customerList.size());
                        for(Customer customer : customerList){
                            redisCustomerMap.put(customer.getId().toString(), JSON.toJSONString(customer));
                        }
                        customerHash.putAll(redisCustomerKey, redisCustomerMap);
                        //如果存在，判断数据库中是否已添加过联系人
                    }else {
                        Integer tempCount = contactsDao.queryByActivityId(activityId);
                        if(tempCount == 0){
                            //删除redis中的联系人
                            redisComponent.deleteByKey(redisCustomerKey);
                            //返回false重新消费
                            return false;
                        }
                    }
                    //如果状态为0，代表不分配，说明是商家自己添加或者是员工自己创建的活动
                    if(status == 0){
                        for (Map.Entry<String, String> customerMap : redisCustomerMap.entrySet()) {
                            Customer customer = JSON.parseObject(customerMap.getValue(), Customer.class);
                            Contacts contact = new Contacts();
                            contact.setCustomerId(customer.getId());
                            contact.setToNum(customer.getMobile());
                            contact.setToNumId(customer.getThirdPartyId());
                            contact.setIntention(-1);
                            contact.setUserId(userId);
                            contact.setVenderId(venderId);
                            contact.setStoreId(storeId);
                            contact.setActivityId(activityId);
                            contact.setYn(YNEnum.YES.getVal());
                            contact.setCreator(userName);
                            contact.setCreateTime(new Date());
                            contactsDao.saveAndFlush(contact);
                            customerHash.delete(redisCustomerKey, customer.getId().toString());
                        }
                        //商家分配客户给员工
                    }else if(status == 1){
                        for(Map<String, Object> map : contactsCountReqs){
                            Long venderEmpId = Long.parseLong(map.get("venderEmpId").toString());
                            Integer contactCount = (Integer) map.get("contactCount");
                            //获取自己添加的数据
                            Integer myNowContactCount = contactsDao.queryByActivityIdAndVenderEmpId(activityId, venderEmpId);
                            //如果已添加联系人数量大于0，说明不是第一次消费
                            if(myNowContactCount > 0){
                                //得到现在要添加的联系人数量
                                contactCount = contactCount - myNowContactCount;
                            }
                            if(contactCount != null && contactCount > 0){
                                Iterator<Map.Entry<String, String>> iterator = redisCustomerMap.entrySet().iterator();
                                for(int i=0; i<contactCount; i++){
                                    Customer customer = JSON.parseObject(iterator.next().getValue(), Customer.class);
                                    Contacts contact = new Contacts();
                                    contact.setCustomerId(customer.getId());
                                    contact.setToNum(customer.getMobile());
                                    contact.setToNumId(customer.getThirdPartyId());
                                    contact.setIntention(-1);
                                    contact.setUserId(userId);
                                    contact.setVenderId(venderId);
                                    contact.setStoreId(storeId);
                                    contact.setActivityId(activityId);
                                    contact.setYn(YNEnum.YES.getVal());
                                    contact.setCreator(userName);
                                    contact.setCreateTime(new Date());
                                    contact.setVenderEmpId(venderEmpId);
                                    contactsDao.saveAndFlush(contact);
                                    customerHash.delete(redisCustomerKey, customer.getId().toString());
                                    iterator.remove();
                                }
                            }
                        }
                    }
                    //删除redis中用来算已处理联系人的数据
                    redisComponent.deleteByKey(RedisKey.LIST_CONTACTS_NUM + activityId);
                }catch (Exception ex){
                    LOGGER.error("MQ_添加联系人消费异常，data={}", mapParam, ex);
                }finally {
                    redisComponent.unLock(RedisKey.MQ_LOCK_ACTIVITY_ID+activityId);
                }
        }
        return true;
    }
}
