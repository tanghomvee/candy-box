package com.candybox.common.mq.consumer.impl;

import com.candybox.common.enums.SeparatorEnum;
import com.candybox.common.mq.consumer.DefaultConsumerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.SubscriptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author ddyunf
 */
public class DefaultConsumerServiceImpl implements DefaultConsumerService {
    protected static Logger LOGGER = null;
    private String topic ;
    private List<String> tags;
    private String nameSrvAddrs;
    private String consumerGroupName;
    private String consumerName;
    private String clientIP;
    private MessageListener messageListener;
    private DefaultMQPushConsumer defaultMQPushConsumer;

    public DefaultConsumerServiceImpl() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.checkProperties();

        defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setConsumerGroup(consumerGroupName);
        defaultMQPushConsumer.setInstanceName(consumerName + SeparatorEnum.UNDERLINE.getVal() + (UUID.randomUUID().toString().replace("-" , "")));
        defaultMQPushConsumer.setNamesrvAddr(nameSrvAddrs);
        defaultMQPushConsumer.setVipChannelEnabled(false);
        if (!StringUtils.isEmpty(clientIP)){
            defaultMQPushConsumer.setClientIP(clientIP);
        }



        StringBuffer subExpression = new StringBuffer();
        if(CollectionUtils.isEmpty(tags)){
            subExpression.append(SubscriptionData.SUB_ALL);
        }else{
            String separator = " || ";

            for (String tag : tags){
                subExpression.append(tag).append(separator);
            }
            subExpression.append(tags.get(0));
        }

        defaultMQPushConsumer.subscribe(topic , subExpression.toString());

        //这里设置的是一个consumer的消费策略
        //CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
        //CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
        //CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.registerMessageListener(messageListener);
        defaultMQPushConsumer.start();
        this.registerShudown();
    }


    private void checkProperties() throws Exception {
        LOGGER.info("consumerGroupName:{}" , consumerGroupName);
        if(StringUtils.isEmpty(consumerGroupName)){
            throw  new Exception("config consumerGroupName error");

        }
        LOGGER.info("nameSrvAddrs:{}" , nameSrvAddrs);
        if(StringUtils.isEmpty(nameSrvAddrs)){
            throw  new Exception("config nameSrvAddrs error");

        }
        LOGGER.info("consumerName:{}" , consumerName);
        if(StringUtils.isEmpty(consumerName)){
            throw  new Exception("config consumerName error");
        }
        LOGGER.info("topic:{}" , topic);
        if(StringUtils.isEmpty(topic)){
            throw  new Exception("config topic error");

        }
        if(messageListener == null){
            throw  new Exception("config messageListener error");
        }

    }


    private void registerShudown(){
        final DefaultMQPushConsumer tmpDefaultMQPushConsumer = defaultMQPushConsumer;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                tmpDefaultMQPushConsumer.shutdown();
            }
        }));
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNameSrvAddrs() {
        return nameSrvAddrs;
    }

    public void setNameSrvAddrs(String nameSrvAddrs) {
        this.nameSrvAddrs = nameSrvAddrs;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public DefaultMQPushConsumer getDefaultMQPushConsumer() {
        return defaultMQPushConsumer;
    }

    public void setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer) {
        this.defaultMQPushConsumer = defaultMQPushConsumer;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
}
