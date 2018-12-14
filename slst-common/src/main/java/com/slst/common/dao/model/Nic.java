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
@Table(name = "t_nic")
public class Nic extends BaseEntity {
	/**mac地址*/
	private String mac;
	/**
	 * @see com.slst.common.enums.MacTypeEnum
	 * 设备类型
	 *
	 **/
	private Integer macType;
	/**制造商英文*/
	private String corporationEn;
	/**制造商中文*/
	private String corporationCn;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getMacType() {
		return macType;
	}

	public void setMacType(Integer macType) {
		this.macType = macType;
	}

	public String getCorporationEn() {
		return corporationEn;
	}

	public void setCorporationEn(String corporationEn) {
		this.corporationEn = corporationEn;
	}

	public String getCorporationCn() {
		return corporationCn;
	}

	public void setCorporationCn(String corporationCn) {
		this.corporationCn = corporationCn;
	}
}