package com.candybox.vender.dao;

import com.candybox.vender.dao.model.Candy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 10:50
 */
public interface CandyDao extends JpaRepository<Candy, Long> , CandyDaoExt {



    @Modifying
    @Query("update User u set u.pwd=:pwd,u.changer=:userName,u.changeTime=:curDate where u.id=:id")
    Integer modifyUserPwd(@Param("pwd") String pwd, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);
}
