package com.slst.common.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.slst.common.dao.model.CallRsp;
import com.slst.common.mq.producer.DefaultProducerService;
import com.slst.common.service.CallRspService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

import static com.slst.common.service.SoundToothService.*;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-07-03 10:11
 */
@Controller
@RequestMapping(path = "/soundtooth")
public class SoundToothCallbackCtrl extends BaseCtrl{

    @Resource
    private CallRspService callRspService;

    @Resource
    private DefaultProducerService defaultProducerService;

    @Value("${mq.soundtooth.producer.topic.call}")
    private String topic;



    @RequestMapping(path = {"/callout"} , method = {RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public Map<String , String> callOutCallback(@RequestBody String data){


        LOGGER.info("声牙外呼回掉数据:{}" , data);

        Map<String , String> retMap = Maps.newHashMap();
        retMap.put(SOUND_TOOTH_CALL_RSP_STATUS, SOUND_TOOTH_CALL_RSP_STATUS_ERR);

        if(ObjectUtils.isEmpty(data)){
            LOGGER.error("声牙外呼回掉数据为空");
            retMap.put(SOUND_TOOTH_CALL_RSP_MSG, "数据为空");
            return retMap;
        }

        JSONObject result = JSONObject.parseObject(data);

        if(result == null){
            LOGGER.error("声牙外呼回掉数据格式错误:{}" , data);
            retMap.put(SOUND_TOOTH_CALL_RSP_MSG, "数据格式错误");
            return retMap;
        }

        String billId = result.getString(SOUND_TOOTH_CALL_RESULT_BILL_ID);
        String callId = result.getString(SOUND_TOOTH_CALL_RESULT_CALL_ID);
        String from = result.getString(SOUND_TOOTH_CALL_RESULT_FROM);
        String to = result.getString(SOUND_TOOTH_CALL_RESULT_TO);
        Long duration = result.getLong(SOUND_TOOTH_CALL_RESULT_DURATION);
        String startTime = result.getString(SOUND_TOOTH_CALL_RESULT_START_TIME);
        if(StringUtils.isEmpty(startTime)){
            duration = -1L;
            LOGGER.info("声牙外呼回掉数据主叫未接通:{}" ,callId);
            result.put(SOUND_TOOTH_CALL_RESULT_DURATION , duration);
        }

        if(StringUtils.isEmpty(billId) || StringUtils.isEmpty(callId)){
            LOGGER.error("声牙外呼回掉数据话单ID或者扣费ID为空:callId={} , billId={}" , callId , billId);
            retMap.put(SOUND_TOOTH_CALL_RSP_MSG, "话单ID或者扣费ID为空");
            return retMap;
        }
        this.sendSoundToothCallback(result.toJSONString());
        CallRsp callRsp = callRspService.findByBillId(billId);
        if(callRsp == null){
            LOGGER.error("系统不存在声牙外呼回掉数据:callId={} , billId={}" , callId , billId);
            retMap.put(SOUND_TOOTH_CALL_RSP_MSG, "话单ID或者扣费ID不存在系统");
            return retMap;
        }

        if(StringUtils.isEmpty(callRsp.getFromPhoneNum())){
            callRsp.setFromPhoneNum(from);
        }

        if(StringUtils.isEmpty(callRsp.getToPhoneNum())){
            callRsp.setToPhoneNum(to);
        }
        callRsp.setDuration(duration);
        callRsp.setRspDate(DateTime.now().toDate());
        callRsp.setChangeTime(DateTime.now().toDate());
        callRsp.setChanger("sys");

        callRsp = callRspService.save(callRsp);
        if(callRsp == null){
            LOGGER.error("保存声牙外呼回掉数据失败:{}" , callRsp);
            retMap.put(SOUND_TOOTH_CALL_RSP_MSG, "保存数据失败");
            return retMap;
        }

        retMap.put(SOUND_TOOTH_CALL_RSP_STATUS, SOUND_TOOTH_CALL_RSP_STATUS_OK);
        return retMap;
    }

    private void sendSoundToothCallback(String content){
        try {
            defaultProducerService.sendMsg(topic , content);
        } catch (Exception e) {
            LOGGER.error("发送声牙语音回调异常:topic = {} ,content={}" , topic ,content ,e);
        }
    }
}
