package com.slst.market.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.slst.common.components.RedisComponent;
import com.slst.common.mq.consumer.listeners.DefaultMessageListenerConcurrently;
import com.slst.market.dao.model.CallRecord;
import com.slst.market.service.CallRecordService;
import org.apache.http.Consts;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.slst.common.service.SoundToothService.*;


@Component("callRecordListener")
public class CallRecordListener extends DefaultMessageListenerConcurrently {

    @Resource
    CallRecordService callRecordService;
    @Resource
    RedisComponent redisComponent;

    @Override
    public boolean consumeMessage(List<MessageExt> msgs) {

        if (CollectionUtils.isEmpty(msgs)) {
            return true;
        }

        //用于跳出循环
        boolean flag = true;
        LOGGER.debug("进入通话扣费===================================================");
        for (MessageExt msg : msgs) {
            String content = null, data = null;

            content = new String(msg.getBody(), Consts.UTF_8);
            JSONObject result = JSONObject.parseObject(content);
            String billId = result.getString(SOUND_TOOTH_CALL_RESULT_BILL_ID);
            String callId = result.getString(SOUND_TOOTH_CALL_RESULT_CALL_ID);
            String from = result.getString(SOUND_TOOTH_CALL_RESULT_FROM);
            String to = result.getString(SOUND_TOOTH_CALL_RESULT_TO);
            Long duration = result.getLong(SOUND_TOOTH_CALL_RESULT_DURATION);

            LOGGER.info("话费消费:{}" , content);


            if (null == duration) {
                LOGGER.warn("话单未回调:{}", callId);
                continue;
            }
            if (duration.equals(-1L)) {
                LOGGER.warn("话单未回调主叫未接通,不扣费用:{}", billId);

                CallRecord callRecord = callRecordService.findByCallId(billId);
                if (null == callRecord) {
                    LOGGER.error("查询通话记录失败,billId(原callId)={}", billId);
                    continue;
                }

                callRecord.setDuration(duration);
                callRecord.setFee(0L);
                callRecord.setBillId(billId);
                callRecord.setAcctRcdId(0L);
                CallRecord rtnCallRecord = callRecordService.modifyCallRecord(callRecord);

                if (null == rtnCallRecord) {
                    LOGGER.error("修改callRecord失败,callRecordId={}", callRecord.getId());
                    continue;
                }


                continue;
            }


            try {
                if (redisComponent.lock(billId, 120L)) {
                    boolean rs = callRecordService.execute(billId, billId, duration);
                    if (rs) {
                        LOGGER.info("处理语音结算回掉数据结果:content={},result={}", content, data);
                    }
                    flag = flag && rs;

                } else {
                    flag = false;
                }

            } catch (Exception e) {
                LOGGER.error("处理语音结算回掉数据异常:content={},result={}", content, data, e);
                flag = false;
            } finally {
                redisComponent.unLock(billId);
            }
        }


        return flag;
    }

}
