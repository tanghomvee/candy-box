package com.candybox.user.dao;

import com.candybox.user.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 10:50
 */
public interface  UserDao extends JpaRepository<User, Long> , UserDaoExt {



    @Modifying
    @Query("update User u set u.pwd=:pwd,u.changer=:userName,u.changeTime=:curDate where u.id=:id")
    Integer modifyUserPwd(@Param("pwd") String pwd, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * find valid user by username
     * @param userName
     * @param yn
     * @return
     */
    User findByUserNameAndYn(String userName, Integer yn);

    /**
     * find valid user by mobile
     * @param userName
     * @param yn
     * @return
     */
    User findByMobileAndYn(String userName, Integer yn);
}
