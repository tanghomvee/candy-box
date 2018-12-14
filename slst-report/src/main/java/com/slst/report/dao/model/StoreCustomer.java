package com.slst.report.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
@Entity
@Table(name = "t_store_customer")
public class StoreCustomer extends BaseEntity {
	/**用户ID*/
	private Long customerId;
	/**店铺ID*/
	private Long storeId;
	/**商家ID*/
	private Long venderId;
	/**到店时间*/
	private Date arriveTime;
	/**离店时间*/
	private Date leaveTime;
	/**停留时间*/
	private Integer stayTime;
	/**门店名称*/
	private String storeName;
	/**商家名称*/
	private String venderName;
	/**客户手机号码*/
	private String mobile;
	/**客户手机mac*/
	private String mac;

	/**客户到店次数(非数据库字段)*/
	@Transient
	private Integer nums;

	public Date getLeaveTime(){
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime){
		this.leaveTime = leaveTime;
	}


	public Date getArriveTime(){
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime){
		this.arriveTime = arriveTime;
	}


	public Long getStoreId(){
		return storeId;
	}

	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}


	public Long getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Long customerId){
		this.customerId = customerId;
	}

	public Long getVenderId() {
		return venderId;
	}

	public void setVenderId(Long venderId) {
		this.venderId = venderId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public Integer getStayTime() {
		return stayTime;
	}

	public void setStayTime(Integer stayTime) {
		this.stayTime = stayTime;
	}

	public Integer getNums() {
		return nums;
	}

	public void setNums(Integer nums) {
		this.nums = nums;
	}
}