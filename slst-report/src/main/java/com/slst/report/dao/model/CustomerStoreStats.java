package com.slst.report.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
* Copyright (c) 2018. ddyunf.com all rights reserved
* @Description 客户每天到各个门店的情况
* @author  Homvee.Tang(tanghongwei@ddcloudf.com)
* @date 2018-05-21 9:27
* @version V1.0
*/
@Entity
@Table(name = "t_customer_store_stats")
public class CustomerStoreStats extends BaseEntity {
	/**用户ID*/
	private Long customerId;
	/**店铺ID*/
	private Long storeId;
	/**店铺名字*/
	private String storeName;
	/**商家ID*/
	private Long venderId;
	/**商家名字*/
	private String venderName;
	/**用户手机号*/
	private String mobile;
	/**用户手机物理信息*/
	private String mac;
	/**用户驻留时长:分钟*/
	private Long stayTime;
	/**用户到店次数*/
	private Long comeTimes;
	/**用户平均驻留时长:分钟*/
	private Double stayAvgTime;
	/**统计时间*/
	private Date statsTime;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Long getVenderId() {
		return venderId;
	}

	public void setVenderId(Long venderId) {
		this.venderId = venderId;
	}

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}


	public Long getStayTime() {
		return stayTime;
	}

	public void setStayTime(Long stayTime) {
		this.stayTime = stayTime;
	}

	public Long getComeTimes() {
		return comeTimes;
	}

	public void setComeTimes(Long comeTimes) {
		this.comeTimes = comeTimes;
	}

	public Double getStayAvgTime() {
		return stayAvgTime;
	}

	public void setStayAvgTime(Double stayAvgTime) {
		this.stayAvgTime = stayAvgTime;
	}

	public Date getStatsTime() {
		return statsTime;
	}

	public void setStatsTime(Date statsTime) {
		this.statsTime = statsTime;
	}

	@Override
	public String toString() {
		return "CustomerStoreStats{" +
				"customerId=" + customerId +
				", storeId=" + storeId +
				", storeName='" + storeName + '\'' +
				", venderId=" + venderId +
				", venderName='" + venderName + '\'' +
				", mobile='" + mobile + '\'' +
				", mac='" + mac + '\'' +
				", stayTime=" + stayTime +
				", comeTimes=" + comeTimes +
				", stayAvgTime=" + stayAvgTime +
				", statsTime=" + statsTime +
				", id=" + id +
				", yn=" + yn +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", changer='" + changer + '\'' +
				", changeTime=" + changeTime +
				'}';
	}
}