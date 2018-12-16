package com.candybox.common.enums;

/**
 *
 * @author ddyunf
 */
public enum OperateKindEnum {
	SIGN(1,"签到"),
	REFERRER(2,"c"),

	;



	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	OperateKindEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static OperateKindEnum getByVal(Integer val){
		for (OperateKindEnum tmp : OperateKindEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
