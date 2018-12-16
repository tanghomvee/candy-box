package com.candybox.common.mq.consumer.listeners;

import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author ddyunf
 */
public abstract class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {

    protected static Logger LOGGER = null;

    public DefaultMessageListenerConcurrently() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        boolean consumeResult = false;
        if(CollectionUtils.isNotEmpty(msgs)){
            try{
                consumeResult = this.consumeMessage(msgs);
            }catch (Exception ex){
                LOGGER.error("消费消息msgs={}异常" , msgs, ex);
            }
        }


        return consumeResult ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    /**
     * 收到消息，如果正常消费消息返回true。
     * 如果消费失败，RocketMQ会重试3次
     * @param msgs
     * @return
     */
    public abstract boolean consumeMessage(List<MessageExt> msgs);
}
