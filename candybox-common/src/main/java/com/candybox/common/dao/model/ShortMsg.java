package com.candybox.common.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_short_msg")
public class ShortMsg extends BaseEntity {
	/**发送者*/
	private String sender;
	/**接收方*/
	private String receiver;
	/**短信内容*/
	private String content;
	/**标志描述*/
	private String remark;


	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ShortMsg{" +
				"sender='" + sender + '\'' +
				", receiver='" + receiver + '\'' +
				", content='" + content + '\'' +
				", remark='" + remark + '\'' +
				", id=" + id +
				", yn=" + yn +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", changer='" + changer + '\'' +
				", changeTime=" + changeTime +
				'}';
	}
}