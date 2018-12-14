package com.slst.user.dao.impl;

import com.slst.common.dao.JpaDaoSupport;
import com.slst.user.dao.RoleDaoExt;
import com.slst.user.dao.model.Role;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 8:35
 */
public class RoleDaoExtImpl extends JpaDaoSupport<Role,Long> implements RoleDaoExt {

    @Override
    public Long findRoleIdByUserId(Long userId) {

//        String sql="select roleId from t_user_role where userId=?";
//        super.do
//
        return null;

    }
}
