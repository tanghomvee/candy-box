package com.slst.vender.web.ctrls;

import com.slst.acct.service.AccountService;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.service.VenderService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/28 11:40
 */
@Controller
@RequestMapping(path = "/vender")
public class VenderCtrl extends BaseCtrl {

    @Resource
    private VenderService venderService;

    @Resource
    private AccountService accountService;

    @RequestMapping(path = {"/list"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg getVenderList(Long agentId,int pageNum, int pageSize, String sortKey, String order, String companyName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
    }

        if (null ==agentId || 0==agentId){
            agentId=curUser.getAgentId();
        }

        Page<Vender> page=null;
        if (curUser.getUserType()==UserTypeEnum.AGENT_EMP.getVal()){
            page=venderService.findVenderByAgentEmpId(curUser.getAgentEmpId(),companyName,pageNum,pageSize,sortKey,order);
        }else {
            page=venderService.findVenderByAgentId(agentId,companyName,pageNum,pageSize,sortKey,order);
        }

        if (null!=page){
            return Msg.success("查询成功",page);
        }

        return Msg.error("查询失败");
    }

    @RequestMapping(path = {"/chargeforvender"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg chargeForAgent(Long venderId, BigDecimal amount, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long venerUserId=venderService.findVenderById(venderId).getUserId();
        Integer result= accountService.chargingByAgent(curUser,venerUserId,amount);

        if (null==result){
            return Msg.error("充值失败");
        }
        if (result>0){
            return Msg.success("充值成功");
        }else if (result==-1){
            return Msg.error("余额不足");
        }

        return Msg.error("充值失败");

    }

    @RequestMapping(path = {"/venderinfo"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg getVenderInfo(Long venderId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (venderId==0){
            venderId=curUser.getVenderId();
        }
        Vender vender=venderService.findVenderById(venderId);

        if (null!=vender){
            return Msg.success("查询成功",vender);
        }

        return Msg.error("查询失败");
    }

    @RequestMapping(path = {"/modifycompanyname"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyCompanyName(Long venderId,String companyName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result=venderService.modifyCompanyName(curUser,companyName,venderId);

        if (result>0){
            return Msg.success("修改成功");
        }else{
            return Msg.error("修改失败");
        }
    }



}
