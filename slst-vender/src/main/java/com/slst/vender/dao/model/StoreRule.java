package com.slst.vender.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_store_rule")
public class StoreRule extends BaseEntity {
	/**门店规则名字*/
	private String ruleName;
	/**门店规则对应的程序方法*/
	private String ruleBean;
	/**规则类型:1:调用外部接口获取手机号码的限制*/
	private Integer ruleType;

	public String getRuleBean(){
		return ruleBean;
	}

	public void setRuleBean(String ruleBean){
		this.ruleBean = ruleBean;
	}


	public String getRuleName(){
		return ruleName;
	}

	public void setRuleName(String ruleName){
		this.ruleName = ruleName;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
}