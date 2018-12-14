package com.slst.common.enums;

/**
 * 服务提供商类型
 *
 * @author ddyunf
 */
public enum SrvTypeEnum {
    SOUND_TOOTH(1,"声牙"),
	OTHER(-1,"其他");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	SrvTypeEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static SrvTypeEnum getByVal(Integer val){
		for (SrvTypeEnum tmp : SrvTypeEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
