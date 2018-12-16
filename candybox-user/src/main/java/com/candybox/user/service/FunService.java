package com.candybox.user.service;

import com.candybox.common.service.BaseService;
import com.candybox.common.web.vo.Msg;
import com.candybox.user.dao.model.Fun;

import java.util.List;

public interface FunService extends BaseService<Fun,Long> {

    /**
     * 创建功能
     * @param fun
     * @return
     */
    Fun createFun(Fun fun);

    /**
     * 修改功能
     * @param fun
     * @return
     */
    Fun modifyFun(Fun fun);

    /**
     * 根据ID或ID集合修改菜单ID
     * @param menuId
     * @param ids
     * @return
     */
    Integer modifyMenuIdByIds(Long menuId,String ids);

    /**
     * 根据ID或ID集合删除功能项
     * @return
     */
    Msg deleteByIds(String ids);

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
    List<Fun> findByMenuIdAndIds(Long menuId,List<Long> ids);


    /**
     * 根据菜单ID和ID集合查询功能列表
     * @param menuId
     * @param ids
     * @return
     */
    List<Fun> findByMenuIdAndIds(Long menuId,String ids);

    /**
     * 获取对应菜单ID的功能项数量
     * @param menuId
     * @return
     */
    Long countByMenuId(Long menuId);
}
