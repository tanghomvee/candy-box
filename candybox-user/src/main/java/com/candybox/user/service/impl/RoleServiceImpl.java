package com.candybox.user.service.impl;

import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.user.dao.RoleDao;
import com.candybox.user.dao.model.Role;
import com.candybox.user.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 9:33
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role,Long> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public Long findRoleIdByUserId(Long userId) {
        return roleDao.findRoleIdByUserId(userId);
    }

    @Override
    public Integer saveUserRole(Long userId, Long roleId) {
        return roleDao.saveUserRole(userId,roleId);
    }
}
