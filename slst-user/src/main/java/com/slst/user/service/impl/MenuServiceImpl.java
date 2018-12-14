package com.slst.user.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.user.dao.MenuDao;
import com.slst.user.dao.model.Menu;
import com.slst.user.service.FunService;
import com.slst.user.service.MenuService;
import com.slst.user.service.PrivilegeService;
import com.slst.user.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("menuService")
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

    private Logger LOGGER=LoggerFactory.getLogger(this.getClass());

    @Resource
    private MenuDao menuDao;
    @Resource
    private FunService funService;
    @Resource
    private RoleService roleService;
    @Resource
    private PrivilegeService privilegeService;

    @Override
    public Menu creatMenu(UserVO curUser, Menu menu) {

        Date curDate = new Date();//当前时间
        Integer yes = YNEnum.YES.getVal();
        String creator = curUser.getUserName();

        menu.setYn(yes);
        menu.setCreator(creator);
        menu.setCreateTime(curDate);

        Menu rtnMenu = menuDao.save(menu);

        if (null != rtnMenu) {
            return rtnMenu;
        }

        return null;
    }

    @Override
    public Menu modifyMenu(UserVO curUser, Menu menu) {

        Date curDate = new Date();//当前时间
        String changer = curUser.getUserName();

        menu.setChanger(changer);
        menu.setChangeTime(curDate);

        Menu rtnMenu = menuDao.saveAndFlush(menu);

        if (null != rtnMenu) {
            return rtnMenu;
        }

        return null;
    }

    @Override
    public Msg deleteByIds(String ids) {

        if (ids.contains(",")) {
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {

                Long id = Long.parseLong(idArray[i]);
                Integer deletedCount = i + 1;//已删除条数
                Integer unDeletedCount = idArray.length - i;//未删除条数

                Long childCount=menuDao.countByParentId(id);

                if (childCount>0){
                    return Msg.error("删除中断,请先清空或转移菜单下子菜单!已删除:" + deletedCount + "条剩余未删:" + unDeletedCount + "条");
                }

                Long funCount = funService.countByMenuId(id);//拥有功能项数量

                if (funCount > 0) {
                    return Msg.error("删除中断,请先清空或转移菜单下功能项!已删除:" + deletedCount + "条剩余未删:" + unDeletedCount + "条");
                }

                try{
                    menuDao.deleteById(id);
                }catch (Exception e){
                    e.printStackTrace();
                    LOGGER.error("删除数据异常:",e);
                    return Msg.error("删除中断,系统开了个小差!已删除:" + deletedCount + "条剩余未删:" + unDeletedCount + "条");
                }

            }
        } else {
            Long id=Long.parseLong(ids);

            Long childCount=menuDao.countByParentId(id);

            if (childCount>0){
                return Msg.error("删除失败,请先清空或转移菜单下子菜单!");
            }

            Long funCount = funService.countByMenuId(id);//拥有功能项数量

            if (funCount > 0) {
                return Msg.error("删除失败,请先清空或转移菜单下功能项!");
            }

            try{
                menuDao.deleteById(id);
            }catch (Exception e){
                e.printStackTrace();
                LOGGER.error("删除数据异常:",e);
                return Msg.error("删除失败,系统开了个小差!");
            }
        }

        return Msg.success();
    }

    @Override
    public List<Menu> findAllMenus() {
        Sort sort=this.getSort();
        List<Menu> menus=menuDao.findAll(sort);
        return menus;
    }

    @Override
    public List<Menu> findMenusByIds(List<Long> ids) {
        Sort sort=this.getSort();
        List<Menu> menus=menuDao.findByIdIn(ids,getSort());
        return menus;
    }

    @Override
    public List<Menu> findMenusByIds(String ids) {
        List<Long> idList=new ArrayList<>();
        String[] idArray=ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            Long id=Long.parseLong(idArray[i]);
            idList.add(id);
        }

        Sort sort=this.getSort();
        List<Menu> menus=menuDao.findByIdIn(idList,getSort());
        return menus;
    }

    @Override
    public Long countByParentId(Long parentId) {
        return menuDao.countByParentId(parentId);
    }

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        Long roleId= roleService.findRoleIdByUserId(userId);

        if (null==roleId || 0==roleId){
            return null;
        }

        Long privId= privilegeService.findPrivIdByRoleId(roleId);

        if (null==privId || 0==privId){
            return null;
        }

        List<Long> menuIds=privilegeService.findMenuIdsByPrivId(privId);

        if (null==privId || 0==menuIds.size()){
            return null;
        }


        List<Menu> menus= menuDao.findByIdIn(menuIds,getSort());

        if (null==menus || 0==menus.size()){
            return null;
        }

        return menus;
    }

    private Sort getSort(){
        Sort sort=new Sort(Sort.Direction.ASC,"runk");
        return sort;
    }



}
