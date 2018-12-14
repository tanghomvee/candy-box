package test.com.slst.service;

import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsBox;
import com.slst.market.service.SmsBoxService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/7 12:17
 */
public class SmsBoxServiceTest extends BaseTest {

    @Resource
    private SmsBoxService smsBoxService;

    @Test
    public void testSendMsg(){

//        Long targetStoreId,Long activityId,Long smsTplId,String cusIds

        UserVO cuuUserVO=new UserVO();
        cuuUserVO.setVenderId(16L);
        cuuUserVO.setDisplayName("eeee");
        cuuUserVO.setVenderName("eeee");
        cuuUserVO.setAgentId(8L);
        cuuUserVO.setUserType(3);
        cuuUserVO.setUserName("ssss");

        cuuUserVO.setId(62L);

        Msg rtnPrepareMag = smsBoxService.prepareToSendSmsByVenderOrStore(cuuUserVO,null,38L,8L,"1");
        if(null!= rtnPrepareMag){
            SmsBox initBox = (SmsBox) rtnPrepareMag.getData();
            smsBoxService.startToSendSmsByVenderOrStore(cuuUserVO,null,initBox.getId());
            System.out.println("11111111111111111111111111");
        }



    }

    @Test
    public void testSendMsg1(){

        System.out.println("----------------------");
    }


}
