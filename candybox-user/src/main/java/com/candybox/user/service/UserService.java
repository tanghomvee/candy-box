package com.candybox.user.service;

import com.candybox.common.service.BaseService;
import com.candybox.user.dao.model.User;


public interface UserService extends BaseService<User, Long> {

    /**
     * find by userName & pwd
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * find by mobile
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * add
     * @param user
     * @return
     */
    User save(User user);
}
