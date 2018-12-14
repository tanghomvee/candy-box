package com.slst.common.service.impl;

import com.slst.common.dao.SysCfgDao;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SysCfgService;
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
public class SysCfgServiceImpl extends BaseServiceImpl<SysCfg , Long> implements SysCfgService {

    @Resource
    private SysCfgDao sysCfgDao;

    @Override
    public SysCfg findByCode(String code) {
        return sysCfgDao.findByCodeAndYn(code , YNEnum.YES.getVal());
    }
}
