package com.candybox.vender.dao.model;

import com.candybox.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_candy")
public class Candy extends BaseEntity {
    /**
     * 糖果名称
     */
    private String candyName;

    /**
     * 提供糖果的商家名称
     */
    private String venderName;

    /**
     * 提供糖果的商家ID
     */
    private Long vender;

    /**
     * 糖果的总数量
     */
    private Long totality;

    /**
     * 糖果的剩余数量
     */
    private Long remainder;


    /**
     *@see com.candybox.common.enums.CandyKindEnum
     * 糖果类型
     */
    private Integer kind;


    /**
     * @see  com.candybox.common.enums.OnlineEnum
     * 是否上线
     */
    private Integer online;

    /**
     * 糖果图标路径
     */
    private String icon;

    /**
     * 糖果描述信息
     */
    private String detail;


    public String getCandyName() {
        return candyName;
    }

    public void setCandyName(String candyName) {
        this.candyName = candyName;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public Long getVender() {
        return vender;
    }

    public void setVender(Long vender) {
        this.vender = vender;
    }

    public Long getTotality() {
        return totality;
    }

    public void setTotality(Long totality) {
        this.totality = totality;
    }

    public Long getRemainder() {
        return remainder;
    }

    public void setRemainder(Long remainder) {
        this.remainder = remainder;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getDetail() {

        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Candy{" +
                "candyName='" + candyName + '\'' +
                ", venderName='" + venderName + '\'' +
                ", vender=" + vender +
                ", totality=" + totality +
                ", remainder=" + remainder +
                ", kind=" + kind +
                ", online=" + online +
                ", icon='" + icon + '\'' +
                ", detail='" + detail + '\'' +
                ", id=" + id +
                ", yn=" + yn +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", changer='" + changer + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }
}