package com.candybox.common.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum CandyKindEnum {
	GOLD(1,"黄金"),
	DIAMOND(2,"钻石"),

	;



	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	CandyKindEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static CandyKindEnum getByVal(Integer val){
		for (CandyKindEnum tmp : CandyKindEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
