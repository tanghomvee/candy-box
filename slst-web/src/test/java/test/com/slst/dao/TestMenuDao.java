package test.com.slst.dao;

import com.slst.user.dao.MenuDao;
import com.slst.user.dao.model.Menu;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/13 16:02
 */
public class TestMenuDao extends BaseTest {

    @Resource
    private MenuDao menuDao;

    @Test
    public void testAllPager(){
        PageRequest pageable=PageRequest.of(0,2);
        Page<Menu> menuPage= menuDao.findAll(pageable);
    }

    @Test
    public void testFindByIds(){
        List<Long> list=new ArrayList<>();
        list.add(1L);
        list.add(2L);
        Sort sort=new Sort(Sort.Direction.ASC,"runk");
        List<Menu> menuPage= menuDao.findByIdIn(list,sort);

        for (Menu menu : menuPage) {
            System.out.println(menu.getMenuName());
        }
    }

    @Test
    public void testSave(){
        Menu menu=new Menu();
        menu.setMenuName("你猜");
        menu.setParentId(0L);
        menu.setRunk(0L);
        menu.setUrl("ssss");
        menu.setCreateTime(new Date());
        menu.setCreator("suibian");
        menu.setYn(1);
        menuDao.save(menu);
    }
}
