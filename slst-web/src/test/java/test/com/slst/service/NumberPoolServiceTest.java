package test.com.slst.service;

import com.slst.market.dao.model.NumberPool;
import com.slst.market.service.NumberPoolService;
import org.junit.Test;
import org.springframework.data.domain.Page;
import test.com.slst.BaseTest;

import javax.annotation.Resource;


public class NumberPoolServiceTest extends BaseTest {

    @Resource
    private NumberPoolService numberPoolService;

    @Test
    public void testSave(){
        numberPoolService.saveRelayNumber();
    }

    @Test
    public void testUnusedAdmin(){
       Page<NumberPool> page=numberPoolService.findNotUsedFromAdmin(0,1);

        for (NumberPool numberPool : page.getContent()) {
            System.out.println(numberPool.getPhone());
        }
    }

}
