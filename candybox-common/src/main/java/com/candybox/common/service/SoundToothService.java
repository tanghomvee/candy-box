package com.candybox.common.service;

import com.candybox.common.web.vo.Msg;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 16:32
 */
public interface SoundToothService {

    /**
     * 回调字段名
     */
     String SOUND_TOOTH_CALL_RSP_STATUS = "status";
     String SOUND_TOOTH_CALL_RSP_MSG = "msg";
     String SOUND_TOOTH_CALL_RSP_STATUS_OK = "200";
     String SOUND_TOOTH_CALL_RSP_STATUS_ERR = "0";

     String SOUND_TOOTH_CALL_RESULT_CALL_ID = "callId";
     String SOUND_TOOTH_CALL_RESULT_BILL_ID = "userData";
     String SOUND_TOOTH_CALL_RESULT_FROM = "telA";
     String SOUND_TOOTH_CALL_RESULT_TO = "telB";

     String SOUND_TOOTH_CALL_RESULT_DURATION = "duration";
     String SOUND_TOOTH_CALL_RESULT_START_TIME = "startTime";



    /**********************************************/
    /**
     * 请求参数
     */
    String SOUND_TOOTH_CODE = "code";
    String SOUND_TOOTH_OK = "10000";
    String SOUND_TOOTH_MSG = "msg";
    String SOUND_TOOTH_RESULT = "result";
    String SOUND_TOOTH_RESULT_LIST = "list";
    String SOUND_TOOTH_RESULT_PAGINATION = "pagination";
    String SOUND_TOOTH_RESULT_PAGINATION_TOTAL = "total";
    String SOUND_TOOTH_RESULT_PAGINATION_CURRENT = "current";
    String SOUND_TOOTH_RESULT_PAGINATION_SIZE = "pageSize";
    String SOUND_TOOTH_RESULT_ERR_MSG = "errMsg";
    String SOUND_TOOTH_RESULT_ERR_CODE = "errCode";
    String SOUND_TOOTH_RESULT_CODE_OK = "0";
    String SOUND_TOOTH_RESULT_SUCCESS = "success";
    String SOUND_TOOTH_RESULT_SUCCESS_OK = "true";
    String SOUND_TOOTH_CHARGE = "charge";
    /**
     * Mobile
     */
    String SOUND_TOOTH_RESULT_STATUS = "status";
    String SOUND_TOOTH_RESULT_STATUS_OK = "200";
    String SOUND_TOOTH_RESULT_DATA = "datas";
    String SOUND_TOOTH_RESULT_IMEI = "imei_md5";
    String SOUND_TOOTH_RESULT_PHONE = "phone";
    String SOUND_TOOTH_RESULT_PHONE_ID = "pid";

    /**
     * SMS TEMPLATE
     */
    String SOUND_TOOTH_RESULT_SMS_TMP_ID = "sid";
    String SOUND_TOOTH_RESULT_SMS_TMP_CHECK = "check";
    /**
     * SMS TEMPLATE AUTH
     */
    String SOUND_TOOTH_RESULT_SMS_AUTHING = "106";
    /**
     * SEND SMS TEMPLATE
     */
//    String SOUND_TOOTH_RESULT_SEND_SMS_PHONE_ID = "phone_id";

    /**
     * matching-port type
     */
    String SOUND_TOOTH_MATCH_PORT_TYPE_MAC = "mac";
    String SOUND_TOOTH_MATCH_PORT_TYPE_IMEI = "imei";
//    String SOUND_TOOTH_MATCH_PORT_TYPE = "type";
//    String SOUND_TOOTH_MATCH_PORT_USR_DEVICE = "user_equipment";

    String SOUND_TOOTH_MATCH_PORT_RESULT_VAL = "value";

    String SOUND_TOOTH_MATCH_PORT_RESULT_INCOME = "income";
    String SOUND_TOOTH_MATCH_PORT_RESULT_CAREER = "occupation";
    String SOUND_TOOTH_MATCH_PORT_RESULT_GENDER = "gender";
    String SOUND_TOOTH_MATCH_PORT_RESULT_AGE = "agebin";
    String SOUND_TOOTH_MATCH_PORT_RESULT_CITY = "permanent_city";
    String SOUND_TOOTH_MATCH_PORT_RESULT_CHILDREN = "kids";
    String SOUND_TOOTH_MATCH_PORT_RESULT_EDUCATION = "edu";
    String SOUND_TOOTH_MATCH_PORT_RESULT_CAR = "car";
    String SOUND_TOOTH_MATCH_PORT_RESULT_BRAND = "cell_factory";
    String SOUND_TOOTH_MATCH_PORT_RESULT_SEGMENT = "segment";
    String SOUND_TOOTH_MATCH_PORT_RESULT_MODEL_LEVEL = "model_level";
    String SOUND_TOOTH_MATCH_PORT_RESULT_HOUSE = "house";
    String SOUND_TOOTH_MATCH_PORT_RESULT_MARRIED = "married";

    /**
     * 语音参数
     */
    /**
     * auth_token
     */
    String SOUND_TOOTH_AUTH_RESULT_TOKEN = "auth_token";
    /**
     * phone list
     */

    String SOUND_TOOTH_PHONE_RESULT_LIST_ID = "pl_id";
    String SOUND_TOOTH_PHONE_RESULT_LIST_USR_ID = "uid";
    String SOUND_TOOTH_PHONE_RESULT_LIST_PHONE_NUM = "phone";
    String SOUND_TOOTH_PHONE_RESULT_LIST_AREA = "area_code";
    String SOUND_TOOTH_PHONE_RESULT_LIST_APP = "appid";
    String SOUND_TOOTH_PHONE_RESULT_LIST_ACCT = "name";
    String SOUND_TOOTH_PHONE_RESULT_LIST_CIRY = "city";
    String SOUND_TOOTH_PHONE_RESULT_LIST_PROVINCE = "province";
    String SOUND_TOOTH_PHONE_RESULT_LIST_SEAT = "iSeatNo";



    /**
     * call
     */
    String SOUND_TOOTH_CALL_RESULT_STATE = "statusCode";
    String SOUND_TOOTH_CALL_RESULT_DATE = "dateCreated";
    String SOUND_TOOTH_CALL_RESULT_USER = "userData";
    String SOUND_TOOTH_CALL_RESULT_ID = "callId";
    String SOUND_TOOTH_CALL_RESULT_TELX = "telx";

    /**
     * consume
     */
    String SOUND_TOOTH_CONSUME_RESULT_LIST_ID = "id";
    String SOUND_TOOTH_CONSUME_RESULT_LIST_UID = "uid";
    String SOUND_TOOTH_CONSUME_RESULT_LIST_AMT = "money";
    String SOUND_TOOTH_CONSUME_RESULT_LIST_BALANCE = "balance";

    String SOUND_TOOTH_CONSUME_RESULT_DETAIL = "consume_detail";
    String SOUND_TOOTH_CONSUME_RESULT_DETAIL_IN = "total_in";
    String SOUND_TOOTH_CONSUME_RESULT_DETAIL_OUT = "total_out";
    String SOUND_TOOTH_CONSUME_RESULT_DETAIL_BALANCE = "balance";


    /**
     * 通过手机mac 获取客户手机信息
     * @param mac
     * @return
     */
    Msg findCustomerDeviceByMac(String mac);

    /**
     * 创建短信模板
     * @param title 标题
     * @param content 内容
     * @param sign 公司的短信签名
     * @return
     */
    Msg createSMSTemplate(String  title , String content , String sign);
    /**
     * 查看短信模板审核状态
     * @param soundToothSmsTemplateId
     * @return
     */
    Msg findSMSTemplateAuthStatus(String soundToothSmsTemplateId);
    /**
     * 请求发送短信
     * @param soundToothPhoneId
     * @param soundToothSmsTemplateId
     * @return
     */
    Msg sendSMSMsg(String soundToothPhoneId , String soundToothSmsTemplateId);


    /**
     * 通过MAC获取客户基础信息
     * @param mac
     * @return
     */
    Msg findCustomerBaseInfoByMac(String mac);
    /**
     * 通过imei获取客户基础信息
     * @param imei
     * @return
     */
    Msg findCustomerBaseInfoByImei(String imei);

    /**
     * 声⽛提供对应合作⽅的账号和密码，在进⾏短信相关服务前需调此接⼝获取auth_token，后⾯所有数据能⼒的接 ⼝需带上此值进⾏请求
     * @return
     */
    Msg findSmsToken();

    /**
     * 声⽛号码池列表
     * @param token
     * @param callType 2:表示隐私号码
     * @param page
     * @param pageSize
     * @return
     */
    Msg listPhoneNumByToken(String token , String callType , Integer page , Integer pageSize);

    /**
     * 声⽛号码池列表
     * @return
     */
    Msg listPhoneNumByToken();


    /**
     * 号码池分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    Msg listPhoneNumByToken(Integer pageNum , Integer pageSize);


    /**
     * 拨打外呼
     * @param plIdOrAreaCode
     * @param toPhoneId
     * @param fromPhoneNum
     * @return
     */
    Msg call(String plIdOrAreaCode , String toPhoneId , String fromPhoneNum);



    /**
     * 拨打外呼
     * @param token
     * @param fromPhoneId
     * @param toPhoneId
     * @param fromPhoneNum
     * @return
     */
    Msg call(String token , String fromPhoneId , String toPhoneId , String fromPhoneNum);

    /**
     * 拨打外呼
     * @param toPhoneId
     * @param fromPhoneNum
     * @return
     */
    Msg call(String toPhoneId,String fromPhoneNum);

    /**
     * 获取消费详情
     * @param pageNum
     * @param pageSize
     * @return
     */
    Msg findConsumeDetailByToken(Integer pageNum , Integer pageSize , String token);

    /**
     * 获取消费详情
     * @return
     */
    Msg findConsumeDetail();

    /**
     * 获取帐号消费
     * @param token
     * @return
     */
    Msg findAcctByToken(String token);

    /**
     * 获取帐号消费
     * @return
     */
    Msg findAcct();
}
