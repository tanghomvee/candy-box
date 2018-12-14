package com.slst.user.dao;

import com.slst.user.dao.model.User;
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
public interface  UserDao extends JpaRepository<User, Long> {


    /**
     * 保存用户信息
     * @param user
     * @return
     */
    User save(User user);

    /**
     * 根据ID删除用户信息
     * @param id
     */
    void deleteById(Long id);


    /**
     * 更新用户信息
     * @param user
     * @return
     */
    User saveAndFlush(User user);

    /**
     * 根据用户名和密码查找用户
     * @param userName
     * @param pwd
     * @return
     */
    User findByUserNameAndPwd(String userName, String pwd);


    /**
     * 根据手机号码和密码查找用户
     * @param mobile
     * @param pwd
     * @return
     */
    List<User> findByMobileAndPwd(String mobile, String pwd);

    /**
     * 根据用户名查找用户,用于判断是否已存在用户
     * @param userName
     * @return
     */
    User findByUserName(String userName);


    @Modifying
    @Query("update User u set u.pwd=:pwd,u.changer=:userName,u.changeTime=:curDate where u.id=:id")
    Integer modifyUserPwd(@Param("pwd") String pwd, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);
}
