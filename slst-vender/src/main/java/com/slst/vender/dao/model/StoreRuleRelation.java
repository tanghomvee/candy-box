package com.slst.vender.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_store_rule_relation")
public class StoreRuleRelation extends BaseEntity {
	/**门店规则ID*/
	private Long storeRuleId;
	/**门店ID*/
	private Long storeId;

	/**规则所对应的表达式*/
	private String exp;
	/**规则所对应的阈值*/
	private String val;

	public Long getStoreId(){
		return storeId;
	}

	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}


	public Long getStoreRuleId(){
		return storeRuleId;
	}

	public void setStoreRuleId(Long storeRuleId){
		this.storeRuleId = storeRuleId;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}