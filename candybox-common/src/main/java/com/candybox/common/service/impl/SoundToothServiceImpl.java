package com.candybox.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.candybox.common.dao.model.CallRsp;
import com.candybox.common.dao.model.MatchRsp;
import com.candybox.common.dao.model.SysCfg;
import com.candybox.common.enums.SeparatorEnum;
import com.candybox.common.enums.SrvTypeEnum;
import com.candybox.common.enums.SysCfgEnum;
import com.candybox.common.service.CallRspService;
import com.candybox.common.service.MatchRspService;
import com.candybox.common.service.SoundToothService;
import com.candybox.common.service.SysCfgService;
import com.candybox.common.utils.DateUtils;
import com.candybox.common.utils.HttpUtils;
import com.candybox.common.web.vo.Msg;
import com.candybox.common.web.vo.Pager;
import com.google.common.collect.Maps;
import com.candybox.common.components.RedisComponent;
import com.candybox.common.constants.RedisKey;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 16:45
 */
@Service("soundToothService")
public class SoundToothServiceImpl implements SoundToothService {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Resource
    protected SysCfgService sysCfgService;

    @Resource
    protected CallRspService callRspService;

    @Resource
    protected MatchRspService matchRspService;

    @Resource
    protected RedisComponent redisComponent;

    @Override
    public Msg findCustomerDeviceByMac(String mac) {
        if(StringUtils.isEmpty(mac)){
            LOGGER.error("请求参数错误：{}", mac);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_ID_MAPPING.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_ID_MAPPING.getVal() , sysCfg);
            return Msg.error("请求客户手机信息系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        Map<String , String> params = Maps.newHashMap();
        params.put("mac", mac);

        try {
            LOGGER.info("请求客户手机信息,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("请求客户手机信息结果{},url={},params={}", retStr, url ,params );
            if (StringUtils.isEmpty(retStr)){
                return Msg.error("请求客户手机信息为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("请求客户手机信息解析为空");
            }
            if(!SOUND_TOOTH_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_CODE))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
            if(!SOUND_TOOTH_RESULT_STATUS_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_STATUS)) ||
                    !SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_MSG));
            }

            JSONObject dataJSON = resultJSON.getJSONObject(SOUND_TOOTH_RESULT_DATA);
            Map<String , Object> retMap = dataJSON.toJavaObject(Map.class);
            retMap.put(SOUND_TOOTH_CHARGE , retJSON.getBooleanValue(SOUND_TOOTH_CHARGE));
            return  Msg.success(retMap);

        } catch (Exception e) {
            LOGGER.error("请求客户手机信息异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }

    @Override
    public Msg createSMSTemplate(String  title , String content , String sign) {

        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(sign)){
            LOGGER.error("请求参数错误:title={},content={},sign={}", title ,content , sign);
            return Msg.error("请求参数错误");
        }
        Map<String, Object> template = Maps.newHashMap();
        template.put("title" , title);
        template.put("content" , content);
        template.put("sign" , sign);

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_SMS_CTX.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_SMS_CTX.getVal() , sysCfg);
            return Msg.error("请求创建短信模板信息系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        try{
            LOGGER.info("创建短信模板信息,url={},params={}",url ,template );
            String retStr = HttpUtils.postJSON(url , template);
            LOGGER.info("创建短信模板信息结果{},url={},params={}", retStr, url ,template );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("请求创建短信模板信息为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("请求创建短信模板信息解析为空");
            }
            if(!SOUND_TOOTH_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_CODE))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
            if(!SOUND_TOOTH_RESULT_CODE_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_CODE)) ||
                    !SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject dataJSON = resultJSON.getJSONObject(SOUND_TOOTH_RESULT);
            return  Msg.success(dataJSON.get(SOUND_TOOTH_RESULT_SMS_TMP_ID));

        }catch (Exception ex){
            LOGGER.error("创建短信模板信息异常,url={},params={}",url ,template ,ex);
        }

        return Msg.error();
    }

    @Override
    public Msg findSMSTemplateAuthStatus(String soundToothSmsTemplateId) {
        if(StringUtils.isEmpty(soundToothSmsTemplateId)){
            LOGGER.error("请求参数错误：{}", soundToothSmsTemplateId);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_SMS_CHECK_TMP.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_SMS_CHECK_TMP.getVal() , sysCfg);
            return Msg.error("查看短信模板系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        Map<String , String> params = Maps.newHashMap();
        params.put("sid", soundToothSmsTemplateId);

        try {
            LOGGER.info("查看短信模板状态信息,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("查看短信模板状态信息结果{},url={},params={}", retStr, url ,params );
            if (StringUtils.isEmpty(retStr)){
                return Msg.error("查看短信模板结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("查看短信模板解析为空");
            }
            if(!SOUND_TOOTH_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_CODE))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
            if(!SOUND_TOOTH_RESULT_CODE_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_CODE)) ||
                    !SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
               return Msg.error(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }
            JSONObject checkResult = resultJSON.getJSONObject(SOUND_TOOTH_RESULT);

            return  checkResult.getBoolean(SOUND_TOOTH_RESULT_SMS_TMP_CHECK)  ? Msg.success("审核通过") : Msg.error("审核失败");

        } catch (Exception e) {
            LOGGER.error("查看短信模板状态信息异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }

    @Override
    public Msg sendSMSMsg(String soundToothPhoneId, String soundToothSmsTemplateId) {
        if(StringUtils.isEmpty(soundToothSmsTemplateId) || StringUtils.isEmpty(soundToothPhoneId)){
            LOGGER.error("请求参数错误：{},{}", soundToothSmsTemplateId , soundToothPhoneId);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_SMS_SEND.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_SMS_SEND.getVal() , sysCfg);
            return Msg.error("发送短信系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        Map<String , String> params = Maps.newHashMap();
        params.put("sid", soundToothSmsTemplateId);
        params.put("phone_id", soundToothPhoneId);

        try {
            LOGGER.info("发送短信信息,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("发送短信信息结果{},url={},params={}", retStr, url ,params );
            if (StringUtils.isEmpty(retStr)){
                return Msg.error("发送短信结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("发送短信结果解析为空");
            }
            if(!SOUND_TOOTH_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_CODE))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
            if(!SOUND_TOOTH_RESULT_CODE_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_CODE)) ||
                    !SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            return  Msg.success("发送成功");

        } catch (Exception e) {
            LOGGER.error("发送短信信息异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }

    @Override
    public Msg findCustomerBaseInfoByMac(String mac) {

        if(StringUtils.isEmpty(mac)){
            LOGGER.error("请求参数错误：{},{}", mac);
            return Msg.error("请求参数错误");
        }

        MatchRsp matchRsp =  matchRspService.findByMac(mac);
        if(matchRsp == null || StringUtils.isEmpty(matchRsp.getResult())){
            Map<String , String> params = Maps.newHashMap();
            params.put("type", SOUND_TOOTH_MATCH_PORT_TYPE_MAC);
            params.put("user_equipment", mac.toLowerCase());
            return this.findCustomerBaseInfo(params);
        }

        String result = matchRsp.getResult();
        JSONObject resultJSON = JSON.parseObject(result);
        Map<String , JSONObject> map = resultJSON.toJavaObject(Map.class);
        JSONObject chargeJSON = new JSONObject();
        chargeJSON.put(SOUND_TOOTH_MATCH_PORT_RESULT_VAL ,false);
        map.put(SOUND_TOOTH_CHARGE, chargeJSON);
        Map<String , String> retMap = Maps.transformEntries(map, (Maps.EntryTransformer<String, JSONObject, String>) (key, value) -> value.getString(SOUND_TOOTH_MATCH_PORT_RESULT_VAL));
        return Msg.success(retMap);
    }

    @Override
    public Msg findCustomerBaseInfoByImei(String imei) {
        if(StringUtils.isEmpty(imei)){
            LOGGER.error("请求参数错误：{},{}", imei);
            return Msg.error("请求参数错误");
        }

        MatchRsp matchRsp =  matchRspService.findByImei(imei);
        if(matchRsp == null || StringUtils.isEmpty(matchRsp.getResult())){
            Map<String , String> params = Maps.newHashMap();
            params.put("type", SOUND_TOOTH_MATCH_PORT_TYPE_IMEI);
            params.put("user_equipment", imei);
            return this.findCustomerBaseInfo(params);
        }

        String result = matchRsp.getResult();
        JSONObject resultJSON = JSON.parseObject(result);
        Map<String , JSONObject> map = resultJSON.toJavaObject(Map.class);
        JSONObject chargeJSON = new JSONObject();
        chargeJSON.put(SOUND_TOOTH_MATCH_PORT_RESULT_VAL ,false);
        map.put(SOUND_TOOTH_CHARGE, chargeJSON);
        Map<String , String> retMap = Maps.transformEntries(map, (Maps.EntryTransformer<String, JSONObject, String>) (key, value) -> value.getString(SOUND_TOOTH_MATCH_PORT_RESULT_VAL));
        return Msg.success(retMap);

    }

    @Override
    public Msg findSmsToken() {
        String redisKey = RedisKey.SOUND_TOOTH_SMS_TOKEN;
        String token = redisComponent.get(redisKey);
        if(!StringUtils.isEmpty(token)){
            return Msg.success((Object) token);
        }
        String lockKey = redisKey + SeparatorEnum.UNDERLINE.getVal() + "LOCK";


        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_SMS_TOKEN.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_SMS_TOKEN.getVal() , sysCfg);
            return Msg.error("声牙获取TOKEN系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_AUTH_NAME.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_AUTH_NAME.getVal() , sysCfg);
            return Msg.error("声牙账户名系统参数未配置");
        }
        String name = sysCfg.getCodeVal();
        sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_AUTH_PWD.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_AUTH_PWD.getVal() , sysCfg);
            return Msg.error("声牙账户密码系统参数未配置");
        }

        String pwd = sysCfg.getCodeVal();


        sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_TOKEN_EXPIRE_TIME.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_TOKEN_EXPIRE_TIME.getVal() , sysCfg);
            return Msg.error("声牙TOKEN有效期限系统参数未配置");
        }

        Long expire = (Long.valueOf(sysCfg.getCodeVal()) -1) * 60 ;


        Map<String , String> params = Maps.newHashMap();
        params.put("name" , name);
        params.put("pwd" , pwd);

        if(!redisComponent.lock(lockKey , expire , 1)){
            token = redisComponent.get(redisKey);
            if(StringUtils.isEmpty(token)){
                LOGGER.warn("获取声牙TOKEN失败");
                return Msg.error();
            }
            return Msg.success((Object) token);
        }
        try{
            LOGGER.info("声牙获取TOKEN接口,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("声牙获取TOKEN结果{},url={},params={}", retStr, url ,params );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("声牙获取TOKEN结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("声牙获取TOKEN解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
            redisComponent.set(redisKey , resultJSON.getString(SOUND_TOOTH_AUTH_RESULT_TOKEN)  , expire);

            return Msg.success(resultJSON.get(SOUND_TOOTH_AUTH_RESULT_TOKEN));

        }catch (Exception ex){
            LOGGER.error("声牙获取TOKEN信息异常,url={},params={}",url ,params ,ex);
        }finally {
            redisComponent.unLock(lockKey);
        }


        return Msg.error();
    }

    @Override
    public Msg listPhoneNumByToken(String token , String callType , Integer page , Integer pageSize) {
        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_OUTER_PHONES.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_OUTER_PHONES.getVal() , sysCfg);
            return Msg.error("声牙获取号码池系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        Map<String , String> params = Maps.newHashMap();
        params.put("call_type" , callType);
        params.put("auth_token" , token);
        if(page != null && page > 0){
            params.put("page" , page.toString());
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 50;
        }
        params.put("page_size" , pageSize.toString());


        try{
            LOGGER.info("声牙获取号码池接口,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("声牙获取号码池结果{},url={},params={}", retStr, url ,params );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("声牙获取号码池结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("声牙获取号码池解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            JSONArray phones = resultJSON.getJSONArray(SOUND_TOOTH_RESULT_LIST);
            if (phones == null || phones.size() < 1){
                return Msg.error("号码池为空");
            }
            Map<String , String> pageMap = resultJSON.getJSONObject(SOUND_TOOTH_RESULT_PAGINATION).toJavaObject(Map.class);
            List<Map<String , String>> phoneList = phones.toJavaObject(new TypeReference<List<Map<String,String>>>(){

            });

            Map<String , Object> retMap = Maps.newHashMap();
            retMap.putAll(pageMap);
            retMap.put(SOUND_TOOTH_RESULT_LIST, phoneList);

            return Msg.success(retMap);

        }catch (Exception ex){
            LOGGER.error("声牙获取号码池信息异常,url={},params={}",url ,params ,ex);
        }

        return Msg.error();
    }

    @Override
    public Msg listPhoneNumByToken() {

        return this.listPhoneNumByToken(1 , 1000);
    }

    @Override
    public Msg listPhoneNumByToken(Integer pageNum , Integer pageSize) {
        Msg msg = findSmsToken();

        if(!msg.isSuccess()){
            return msg;
        }
        String token = (String) msg.getData();
        return this.listPhoneNumByToken(token , "3" , pageNum, pageSize);
    }


    @Override
    public Msg call(String toPhoneId , String fromPhoneNum) {
        Msg  msg = listPhoneNumByToken();
        if(!msg.isSuccess()){
            return msg;
        }
        Map<String , Object> results = (Map<String, Object>) msg.getData();
        List<Map<String ,String>> phones = (List<Map<String, String>>) results.get(SOUND_TOOTH_RESULT_LIST);
        Random random = new Random();
        int index = random.nextInt(phones.size());
        Map<String , String> phone = phones.get(index);
        String plId = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_ID , "");
        String uid = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_USR_ID , "");
        String area = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_AREA , "");
        String appId = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_APP , "");
        String acct = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_ACCT , "");
        String city = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_CIRY , "");
        String province = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_PROVINCE , "");
        String seatNo = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_SEAT , "");

        return call(plId , toPhoneId , fromPhoneNum);
    }


    @Override
    public Msg call(String plIdOrAreaCode, String toPhoneId , String fromPhoneNum) {
        Msg msg = findSmsToken();

        if(!msg.isSuccess()){
            return msg;

        }
        String token = (String) msg.getData();

        return call(token , plIdOrAreaCode, toPhoneId , fromPhoneNum);
    }


    @Override
    public Msg call(String token, String plIdOrAreaCode, String toPhoneId, String fromPhoneNum) {

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_CALL.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_CALL.getVal() , sysCfg);
            return Msg.error("声牙外呼系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Map<String , String> params = Maps.newHashMap();

        if(!NumberUtils.isDigits(plIdOrAreaCode) || plIdOrAreaCode.startsWith("0")){
            params.put("area_code" , plIdOrAreaCode);
        }else{
            params.put("pl_id" , plIdOrAreaCode);
        }

        params.put("phone_id" , toPhoneId);
        params.put("phone_from" , fromPhoneNum);
        params.put("auth_token" , token);

        try{
            LOGGER.info("声牙外呼接口,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("声牙外呼结果{},url={},params={}", retStr, url ,params );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("声牙外呼结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("声牙外呼结果解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            Map<String , String> retMap = resultJSON.toJavaObject(Map.class);

            this.saveSoundToothCallResult(toPhoneId ,fromPhoneNum ,retMap);

            return Msg.success(retMap);

        }catch (Exception ex){
            LOGGER.error("声牙外呼异常,url={},params={}",url ,params ,ex);
        }

        return Msg.error();
    }

    @Override
    public Msg findConsumeDetailByToken(Integer pageNum, Integer pageSize, String token) {

        if (StringUtils.isEmpty(token)){
            LOGGER.error("token错误");
            return Msg.error("token错误");
        }

        if(pageNum == null || pageNum < 1){
            pageNum = 1;
        }

        if(pageSize == null || pageSize < 1){
            pageSize = 10;
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_CONSUME_DETAIL.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_CONSUME_DETAIL.getVal() , sysCfg);
            return Msg.error("声牙消费明细系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Map<String , String> params = Maps.newHashMap();

        params.put("page" , pageNum.toString());
        params.put("page_size" , pageSize.toString());
        params.put("auth_token" , token);
        try{
            LOGGER.info("声牙消费明细接口,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("声牙消费明细结果{},url={},params={}", retStr, url ,params );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("声牙消费明细结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("声牙消费明细结果解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            Map<String , Object> retMap = Maps.newHashMap();
            JSONObject detail = resultJSON.getJSONObject(SOUND_TOOTH_CONSUME_RESULT_DETAIL);
            JSONObject page = resultJSON.getJSONObject(SOUND_TOOTH_RESULT_PAGINATION);
            JSONArray data = resultJSON.getJSONArray(SOUND_TOOTH_RESULT_LIST);
            retMap.put(SOUND_TOOTH_CONSUME_RESULT_DETAIL , detail.toJavaObject(Map.class));
            Pager pager = new Pager();
            pager.setPageNum(page.getInteger(SOUND_TOOTH_RESULT_PAGINATION_CURRENT));
            pager.setPageSize(page.getInteger(SOUND_TOOTH_RESULT_PAGINATION_SIZE));
            pager.setTotal(page.getInteger(SOUND_TOOTH_RESULT_PAGINATION_TOTAL));
            if(data != null && data.size() > 1){
                pager.setData(data.toJavaList(Map.class));
            }
            retMap.put(SOUND_TOOTH_RESULT_PAGINATION , pager);

            return Msg.success(retMap);

        }catch (Exception ex){
            LOGGER.error("声牙消费明细异常,url={},params={}",url ,params ,ex);
        }

        return Msg.error();
    }

    @Override
    public Msg findConsumeDetail() {
        Msg msg = findSmsToken();

        if(!msg.isSuccess()){
            return msg;
        }
        String token = (String) msg.getData();

        return this.findConsumeDetailByToken(null ,null , token);
    }

    @Override
    public Msg findAcctByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return Msg.error("token错误");
        }
        Msg msg = this.findConsumeDetailByToken(1,1, token);
        if(!msg.isSuccess()){
            return msg;
        }
        Map<String , Object> data = (Map<String, Object>) msg.getData();
        if(CollectionUtils.isEmpty(data)){
            return Msg.error("无账号信息");
        }
        Object  acct= data.get(SOUND_TOOTH_CONSUME_RESULT_DETAIL);

        return Msg.success(acct);
    }

    @Override
    public Msg findAcct() {
        Msg msg = findSmsToken();

        if(!msg.isSuccess()){
            return msg;
        }
        String token = (String) msg.getData();
        return findAcctByToken(token);

    }

    private Msg findCustomerBaseInfo(Map<String , String> params){

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_MATCH_PORT.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_MATCH_PORT.getVal() , sysCfg);
            return Msg.error("声牙获取用户画像系统参数未配置");
        }
        String url = sysCfg.getCodeVal();

        try{
            LOGGER.info("声牙获取用户画像接口,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("声牙获取用户画像结果{},url={},params={}", retStr, url ,params );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("声牙获取用户画像结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("声牙获取用户画像解析为空");
            }
            if(!SOUND_TOOTH_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_CODE))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            if(!SOUND_TOOTH_RESULT_CODE_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_CODE)) ||
                    !SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(resultJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(resultJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            //保存客户画像记录
            this.saveSoundToothCustomerBaseInfo(resultJSON.getJSONObject(SOUND_TOOTH_RESULT).toJSONString() , params.getOrDefault("user_equipment" , null));

            Map<String , JSONObject> map = resultJSON.getJSONObject(SOUND_TOOTH_RESULT).toJavaObject(Map.class);
            JSONObject chargeJSON = new JSONObject();
            chargeJSON.put(SOUND_TOOTH_MATCH_PORT_RESULT_VAL , retJSON.getString(SOUND_TOOTH_CHARGE));
            map.put(SOUND_TOOTH_CHARGE, chargeJSON);
            Map<String , String> retMap = Maps.transformEntries(map, (Maps.EntryTransformer<String, JSONObject, String>) (key, value) -> value.getString(SOUND_TOOTH_MATCH_PORT_RESULT_VAL));
            return Msg.success(retMap);

        }catch (Exception ex){
            LOGGER.error("声牙获取用户画像信息异常,url={},params={}",url ,params ,ex);
        }

        return Msg.error();
    }

    private void saveSoundToothCustomerBaseInfo(String result , String val){
        MatchRsp matchRsp = null;
        String mac = null , imei = null;
        if(!StringUtils.isEmpty(val)){
            if(val.contains(SeparatorEnum.COLON.getVal())){
                mac = val;
                matchRsp = matchRspService.findByMac(val);
            }else{
                imei = val;
                matchRsp = matchRspService.findByImei(val);
            }
        }
        if(matchRsp != null){
            matchRsp.setChanger("sys");
            matchRsp.setChangeTime(DateTime.now().toDate());
        }else{
            matchRsp = new MatchRsp();
            matchRsp.setCreator("sys");
            matchRsp.setSrvType(SrvTypeEnum.SOUND_TOOTH.getVal());
        }
        matchRsp.setMac(mac);
        matchRsp.setImei(imei);
        matchRsp.setResult(result);
        try{

            matchRsp =  matchRspService.save(matchRsp);
        }catch (Exception ex){
            LOGGER.error("保存客户画像记录异常:{}" , matchRsp , ex);
        }

    }
    private void saveSoundToothCallResult(String toPhoneId ,String fromPhoneNum , Map<String , String> result){
        CallRsp callRsp = new CallRsp();
        callRsp.setCreator("sys");
        callRsp.setToPhoneId(toPhoneId);
        callRsp.setReqDate(DateTime.now().toDate());
        callRsp.setFromPhoneNum(fromPhoneNum);
        callRsp.setApplyDate(DateUtils.str2DateTime(result.getOrDefault(SOUND_TOOTH_CALL_RESULT_DATE , null) , DateUtils.DateTimeFormatters.YYYY_SLASH_MM_SLASH_DD_HH_MM_SS));
        callRsp.setCallId(result.getOrDefault(SOUND_TOOTH_CALL_RESULT_ID , null));
        callRsp.setBillId(result.getOrDefault(SOUND_TOOTH_CALL_RESULT_USER , null));
        callRsp.setSrvType(SrvTypeEnum.SOUND_TOOTH.getVal());
        try{
            callRsp =  callRspService.save(callRsp);
        }catch (Exception ex){
            LOGGER.error("保存外呼记录异常:{}" , callRsp , ex);
        }

    }
}
