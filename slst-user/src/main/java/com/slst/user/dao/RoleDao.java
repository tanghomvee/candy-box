package com.slst.user.dao;

import com.slst.user.dao.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 8:32
 */
public interface RoleDao extends JpaRepository<Role, Long>, RoleDaoExt {


    @Query(value = "select roleId from t_user_role where userId=?1", nativeQuery = true)
    Long findRoleIdByUserId(Long userId);

    @Modifying
    @Query(value = "insert into t_user_role(userId,roleId) values(?1,?2)",nativeQuery = true)
    Integer saveUserRole(Long userId,Long roleId);

}
