package com.candybox.common.enums;

/**
 * 维度
 *
 * @author ddyunf
 */
public enum DimensionEnum {
    GENDER(1,"性别"),AGE(2,"年龄"),CAREER(3,"职业")
	;

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	DimensionEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static DimensionEnum getByVal(Integer val){
		for (DimensionEnum tmp : DimensionEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
