package com.slst.common.mq.producer.impl;

import com.slst.common.mq.producer.DefaultProducerService;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author ddyunf
 */
public class DefaultProducerServiceImpl implements DefaultProducerService {
    protected static Logger LOGGER = null;
    private String nameSrvAddrs;
    private String producerGroupName;
    private String producerName;

    private String charset = RemotingHelper.DEFAULT_CHARSET;
    private long timeout = 3000L;

    private int checkThreadPoolMinSize = 1;
    private int checkThreadPoolMaxSize = 3;
    private int checkRequestHoldMax = 2000;
    private int retryTimes = 2;

    private TransactionCheckListener transactionCheckListener;
    private LocalTransactionExecuter localTransactionExecuter;
    private RPCHook rpcHook;
    private TransactionMQProducer mqProducer;

    public DefaultProducerServiceImpl() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void sendMsg(String topic, String content) throws Exception {
        this.sendMsg(topic  ,null , content);
    }

    @Override
    public void sendMsg(String topic, String tag, String content, String... keys) throws Exception {
        Message msg = buildMsg(topic ,tag ,content ,keys);
        try{

            SendResult sendResult = mqProducer.sendMessageInTransaction(msg , localTransactionExecuter , null);
            LOGGER.info("发送msg=[topic:{},tag:{},content:{},keys:{}]结果result={}" , topic,tag , content,Arrays.toString(keys) ,sendResult);
            if(sendResult != null || !SendStatus.SEND_OK.equals(sendResult.getSendStatus())){
                LOGGER.warn("发送msg=[topic:{},tag:{},content:{},keys:{}]失败" , topic,tag , content ,Arrays.toString(keys));
            }
        }catch (Exception ex){
            LOGGER.error("发送msg=[topic:{},tag:{},content:{} , keys:{}]异常" , topic, tag, Arrays.toString(keys), content ,ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        this.checkProperties();

        mqProducer = new TransactionMQProducer(producerGroupName ,rpcHook);
        mqProducer.setCheckRequestHoldMax(checkRequestHoldMax);
        mqProducer.setCheckThreadPoolMaxSize(checkThreadPoolMaxSize);
        mqProducer.setCheckThreadPoolMinSize(checkThreadPoolMinSize);
        mqProducer.setTransactionCheckListener(transactionCheckListener);

        mqProducer.setSendMsgTimeout(Long.valueOf(timeout).intValue());
        mqProducer.setInstanceName(producerName);
        mqProducer.setNamesrvAddr(nameSrvAddrs);
        mqProducer.setVipChannelEnabled(false);

        if(retryTimes > 0){
            mqProducer.setRetryTimesWhenSendFailed(retryTimes);
            mqProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
        }

        mqProducer.start();

        this.registerShudown();
    }


    private void registerShudown(){
        final  TransactionMQProducer transactionMQProducer = mqProducer;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                transactionMQProducer.shutdown();
            }
        }));
    }

    protected void checkProperties() throws Exception {
        LOGGER.info("producerGroupName:{}" , producerGroupName);
        if(StringUtils.isEmpty(producerGroupName)){
            throw  new Exception("config producerGroupName error");

        }
        LOGGER.info("nameSrvAddrs:{}" , nameSrvAddrs);
        if(StringUtils.isEmpty(nameSrvAddrs)){
            throw  new Exception("config nameSrvAddrs error");

        }

        LOGGER.info("checkThreadPoolMinSize:{}" , checkThreadPoolMinSize);
        if(checkThreadPoolMinSize < 1){
            throw  new Exception("config checkThreadPoolMinSize error");

        }
        LOGGER.info("checkThreadPoolMaxSize:{}" , checkThreadPoolMaxSize);
        if(checkThreadPoolMaxSize < 1){
            throw  new Exception("config checkThreadPoolMaxSize error");

        }
        LOGGER.info("checkRequestHoldMax:{}" , checkRequestHoldMax);
        if(checkRequestHoldMax < 1){
            throw  new Exception("config checkRequestHoldMax error");

        }

        LOGGER.info("timeout:{}" , timeout);
        if(timeout < 1){
            throw  new Exception("config timeout error");

        }

        LOGGER.info("retryTimes:{}" , retryTimes);

        if(transactionCheckListener == null){
            LOGGER.warn("not config transactionCheckListener");
            final  String tmpCharset = charset;
            transactionCheckListener = new TransactionCheckListener() {
                @Override
                public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
                    try {
                        LOGGER.info("checkLocalTransactionState:{},content:{}" , msg , new String(msg.getBody() , tmpCharset));
                    } catch (Exception e) {
                        LOGGER.error("",e);
                    }
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            };
        }

        if(localTransactionExecuter == null){
            LOGGER.warn("not config localTransactionExecuter");
            final  String tmpCharset = charset;
            localTransactionExecuter = new LocalTransactionExecuter() {
                @Override
                public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                    try {
                        LOGGER.info("localTransactionExecuter:{},content:{}" , msg , new String(msg.getBody() , tmpCharset));
                    } catch (Exception e) {
                        LOGGER.error("",e);
                    }

                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            };
        }

        if(rpcHook == null){
            LOGGER.warn("not config rpcHook");
            rpcHook = new RPCHook() {
                @Override
                public void doBeforeRequest(String remoteAddr, RemotingCommand request) {

                }

                @Override
                public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {

                }
            };
        }
    }


    protected Message buildMsg(String topic , String tag , String content , String ... keys) throws Exception {
        if(StringUtils.isEmpty(topic)){
            throw  new Exception("config topic error");
        }

        if(StringUtils.isEmpty(content)){
            throw  new Exception("message content is empty");
        }
        byte[] msgByte = content.getBytes(charset);
        StringBuffer tmpKeys = new StringBuffer();
        if(keys != null && keys.length < 0){
            for (String key : keys){

                tmpKeys.append(key).append(MessageConst.KEY_SEPARATOR);
            }
            tmpKeys = tmpKeys.deleteCharAt(tmpKeys.lastIndexOf(MessageConst.KEY_SEPARATOR));
        }

        Message msg = new Message(topic , tag , tmpKeys.toString() , msgByte);

        return msg;
    }

    public String getNameSrvAddrs() {
        return nameSrvAddrs;
    }

    public void setNameSrvAddrs(String nameSrvAddrs) {
        this.nameSrvAddrs = nameSrvAddrs;
    }

    public String getProducerGroupName() {
        return producerGroupName;
    }

    public void setProducerGroupName(String producerGroupName) {
        this.producerGroupName = producerGroupName;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }


    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getCheckThreadPoolMinSize() {
        return checkThreadPoolMinSize;
    }

    public void setCheckThreadPoolMinSize(int checkThreadPoolMinSize) {
        this.checkThreadPoolMinSize = checkThreadPoolMinSize;
    }

    public int getCheckThreadPoolMaxSize() {
        return checkThreadPoolMaxSize;
    }

    public void setCheckThreadPoolMaxSize(int checkThreadPoolMaxSize) {
        this.checkThreadPoolMaxSize = checkThreadPoolMaxSize;
    }

    public int getCheckRequestHoldMax() {
        return checkRequestHoldMax;
    }

    public void setCheckRequestHoldMax(int checkRequestHoldMax) {
        this.checkRequestHoldMax = checkRequestHoldMax;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public TransactionCheckListener getTransactionCheckListener() {
        return transactionCheckListener;
    }

    public void setTransactionCheckListener(TransactionCheckListener transactionCheckListener) {
        this.transactionCheckListener = transactionCheckListener;
    }

    public LocalTransactionExecuter getLocalTransactionExecuter() {
        return localTransactionExecuter;
    }

    public void setLocalTransactionExecuter(LocalTransactionExecuter localTransactionExecuter) {
        this.localTransactionExecuter = localTransactionExecuter;
    }

    public RPCHook getRpcHook() {
        return rpcHook;
    }

    public void setRpcHook(RPCHook rpcHook) {
        this.rpcHook = rpcHook;
    }

    public TransactionMQProducer getMqProducer() {
        return mqProducer;
    }

    public void setMqProducer(TransactionMQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }
}
