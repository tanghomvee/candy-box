package com.slst.common.web.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-01-04 19:10
 */
public class BaseVO implements Serializable{


    protected Long id;

    /**
     * 是否有效
     * 1:有效,-1无效
     */
    protected Integer yn;

    /**
     * 创建人
     */
    protected String creator;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 修改人
     */
    protected String changer;

    /**
     * 修改时间
     */
    protected Date changeTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime == null ? new Date() : createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChanger() {
        return changer;
    }

    public void setChanger(String changer) {
        this.changer = changer;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}
