package com.slst.vender.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_vender_emp")
public class VenderEmp extends BaseEntity {

	/**员工名字*/
	private String empName;
	/**门店ID*/
	private Long storeId;
	/**商家ID*/
	private Long venderId;
	/**联系电话*/
	private String mobile;
	/**详细地址*/
	private String address;
	/**
	 * 职位描述
	 */
	private String jobDesc;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 数据是否有效
	*/
	private Integer yn;

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}


	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}


	public Long getVenderId(){
		return venderId;
	}

	public void setVenderId(Long venderId){
		this.venderId = venderId;
	}


	public Long getStoreId(){
		return storeId;
	}

	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}


	public String getEmpName(){
		return empName;
	}

	public void setEmpName(String empName){
		this.empName = empName;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public Integer getYn() {
		return yn;
	}

	@Override
	public void setYn(Integer yn) {
		this.yn = yn;
	}
}