package com.candybox.common.service.impl;

import com.candybox.common.dao.SysCfgDao;
import com.candybox.common.dao.model.SysCfg;
import com.candybox.common.enums.YNEnum;
import com.candybox.common.service.SysCfgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 16:26
 */
@Service("sysCfgService")
public class SysCfgServiceImpl extends BaseServiceImpl<SysCfg, Long> implements SysCfgService {

    @Resource
    private SysCfgDao sysCfgDao;

    @Override
    public SysCfg findByCode(String code) {
        return sysCfgDao.findByCodeAndYn(code , YNEnum.YES.getVal());
    }
}
