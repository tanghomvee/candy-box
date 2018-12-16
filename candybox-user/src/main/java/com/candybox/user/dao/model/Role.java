package com.candybox.user.dao.model;


import com.candybox.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_role")
public class Role extends BaseEntity {
    /**角色名字*/
    private String rolesName;

    public String getRolesName(){
        return rolesName;
    }

    public void setRolesName(String rolesName){
        this.rolesName = rolesName;
    }

    public Role(){

    }

    public Role(Long id){
        super.id=id;
    }

}