package com.slst.agent.web.ctrls;

import com.slst.agent.dao.model.AgentEmp;
import com.slst.agent.service.AgentEmpService;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
@RequestMapping(path = "/agentemp")
public class AgentEmpCtrl extends BaseCtrl {

    @Resource
    private AgentEmpService agentEmpService;


    @RequestMapping(path = {"/delemp"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg delEmp(Long empId, HttpSession session) {

        return agentEmpService.deleteAgentEmp(empId);
    }

    @RequestMapping(path = {"/agtempinfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAgtEmpInfo(Long empId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (0==empId){
            if (curUser.getUserType()==UserTypeEnum.VENDER_EMP.getVal()){
                empId=curUser.getVenderEmpId();
            }else if(curUser.getUserType()==UserTypeEnum.AGENT_EMP.getVal()){
                empId=curUser.getAgentEmpId();
            }
        }

        AgentEmp agentEmp= agentEmpService.findAgentEmpById(empId);

        if (null!=agentEmp){
            return Msg.success("查询成功",agentEmp);
        }

        return Msg.error("查询失败");
    }




    @RequestMapping(path = {"/ranklist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg rankList(HttpSession session) {
        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Map<String,Long>  map=agentEmpService.deviceSalesTopTenOfAgEmp(curUser);

        return Msg.success("查询成功",map);

    }


}
