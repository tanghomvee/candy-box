package com.slst.common.enums;

/**
 * 性别标志
 *
 * @author ddyunf
 */
public enum GenderEnum {
	FEMALE(0 , "女") ,MALE(1,"男"), UNDEFINED(-1 , "不确定");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	GenderEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static GenderEnum getByVal(Integer val){
		for (GenderEnum tmp : GenderEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}
	public  static GenderEnum getByDesc(String desc){
		for (GenderEnum tmp : GenderEnum.values()){
			if(tmp.desc.equals(desc)){
				return tmp;
			}
		}
		return null;
	}

}
