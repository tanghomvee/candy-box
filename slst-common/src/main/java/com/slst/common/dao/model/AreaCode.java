package com.slst.common.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
* Copyright (c) 2018. ddyunf.com all rights reserved
* @Description Network Interface Card（网卡信息表）
* @author  Homvee.Tang(tanghongwei@ddcloudf.com)
* @date 2018-06-13 10:14
* @version V1.0
*/
@Entity
@Table(name = "t_area_code")
public class AreaCode extends BaseEntity {

	private String areaCode;

	private String city;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}