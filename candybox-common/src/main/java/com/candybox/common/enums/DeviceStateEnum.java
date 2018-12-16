package com.candybox.common.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum DeviceStateEnum {
    RUNNING(1,"设备正在使用"), OFFLINE(0,"设备掉线"), UN_ACTIVE(-1,"设备未激活");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	DeviceStateEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static DeviceStateEnum getByVal(Integer val){
		for (DeviceStateEnum tmp : DeviceStateEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
