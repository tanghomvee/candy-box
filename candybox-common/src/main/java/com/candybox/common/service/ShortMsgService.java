package com.candybox.common.service;

import com.candybox.common.dao.model.ShortMsg;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface ShortMsgService extends BaseService <ShortMsg, Long> {


    /**
     * send msg
     * @param from
     * @param to
     * @param content
     * @param remark
     * @return
     */
    ShortMsg save(String from , String to , String content , String remark);

    /**
     * send chk code
     * @param to
     * @param content
     * @param remark
     * @return
     */
    ShortMsg sendChkMsg(String to , String content , String remark);
}
