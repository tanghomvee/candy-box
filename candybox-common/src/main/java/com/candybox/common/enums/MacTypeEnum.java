package com.candybox.common.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum MacTypeEnum {
    MOBILE(1,"手机MAC"), OTHER(-1,"其他MAC");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	MacTypeEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static MacTypeEnum getByVal(Integer val){
		for (MacTypeEnum tmp : MacTypeEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
