package com.slst.vender.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_vender")
public class Vender extends BaseEntity {
	/**公司名字*/
	private String companyName;
	/**公司简称*/
	private String simpleName;
	/**联系人名字*/
	private String contact;
	/**联系电话*/
	private String mobile;
	/**城市ID*/
	private String  cityId;
	/**城市名字*/
	private String cityName;
	/**详细地址*/
	private String address;
	/**行业ID*/
	private String industryId;
	/**行业名字*/
	private String industryName;
	/**营业执照*/
	private String bizlicense;
	/**公司LOGO*/
	private String logo;
	/**公司官网*/
	private String website;
	/**代理商ID*/
	private Long agentId;
	/**代理商员工ID*/
	private Long agentEmpId;
	/**用户ID*/
	private Long userId;
	/**纳税唯一标识*/
	private String taxIdNum;

	public String getTaxIdNum(){
		return taxIdNum;
	}

	public void setTaxIdNum(String taxIdNum){
		this.taxIdNum = taxIdNum;
	}


	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}


	public Long getAgentId(){
		return agentId;
	}

	public void setAgentId(Long agentId){
		this.agentId = agentId;
	}

	public Long getAgentEmpId() {
		return agentEmpId;
	}

	public void setAgentEmpId(Long agentEmpId) {
		this.agentEmpId = agentEmpId;
	}

	public String getWebsite(){
		return website;
	}

	public void setWebsite(String website){
		this.website = website;
	}


	public String getLogo(){
		return logo;
	}

	public void setLogo(String logo){
		this.logo = logo;
	}


	public String getBizlicense(){
		return bizlicense;
	}

	public void setBizlicense(String bizlicense){
		this.bizlicense = bizlicense;
	}


	public String getIndustryName(){
		return industryName;
	}

	public void setIndustryName(String industryName){
		this.industryName = industryName;
	}


	public String getIndustryId(){
		return industryId;
	}

	public void setIndustryId(String industryId){
		this.industryId = industryId;
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


	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}


	public String getContact(){
		return contact;
	}

	public void setContact(String contact){
		this.contact = contact;
	}


	public String getSimpleName(){
		return simpleName;
	}

	public void setSimpleName(String simpleName){
		this.simpleName = simpleName;
	}


	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

}