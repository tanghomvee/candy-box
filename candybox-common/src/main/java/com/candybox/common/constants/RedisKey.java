package com.candybox.common.constants;


/**
 * @author ddyunf
 */
public class RedisKey {
    /**
     * 保存门店信息的前缀
     */
    public static final String STORE = "STORE";
    /**
     * 保存设备信息的前缀
     */
    public static final String DEVICE = "DEVICE";
    /**
     * 保存门店设备信息的前缀
     */
    public static final String STORE_DEVICE = "STORE_DEVICE";
    /**
     * 保存门店规则的前缀
     */
    public static final String STORE_RULE = "STORE_RULE";
    /**
     * 保存门店调用外部接口获取手机号码的的次数限制规则
     */
    public static final String STORE_RULE_ST_PH_LIMIT = "STORE_RULE_ST_PH_LIMIT";
    /**
     * 保存门店客户信息前缀
     */
    public static final String STORE_CUSTOMER = "STORE_CUSTOMER";
    /**
     * 声压授权TOKEN前缀
     */
    public static final String SOUND_TOOTH_SMS_TOKEN = "SOUND_TOOTH_SMS_TOKEN";

    /**
     * 客户门店统计分布式锁key
     */
    public static final String LOCK_CUSTOMER_STORE_STATS_TASK = "LOCK_CUSTOMER_STORE_STATS_TASK";
    /**
     * 每个店铺的客户数据统计分布式锁key
     */
    public static final String LOCK_STORE_CUSTOMER_STATS_TASK = "LOCK_STORE_CUSTOMER_STATS_TASK";
    /**
     * 电话或短信筛选客户信息的SQL与SQL参数（后面要加自己的用户id才能取值）
     */
    public static final String LIST_CUSTOMER_NUM = "LIST_CUSTOMER_NUM";
    /**
     * 短信筛选的客户信息数据集合
     */
    public static final String LIST_CUSTOMER_ENTITY = "LIST_CUSTOMER_ENTITY";
    /**
     * 电话筛选的客户信息数据集合
     */
    public static final String LIST_CUSTOMER_ENTITY_CALL = "LIST_CUSTOMER_ENTITY_CALL";
    /**
     * 计算创建活动联系人分配极时响应key
     */
    public static final String LIST_CONTACTS_NUM = "LIST_CONTACTS_NUM";
    /**
     * 计算MQ创建联系人的进度
     */
//    public static final String LIST_CONTACTS_CALL_NUM = "LIST_CONTACTS_CALL_NUM";
    /**
     * 临时存放发送短信的总条数放到redis（用于时时计算短信发送情况，MQ执行完会删除）
     */
    public static final String MQ_TEMP_SEND_SMS_NUM = "MQ_TEMP_SEND_SMS_NUM";
    /**
     * MQ执行添加联系人，加锁当前活动ID
     */
    public static final String MQ_LOCK_ACTIVITY_ID = "MQ_LOCK_ACTIVITY_ID";
}
