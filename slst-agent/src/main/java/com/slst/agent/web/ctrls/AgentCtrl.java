package com.slst.agent.web.ctrls;

import com.slst.acct.service.AccountService;
import com.slst.agent.dao.model.Agent;
import com.slst.agent.dao.model.AgentEmp;
import com.slst.agent.service.AgentEmpService;
import com.slst.agent.service.AgentService;
import com.slst.common.enums.AgentLevelEnum;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
@RequestMapping(path = "/agent")
public class AgentCtrl extends BaseCtrl {

    @Resource
    private AgentService agentService;

    @Resource
    private AgentEmpService agentEmpService;

    @Resource
    private AccountService accountService;

    @RequestMapping(path = {"/topagentlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getTopAgentList(int pageNum, int pageSize, String sortKey, String order, String companyName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }


        Integer agentLevel=0;

        if (curUser.getUserType()==UserTypeEnum.ADMIN.getVal()){
            agentLevel=AgentLevelEnum.TOP_LEVEL.getVal();
        }


        Page<Agent> page = agentService.findAgentByLevelForAdmin(agentLevel,pageNum,pageSize,sortKey,order,companyName );


        return Msg.success("查询成功",page);
    }



    @RequestMapping(path = {"/chargeforagent"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg chargeForAgent(Long agentId, BigDecimal amount, HttpSession session) {


        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long agentUserId=agentService.findAgentById(agentId).getUserId();
        Integer result= accountService.chargingForAgentByAdmin(curUser,agentUserId,amount);

        if (result>0){
            return Msg.success("充值成功");
        }else{
            return Msg.error("充值失败");
        }

    }


    @RequestMapping(path = {"/getagentinfo"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg getAgentInfo(Long agentId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Agent agent=null;

        if (agentId==0){

            agent=agentService.findAgentById(curUser.getAgentId());
        }else{
            agent=agentService.findAgentById(agentId);
        }

        if (null!=agent){
            return Msg.success("查询成功",agent);
        }else{
            return Msg.error("查询失败");
        }
    }

    @RequestMapping(path = {"/modifycompanyname"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyCompanyName(Long agentId,String companyName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result=agentService.modifyCompanyName(curUser,companyName,agentId);
        if (result>0){
            return Msg.success("修改成功");
        }
        return Msg.error("查询失败");
    }


    @RequestMapping(path = {"/emplist"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg getEmpList(int pageNum, int pageSize, String sortKey, String order,String empName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<AgentEmp> page=agentEmpService.findAgEmpByAgentId(curUser.getAgentId(),pageNum,pageSize,sortKey,order,empName);

        if (null!=page){
            return Msg.success("查询成功",page);
        }

        return Msg.error("查询失败");
    }


}
