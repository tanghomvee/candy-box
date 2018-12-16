package com.candybox.common.enums;

/**
 * 客户停留标志
 *
 * @author ddyunf
 */
public enum StayStateEnum {
    STAYING(1 ,"驻留客户"),
	STAID (2 ,"到店客户"),
	STRANGER(3 ,"路过客户");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	StayStateEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static StayStateEnum getByVal(Integer val){
		for (StayStateEnum tmp : StayStateEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
