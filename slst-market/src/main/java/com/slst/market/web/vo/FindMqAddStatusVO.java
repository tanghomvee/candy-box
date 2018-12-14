package com.slst.market.web.vo;

/**
 * @author: daiyou.zhong
 * @description:
 * @create: 2018-09-21 16:27
 * @version: 2.0
 **/
public class FindMqAddStatusVO {

    /** 总数量 **/
    private Integer totalNumber;
    /** 当前数量 **/
    private Integer nowNumber;
    /** 剩余数量 **/
    private Integer residueNumber;

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getNowNumber() {
        return nowNumber;
    }

    public void setNowNumber(Integer nowNumber) {
        this.nowNumber = nowNumber;
    }

    public Integer getResidueNumber() {
        return residueNumber;
    }

    public void setResidueNumber(Integer residueNumber) {
        this.residueNumber = residueNumber;
    }
}
