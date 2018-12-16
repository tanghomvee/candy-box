package com.candybox.user.dao;

import com.candybox.user.dao.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu,Long>,MenuDaoExt {

    /**
     * 新建菜单
     * @param menu
     * @return
     */
    Menu save(Menu menu);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    Menu saveAndFlush(Menu menu);

    /**
     * 删除菜单
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据菜单ID集合查询菜单列表
     * @param ids
     * @return
     */
    List<Menu> findByIdIn(List<Long> ids, Sort sort);



    /**
     * 判断是否有子菜单
     * @param parentId
     * @return
     */
    Long countByParentId(Long parentId);



}
