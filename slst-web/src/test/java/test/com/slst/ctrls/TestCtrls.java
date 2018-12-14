package test.com.slst.ctrls;

import com.slst.common.components.RedisComponent;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Title: TestDataCmpCtrl
 * @Package test.com.ddyunf.angel.ctrls
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2018-01-02 16:14
 */
public class TestCtrls extends BaseTest {

    @Resource
    private RedisComponent redisComponent;

    @Test
    public void testRedisExpire(){
        redisComponent.set("homvee" , "hello expire" , 5);
        System.out.println(redisComponent.get("homvee"));
    }

    @Test
    public void testRedisDML() throws InterruptedException {


    }
}
