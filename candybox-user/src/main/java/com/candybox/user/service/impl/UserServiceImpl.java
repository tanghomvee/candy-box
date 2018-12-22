package com.candybox.user.service.impl;

import com.candybox.common.enums.YNEnum;
import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.user.dao.UserDao;
import com.candybox.user.dao.model.User;
import com.candybox.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:15
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    @Resource
    private UserDao userDao;


    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserNameAndYn(userName , YNEnum.YES.getVal());
    }

    @Override
    public User findByMobile(String mobile) {
        return userDao.findByMobileAndYn(mobile , YNEnum.YES.getVal());
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public Long statsReferrer(Long referrer) {
        return userDao.statsReferrer(referrer);
    }
}
