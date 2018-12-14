package com.slst.user.dao;

import com.slst.user.dao.model.Fun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FunDao extends JpaRepository<Fun,Long>,FunDaoExt {


    /**
     * 创建功能记录
     * @param fun
     * @return
     */
    Fun save(Fun fun);

    /**
     * 修改功能记录
     * @param fun
     * @return
     */
    Fun saveAndFlush(Fun fun);

    /**
     * 根据ID修改菜单ID,用于将功能分配给其他页面
     * @param menuId
     * @param id
     * @return
     */

    @Modifying
    @Query("update Fun f set f.menuId=:menuId where f.id=:id")
    Integer modifyMenuIdById(@Param("menuId") Long menuId,@Param("id") Long id);

    /**
     * 根据ID集合批量修改菜单ID,用于将功能分配给其他页面
     * @param menuId
     * @param ids
     * @return
     */
    @Modifying
    @Query("update Fun f set f.menuId=:menuId where f.id=:ids")
    Integer modifyMenuIdByIds(@Param("menuId") Long menuId,@Param("ids") List<Long> ids);

    /**
     * 根据ID删除功能记录
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据菜单ID查询功能列表
     * @param menuId
     * @return
     */
    List<Fun> findByMenuId(Long menuId);

    /**
     * 根据菜单ID和ID集合查询功能列表
     * @param menuId
     * @param ids
     * @return
     */
    List<Fun> findByMenuIdAndIdIn(Long menuId,List<Long> ids);

    /**
     * 获取对应菜单ID的功能项数量
     * @param menuId
     * @return
     */
    Long countByMenuId(Long menuId);
}
