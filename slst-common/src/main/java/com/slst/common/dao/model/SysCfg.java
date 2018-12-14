package com.slst.common.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sys_cfg")
public class SysCfg extends BaseEntity {
	/**程序标志*/
	private String code;
	/**标志对应的值*/
	private String codeVal;
	/**标志描述*/
	private String remark;

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}


	public String getCodeVal(){
		return codeVal;
	}

	public void setCodeVal(String codeVal){
		this.codeVal = codeVal;
	}


	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}


}