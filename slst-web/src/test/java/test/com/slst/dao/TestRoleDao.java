package test.com.slst.dao;

import com.slst.user.dao.RoleDao;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/2 8:53
 */
public class TestRoleDao extends BaseTest {

    @Resource
    private RoleDao roleDao;

    @Test
    public void testGetRoleId(){
       Long result= roleDao.findRoleIdByUserId(1L);
        System.out.println("返回的ID:"+result);
    }

    @Test
    public void testSaveUserRole(){
//        Integer result= roleDao.saveUserRole(5L,6L);
//        System.out.println("返回的关联ID:"+result);
    }
}
