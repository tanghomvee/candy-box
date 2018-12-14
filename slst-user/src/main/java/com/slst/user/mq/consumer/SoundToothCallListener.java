package com.slst.user.mq.consumer;

import com.slst.common.mq.consumer.listeners.DefaultMessageListenerConcurrently;
import com.slst.common.utils.HttpUtils;
import org.apache.http.Consts;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component("soundToothCallListener")
public class SoundToothCallListener extends DefaultMessageListenerConcurrently {

    @Value("${call.out.callback.url}")
    private String callbackUrl;
    @Override
    public boolean consumeMessage(List<MessageExt> msgs) {
        if(CollectionUtils.isEmpty(msgs)){
            return true;
        }

        for (MessageExt msg : msgs){
            String content = null , data = null;
            try {
                content = new String(msg.getBody() , Consts.UTF_8);
                data =  HttpUtils.postJSON(callbackUrl ,content);
               LOGGER.info("发送声牙呼叫回掉数据结果:url={} , content={},result={}" , callbackUrl ,content , data);
            } catch (Exception e) {
                LOGGER.error("发送声牙呼叫回掉数据异常:url={} , content={},result={}" , callbackUrl ,content , data , e);
            }
        }


        return true;
    }
}
