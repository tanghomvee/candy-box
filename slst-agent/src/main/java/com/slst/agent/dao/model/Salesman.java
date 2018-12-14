package com.slst.agent.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-18 14:21
 */
@Entity
@Table(name = "t_salesman")
public class Salesman extends BaseEntity {

    /**
     * 业务员所在的代理商
     */
    private Long agentId;

    /**
     * 业务员所对应的用户Id
     */
    private Long userId;

    /**
     * 业务员姓名
     */
    private String name;
    /**
     * 业务员年龄
     */
    private Integer age;
    /**
     * 业务员生日
     */
    private Date birthday;

    /**
     * 业务员地址
     */
    private String addr;
    /**
     * 业务员电话号码
     */
    private String mobile;
    /**
     * 在职状态(1,-1)
     */
    private Integer worked;
}
