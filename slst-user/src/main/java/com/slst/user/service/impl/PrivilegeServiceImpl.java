package com.slst.user.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.user.dao.PrivilegeDao;
import com.slst.user.dao.model.Privilege;
import com.slst.user.service.PrivilegeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 9:27
 */
@Service("privilegeService")
public class PrivilegeServiceImpl extends BaseServiceImpl<Privilege,Long> implements PrivilegeService {

    @Resource
    private PrivilegeDao privilegeDao;

    @Override
    public Long findPrivIdByRoleId(Long roleId) {
        return privilegeDao.findPrivIdByRoleId(roleId);
    }

    @Override
    public List<Long> findMenuIdsByPrivId(Long privId) {
        List<Long> menuIds=new ArrayList<>();
        List<BigInteger> results=privilegeDao.findMenuIdsByPrivId(privId);
        for (BigInteger result : results) {
            menuIds.add(result.longValue());
        }
        return menuIds;
    }
}
