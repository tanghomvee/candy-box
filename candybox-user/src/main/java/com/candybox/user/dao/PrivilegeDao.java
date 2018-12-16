package com.candybox.user.dao;

import com.candybox.user.dao.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 8:59
 */
public interface PrivilegeDao extends JpaRepository<Privilege,Long> {

    @Query(value = "select privId from t_role_priv where roleId=?1",nativeQuery = true)
    Long findPrivIdByRoleId(Long roleId);

    @Query(value = "select menuId from t_priv_menu where privId=?1",nativeQuery = true)
    List<BigInteger> findMenuIdsByPrivId(Long privId);
}
