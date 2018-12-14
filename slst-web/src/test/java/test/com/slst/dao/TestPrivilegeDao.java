package test.com.slst.dao;

import com.slst.user.dao.MenuDao;
import com.slst.user.dao.PrivilegeDao;
import com.slst.user.dao.model.Menu;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 9:09
 */
public class TestPrivilegeDao extends BaseTest {

    @Resource
    private PrivilegeDao privilegeDao;

    @Resource
    private MenuDao menuDao;

    @Test
    public void getMenuIds(){
        Long privId=privilegeDao.findPrivIdByRoleId(1L);
        System.out.println(privId);
        List<BigInteger> list=privilegeDao.findMenuIdsByPrivId(privId);
        List<Long> list1=new ArrayList<>();
        for (BigInteger aLong : list) {
            System.out.println("menuID是:"+aLong);
            list1.add(aLong.longValue());
        }

        Sort sort=new Sort(Sort.Direction.ASC,"runk");
        List<Menu> menus= menuDao.findByIdIn(list1,sort);
        for (Menu menu : menus) {
            System.out.println(menu.getMenuName());
        }
    }
}
