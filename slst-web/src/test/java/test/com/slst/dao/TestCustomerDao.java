package test.com.slst.dao;

import com.slst.customer.dao.CustomerDao;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/3 15:36
 */
public class TestCustomerDao extends BaseTest {

    @Resource
    private CustomerDao customerDao;

    @Test
    public void testMobileBrand(){
//        List<String> list=customerDao.findAllMobileBrand();
//        for (String s : list) {
//            System.out.println(s);
//        }
    }

}
