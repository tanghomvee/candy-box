package com.slst.customer.dao.model;

import com.slst.common.dao.model.BaseEntity;
import com.slst.common.enums.GenderEnum;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_customer")
public class Customer extends BaseEntity {
	/**客户手机物理地址*/
	private String mac;
	/**客户手机号*/
	private String mobile;
	/**第三方手机号暗号ID*/
	private String thirdPartyId;
	/**手机品牌*/
	private String mobileBrand;
	/**
	 * @see GenderEnum
	 * 用户性别:FEMALE(1 , "女") ,MALE(2,"男"), UNDEFINED(-1 , "不确定")
	 */
	private Integer sex;
	/**年龄*/
	private Integer age;
	/**客户信息所对应的客户设备ID*/
	private Long deviceId;
	/**客户年龄段*/
	private String ageSlot;
	/**客户小孩情况*/
	private String children;
	/**客户汽车情况*/
	private String car;
	/**客户教育情况*/
	private String education;
	/**客户职业情况*/
	private String career;
	/**客户收入情况*/
	private String incomeSlot;

	/**常驻城市*/
	private String permCity;

	/**设备档次*/
	private String mdLevel;

	/**房产情况*/
	private String house;
	/**婚姻情况*/
	private String married;

	/**兴趣爱好:不存入此表,仅作数据传输*/
	@Transient
	private String interest;


	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getThirdPartyId() {
		return thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public String getMobileBrand() {
		return mobileBrand;
	}

	public void setMobileBrand(String mobileBrand) {
		this.mobileBrand = mobileBrand;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}


	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getAgeSlot() {
		return ageSlot;
	}

	public void setAgeSlot(String ageSlot) {
		this.ageSlot = ageSlot;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getIncomeSlot() {
		return incomeSlot;
	}

	public void setIncomeSlot(String incomeSlot) {
		this.incomeSlot = incomeSlot;
	}

	public void setPermCity(String permCity) {
		this.permCity = permCity;
	}

	public String getPermCity() {
		return permCity;
	}

	public String getMdLevel() {
		return mdLevel;
	}

	public void setMdLevel(String mdLevel) {
		this.mdLevel = mdLevel;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getInterest() {
		return interest;
	}
}