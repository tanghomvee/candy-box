package com.slst.common.service.impl;

import com.slst.common.dao.AreaCodeDao;
import com.slst.common.dao.model.AreaCode;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.AreaCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-06-13 10:03
 */
@Service("areaCodeService")
public class AreaCodeServiceImpl extends BaseServiceImpl<AreaCode, Long> implements AreaCodeService {

    @Resource
    private AreaCodeDao areaCodeDao;


    @Override
    public AreaCode findByCity(String city) {
        return areaCodeDao.findByCityContaining(city);
    }

    @Override
    public AreaCode save(AreaCode areaCode) {
        areaCode.setYn(YNEnum.YES.getVal());
        areaCode.setCreator("sys");
        areaCode.setCreateTime(new Date());

        return areaCodeDao.save(areaCode);
    }
}
