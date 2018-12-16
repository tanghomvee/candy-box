package com.candybox.user.dao.model;

import com.candybox.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_fun")
public class Fun extends BaseEntity {
    /**功能名字*/
    private String funName;
    /**功能链接*/
    private String url;
    /**功能排序*/
    private Long runk;
    /**父菜单ID*/
    private Long parentId;
    /**是否收费接口1为收费-1不收费*/
    private Integer isCharge;
    /**菜单ID*/
    private Long menuId;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getIsCharge(){
        return isCharge;
    }

    public void setIsCharge(Integer isCharge){
        this.isCharge = isCharge;
    }


    public Long getParentId(){
        return parentId;
    }

    public void setParentId(Long parentId){
        this.parentId = parentId;
    }


    public Long getRunk(){
        return runk;
    }

    public void setRunk(Long runk){
        this.runk = runk;
    }


    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }


    public String getFunName(){
        return funName;
    }

    public void setFunName(String funName){
        this.funName = funName;
    }

}