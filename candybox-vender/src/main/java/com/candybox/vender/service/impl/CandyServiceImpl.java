package com.candybox.vender.service.impl;

import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.common.web.vo.Pager;
import com.candybox.vender.dao.CandyDao;
import com.candybox.vender.dao.model.Candy;
import com.candybox.vender.service.CandyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:15
 */
@Service("candyService")
public class CandyServiceImpl extends BaseServiceImpl<Candy, Long> implements CandyService {
    @Resource
    private CandyDao candyDao;

    @Override
    public Pager listPage(Pager pager) {
        return candyDao.listPage(pager);
    }
}
