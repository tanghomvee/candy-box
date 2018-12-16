package com.candybox.user.service;

import com.candybox.common.service.BaseService;
import com.candybox.user.dao.model.Privilege;

import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 9:25
 */
public interface PrivilegeService extends BaseService<Privilege,Long> {

    Long findPrivIdByRoleId(Long roleId);

    List<Long> findMenuIdsByPrivId(Long privId);
}
