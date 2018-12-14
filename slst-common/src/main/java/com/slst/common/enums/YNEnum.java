package com.slst.common.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum YNEnum {
    YES(1,"有效"), NO(-1,"无效");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	YNEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static YNEnum getByVal(Integer val){
		for (YNEnum tmp : YNEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
