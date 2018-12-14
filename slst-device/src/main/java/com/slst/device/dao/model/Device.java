package com.slst.device.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_device")
public class Device extends BaseEntity {
	/**设备物理地址*/
	private String mac;
	/**设备A值*/
	private Integer aVal;
	/**设备N值*/
	private Integer nVal;
	/**代理商ID*/
	private Long agentId;
	/**代理商员工ID*/
	private Long agentEmpId;
	/**商家ID*/
	private Long venderId;
	/**门店ID*/
	private Long storeId;
	/**设备采集范围:半径:单位:厘米(0到999)(采集客户数据时单位似乎是用的米)*/
	private Integer distance;
	/**设备状态1为正在使用0为掉线-1为未激活*/
	private Integer state;

	public Integer getState(){
		return state;
	}

	public void setState(Integer state){
		this.state = state;
	}


	public Integer getDistance(){
		return distance;
	}

	public void setDistance(Integer distance){
		this.distance = distance;
	}


	public Long getStoreId(){
		return storeId;
	}

	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}


	public Long getVenderId(){
		return venderId;
	}

	public void setVenderId(Long venderId){
		this.venderId = venderId;
	}


	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getAgentEmpId() {
		return agentEmpId;
	}

	public void setAgentEmpId(Long agentEmpId) {
		this.agentEmpId = agentEmpId;
	}

	public Integer getNVal(){
		return nVal;
	}

	public void setNVal(Integer nVal){
		this.nVal = nVal;
	}


	public Integer getAVal(){
		return aVal;
	}

	public void setAVal(Integer aVal){
		this.aVal = aVal;
	}


	public String getMac(){
		return mac;
	}

	public void setMac(String mac){
		this.mac = mac;
	}

}