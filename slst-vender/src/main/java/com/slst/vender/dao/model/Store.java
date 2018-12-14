package com.slst.vender.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_store")
public class Store extends BaseEntity {
	/**门店名字*/
	private String storeName;
	/**城市id*/
	private String cityId;
	/**城市名字*/
	private String cityName;
	/**门店详细地址*/
	private String address;
	/**经度.*/
	private String lat;
	/**纬度*/
	private String lng;
	/**商家ID*/
	private Long venderId;
	/**商家名称*/
	private String venderName;
	/**纳税唯一标识*/
	private String taxIdNum;

	public String getTaxIdNum(){
		return taxIdNum;
	}

	public void setTaxIdNum(String taxIdNum){
		this.taxIdNum = taxIdNum;
	}


	public String getVenderName(){
		return venderName;
	}

	public void setVenderName(String venderName){
		this.venderName = venderName;
	}


	public Long getVenderId(){
		return venderId;
	}

	public void setVenderId(Long venderId){
		this.venderId = venderId;
	}


	public String getLng(){
		return lng;
	}

	public void setLng(String lng){
		this.lng = lng;
	}


	public String getLat(){
		return lat;
	}

	public void setLat(String lat){
		this.lat = lat;
	}


	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}


	public String getCityName(){
		return cityName;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}


	public String getCityId(){
		return cityId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}


	public String getStoreName(){
		return storeName;
	}

	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

}