package test.com.slst.service;

import com.alibaba.fastjson.JSON;
import com.slst.common.mq.consumer.listeners.DefaultMessageListenerConcurrently;
import com.slst.common.mq.producer.DefaultProducerService;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.CallRecord;
import com.slst.market.service.CallRecordService;
import com.slst.user.dao.model.User;
import com.slst.user.service.UserService;
import com.slst.user.web.vo.AgentVO;
import com.slst.user.web.vo.VenderVO;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

public class TestUserSrv extends BaseTest {

    @Resource
    private UserService userService;

    @Resource(name = "callRecordListener")
    private DefaultMessageListenerConcurrently callRecordListener;

    @Resource
    private CallRecordService callRecordService;

    @Resource
    private DefaultProducerService defaultProducerService;

    @Test
    public void testLogin() throws Exception {

//        CallRecord callRecord = callRecordService.findByCallId("f07efd79bc174cc6bad0959bc65398f2");
//
//        callRecord.setDuration(-1L);
//        callRecord.setFee(0L);
//        callRecord.setBillId("sss");
//        callRecord.setAcctRcdId(0L);
//        CallRecord rtnCallRecord = callRecordService.modifyCallRecord(callRecord);
//
//        System.out.println(rtnCallRecord);

        try {
            String content="{\"callId\":\"b39ad20aa50a46769bec6f55c9e5c6ae\",\"userData\":\"e8f8b5a40a1871a4c631ad9ef4b1e7dd\",\"user_name\":\"商联数投\",\"abline\":\"\",\"telX\":\"15680744584\",\"type\":\"13\",\"telA\":\"18008259511\",\"duration\":\"38\",\"subid\":\"\",\"telB\":\"*******6968\",\"dateCreated\":\"20180804213330\",\"telG\":\"075533541534\",\"startTime\":\"20180804213236\",\"endTime\":\"20180804213330\"}";

            defaultProducerService.sendMsg("DEV_SOUND_TOOTH_CALLBACK" , JSON.parseObject(content).toJSONString());
        } catch (Exception e) {
//            LOGGER.error("发送声牙语音回调异常:topic = {} ,content={}" , topic ,content ,e);
        }

//        callRecordListener.consumeMessage(null);
//        List<User> users=userService.userLogin("1848365690s6","8ddcff3a80f4189ca1c9d4d902c3c909");
//        System.out.println(users.size());
//        if (null!=users && users.size()>0){
//            for (User user : users) {
//                System.out.println(user.getId());
//                System.out.println(user.getUserName());
//                System.out.println(user.getPwd());
//            }
//        }
    }

    @Test
    public void testAgentRegister(){
        AgentVO agentVO=new AgentVO();
        User user=userService.userLogin("18888888888","123456").get(0);

        UserVO curUser=new UserVO();

        curUser.setId(user.getId());
        curUser.setUserName(user.getUserName());
        curUser.setMobile(user.getMobile());

        agentVO.setUserName("testAgent2");
        agentVO.setAddress("大门口");
        agentVO.setBizlicense("testinglicence");
        agentVO.setCityId("1L");
        agentVO.setCityName("四川成都");
        agentVO.setCompanyName("测试公司名字");
        agentVO.setContact("老板");
        agentVO.setEarningsRate(30);
        agentVO.setIndustryId("1L");
        agentVO.setIndustryName("随便行业");
        agentVO.setLogo("testingLogo");
        agentVO.setMobile("13888888888");
        agentVO.setTaxIdNum("testingTaxIdNumb");
        agentVO.setWebsite("testingWebSite");


        agentVO =userService.registerAgent(curUser,agentVO);
        System.out.println(agentVO.getId()+"___________________"+agentVO.getAgentId());
    }

    @Test
    public void testVenderR(){
//        userName: 商家大佬01
//        mobile: 15108435889
//        companyName: 22
//        simpleName: 222
//        contact: 2222
//        cityId: 110101
//        cityName: 北京市市辖区东城区
//        address: 22222
//        industryId: 1
//        industryName: 零食/杂粮/列表
//        bizlicense:
//        logo:
//        website: 22222
//        earningsRate: 2222
//        taxIdNum:

        VenderVO agentVO=new VenderVO();
        User user=userService.userLogin("SLSTAdmin","88888888").get(0);

        UserVO curUser=new UserVO();

        curUser.setId(user.getId());
        curUser.setUserName(user.getUserName());
        curUser.setMobile(user.getMobile());
        curUser.setAgentId(8L);
        curUser.setAgentEmpId(0L);

        agentVO.setUserName("商家大佬055");
        agentVO.setBizlicense("");
        agentVO.setCityId("110101L");
        agentVO.setCityName("北京市市辖区东城区");
        agentVO.setAddress("22222");
        agentVO.setCompanyName("22");
        agentVO.setContact("2222");
        agentVO.setIndustryId("1L");
        agentVO.setIndustryName("随便行业");
        agentVO.setLogo("");
        agentVO.setMobile("15108435889");
        agentVO.setTaxIdNum("");
        agentVO.setWebsite("");

        agentVO =userService.registerVender(curUser,agentVO);
        System.out.println(agentVO.getId()+"___________________"+agentVO.getVenderId());
    }


}
