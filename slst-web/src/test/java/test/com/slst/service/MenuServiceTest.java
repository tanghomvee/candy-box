package test.com.slst.service;

import com.slst.user.dao.model.Menu;
import com.slst.user.service.MenuService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 9:44
 */
public class MenuServiceTest extends BaseTest {

    @Resource
    private MenuService menuService;

    @Test
    public void getMenuByUserId(){
        List<Menu> menus=menuService.getMenusByUserId(1L);
        for (Menu menu : menus) {
            System.out.println(menu.getMenuName());
        }
    }

}
