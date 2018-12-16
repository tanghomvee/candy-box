package com.candybox.user.web.ctrls;

import com.candybox.common.web.ctrls.BaseCtrl;
import com.candybox.common.web.vo.Msg;
import com.candybox.common.web.vo.UserVO;
import com.candybox.user.dao.model.Menu;
import com.candybox.user.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path = "/menu")
public class MenuCtrl extends BaseCtrl {


    @Resource
    private MenuService menuService;

    @RequestMapping(path = {"/usermenu"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getTopAgentList() {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        List<Menu> menus= menuService.getMenusByUserId(curUser.getId());
        if(null!= menus && menus.size()>0){
            return Msg.success("查询成功",menus);
        }

        return Msg.error("你没有菜单权限");
    }

}
