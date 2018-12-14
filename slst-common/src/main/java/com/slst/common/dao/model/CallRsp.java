package com.slst.common.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
* Copyright (c) 2018. ddyunf.com all rights reserved
* @Description call result
* @author  Homvee.Tang(tanghongwei@ddcloudf.com)
* @date 2018-06-13 10:14
* @version V1.0
*/
@Entity
@Table(name = "t_call_rsp")
public class CallRsp extends BaseEntity {

	/**
	 * @see com.slst.common.enums.SrvTypeEnum
	 * 服务提供商
	 * */
	private Integer srvType;
	/**
	 * 主叫号码
	 */
	private String fromPhoneNum;

	/**
	 * 被叫号码
	 */
	private String toPhoneNum;

	/**
	 * 被叫号码ID
	 */
	private String toPhoneId;

	/**
	 *话单ID
	 */
	private String callId;
	/**
	 *话费单ID
	 */
	private String billId;


	/**
	 * 通话时长(秒)
	 */
	private Long duration;

	/**
	 * 申请呼叫时间
	 */
	private Date applyDate;
	/**
	 * 请求时间
	 */
	private Date reqDate;
	/**
	 * 回掉时间
	 */
	private Date rspDate;

	public Integer getSrvType() {
		return srvType;
	}

	public void setSrvType(Integer srvType) {
		this.srvType = srvType;
	}

	public String getFromPhoneNum() {
		return fromPhoneNum;
	}

	public void setFromPhoneNum(String fromPhoneNum) {
		this.fromPhoneNum = fromPhoneNum;
	}

	public String getToPhoneNum() {
		return toPhoneNum;
	}

	public void setToPhoneNum(String toPhoneNum) {
		this.toPhoneNum = toPhoneNum;
	}

	public String getToPhoneId() {
		return toPhoneId;
	}

	public void setToPhoneId(String toPhoneId) {
		this.toPhoneId = toPhoneId;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	public Date getRspDate() {
		return rspDate;
	}

	public void setRspDate(Date rspDate) {
		this.rspDate = rspDate;
	}

	@Override
	public String toString() {
		return "CallRsp{" +
				"srvType=" + srvType +
				", fromPhoneNum='" + fromPhoneNum + '\'' +
				", toPhoneNum='" + toPhoneNum + '\'' +
				", toPhoneId='" + toPhoneId + '\'' +
				", callId='" + callId + '\'' +
				", billId='" + billId + '\'' +
				", duration=" + duration +
				", applyDate=" + applyDate +
				", reqDate=" + reqDate +
				", rspDate=" + rspDate +
				", id=" + id +
				", yn=" + yn +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", changer='" + changer + '\'' +
				", changeTime=" + changeTime +
				'}';
	}
}