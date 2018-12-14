package com.slst.common.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
* Copyright (c) 2018. ddyunf.com all rights reserved
* @Description match result
* @author  Homvee.Tang(tanghongwei@ddcloudf.com)
* @date 2018-06-13 10:14
* @version V1.0
*/
@Entity
@Table(name = "t_match_rsp")
public class MatchRsp extends BaseEntity {

	/**mac地址*/
	private String mac;

	/**imei*/
	private String imei;
	/**
	 * @see com.slst.common.enums.SrvTypeEnum
	 * 服务提供商
	 * */
	private Integer srvType;

	/**
	 * 客户画像数据
	 */
	private String result;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Integer getSrvType() {
		return srvType;
	}

	public void setSrvType(Integer srvType) {
		this.srvType = srvType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}