package test.com.slst.service;

import com.slst.acct.service.AccountRecordService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/10 14:02
 */
public class AcctRecortdTest extends BaseTest {

    @Resource
    private AccountRecordService accountRecordService;

    @Test
    public void testDel(){
//      boolean isu=  accountRecordService.deleteByAcctId(1115L);
//
//        System.out.println(isu);
    }
}
