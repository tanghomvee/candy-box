package com.candybox.common.service;

import com.candybox.common.dao.model.AreaCode;

public interface AreaCodeService extends BaseService<AreaCode, Long> {


    AreaCode findByCity(String city);

    AreaCode save(AreaCode areaCode);

}
