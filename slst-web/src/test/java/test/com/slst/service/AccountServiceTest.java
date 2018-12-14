package test.com.slst.service;

import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.common.web.vo.UserVO;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/4 16:51
 */
public class AccountServiceTest extends BaseTest {

    @Resource
    private AccountService accountService;

    @Test
    public void testCreateAcct(){
        Account account=new Account();
        account.setUserId(6L);
        account.setAcctName("测试"+"的账户");
        account.setYn(1);
        account.setCreateTime(new Date());
        account.setCreator("aaaaaa");
        accountService.createAccount(account);
    }

//    @Test
//    public void testUserCharging(){
//        accountService.chargingForUser("ccccc",1L,10);
//    }

    @Test
    public void testCallSrvCost(){
        UserVO userVO=new UserVO();
        userVO.setUserName("test");
        userVO.setId(240L);

        Integer result= accountService.venderCallSrvCost(userVO,10000L);

        System.out.println(result);
    }

    @Test
    public void testFrozen(){
        UserVO userVO=new UserVO();
        userVO.setUserName("test");
        userVO.setId(240L);

        Long acctRcdId= accountService.venderCallSrvFrozen(userVO,10000L);

        System.out.println(acctRcdId);
    }


    @Test
    public void testUnfreeze() throws Exception {
        UserVO userVO=new UserVO();
        userVO.setUserName("test");
        userVO.setId(240L);

        Long acctRcdId= accountService.venderUnfreeze(userVO,72408L,0L);

        System.out.println(acctRcdId);
    }

    @Test
    public void testUnfreezeWithCost() throws Exception {
        UserVO userVO=new UserVO();
        userVO.setUserName("test");
        userVO.setId(240L);

        Long acctRcdId= accountService.unfreezeWithCallSrvCost(userVO,76133L,239L,20L,5000L,1500L);
        System.out.println(acctRcdId);
    }

    @Test
    public void testCostWithEarning() throws Exception {
        UserVO userVO=new UserVO();
        userVO.setUserName("test");
        userVO.setId(240L);

        Integer result= accountService.callSrvCostWithEarning(userVO,239L,20L,5000L,1500L);
        System.out.println(result);
    }


    @Test
    public void testBigDecimal(){
        long duration=0;
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(duration)).divide(new BigDecimal("60"),BigDecimal.ROUND_CEILING);
        long talkTime=bigDecimal.longValue();
        System.out.println(talkTime);
    }
}
