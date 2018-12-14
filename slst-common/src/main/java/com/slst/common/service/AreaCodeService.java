package com.slst.common.service;

import com.slst.common.dao.model.AreaCode;

public interface AreaCodeService extends BaseService<AreaCode, Long> {


    AreaCode findByCity(String city);

    AreaCode save(AreaCode areaCode);

}
