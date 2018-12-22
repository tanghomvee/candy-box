package com.candybox.user.dao;

import com.candybox.user.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 10:50
 */
public interface  UserDao extends JpaRepository<User, Long> , UserDaoExt {


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


    @Query(value = "SELECT count(id) FROM t_user where yn =1 and referrer=?1" , nativeQuery = true)
    Long statsReferrer(Long referrer);
}
