package com.slst.user.dao.model;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 8:26
 */

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_privilege")
public class Privilege extends BaseEntity {
    /**权限名字*/
    private String privName;

    public String getPrivName(){
        return privName;
    }

    public void setPrivName(String privName){
        this.privName = privName;
    }

}
