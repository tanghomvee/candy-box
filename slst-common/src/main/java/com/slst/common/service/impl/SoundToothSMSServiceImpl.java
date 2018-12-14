package com.slst.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.service.SoundToothSMSService;
import com.slst.common.utils.HttpUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-20 09:51
 */
@Service("soundToothSMSService")
public class SoundToothSMSServiceImpl extends SoundToothServiceImpl implements SoundToothSMSService {



    @Override
    public Msg modifySMSTemplate(String title, String content, String sign, String sid) {
        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(sid)){
            LOGGER.error("请求参数错误:title={},content={},sign={},sid={}", title ,content , sign,sid);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_CHANGE.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_CHANGE.getVal() , sysCfg);
            return Msg.error("请求修改短信模板信息系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Msg msg = super.findSmsToken();
        if (!msg.isSuccess()){
            return msg;
        }
        Map<String, Object> template = Maps.newHashMap();
        template.put("title" , title);
        template.put("content" , content);
        template.put("sign" , sign);
        template.put("sid" , sid);
        template.put("auth_token" , msg.getData());

        try{
            LOGGER.info("修改短信模板信息,url={},params={}",url ,template );
            String retStr = HttpUtils.postJSON(url , template);
            LOGGER.info("修改短信模板信息结果{},url={},params={}", retStr, url ,template );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("请求修改短信模板信息为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("请求修改短信模板信息解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            return  Msg.success(resultJSON.get(SOUND_TOOTH_RESULT_SMS_TMP_ID));

        }catch (Exception ex){
            LOGGER.error("修改短信模板信息异常,url={},params={}",url ,template ,ex);
        }

        return Msg.error();
    }

    @Override
    public Msg createSMSTemplate(String title, String content, String sign) {
        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(sign)){
            LOGGER.error("请求参数错误:title={},content={},sign={}", title ,content , sign);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_ADD.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_ADD.getVal() , sysCfg);
            return Msg.error("请求增加短信模板信息系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Msg msg = super.findSmsToken();
        if (!msg.isSuccess()){
            return msg;
        }

        Map<String, Object> template = Maps.newHashMap();
        template.put("title" , title);
        template.put("content" , content);
        template.put("sign" , sign);
        template.put("auth_token" , msg.getData());

        try{
            LOGGER.info("增加短信模板信息,url={},params={}",url ,template );
            String retStr = HttpUtils.postJSON(url , template);
            LOGGER.info("增加短信模板信息结果{},url={},params={}", retStr, url ,template );

            if (StringUtils.isEmpty(retStr)){
                return Msg.error("请求增加短信模板信息为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("请求增加短信模板信息解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            return  Msg.success(resultJSON.get(SOUND_TOOTH_RESULT_SMS_TMP_ID));

        }catch (Exception ex){
            LOGGER.error("增加短信模板信息异常,url={},params={}",url ,template ,ex);
        }

        return Msg.error();
    }

    @Override
    public Msg sendSMSMsg(String soundToothPhoneId, String soundToothSmsTemplateId) {
        if(StringUtils.isEmpty(soundToothSmsTemplateId) || StringUtils.isEmpty(soundToothPhoneId)){
            LOGGER.error("请求参数错误：{},{}", soundToothSmsTemplateId , soundToothPhoneId);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_SMS_SEND.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_SMS_SEND.getVal() , sysCfg);
            return Msg.error("发送短信系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Msg msg = super.findSmsToken();
        if (!msg.isSuccess()){
            return msg;
        }
        Map<String , String> params = Maps.newHashMap();
        params.put("sid", soundToothSmsTemplateId);
        params.put("phone_id", soundToothPhoneId);
        params.put("auth_token" , msg.getData() + "");

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
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            return  Msg.success("发送成功");

        } catch (Exception e) {
            LOGGER.error("发送短信信息异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }

    @Override
    public Msg delSMSTemplate(String soundToothSmsTemplateId) {
        if(StringUtils.isEmpty(soundToothSmsTemplateId) ){
            LOGGER.error("请求参数错误：{},{}", soundToothSmsTemplateId);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_DEL.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_DEL.getVal() , sysCfg);
            return Msg.error("删除短信模板系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Msg msg = super.findSmsToken();
        if (!msg.isSuccess()){
            return msg;
        }
        Map<String , String> params = Maps.newHashMap();
        params.put("sid", soundToothSmsTemplateId);
        params.put("auth_token" , msg.getData() + "");

        try {
            LOGGER.info("删除短信模板,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("删除短信模板结果{},url={},params={}", retStr, url ,params );
            if (StringUtils.isEmpty(retStr)){
                return Msg.error("删除短信模板结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("删除短信模板结果解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            return  Msg.success("删除模板成功");

        } catch (Exception e) {
            LOGGER.error("删除短信模板异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }

    @Override
    public Msg listSMSTemplate(String keyWord, Integer state, Integer platType, Integer page, Integer pageSize) {


        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_LIST.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_LIST.getVal() , sysCfg);
            return Msg.error("列举短信模板系统参数未配置");
        }
        String url = sysCfg.getCodeVal();


        Map<String , Object> params = Maps.newHashMap();
        params.put("title_keyword", keyWord);
        params.put("is_adopt" ,state);
        params.put("plat_type" , platType);
        params.put("page_size" ,pageSize);
        params.put("page" , page);

        try {
            LOGGER.info("列举短信模板,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("列举短信模板结果{},url={},params={}", retStr, url ,params );
            if (StringUtils.isEmpty(retStr)){
                return Msg.error("列举短信模板结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("列举短信模板结果解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject pageJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
            JSONObject pageInfo = pageJSON.getJSONObject(SOUND_TOOTH_RESULT_PAGINATION);
            Pager pager = new Pager();
            pager.setPageSize(pageSize);
            pager.setTotal(pageInfo.getInteger(SOUND_TOOTH_RESULT_PAGINATION_TOTAL));
            pager.setPageNum(pageInfo.getInteger(SOUND_TOOTH_RESULT_PAGINATION_CURRENT));
            pager.setData(pageJSON.getJSONArray(SOUND_TOOTH_RESULT_LIST).toJavaList(Map.class));
            return  Msg.success(pager);

        } catch (Exception e) {
            LOGGER.error("列举短信模板异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }

    @Override
    public Msg bindPhone(String privatePhoneId, String bindPhone) {
        if(StringUtils.isEmpty(privatePhoneId) || StringUtils.isEmpty(bindPhone) ){
            LOGGER.error("请求参数错误：{},{}", privatePhoneId , bindPhone);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_CALL_BIND.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_CALL_BIND.getVal() , sysCfg);
            return Msg.error("绑定呼叫号码系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Msg msg = super.findSmsToken();
        if (!msg.isSuccess()){
            return msg;
        }
        Map<String , String> params = Maps.newHashMap();
        params.put("phone_id", privatePhoneId);
        params.put("bind_phone", bindPhone);
        params.put("auth_token" , msg.getData() + "");

        try {
            LOGGER.info("绑定呼叫号码,url={},params={}",url ,params );
            String retStr = HttpUtils.postJSON(url , params);
            LOGGER.info("绑定呼叫号码结果{},url={},params={}", retStr, url ,params );
            if (StringUtils.isEmpty(retStr)){
                return Msg.error("绑定呼叫号码结果为空");
            }
            JSONObject retJSON = JSON.parseObject(retStr);
            if (retJSON == null){
                return Msg.error("绑定呼叫号码结果解析为空");
            }
            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }

            JSONObject result = retJSON.getJSONObject(SOUND_TOOTH_RESULT);

            return  Msg.success(result.get(SOUND_TOOTH_CALL_RESULT_TELX));

        } catch (Exception e) {
            LOGGER.error("绑定呼叫号码异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();

    }

    @Override
    public Msg findSMSTemplateAuthStatus(String soundToothSmsTemplateId) {
        if(StringUtils.isEmpty(soundToothSmsTemplateId)){
            LOGGER.error("请求参数错误：{}", soundToothSmsTemplateId);
            return Msg.error("请求参数错误");
        }

        SysCfg sysCfg = sysCfgService.findByCode(SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_CHK.getVal());
        if(sysCfg == null || StringUtils.isEmpty(sysCfg.getCodeVal())){
            LOGGER.error("获取声牙请求参数[{}]失败：{}", SysCfgEnum.SOUND_TOOTH_V2_SMS_TMP_CHK.getVal() , sysCfg);
            return Msg.error("查看短信模板系统参数未配置");
        }
        String url = sysCfg.getCodeVal();
        Msg msg = super.findSmsToken();
        if (!msg.isSuccess()){
            return msg;
        }
        Map<String , String> params = Maps.newHashMap();
        params.put("sid", soundToothSmsTemplateId);
        params.put("auth_token" , msg.getData() + "");

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


            if(!SOUND_TOOTH_RESULT_SUCCESS_OK.equalsIgnoreCase(retJSON.getString(SOUND_TOOTH_RESULT_SUCCESS))){
                return Msg.error(retJSON.getString(SOUND_TOOTH_RESULT_ERR_MSG));
            }
            JSONObject resultJSON = retJSON.getJSONObject(SOUND_TOOTH_RESULT);
//            JSONObject checkResult = resultJSON.getJSONObject(SOUND_TOOTH_RESULT_SMS_TMP_CHECK);
            return  resultJSON.getBoolean(SOUND_TOOTH_RESULT_SMS_TMP_CHECK)  ? Msg.success("审核通过") : Msg.error("审核失败");

        } catch (Exception e) {
            LOGGER.error("查看短信模板状态信息异常,url={},params={}",url ,params ,e);
        }

        return Msg.error();
    }
}
