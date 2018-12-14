package com.slst.user.web.ctrls;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.agent.dao.model.Agent;
import com.slst.agent.service.AgentEmpService;
import com.slst.agent.service.AgentService;
import com.slst.common.components.RedisComponent;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.user.dao.model.User;
import com.slst.user.service.UserService;
import com.slst.user.web.vo.AgentEmpVO;
import com.slst.user.web.vo.AgentVO;
import com.slst.user.web.vo.VenderEmpVO;
import com.slst.user.web.vo.VenderVO;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.service.VenderEmpService;
import com.slst.vender.service.VenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = "/user")
public class UserCtrl extends BaseCtrl {

    @Resource
    private UserService userService;
    @Resource
    private AgentService agentService;

    @Resource
    private AgentEmpService agentEmpService;

    @Resource
    private VenderService venderService;

    @Resource
    private VenderEmpService venderEmpService;

    @Resource
    private AccountService accountService;

    @Resource
    RedisComponent redisComponent;

    /**
     * session失效时间
     */
    @Value("${out.of.service.time}")
    private String outTime;


    @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg list() {
        return Msg.success();
    }


    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg login(String username, String pwd, HttpSession session) {

//        getUser();
//
//        if (null != session || null != session.getAttribute("user")) {
//            session.removeAttribute("user");
//        }

        List<User> users = userService.userLogin(username, pwd);
        String uuid=UUID.randomUUID().toString().replace("-","").toUpperCase();
        UserVO userVO = null;
        if (null != users && users.size() > 0) {

            if (users.size() > 1) {
                return Msg.success(users);
            } else {
                userVO = userService.initUserVO(users.get(0));
                if (null == userVO) {
                    return Msg.error("未找到匹配数据");
                } else {
//                    session.setAttribute("user", userVO);
                    //登录成功保存到redis
                    String userVOStr=JSON.toJSONString(userVO);
                    LOGGER.info("登录用户对象转换的Json={}",userVOStr);
                    redisComponent.set(uuid,userVOStr,Long.parseLong(outTime));
                }
            }
        } else {
            return Msg.error("用户名或密码错误");
        }

        Map<String,Object> map= Maps.newHashMap();
        map.put("authorization",uuid);
        map.put("userId",userVO.getId());
        map.put("userType",userVO.getUserType());
        map.put("displayName",userVO.getDisplayName());

        return Msg.success("登录成功", map);
    }

    @RequestMapping(path = {"/selecteduser"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg selectedUser(User user, HttpSession session) {

//        if (null != session || null != session.getAttribute("user")) {
//            session.removeAttribute("user");
//        }

        UserVO userVO = userService.initUserVO(user);

        String uuid=UUID.randomUUID().toString().replace("-","").toUpperCase();

        if (null == userVO) {
            return Msg.error("未找到匹配数据");
        } else {
//            session.setAttribute("user", userVO);
            //登录成功保存到redis
            String userVOStr=JSON.toJSONString(userVO);
            redisComponent.set(uuid,userVOStr,Long.parseLong(outTime));
            LOGGER.info("登录用户对象转换的Json={}",userVOStr);
        }


        Map<String,Object> map= Maps.newHashMap();
        map.put("authorization",uuid);
        map.put("userId",userVO.getId());
        map.put("userType",userVO.getUserType());
        map.put("displayName",userVO.getDisplayName());

        return Msg.success("登录成功", map);
    }


    @RequestMapping(path = {"/logout"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg login(HttpServletRequest request) {
//        request.getSession().removeAttribute("user");
//        request.getSession().invalidate();

        return Msg.success("退出登录成功");
    }


    @RequestMapping(path = {"/agentregister"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg agentRegister(AgentVO agentVO, HttpServletRequest request, HttpSession session) {


        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        String contextPath = request.getContextPath();
        //访问地址
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + contextPath + "/";
        //物理地址
        String realPath = request.getSession().getServletContext().getRealPath("");

        agentVO.setBasePath(basePath);
        agentVO.setRealPath(realPath);

        AgentVO rtnAgentVO = userService.registerAgent(curUser, agentVO);
        if (null != rtnAgentVO && rtnAgentVO.getAgentId() > 0) {
            return Msg.success("注册成功");
        } else {
            return Msg.error("注册失败");
        }

    }




    @RequestMapping(path = {"/modifyagent"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyAgent(AgentVO agentVO, HttpServletRequest request, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        String contextPath = request.getContextPath();
        //访问地址
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + contextPath + "/";
        //物理地址
        String realPath = request.getSession().getServletContext().getRealPath("");
        agentVO.setId(curUser.getId());
        agentVO.setAgentId(curUser.getAgentId());
        agentVO.setBasePath(basePath);
        agentVO.setRealPath(realPath);

        AgentVO rtnAgentVO = userService.modifyAgent(agentVO);

        if (null == rtnAgentVO) {
            return Msg.error("修改失败");
        } else {
            return Msg.success("修改成功", rtnAgentVO);
        }

    }

    @RequestMapping(path = {"/agtempregister"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg agtEmpRegister(AgentEmpVO agentEmpVO, HttpServletRequest request, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        AgentEmpVO rtnAgentEmpVO = userService.registerAgentEmp(curUser, agentEmpVO);
        if (null != rtnAgentEmpVO && rtnAgentEmpVO.getAgentEmpId() > 0) {
            return Msg.success("注册成功");
        } else {
            return Msg.error("注册失败");
        }

    }

    @RequestMapping(path = {"/modifyagentemp"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyAgentEmp(AgentEmpVO agentEmpVO, HttpServletRequest request, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (null==agentEmpVO.getAgentEmpId() || agentEmpVO.getAgentEmpId().equals(0)){

            agentEmpVO.setId(curUser.getId());
            agentEmpVO.setAgentEmpId(curUser.getAgentEmpId());

        }

        Long agtEmpId=agentEmpVO.getAgentEmpId();

        Long userId=0L;

        try{
            userId=agentEmpService.findAgentEmpById(agtEmpId).getUserId();

            agentEmpVO.setId(userId);
        }catch (Exception e){
            LOGGER.error("获取员工用户信息失败,员工ID={}",agtEmpId,e);
            return Msg.error("修改失败");
        }


        AgentEmpVO rtnAgentEmpVO = userService.modifyAgentEmpByAgent(curUser, agentEmpVO);

        if (null == rtnAgentEmpVO) {
            return Msg.error("修改失败");
        } else {
            return Msg.success("修改成功", rtnAgentEmpVO);
        }
    }

    /**
     * 商家员工修改接口
     * @param id
     * @return
     */
    @RequestMapping(path = {"/delagtemp"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg delAgtEmp(Long id) {

        return userService.deleteAgentEmp(id);
    }



    @RequestMapping(path = {"/venderregister"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg venderRegister(VenderVO venderVO, HttpServletRequest request, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        String contextPath = request.getContextPath();
        //访问地址
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + contextPath + "/";
        //物理地址
        String realPath = request.getSession().getServletContext().getRealPath("");

        venderVO.setBasePath(basePath);
        venderVO.setRealPath(realPath);

        if (curUser.getUserType() == UserTypeEnum.AGENT.getVal()) {
            curUser.setAgentEmpId(0L);
        }
        VenderVO rtnVenderVO = userService.registerVender(curUser, venderVO);
        if (null != rtnVenderVO && rtnVenderVO.getVenderId() > 0) {
            return Msg.success("注册成功");
        } else {
            return Msg.error("注册失败");
        }

    }

    @RequestMapping(path = {"/modifyvender"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyVender(VenderVO venderVO, HttpServletRequest request, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        String contextPath = request.getContextPath();
        //访问地址
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + contextPath + "/";
        //物理地址
        String realPath = request.getSession().getServletContext().getRealPath("");

        venderVO.setBasePath(basePath);
        venderVO.setRealPath(realPath);

        venderVO.setId(curUser.getId());
        venderVO.setVenderId(curUser.getVenderId());

        VenderVO rtnVenderVO = userService.modifyVender(venderVO);

        if (null == rtnVenderVO) {
            return Msg.error("修改失败");
        } else {
            return Msg.success("修改成功", rtnVenderVO);
        }

    }


    /**
     * 商家员工注册
     * @param venderEmpVO
     * @return
     */
    @RequestMapping(path = {"/vdrempregister"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg vdrEmpRegister(VenderEmpVO venderEmpVO) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        VenderEmpVO rtnVenderEmpVO = userService.registerVenderEmp(curUser, venderEmpVO);
        if (null != rtnVenderEmpVO && rtnVenderEmpVO.getVenderEmpId() > 0) {
            return Msg.success("注册成功");
        } else {
            return Msg.error("注册失败");
        }

    }

    /**
     * 商家员工修改接口
     * @param venderEmpVO
     * @return
     */
    @RequestMapping(path = {"/modifyvdremp"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyVdrEmp(VenderEmpVO venderEmpVO) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (null==venderEmpVO.getVenderEmpId() || venderEmpVO.getVenderEmpId().equals(0)){

            venderEmpVO.setId(curUser.getId());
            venderEmpVO.setVenderEmpId(curUser.getVenderEmpId());

        }

        Long vdrEmpId=venderEmpVO.getVenderEmpId();

        Long userId=0L;

        try{
            userId=venderEmpService.findVenderEmpById(vdrEmpId).getUserId();

            venderEmpVO.setId(userId);
        }catch (Exception e){
            LOGGER.error("获取商家员工用户信息失败,员工ID={}",vdrEmpId,e);
            return Msg.error("修改失败");
        }


        VenderEmpVO rtnVenderEmpVO=userService.modifyVenderEmpByVender(curUser,venderEmpVO);

        if (null == rtnVenderEmpVO) {
            return Msg.error("修改失败");
        } else {
            return Msg.success("修改成功", rtnVenderEmpVO);
        }
    }

    /**
     * 商家员工修改接口
     * @param id
     * @return
     */
    @RequestMapping(path = {"/delvdremp"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg delVdrEmp(Long id) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return userService.deleteVenderEmp(id);
    }




    @RequestMapping(path = {"/resetpwd"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg resetPassWord(Long agentId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long userId = agentService.findAgentById(agentId).getUserId();

        Integer result = userService.resetUserPwd(curUser, userId);

        if (result > 0) {
            return Msg.success("修改成功");
        } else {
            return Msg.error("修改失败");
        }

    }

    @RequestMapping(path = {"/resetpwdforvender"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg resetPassWordForVender(Long venderId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long userId = venderService.findVenderById(venderId).getUserId();

        Integer result = userService.resetUserPwd(curUser, userId);

        if (result > 0) {
            return Msg.success("修改成功");
        } else {
            return Msg.error("修改失败");
        }

    }


    @RequestMapping(path = {"/resetpwdforemp"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg resetPassWordForEmp(Long empId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long userId = 0L;

        if (curUser.getUserType() == UserTypeEnum.AGENT.getVal()) {
            userId = agentEmpService.findAgentEmpById(empId).getUserId();
        }

        if (curUser.getUserType() == UserTypeEnum.VENDER.getVal()) {
            userId = venderEmpService.findVenderEmpById(empId).getUserId();
        }


        Integer result = userService.resetUserPwd(curUser, userId);

        if (result > 0) {
            return Msg.success("修改成功");
        } else {
            return Msg.error("修改失败");
        }

    }

    @RequestMapping(path = {"/modifypwd"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg modifyPwd(String oldPwd,String newPwd,HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return userService.modifyUserPwd(curUser,oldPwd,newPwd);
    }


    @RequestMapping(path = {"/isexist"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg isExist(String userName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        User user = userService.findByUserName(userName);
        if (null != user) {
            return Msg.error("已经存在的用户名");
        }

        return Msg.success("可以使用的用户名");

    }


    @RequestMapping(path = {"/getacctbyUserId"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAcctByUserId(Long id, Long storeId, Integer userType, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Account account = null;

        if (userType == UserTypeEnum.AGENT.getVal()) {
            Long userId = agentService.findAgentById(id).getUserId();
            account = accountService.findAcctByUserId(userId);
        }

        if (userType == UserTypeEnum.VENDER.getVal()) {
            Long userId = venderService.findVenderById(id).getUserId();
            account = accountService.findAcctByUserId(userId);
        }

        if (storeId != 0) {
            account=accountService.findAcctByStoreId(storeId);
        }

        if (null != account) {
            return Msg.success("查询成功", account);
        }

        return Msg.error("查询失败");
    }


    /**---------------------------------------退款--------------------------------------------*/

    @RequestMapping(path = {"/retoadmin"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg reToAdmin(Long agentId, BigDecimal amount, String reason, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long topAgentUserId=0L;
        try{
            Agent agent=agentService.findAgentById(agentId);
            topAgentUserId=agent.getUserId();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查询代理商信息失败,代理商ID={},Msg={}",agentId,e);
            return Msg.error("查询代理商账户信息失败");
        }


        return accountService.chargingRefundFromTopAgToAdmin(curUser,topAgentUserId,amount,reason);

    }


    @RequestMapping(path = {"/retoagent"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg reToAgent(Long venderId, BigDecimal amount, String reason, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long venderUserId=0L;
        try{
            Vender vender=venderService.findVenderById(venderId);
            venderUserId=vender.getUserId();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查询商家信息失败,代理商ID={},Msg={}",venderId,e);
            return Msg.error("查询商家账户信息失败");
        }

        return accountService.chargingRefundFromVenderToTopAg(curUser,venderUserId,amount,reason);
    }


}
