package test.com.slst.marketService;

import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsTpl;
import com.slst.market.service.SmsTplService;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * @author: daiyou.zhong
 * @description:
 * @create: 2018-09-30 16:41
 * @version: 2.0
 **/
public class SmsTplServiceTest extends BaseTest {
    @Resource
    private SmsTplService smsTplService;

    @Test
    public void createSmsTplTest(){
        UserVO cuuUserVO=new UserVO();
        cuuUserVO.setVenderId(16L);
        cuuUserVO.setDisplayName("eeee");
        cuuUserVO.setVenderName("eeee");
        cuuUserVO.setAgentId(8L);
        cuuUserVO.setUserType(3);
        cuuUserVO.setUserName("ssss");
        cuuUserVO.setId(62L);
        SmsTpl smsTpl = new SmsTpl();
        smsTpl.setTitle("333300000000000000000000000");
        smsTpl.setSignname("三本日本料理");
        smsTpl.setContent("龙腾中路店开业大庆，9月30号至10月6号啤酒买一送一，凭此短信消费还可获赠精美寿司或精美烤肉一份，63924767");
        SmsTpl resultSmsTpl = smsTplService.createSmsTpl(cuuUserVO, smsTpl);
        System.out.println("resultSmsTpl = "+resultSmsTpl);
    }

}
