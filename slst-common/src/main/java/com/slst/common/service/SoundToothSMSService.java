package com.slst.common.service;

import com.slst.common.web.vo.Msg;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 16:32
 */
public interface SoundToothSMSService extends  SoundToothService{

    /**
     * 修改短信模板
     * @param title 标题
     * @param content 内容
     * @param sign 公司的短信签名
     * @param sid 模板id
     * @return
     */
    Msg modifySMSTemplate(String  title , String content , String sign , String sid);
    /**
     * 删除短信模板
     * @param templateId 模板id
     * @return
     */
    Msg delSMSTemplate(String templateId);

    /**
     * 列举短信模板
     * @param keyWord 关键词搜索
     * @param state  0(失败)1(通过)2(审核中)
     * @param platType  1(短信)2(闪信)
     * @param page
     * @param pageSize
     * @return
     */
    Msg listSMSTemplate(String keyWord ,Integer state, Integer platType , Integer page , Integer pageSize);

    /**
     * 绑定手机号码
     * @param privatePhoneId
     * @param bindPhone
     * @return
     */
    Msg bindPhone(String privatePhoneId , String bindPhone);
}
