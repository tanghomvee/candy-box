package com.slst.report.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
* Copyright (c) 2018. ddyunf.com all rights reserved
* @Description 各个门店的客户情况
* @author  Homvee.Tang(tanghongwei@ddcloudf.com)
* @date 2018-05-21 9:27
* @version V1.0
*/
@Entity
@Table(name = "t_store_customer_stats")
public class StoreCustomerStats extends BaseEntity {

	/**店铺ID*/
	private Long storeId;
	/**店铺名字*/
	private String storeName;
	/**商家ID*/
	private Long venderId;
	/**商家名字*/
	private String venderName;
	/**总的到店人数*/
	private Integer clientNum;
	/**新客户人数*/
	private Integer newClientNum;
	/**老客户人数*/
	private Integer oldClientNum;

	/**新用户平均驻留时长:分钟*/
	private Double newClientStayAvgTime;
	/**老用户平均驻留时长:分钟*/
	private Double oldClientStayAvgTime;
	/**用户平均驻留时长:分钟*/
	private Double stayAvgTime;
	/**统计时间*/
	private Date statsTime;


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

	public Integer getClientNum() {
		return clientNum;
	}

	public void setClientNum(Integer clientNum) {
		this.clientNum = clientNum;
	}

	public Integer getNewClientNum() {
		return newClientNum;
	}

	public void setNewClientNum(Integer newClientNum) {
		this.newClientNum = newClientNum;
	}

	public Integer getOldClientNum() {
		return oldClientNum;
	}

	public void setOldClientNum(Integer oldClientNum) {
		this.oldClientNum = oldClientNum;
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

	public Double getNewClientStayAvgTime() {
		return newClientStayAvgTime;
	}

	public void setNewClientStayAvgTime(Double newClientStayAvgTime) {
		this.newClientStayAvgTime = newClientStayAvgTime;
	}

	public Double getOldClientStayAvgTime() {
		return oldClientStayAvgTime;
	}

	public void setOldClientStayAvgTime(Double oldClientStayAvgTime) {
		this.oldClientStayAvgTime = oldClientStayAvgTime;
	}
}