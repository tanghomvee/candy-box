package com.slst.common.enums;

/**
 * 规则类型
 *
 * @author ddyunf
 */
public enum RuleTypeEnum {
    RULE_TYPE_ST_PH_LIMIT(1,"门店调用外部接口获取手机号码的的次数限制规则") ,
    RULE_TYPE_ST_CUST_STAY_TIME(2,"客户默认多少分钟已离开的限制规则") ,
    RULE_TYPE_ST_COLLECT_TIME_LIMIT(3,"门店接受上报数据时间点限制规则") ,

    RULE_TYPE_ST_STRANGER_STAY_TIME(4,"客户停留多少分钟离开,表示路过客户") ,

	RULE_TYPE_ST_COLLECT_DATE_LIMIT(5,"门店接受上报数据日期限制规则") ,
	;
	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	RuleTypeEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static RuleTypeEnum getByVal(Integer val){
		for (RuleTypeEnum tmp : RuleTypeEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}


}
