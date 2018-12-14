package com.slst.common.enums;

/**
 * 规则对应的表达式
 *
 * @author ddyunf
 */
public enum RuleExpEnum {
    EQ("EQ","等于") {
		@Override
		public boolean execute(Comparable specifiedVal, Comparable thresholdVal) {
			return specifiedVal.compareTo(thresholdVal) == 0;
		}
	},
	NE("NE","不等于") {
		@Override
		public boolean execute(Comparable specifiedVal, Comparable thresholdVal) {
			return specifiedVal.compareTo(thresholdVal) != 0;
		}
	},
	GE("GE","大于等于") {
		@Override
		public boolean execute(Comparable specifiedVal, Comparable thresholdVal) {
			return specifiedVal.compareTo(thresholdVal) > 0 || specifiedVal.compareTo(thresholdVal) == 0;
		}
	},
	GT("GT","大于") {
		@Override
		public boolean execute(Comparable specifiedVal, Comparable thresholdVal) {
			return specifiedVal.compareTo(thresholdVal) > 0;
		}
	},
	LE("LE","小于等于") {
		@Override
		public boolean execute(Comparable specifiedVal, Comparable thresholdVal) {
			return specifiedVal.compareTo(thresholdVal) < 0 || specifiedVal.compareTo(thresholdVal) == 0;
		}
	},
	LT("LT","小于") {
		@Override
		public boolean execute(Comparable specifiedVal, Comparable thresholdVal) {
			return specifiedVal.compareTo(thresholdVal) < 0;
		}
	},
	;
	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	RuleExpEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static RuleExpEnum getByVal(String val){
		for (RuleExpEnum tmp : RuleExpEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 *
	 * @param   specifiedVal the object to be comparing.
	 * @param   thresholdVal the object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *          is less than, equal to, or greater than the specified object.
	 *
	 * @throws NullPointerException if the specified object is null
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this object.
	 */
	public  abstract boolean execute(Comparable specifiedVal , Comparable thresholdVal);

}
