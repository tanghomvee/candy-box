package test.com.slst.dao;

import com.slst.user.dao.UserDao;
import com.slst.user.dao.model.User;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.Date;

public class TestUserDAO extends BaseTest {

    @Resource
    UserDao userDao;

    @Test
    public void testSave(){
        User user =new User();
        user.setUserName("testUsersss");
        user.setPwd("123456");
        user.setSignDate(new Date());
        user.setUserType(1);
        user.setMobile("18888888888");
        user.setYn(1);
        user.setCreator("system");
        user.setCreateTime(new Date());
        User rtnUser=userDao.save(user);
        System.out.println(rtnUser.getId());
    }

    @Test
    public void testSaveAndFlush(){
        User user= userDao.findById(1L).get();
        user.setPwd("888888888");
        userDao.saveAndFlush(user);
    }

    @Test
    public void testFindByUserName(){

        User rtnUser=userDao.findByUserName("testAdmin");
        System.out.println(rtnUser.getUserName());
        System.out.println(rtnUser.getId());
    }

    @Test
    public void testDeleteById(){
        userDao.deleteById(6L);
    }


    @Test
    public void testGetById(){
        User user= userDao.findById(1L).get();
        System.out.println(user+"_______________________________________________");
    }
}
