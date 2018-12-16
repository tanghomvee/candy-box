package com.candybox.common.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum OnlineEnum {
    ONLINE(1,"上线"), OFFLINE(-1,"下线");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	OnlineEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static OnlineEnum getByVal(Integer val){
		for (OnlineEnum tmp : OnlineEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
