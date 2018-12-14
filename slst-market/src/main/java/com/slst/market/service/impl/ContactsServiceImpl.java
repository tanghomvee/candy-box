package com.slst.market.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.slst.common.components.RedisComponent;
import com.slst.common.constants.RedisKey;
import com.slst.common.enums.YNEnum;
import com.slst.common.mq.producer.DefaultProducerService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.BeanMapUtil;
import com.slst.common.utils.PageableUtil;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.market.dao.ContactsDao;
import com.slst.market.dao.model.ActivityVenderEmpRelation;
import com.slst.market.dao.model.Contacts;
import com.slst.market.service.ActivityVenderEmpRelationService;
import com.slst.market.service.ContactsService;
import com.slst.market.web.vo.ContactsCountReq;
import com.slst.market.web.vo.ContactsSaveByActivityIdReq;
import com.slst.market.web.vo.ContactsSaveByActivityIdVO;
import com.slst.market.web.vo.FindMqAddStatusVO;
import com.slst.vender.service.VenderEmpService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service("contactsService")
public class ContactsServiceImpl extends BaseServiceImpl<Contacts, Long> implements ContactsService {

    @Resource
    private ContactsDao contactsDao;
    @Resource
    private CustomerService customerService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private DefaultProducerService defaultProducerService;
    @Resource
    private VenderEmpService venderEmpService;
    @Value(value = "${mq.contacts.producer.topic.call}")
    private String topic;
    @Resource
    private ActivityVenderEmpRelationService activityVenderEmpRelationService;
    @Override
    public Contacts save(Contacts contacts) {
        return contactsDao.save(contacts);
    }

    @Override
    public Msg createContacts(UserVO curUser, Long activityId, String customerIds) {
        if (customerIds.contains(",")) {
            List<Customer> customers = customerService.findById(customerIds);
            List<Contacts> contacts = new ArrayList<>();
            for (Customer customer : customers) {
                Contacts contact = initContacts(curUser, customer, activityId);
                contacts.add(contact);
            }
            List<Contacts> rtnContacts = contactsDao.saveAll(contacts);

            if (rtnContacts.size() > 0) return Msg.success("创建成功");
        } else {

            Long customerId = 0L;

            if (!StringUtils.isEmpty(customerIds)) {
                customerId = Long.parseLong(customerIds);
            }

            Customer customer = customerService.findById(customerId);

            try {
                Contacts contacts = initContacts(curUser, customer, activityId);

                Contacts rtnContacts = save(contacts);

                if (null != rtnContacts) return Msg.success("创建成功");

            } catch (Exception e) {

                LOGGER.error("获取客户信息出错,客户ID={}", customerId, e);
                return Msg.error("创建失败");
            }
        }

        return Msg.error("创建失败");
    }

    @Override
    public Contacts modifyContact(UserVO userVO, Contacts contacts) {
        contacts.setChangeTime(new Date());
        contacts.setChanger(userVO.getUserName());
        return contactsDao.saveAndFlush(contacts);
    }

    @Override
    public Contacts findById(Long id) {
        return contactsDao.findById(id).get();
    }

    @Override
    public Page<Contacts> findByActivityId(Long activityId, Integer intention, String toNum, int pageNum, int pageSize) {

        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, "createTime", "desc");

        Page<Contacts> page = null;

        if (null == intention) {
            if (StringUtils.isEmpty(toNum)) {
                return contactsDao.findByActivityId(activityId, pageable);
            }

            return contactsDao.findByActivityIdAndToNumContaining(activityId, toNum, pageable);
        } else {
            if (StringUtils.isEmpty(toNum)) {
                return contactsDao.findByActivityIdAndIntention(activityId, intention, pageable);
            }

            return contactsDao.findByActivityIdAndIntentionAndToNumContaining(activityId, intention, toNum, pageable);
        }

    }

    @Override
    public Page<Contacts> findByActivityIdAndVenderEmpId(Long activityId, Long venderEmpId, Integer intention, String toNum, int pageNum, int pageSize) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, "createTime", "desc");

        Page<Contacts> page = null;

        if (null == intention) {
            if (StringUtils.isEmpty(toNum)) {
                return contactsDao.findByActivityIdAndVenderEmpId(activityId,venderEmpId, pageable);
            }

            return contactsDao.findByActivityIdAndVenderEmpIdAndToNumContaining(activityId,venderEmpId, toNum, pageable);
        } else {
            if (StringUtils.isEmpty(toNum)) {
                return contactsDao.findByActivityIdAndVenderEmpIdAndIntention(activityId,venderEmpId, intention, pageable);
            }

            return contactsDao.findByActivityIdAndVenderEmpIdAndIntentionAndToNumContaining(activityId,venderEmpId, intention, toNum, pageable);
        }
    }

    @Override
    public Msg callRemark(UserVO curUser, Integer intention, String remark, Long id) {
        try {
            Contacts contacts = contactsDao.findById(id).get();

            contacts.setIntention(intention);
            contacts.setRemark(remark);

            Contacts rtnContacts = modifyContact(curUser, contacts);

            if (null != rtnContacts) {
                return Msg.success();
            }

        } catch (Exception e) {
            LOGGER.error("获取通话记录失败,记录ID={}", id, e);
            return Msg.error("获取通话记录失败");
        }
        return Msg.error("");
    }

    @Override
    public Msg addContacts(UserVO curUser, ContactsSaveByActivityIdVO contactsSaveByActivityIdVO) {
        //定义map放redis中，用于后面计算极时响应已增加多少个联系人，多少联系人没有增加
        Map<String, Object> contactsNum = new HashMap<>();
        //表示是分配给员工
        contactsNum.put("status", contactsSaveByActivityIdVO.getStatus());
        ContactsSaveByActivityIdReq req = new ContactsSaveByActivityIdReq();
        req.setActivityId(contactsSaveByActivityIdVO.getActivityId());
        req.setStatus(contactsSaveByActivityIdVO.getStatus());

        if(req == null){
            return Msg.error("ContactsSaveByActivityIdReq [必填]");
        }
        Long activityId = req.getActivityId();
        if(activityId == null || activityId == 0L){
            return Msg.error("activityId [必填]");
        }
        Integer status = req.getStatus();
        if(status == null){
            return Msg.error("status [必填]");
        }
        String redisCustomerKey = RedisKey.LIST_CUSTOMER_NUM + activityId;
        //从redis中拿取查询客户的sql与参数
        Map<Object, Object> redisMap = redisComponent.getRedisMapByKey(redisCustomerKey);
        //创建查询客户的map
        Map<String, Object> queryCustomerMap = new HashMap<>();
        if(MapUtils.isEmpty(redisMap)){
            return Msg.error("请重新筛选客户");
        }
        //将 Map<Object, Object> 转为 Map<String, Object>
        redisMap.entrySet().stream().forEach(item -> queryCustomerMap.put((String) item.getKey(), item.getValue()));
        //获取筛选的客户数量
        String sql = queryCustomerMap.get("sql").toString();
        queryCustomerMap.remove("sql");
        Integer contactCount = customerService.countCustomerBySql(sql, queryCustomerMap);
        queryCustomerMap.put("sql", sql);

        //如果商家要将客户分给员工
        if(status == 1){
            //定义商家给员工分配客户
            List<ContactsCountReq> contactsCountReqs = new ArrayList<>();
            String venderEmpIdsStr = contactsSaveByActivityIdVO.getVenderEmpId();
            String contactCountsStr = contactsSaveByActivityIdVO.getContactCount();
            if(StringUtils.isEmpty(venderEmpIdsStr) || StringUtils.isEmpty(contactCountsStr)){
                return Msg.error("商家员工或客户数量不能为空");
            }
            String[] venderEmpIds = venderEmpIdsStr.split(",");
            String[] contactCounts = contactCountsStr.split(",");
            int i = 0;
            for(String venderEmpId : venderEmpIds){
                ContactsCountReq contactsCountReq = new ContactsCountReq();
                contactsCountReq.setVenderEmpId(Long.parseLong(venderEmpId));
                contactsCountReq.setContactCount(Integer.valueOf(contactCounts[i]));
                contactsCountReqs.add(contactsCountReq);
                ++i;
            }
            req.setContactsCountReqs(contactsCountReqs);
            //如果门店Id不为空，说明是员工账号，员工账号不能给员工分配客户
            if(curUser.getStoreId() != null){
                return Msg.error("员工账号不能给员工分配客户");
            }
            if(CollectionUtils.isEmpty(contactsCountReqs)){
                return Msg.error("contactsCountReqs [必填]");
            }else {
                //客户总数
                Integer tempContactCount = 0;
                //循环得到客户的总数
                for(ContactsCountReq contactsCountReq : contactsCountReqs){
                    if(contactsCountReq.getVenderEmpId() == null){
                        return Msg.error("venderEmpId [必填]");
                    }
                    tempContactCount += contactsCountReq.getContactCount();
                    //商家员工与活动关系保存
                    ActivityVenderEmpRelation activityVenderEmpRelation = new ActivityVenderEmpRelation();
                        activityVenderEmpRelation.setActivityId(activityId);
                        activityVenderEmpRelation.setVenderEmpId(contactsCountReq.getVenderEmpId());
                        activityVenderEmpRelation.setYn(YNEnum.YES.getVal());
                        activityVenderEmpRelation.setCreator(curUser.getUserName());
                        activityVenderEmpRelation.setCreateTime(new Date());
                    Msg save = activityVenderEmpRelationService.save(activityVenderEmpRelation);
                    if(!save.isSuccess()){
                        return Msg.error(save.getMsg());
                    }
                }
                //要分配给员工的数量
                contactsNum.put("empContactCounts", req.getContactsCountReqs());
                //分配的客户数量不能大于筛选的客户数量
                if(contactCount<tempContactCount){
                    return Msg.error("分配的客户数量不能大于筛选的客户数量");
                }
            }
        }
        //联系人总数
        contactsNum.put("contactCount", contactCount);
        //把登陆信息与活动id一起放到map中
        queryCustomerMap.put("user", BeanMapUtil.bean2Map(curUser));
        queryCustomerMap.put("req", req);
        //将查询客户的sql与参数map转为json，然后放消息队列中
        String redisMapJson = JSONObject.toJSONString(queryCustomerMap);
        try {
            defaultProducerService.sendMsg(topic, redisMapJson);
            redisComponent.deleteByKey(redisCustomerKey);
            //把联系人分配请求参数与总数放入redis
            Map<String, Object> contactsRedisMap = new HashMap<>(2);
                contactsRedisMap.put("status", req.getStatus());
                contactsRedisMap.put("activityId", req.getActivityId());
                contactsRedisMap.put("contactCount", contactCount);
                for (ContactsCountReq contactsCountReq : req.getContactsCountReqs()){
                    contactsRedisMap.put(contactsCountReq.getVenderEmpId().toString(), contactsCountReq.getContactCount());
                }
            redisComponent.redisSaveMap(RedisKey.LIST_CONTACTS_NUM+activityId, contactsRedisMap);
        }catch (Exception ex){
            LOGGER.error("客户筛选sql语句放消息队列异常：topic = {}, content = {}", topic, redisMapJson, ex);
        }
        return Msg.success("创建成功");
    }

    @Override
    public Msg getSaveContactsStatus(UserVO userVO, Long activityId) {
        //redis中分配联系人KEY
        String redisCustomerKey = RedisKey.LIST_CONTACTS_NUM + activityId;
        HashOperations customerHash = redisTemplate.opsForHash();
        //定义返回对象
        FindMqAddStatusVO result = new FindMqAddStatusVO();
        //从redis中获取分配联系人数据
        Map<Object, Object> redisMapByKey = redisComponent.getRedisMapByKey(redisCustomerKey);
        if(MapUtils.isEmpty(redisMapByKey)){
            return Msg.error("添加联系人已完成");
        }
        //获取分配联系人的总数
        Integer contactCount = Integer.valueOf(redisMapByKey.get("contactCount").toString());
        //获取状态0不分配给员工，1分配给员工
        Integer status = Integer.valueOf(redisMapByKey.get("status").toString());
        //如果请求为商家，把分配改为未分配来查询
        if(userVO.getVenderEmpId()==null || userVO.getVenderEmpId()==0L){
            status = 0;
        }
        if(status == 1){
            Long venderEmpId = userVO.getVenderEmpId();
            //查询出自己须要分配联系人数量
            Integer myContactCount = Integer.valueOf(redisMapByKey.get(venderEmpId.toString()).toString());
            //获取已添加联系人的数量
            Integer nowMyContactCount = contactsDao.queryByActivityIdAndVenderEmpId(activityId, userVO.getVenderEmpId());
            //得到剩余要添加联系人的数量
            Integer count = myContactCount - nowMyContactCount;
            //如果自己的联系人已添加完，直接返回
            if(count == 0){
                return Msg.error("添加联系人已完成");
            }
            result.setNowNumber(nowMyContactCount);
            result.setTotalNumber(myContactCount);
            result.setResidueNumber(count);
            return Msg.success(result);
        }else {
            //查询须要分配联系人总数量
            Integer contactCountAll = Integer.valueOf(redisMapByKey.get("contactCount").toString());
            //获取已添加联系人的数量
            Integer nowMyContactCount = contactsDao.queryByActivityId(activityId);
            //得到剩余要添加联系人的数量
            Integer count = contactCountAll - nowMyContactCount;
            //如果自己的联系人已添加完，直接返回
            if(count == 0){
                return Msg.error("添加联系人已完成");
            }
            result.setNowNumber(nowMyContactCount);
            result.setTotalNumber(contactCountAll);
            result.setResidueNumber(count);
            return Msg.success(result);
        }
    }

    private Contacts initContacts(UserVO curUser, Customer customer, Long activityId) {
        Long customerId = customer.getId();
        String toNum = customer.getMobile();
        String toNumId = customer.getThirdPartyId();
        Long userId = curUser.getId();
        Long venderId = curUser.getVenderId();
        Long storeId = null == curUser.getStoreId() ? 0L : curUser.getStoreId();
        Integer yn = YNEnum.YES.getVal();
        String creator = curUser.getUserName();
        Date createTime = new Date();

        Contacts contacts = new Contacts();
        contacts.setCustomerId(customerId);
        contacts.setToNum(toNum);
        contacts.setToNumId(toNumId);
        contacts.setIntention(-1);
        contacts.setUserId(userId);
        contacts.setVenderId(venderId);
        contacts.setStoreId(storeId);
        contacts.setActivityId(activityId);
        contacts.setYn(yn);
        contacts.setCreator(creator);
        contacts.setCreateTime(createTime);
        return contacts;
    }
}
