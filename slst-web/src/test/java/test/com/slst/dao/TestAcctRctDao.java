package test.com.slst.dao;

import com.slst.acct.dao.AccountRecordDao;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/10 11:53
 */
public class TestAcctRctDao extends BaseTest {

    @Resource
    private AccountRecordDao accountRecordDao;

    @Test
    public void getCount(){
//        Long res=accountRecordDao.countByAcctId(1L);
//        System.out.println(res);
    }
}
