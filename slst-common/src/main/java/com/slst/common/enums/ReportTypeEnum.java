package com.slst.common.enums;

/**
 * 设备上报数据类型
 *
 * @author ddyunf
 */
public enum ReportTypeEnum {
	DATA_WIFI_A("probea", "\1","接收wifi-a数据"),
	DATA_WIFI_B("probeb","\1","接收wifi-b数据"),
	DATA_FLASH("flash","\1","设备将本地存储的信息上报"),

	CFG_WIFI_A("configa","接收wifi-a配置信息"),
	CFG_WIFI_B("configb","接收wifi-n配置信息"),

	CHECK_WIFI("check","接收设备心跳信息"),

	AP_WIFI("ap","上报门店端wifi信息"),

	TIME_WIFI("unixtime","设备获取服务器时间"),
	REGISTER_WIFI("register","设备上报客户注册的信息"),

	;

	private String val;
	private String separator;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	ReportTypeEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	ReportTypeEnum(String val, String separator, String desc) {
		this.val = val;
		this.separator = separator;
		this.desc = desc;
	}

	public  static ReportTypeEnum getByVal(String val){
		for (ReportTypeEnum tmp : ReportTypeEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
