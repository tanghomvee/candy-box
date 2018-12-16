package com.candybox.common.enums;

/**
 * 系统配置的参数
 *
 * @author ddyunf
 */
public enum SysCfgEnum {

	SOUND_TOOTH_APP_KEY("SOUND_TOOTH_APP_KEY","声牙APP_KEY"),
	SOUND_TOOTH_ID_MAPPING("SOUND_TOOTH_ID_MAPPING","声牙匹配电话号码接口"),
	SOUND_TOOTH_SMS_CTX("SOUND_TOOTH_SMS_CTX","声牙创建短信模板接口"),
	SOUND_TOOTH_SMS_CHECK_TMP("SOUND_TOOTH_SMS_CHECK_TMP","声牙检查短信模板审核接口"),
	SOUND_TOOTH_SMS_SEND("SOUND_TOOTH_SMS_SEND","声牙发送短信接口"),
	SOUND_TOOTH_MATCH_PORT("SOUND_TOOTH_MATCH_PORT","声牙获取用户画像接口"),

	SOUND_TOOTH_SMS_TOKEN("SOUND_TOOTH_SMS_TOKEN","声牙获取auth_token接口"),
	SOUND_TOOTH_OUTER_PHONES("SOUND_TOOTH_OUTER_PHONES","声牙获取号码池列表接口"),
	SOUND_TOOTH_CALL("SOUND_TOOTH_CALL","声牙获取拨打外接口"),

	SOUND_TOOTH_AUTH_NAME("SOUND_TOOTH_AUTH_NAME","声牙账户名"),
	SOUND_TOOTH_AUTH_PWD("SOUND_TOOTH_AUTH_PWD","声牙账户密码"),
	SOUND_TOOTH_TOKEN_EXPIRE_TIME("SOUND_TOOTH_TOKEN_EXPIRE_TIME","TOKEN有效期单位分钟"),
    SOUND_TOOTH_CONSUME_DETAIL("SOUND_TOOTH_CONSUME_DETAIL","消费详细"),


	SOUND_TOOTH_V2_SMS_TMP_CHANGE("SOUND_TOOTH_V2_SMS_TMP_CHANGE","第二版本修改短信模板接口"),
	SOUND_TOOTH_V2_SMS_TMP_ADD("SOUND_TOOTH_V2_SMS_TMP_ADD","第二版本增加短信模板接口"),
	SOUND_TOOTH_V2_SMS_TMP_DEL("SOUND_TOOTH_V2_SMS_TMP_DEL","第二版本删除短信模板接口"),
	SOUND_TOOTH_V2_SMS_TMP_LIST("SOUND_TOOTH_V2_SMS_TMP_LIST","第二版本列举短信模板接口"),
	SOUND_TOOTH_V2_SMS_TMP_CHK("SOUND_TOOTH_V2_SMS_TMP_CHK","第二版本列举短信模板状态接口"),
	SOUND_TOOTH_V2_SMS_SEND("SOUND_TOOTH_V2_SMS_SEND","第二版本发送短信模板接口"),
	SOUND_TOOTH_V2_CALL_BIND("SOUND_TOOTH_V2_CALL_BIND","第二版本绑定呼叫号码"),



	/**
	 * 以下费用计价单位都是厘
	 */
	SYS_SOUND_TOOTH_ID_MAPPING_FEE("SYS_SOUND_TOOTH_ID_MAPPING_FEE","声牙电话匹配单价"),
	SYS_SOUND_TOOTH_MATCH_PORT_FEE("SYS_SOUND_TOOTH_MATCH_PORT_FEE","声牙画像匹配单价"),
	SYS_SOUND_TOOTH_SMS_FEE("SYS_SOUND_TOOTH_SMS_FEE","声牙短信单价"),
	SYS_SOUND_TOOTH_CALL_FEE("SYS_SOUND_TOOTH_CALL_FEE","声牙语音服务单价"),
	VENDER_SOUND_TOOTH_SMS_FEE("VENDER_SOUND_TOOTH_SMS_FEE","商家短信默认单价"),
	VENDER_SOUND_TOOTH_CARD_FEE("VENDER_SOUND_TOOTH_CARD_FEE","商家号卡购买费"),
	VENDER_SOUND_TOOTH_RENT_FEE("VENDER_SOUND_TOOTH_RENT_FEE","商家号卡月租费"),
	VENDER_SOUND_TOOTH_CALL_FEE("VENDER_SOUND_TOOTH_CALL_FEE","商家通话单价"),


	SYS_DEFAULT_CUSTOMER_STAY_TIME("SYS_DEFAULT_CUSTOMER_STAY_TIME","默认客户逗留时间单位分钟"),

	/**
	 * 商家秒到分钟的换算
	 */
	VENDER_SECONDS_TO_MIN("VENDER_SECONDS_TO_MIN","商家秒到分钟的换算"),

	/**
	 * 系统账户名称
	 */
	SYS_ACCT_NAME("SYS_ACCT_NAME","系统账户名称"),


	SYS_TASK_RETRY_CUSTOMER_STORE_STATS("SYS_TASK_RETRY_CUSTOMER_STORE_STATS", "统计每个客户每天道每个门店的数据重试次数"),
    SYS_TASK_RETRY_STORE_CUSTOMER_STATS("SYS_TASK_RETRY_STORE_CUSTOMER_STATS", "统计每个门店每天的客户数据的重试次数");


	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	SysCfgEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static SysCfgEnum getByVal(String val){
		for (SysCfgEnum tmp : SysCfgEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}



}
