package test.com.slst.service;

import com.slst.market.dao.model.Activity;
import com.slst.market.service.ActivityService;
import org.junit.Test;
import org.springframework.data.domain.Page;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

public class ActivityServiceTest extends BaseTest {
    @Resource
    private ActivityService activityService;

    @Test
    public void testFindActivByEmpId(){

        Page<Activity> activByEmpId = activityService.findActivByEmpId(1L, 2, "我是", 0, 2,null, null);

        System.out.println(activByEmpId);
    }
}
