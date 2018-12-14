package com.slst.user.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.user.dao.model.Menu;

import java.util.List;

public interface MenuService extends BaseService<Menu,Long> {

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    Menu creatMenu(UserVO curUser,Menu menu);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    Menu modifyMenu(UserVO curUser,Menu menu);

    /**
     * 根据ID或ID集合删除菜单项
     * @param ids
     * @return
     */
    Msg deleteByIds(String ids);

    /**
     * 分页查询所有菜单项
     * @return
     */
    List<Menu> findAllMenus();

    /**
     * 根据ID集合查询相应菜单项
     * @param ids
     * @return
     */
    List<Menu> findMenusByIds(List<Long> ids);

    /**
     * 根据ID集合查询相应菜单项
     * @param ids
     * @return
     */
    List<Menu> findMenusByIds(String ids);

    /**
     * 判断是否有子菜单
     * @param parentId
     * @return
     */
    Long countByParentId(Long parentId);

    List<Menu> getMenusByUserId(Long userId);
}
