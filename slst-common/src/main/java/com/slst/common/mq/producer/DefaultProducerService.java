package com.slst.common.mq.producer;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author ddyunf
 */
public interface DefaultProducerService extends InitializingBean {

    /**
     * 发送消息
     * @param topic 消息主题
     * @param content 消息内容
     * @throws Exception
     */
    void sendMsg(String topic, String content) throws Exception ;

    /**
     * 发送消息
     * @param topic 消息主题
     * @param tag 消息标签
     * @param content 消息内容
     * @param keys 消息关键字
     * @throws Exception
     */
    void sendMsg(String topic, String tag, String content, String... keys) throws Exception;
}
