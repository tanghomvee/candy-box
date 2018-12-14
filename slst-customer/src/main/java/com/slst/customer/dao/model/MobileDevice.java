package com.slst.customer.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_mobile_device")
public class MobileDevice extends BaseEntity {
	/**客户手机物理地址*/
	private String mac;
	/**客户手机号*/
	private String mobile;
	/**md5加密的imei号*/
	private String imeiMd5;
	/**非加密imei*/
	private String imei;
	/**md5加密的idfa*/
	private String idfaMd5;
	/**非加密idfa*/
	private String idfa;

	/**第三方手机号暗号ID:不存入此表,仅作数据传输*/
	@Transient
	private String thirdPartyId;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getImeiMd5() {
		return imeiMd5;
	}

	public void setImeiMd5(String imeiMd5) {
		this.imeiMd5 = imeiMd5;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getIdfaMd5() {
		return idfaMd5;
	}

	public void setIdfaMd5(String idfaMd5) {
		this.idfaMd5 = idfaMd5;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public String getThirdPartyId() {
		return thirdPartyId;
	}

	@Override
	public String toString() {
		return "CustomerDevice{" +
				"mac='" + mac + '\'' +
				", mobile='" + mobile + '\'' +
				", imeiMd5='" + imeiMd5 + '\'' +
				", imei='" + imei + '\'' +
				", idfaMd5='" + idfaMd5 + '\'' +
				", idfa='" + idfa + '\'' +
				", id=" + id +
				", yn=" + yn +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", changer='" + changer + '\'' +
				", changeTime=" + changeTime +
				'}';
	}


}