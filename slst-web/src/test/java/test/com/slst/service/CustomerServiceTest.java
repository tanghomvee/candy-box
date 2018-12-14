package test.com.slst.service;

import com.slst.customer.service.CustomerService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/5 16:12
 */
public class CustomerServiceTest extends BaseTest {

    @Resource
    private CustomerService customerService;

    @Test
    public void testMobileBrands(){
//        List list= customerService.findAllMobileBrand();
//        for (int i = 0; i < list.size(); i++) {
//            Map<String,String> map=(Map<String,String>)list.get(i);
//            System.out.println(map.get("key")+":"+map.get("value"));
//        }
    }

    @Test
    public void testStoreCus(){
       List list= customerService.getCustomerByMacOrMobile(16L,null,"89");

        for (int i = 0; i < list.size(); i++) {
            Object[] objects=(Object[])list.get(i);
            System.out.println("cid="+objects[0]+",moible="+objects[1]+"mac="+objects[2]);
        }
    }

}
