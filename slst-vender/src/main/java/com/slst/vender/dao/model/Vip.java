package com.slst.vender.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "t_vip")
public class Vip extends BaseEntity {
	/**会员名字*/
	private String vipname;
	/**会员电话*/
	private String mobile;
	/**会员生日*/
	private Date birthday;
	/**会员身份证*/
	private String idCard;
	/**会员性别0为女1为男*/
	private Integer sex;
	/**会员年龄*/
	private Integer age;

	public Integer getAge(){
		return age;
	}

	public void setAge(Integer age){
		this.age = age;
	}


	public Integer getSex(){
		return sex;
	}

	public void setSex(Integer sex){
		this.sex = sex;
	}


	public String getIdCard(){
		return idCard;
	}

	public void setIdCard(String idCard){
		this.idCard = idCard;
	}


	public Date getBirthday(){
		return birthday;
	}

	public void setBirthday(Date birthday){
		this.birthday = birthday;
	}


	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}


	public String getVipname(){
		return vipname;
	}

	public void setVipname(String vipname){
		this.vipname = vipname;
	}

}