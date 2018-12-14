package com.slst.customer.task.impl;

import com.slst.customer.task.CustomerTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-27 17:57
 */
@Component("customerTask")
public class CustomerTaskImpl implements CustomerTask {

    @Async
    @Override
    public void complete() {
        //TODO
        System.out.println("task----------->"+System.currentTimeMillis());
    }
}
