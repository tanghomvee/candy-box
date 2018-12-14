package com.slst.user.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_industry")
public class Industry extends BaseEntity {
    /**行业名字*/
    private String industryName;
    /**行业父ID*/
    private Long parentId;
    /**首字母*/
    private String firstLetter;

    public String getFirstLetter(){
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter){
        this.firstLetter = firstLetter;
    }


    public Long getParentId(){
        return parentId;
    }

    public void setParentId(Long parentId){
        this.parentId = parentId;
    }


    public String getIndustryName(){
        return industryName;
    }

    public void setIndustryName(String industryName){
        this.industryName = industryName;
    }

}
