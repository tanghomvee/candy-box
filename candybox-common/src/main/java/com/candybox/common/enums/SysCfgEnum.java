package com.candybox.common.enums;

/**
 * 系统配置的参数
 *
 * @author ddyunf
 */
public enum SysCfgEnum {

	ALI_SMS_KEY("ALI_SMS_KEY","阿里短信服务KEY"),
	ALI_SMS_SECRET("ALI_SMS_SECRET","阿里短信服务秘钥"),


	;


	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	SysCfgEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static SysCfgEnum getByVal(String val){
		for (SysCfgEnum tmp : SysCfgEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}



}
