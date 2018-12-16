package com.candybox.user.dao.model;

import com.candybox.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_menu")
public class Menu extends BaseEntity {
    /**菜单名字*/
    private String menuName;
    /**菜单链接*/
    private String url;
    /**菜单排序*/
    private Long runk;
    /**父菜单ID*/
    private Long parentId;

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


    public String getMenuName(){
        return menuName;
    }

    public void setMenuName(String menuName){
        this.menuName = menuName;
    }

}