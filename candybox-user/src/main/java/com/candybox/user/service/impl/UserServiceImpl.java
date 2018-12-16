package com.candybox.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.candybox.common.enums.*;
import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.agent.dao.model.Agent;
import com.slst.agent.dao.model.AgentEmp;
import com.slst.agent.service.AgentEmpService;
import com.slst.agent.service.AgentService;
import com.candybox.common.dao.model.SysCfg;
import com.candybox.common.service.ImageService;
import com.candybox.common.service.SysCfgService;
import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.common.utils.RegexUtils;
import com.candybox.common.utils.StringUtils;
import com.candybox.common.web.vo.Msg;
import com.candybox.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsFee;
import com.slst.market.service.SmsFeeService;
import com.candybox.user.dao.UserDao;
import com.candybox.user.dao.model.User;
import com.candybox.user.service.RoleService;
import com.candybox.user.service.UserService;
import com.candybox.user.web.vo.AgentEmpVO;
import com.candybox.user.web.vo.AgentVO;
import com.candybox.user.web.vo.VenderEmpVO;
import com.candybox.user.web.vo.VenderVO;
import com.slst.vender.dao.model.Store;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.dao.model.VenderEmp;
import com.slst.vender.service.StoreService;
import com.slst.vender.service.VenderEmpService;
import com.slst.vender.service.VenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:15
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserDao userDao;

    @Resource
    private AgentService agentService;

    @Resource
    private VenderService venderService;

    @Resource
    private ImageService imageService;

    @Resource
    private AccountService accountService;

    @Resource
    private AgentEmpService agentEmpService;

    @Resource
    private VenderEmpService venderEmpService;
    @Resource
    private StoreService storeService;

    @Resource
    private RoleService roleService;

    @Resource
    private SmsFeeService smsFeeService;

    @Resource
    private SysCfgService sysCfgService;


    /**
     * 用户登录
     *
     * @param loginName(登录名可以是用户名,也可以是手机号)
     * @param pwd                          用户密码
     * @return
     */
    @Override
    public List<User> userLogin(String loginName, String pwd) {
        List<User> users = null;
        boolean ismobile = RegexUtils.isMobile(loginName);
        if (ismobile) {
            users = userDao.findByMobileAndPwd(loginName, pwd);
        } else {
            users = new ArrayList<>();
            User user = userDao.findByUserNameAndPwd(loginName, pwd);
            if (null != user)
                users.add(user);
        }
        return users;
    }



    @Override
    public UserVO initUserVO(User user) {

        if (null==user){
            return null;
        }
        Long userId=user.getId();
        UserVO userVo=new UserVO();

        userVo.setId(userId);
        userVo.setUserName(user.getUserName());
        userVo.setMobile(user.getMobile());
        userVo.setUserType(user.getUserType());
        userVo.setUserTypeDesc(UserTypeEnum.getByVal(user.getUserType()).getDesc());
        switch (user.getUserType()) {
            case 1://admin
                userVo.setDisplayName("超级管理员");
                break;
            case 2://代理商
                Agent agent=agentService.findByUserId(userId);
                if (null!=agent){
                    userVo.setAgentId(agent.getId());
                    userVo.setDisplayName(agent.getCompanyName());
                }else{
                    userVo=null;
                }
                break;
            case 3://商家
                Vender vender=venderService.findVenderByUserId(userId);
                if (null!=vender){
                    userVo.setVenderId(vender.getId());
                    userVo.setDisplayName(vender.getCompanyName());
                    userVo.setVenderName(vender.getCompanyName());
                    userVo.setAgentId(vender.getAgentId());
                }else{
                    userVo=null;
                }
                break;
            case 21://代理商员工
                AgentEmp agentEmp=agentEmpService.findAgEmpByUserId(userId);
                if(null!= agentEmp){
                    userVo.setAgentId(agentEmp.getAgentId());
                    userVo.setAgentEmpId(agentEmp.getId());
                    userVo.setDisplayName(agentEmp.getEmpName());
                }else{
                    userVo=null;
                }
                break;
            case 31://商家员工
                VenderEmp venderEmp=venderEmpService.findVenderEmpByUserId(userId);
                if(null!= venderEmp){
                    userVo.setVenderId(venderEmp.getVenderId());
                    userVo.setVenderEmpId(venderEmp.getId());
                    userVo.setDisplayName(venderEmp.getEmpName());
                    userVo.setStoreId(venderEmp.getStoreId());
                    Vender empVender=venderService.findVenderById(venderEmp.getVenderId());
                    if(null!= empVender){
                        userVo.setVenderName(empVender.getCompanyName());
                    }else{
                        userVo=null;
                    }
                    Store store=storeService.findStoreById(venderEmp.getStoreId());
                    if(null!= store){
                        userVo.setStoreName(store.getStoreName());
                    }else{
                        userVo.setStoreName("");
                    }
                }else{
                    userVo=null;
                }
                break;
            case 4://数据使用方

                break;
        }
        return userVo;
    }

    /**
     * 代理商注册
     *
     * @param curUser
     * @param agentVO
     * @return
     */
    @Transactional
    @Override
    public AgentVO registerAgent(UserVO curUser, AgentVO agentVO) {

        switch (curUser.getUserType()) {
            case 1:
                agentVO = registerAgentByAdmin(curUser, agentVO);//通过ADMIN注册代理商
                break;
            case 2:
                agentVO = registerAgentByAgent(curUser, agentVO);//通过代理商注册代理商
                break;
        }

        return agentVO;
    }

    /**
     * 修改代理商基本信息
     *
     * @param agentVO
     * @return
     */
    @Transactional
    @Override
    public AgentVO modifyAgent(AgentVO agentVO) {
        Long id = agentVO.getId();
        Long agentId = agentVO.getAgentId();
        Date curDate = new Date();

        User user = null;
        Agent agent = null;
        try {
            user = userDao.findById(id).get();
            agent = agentService.findAgentById(agentId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("数据库查找user/agent操作异常！");
            return null;
        }

        if (null != user && null != agent) {
            user.setMobile(agentVO.getMobile());
            user.setChanger(agentVO.getUserName());
            user.setChangeTime(curDate);

            User modifiedUser = null;
            try {
                modifiedUser = userDao.saveAndFlush(user);
            }catch (Exception e){
                e.printStackTrace();
                LOGGER.error("更新数据库user表操作异常！");
                return null;
            }

            if (null != modifiedUser) {
                agent.setMobile(agentVO.getMobile());
                agent.setContact(agentVO.getContact());
                agent.setTaxIdNum(agentVO.getTaxIdNum());
                agent.setWebsite(agentVO.getWebsite());
                agent.setIndustryId(agentVO.getIndustryId());
                agent.setIndustryName(agentVO.getIndustryName());
                agent.setCityId(agentVO.getCityId());
                agent.setCityName(agentVO.getCityName());
                agent.setAddress(agentVO.getAddress());
                agent.setChanger(agentVO.getUserName());
                agent.setChangeTime(curDate);

                String blImageStr = agentVO.getBizlicense();//营业执照图片二进制
                String logoImageStr = agentVO.getLogo();//LOGO图片二进制
                String basePath = agentVO.getBasePath();//网站访问地址
                String realPath = agentVO.getRealPath();//项目物理地址

                if (!(StringUtils.isNullOrEmpty(blImageStr)) && !(blImageStr.equals(agent.getBizlicense()))) {
                    String blPath = imageService.saveBizLicense(blImageStr, realPath, basePath);//营业执照访问路径
                    agent.setBizlicense(blPath);
                }

                if (!(StringUtils.isNullOrEmpty(logoImageStr)) && !(logoImageStr.equals(agent.getLogo()))) {
                    String logoPath = imageService.saveLogo(logoImageStr, realPath, basePath);//LOGO访问路径
                    agent.setLogo(logoPath);
                }

                Agent rtnAgent = null;
                try {
                    rtnAgent = agentService.updateAgent(agent);
                }catch (Exception e){
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                    LOGGER.error("更新数据库agent表操作异常！");
                    return null;
                }

                if (null == rtnAgent) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("更新数据库agent表返回错误！");
                    return null;
                }
                return agentVO;
            }

        }
        return null;
    }

    /**
     * 通过ADMIN注册代理商
     *
     * @param agentVO
     * @return
     */
    @Transactional
    @Override
    public AgentVO registerAgentByAdmin(UserVO curUser, AgentVO agentVO) {

//        Long result= 0L;

        Date curDate = new Date();//当前时间
        Integer yes = YNEnum.YES.getVal();
        String creator = curUser.getUserName();

        User user = new User();
        user.setUserName(agentVO.getUserName());
        String pwd="88888888";//默认密码8个8
        try {
            pwd= EncryptionEnum.MD5_32BIT.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPwd(pwd);
        user.setMobile(agentVO.getMobile());
        user.setUserType(UserTypeEnum.AGENT.getVal());
        user.setSignDate(curDate);
        user.setYn(yes);//标识该条数据可用
        user.setCreateTime(curDate);
        user.setCreator(creator);

        User retUser = null;
        try{
            retUser = userDao.save(user);
        }catch (Exception e){
            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("新建user信息，数据库操作异常！");
            return null;
        }

        if (null != retUser) {
            //初始化代理商对象
            Agent agent = new Agent();
            agent.setUserId(retUser.getId());
            agent.setCompanyName(agentVO.getCompanyName());
            agent.setSimpleName("");
            agent.setContact(agentVO.getContact());
            agent.setMobile(agentVO.getMobile());
            agent.setCityId(agentVO.getCityId());
            agent.setCityName(agentVO.getCityName());
            agent.setAddress(agentVO.getAddress());
            agent.setIndustryId(agentVO.getIndustryId());
            agent.setIndustryName(agentVO.getIndustryName());
            agent.setWebsite(agentVO.getWebsite());
            agent.setEarningsRate(agentVO.getEarningsRate());
            agent.setTaxIdNum(agentVO.getTaxIdNum());
            agent.setLevel(AgentLevelEnum.TOP_LEVEL.getVal());
            agent.setParentId(0L);
            agent.setYn(yes);
            agent.setCreateTime(curDate);
            agent.setCreator(creator);

            String blImageStr = agentVO.getBizlicense();//营业执照图片二进制
            String logoImageStr = agentVO.getLogo();//LOGO图片二进制
            String basePath = agentVO.getBasePath();//网站访问地址
            String realPath = agentVO.getRealPath();//项目物理地址

            String blPath = imageService.saveBizLicense(blImageStr, realPath, basePath);//营业执照访问路径
            String logoPath = imageService.saveLogo(logoImageStr, realPath, basePath);//LOGO访问路径

            agent.setBizlicense(blPath);
            agent.setLogo(logoPath);

            Agent rtnAgent = null;
            try {
                rtnAgent = agentService.createAgent(agent);
            } catch (Exception e) {//Agent表存入失败
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建agent信息，数据库操作异常！");
                return null;
            }

            if (null == rtnAgent) { //Agent表存入失败
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建agent信息，数据库返回错误！");
                return null;
            }

            //Agent表存入成功，则进行账户初始化，创建账户
            Account account = new Account();
            account.setUserId(retUser.getId());
            account.setAcctName(retUser.getUserName() + "_acct");
            account.setYn(yes);
            account.setCreateTime(curDate);
            account.setCreator(creator);


            Account rtnAcct = null;
            try {
                rtnAcct = accountService.createAccount(account);
            } catch (Exception e) {
                //存入Acct表失败。删除刚创建的对应的User表和Agent表信息
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建acct信息，数据库操作异常！");
                return null;
            }

            if (null == rtnAcct) {   //存入Acct表失败
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建acct信息，数据库返回异常！");
                return null;
            }


            Integer result= roleService.saveUserRole(retUser.getId(),2L);//TODO 这里需要修改
            if (result<=0){
                return null;
            }

            agentVO.setId(retUser.getId());
            agentVO.setAgentId(rtnAgent.getId());
            return agentVO;

        }

        return null;
    }

    /**
     * 通过代理商注册代理商
     *
     * @param agentVO
     * @return
     */
    private AgentVO registerAgentByAgent(UserVO curUser, AgentVO agentVO) {

        return null;
    }


    /**
     * 代理商员工注册（通过代理商开户）
     *
     * @param curUser
     * @param agentEmpVO
     * @return
     */
    @Transactional
    @Override
    public AgentEmpVO registerAgentEmp(UserVO curUser, AgentEmpVO agentEmpVO) {

        Date curDate = new Date();  //当前时间
        Integer yes = YNEnum.YES.getVal();
        String creator = curUser.getUserName();

        User user = new User();
        user.setUserName(agentEmpVO.getUserName());
        String pwd="88888888";//默认密码8个8
        try {
            pwd=EncryptionEnum.MD5_32BIT.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPwd(pwd);
        user.setMobile(agentEmpVO.getMobile());
        user.setUserType(UserTypeEnum.AGENT_EMP.getVal());
        user.setSignDate(curDate);
        user.setYn(yes);//标识该条数据可用
        user.setCreateTime(curDate);
        user.setCreator(creator);

        User rtnUser = null;
        try {
            rtnUser = userDao.save(user);//存入User表并返回
        } catch (Exception e) {
            //存入Acct表失败
            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("新建User信息，数据库操作异常！");
            return null;
        }

        if (null != rtnUser) {    //存入User表成功
            //初始化代理商员工对象
            AgentEmp agentEmp = new AgentEmp();
            //agentEmp:empName; agentId; mobile; address; jobDesc; isSalesman; userId;
            agentEmp.setEmpName(agentEmpVO.getEmpName());
            agentEmp.setAgentId(curUser.getAgentId());
            agentEmp.setMobile(agentEmpVO.getMobile());
            agentEmp.setAddress(agentEmpVO.getAddress());
            agentEmp.setJobDesc(agentEmpVO.getJobDesc());
            agentEmp.setIsSalesman(agentEmpVO.getIsSalesman());
            agentEmp.setUserId(rtnUser.getId());

            agentEmp.setYn(yes);
            agentEmp.setCreateTime(curDate);
            agentEmp.setCreator(creator);


            AgentEmp rtnAgentEmp = null;
            try {
                rtnAgentEmp = agentEmpService.createAgentEmp(agentEmp);//存入AgentEmp表并返回
            } catch (Exception e) {
                //存入AgentEmp表失败。删除刚才创建的对应的User表信息
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建agentEmp信息，数据库操作异常！");
                return null;
            }

            if (null == rtnAgentEmp) {    //存入AgentEmp表失败
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建agentEmp信息，数据库返回错误！");
                return null;
            }

            Integer result= roleService.saveUserRole(rtnUser.getId(),3L);//TODO 这里需要修改
            if (result<=0){
                return null;
            }

            agentEmpVO.setId(rtnUser.getId());
            agentEmpVO.setAgentEmpId(rtnAgentEmp.getId());
            return agentEmpVO;  //存入成功
        }

        return null;  //存入失败
    }

    /**
     * 代理商员工修改密码
     *
     * @return
     */
    @Transactional
    @Override
    public AgentEmpVO modifyAgentEmpByAgEmp(AgentEmpVO agentEmpVO) {
        Date curDate = new Date();
        String changer = agentEmpVO.getUserName();
        Long id = agentEmpVO.getId();
        String reMobile = agentEmpVO.getMobile();

        User user = null;
        try {
            user = userDao.findById(id).get();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找user，数据库操作异常！");
            return null;
        }

        if (null != user) {
            Boolean flag = !(reMobile.equals(user.getMobile()));  //是否修改了电话号码

//            user.setPwd(agentEmpVO.getPwd());
            user.setMobile(reMobile);
            user.setChanger(changer);
            user.setChangeTime(curDate);
            User rtnUser = null;
            try{
                rtnUser = userDao.saveAndFlush(user);//更新User表中信息
            }catch (Exception e){
                e.printStackTrace();
                LOGGER.error("更新user表，数据库操作异常！");
                return null;
            }

            if (null != rtnUser) {//User表更新成功

                if (flag) {   //如果修改了电话，则更新agentEmp
                    AgentEmp agentEmp = null;
                    try {
                        agentEmp = agentEmpService.findAgentEmpById(agentEmpVO.getAgentEmpId());//查找相应agentEmp
                    }catch (Exception e){
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        LOGGER.error("查找agentEmp，数据库操作异常！");
                        return null;
                    }

                    if(null == agentEmp){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        LOGGER.error("数据库中，未找到对应的agentEmp数据！");
                        return null;
                    }

                    //修改电话等信息
                    agentEmp.setMobile(reMobile);
                    agentEmp.setChanger(changer);
                    agentEmp.setChangeTime(curDate);
                    AgentEmp rtnAgentEmp = null;
                    try {
                        rtnAgentEmp = agentEmpService.updateAgentEmp(agentEmp);//更新AgentEmp表中信息
                    }catch (Exception e){
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        LOGGER.error("更新agentEmp，数据库操作异常！");
                        return null;
                    }

                    if (null == rtnAgentEmp) {//更新AgentEmp表失败
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        LOGGER.error("更新agentEmp，数据库返回错误！");
                        return null;
                    }
                }

                return agentEmpVO;   //修改成功
            }
        }

        return null;    //更新User表失败
    }

    /**
     * 代理商使用。修改代理商员工信息
     *
     * @param curUser
     * @param agentEmpVO
     * @return
     */
    @Transactional
    @Override
    public AgentEmpVO modifyAgentEmpByAgent(UserVO curUser, AgentEmpVO agentEmpVO) {
        Date curDate = new Date();
        String changer = curUser.getUserName();
        String reMobile = agentEmpVO.getMobile();

        AgentEmp agentEmp = null;
        try {
            agentEmp = agentEmpService.findAgentEmpById(agentEmpVO.getAgentEmpId());
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找agentEmp，数据库操作异常！");
            return null;
        }

        if (null != agentEmp) { //修改agentEmp
            Boolean flag = !(reMobile.equals(agentEmp.getMobile()));//是否修改了电话号码

            agentEmp.setEmpName(agentEmpVO.getEmpName());
            agentEmp.setMobile(agentEmpVO.getMobile());
            agentEmp.setAddress(agentEmpVO.getAddress());
            agentEmp.setJobDesc(agentEmpVO.getJobDesc());
            agentEmp.setIsSalesman(agentEmpVO.getIsSalesman());
            agentEmp.setChanger(changer);
            agentEmp.setChangeTime(curDate);
            AgentEmp rtnAgentEmp = null;
            try {
                rtnAgentEmp = agentEmpService.updateAgentEmp(agentEmp);//更新AgentEmp表
            }catch (Exception e){
                e.printStackTrace();
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("更新agentEmp，数据库操作异常！");
                return null;
            }

            if (null != rtnAgentEmp) {    //更新agentEmp表成功

                if (flag) {//修改了电话，则需更新User信息
                    User user = userDao.findById(rtnAgentEmp.getUserId()).get();
                    user.setMobile(reMobile);
                    user.setChanger(changer);
                    user.setChangeTime(curDate);
                    User rtnUser = null;
                    try {
                        rtnUser = userDao.saveAndFlush(user);//更新User表
                    }catch (Exception e){
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        LOGGER.error("更新user，数据库操作异常！");
                        return null;
                    }

                    if (null == rtnUser) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        LOGGER.error("更新user，数据库返回错误！");
                        return null; //更新User表失败
                    }
                }

                return agentEmpVO;  //修改成功
            }
        }
        return null;//更新AgentEmp表失败
    }

    /**
     * 删除指定AgentEmpId的代理商员工
     *
     * @param agentEmpId
     * @return
     */
    @Transactional
    @Override
    public Msg deleteAgentEmp(Long agentEmpId) {
        String succ = Msg.success().getMsg();
        Msg msg = null;

        AgentEmp agentEmp = null;
        try {
            agentEmp = agentEmpService.findAgentEmpById(agentEmpId);//根据agentEmpId查找agentEmp
        }catch (Exception e){
            e.printStackTrace();
            msg = Msg.error("查找agentEmp,数据库操作异常！");
            return msg;
        }

        Long id = agentEmp.getUserId(); //取得agentEmpId对应的userId
        try {
            msg = agentEmpService.deleteAgentEmp(agentEmpId);
        }catch (Exception e){
            e.printStackTrace();
            msg = Msg.error("删除agentEmp,数据库操作异常！");
            return msg;
        }

        if (succ.equals(msg.getMsg())) {//AgentEmp表删除成功

            try {
                userDao.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                msg = Msg.error("删除对应的user,数据库操作异常！");
                return msg;
            }

        }

        return msg;
    }

    /**
     * 商家注册（通过代理商/代理商员工开户）
     *
     * @param curUser  当前操作的代理商
     * @param venderVO 开户商家
     * @return
     */
    @Transactional
    @Override
    public VenderVO registerVender(UserVO curUser, VenderVO venderVO) {

        Date curDate = new Date();//当前时间
        Integer yes = YNEnum.YES.getVal();
        String creator = curUser.getUserName();

        User user = new User();
        user.setUserName(venderVO.getUserName());
        String pwd="88888888";//默认密码8个8
        try {
            pwd=EncryptionEnum.MD5_32BIT.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPwd(pwd);
        user.setMobile(venderVO.getMobile());
        user.setUserType(UserTypeEnum.VENDER.getVal());
        user.setSignDate(curDate);
        user.setYn(yes);//标识该条数据可用
        user.setCreateTime(curDate);
        user.setCreator(creator);

        User retUser = null;
        try {
            retUser = userDao.save(user);   //存入User表
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("新建user，数据库操作异常！");
            return null;
        }

        if (null != retUser) {  //存入User表成功
            //初始化商家对象
            Vender vender = new Vender();
            vender.setUserId(retUser.getId());
            vender.setCompanyName(venderVO.getCompanyName());
            vender.setSimpleName("");
            vender.setContact(venderVO.getContact());
            vender.setMobile(venderVO.getMobile());
            vender.setCityId(venderVO.getCityId());
            vender.setCityName(venderVO.getCityName());
            vender.setAddress(venderVO.getAddress());
            vender.setIndustryId(venderVO.getIndustryId());
            vender.setIndustryName(venderVO.getIndustryName());
            vender.setWebsite(venderVO.getWebsite());
            vender.setTaxIdNum(venderVO.getTaxIdNum());
            vender.setAgentId(curUser.getAgentId());//所属的代理商id
            vender.setAgentEmpId(curUser.getAgentEmpId());//所属代理商员工ID。为0时则为代理商直接开户
            vender.setYn(yes);
            vender.setCreateTime(curDate);
            vender.setCreator(creator);

            String blImageStr = venderVO.getBizlicense();//营业执照图片二进制
            String logoImageStr = venderVO.getLogo();//LOGO图片二进制
            String basePath = venderVO.getBasePath();//网站访问地址
            String realPath = venderVO.getRealPath();//项目物理地址

            String blPath = imageService.saveBizLicense(blImageStr, realPath, basePath);//营业执照访问路径
            String logoPath = imageService.saveLogo(logoImageStr, realPath, basePath);//LOGO访问路径

            vender.setBizlicense(blPath);
            vender.setLogo(logoPath);


            Vender rtnVender = null;
            try {
                rtnVender = venderService.createVender(vender);
            } catch (Exception e) {//存入Vender表失败
                e.printStackTrace();
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建vender，数据库操作异常！");
                return null;
            }

            if (null == rtnVender) {  //存入Vender表失败
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建vender，数据库返回错误！");
                return null;
            }

            //存入Vender表成功
            Account account = new Account();
            account.setUserId(retUser.getId());
            account.setAcctName(retUser.getUserName() + "_acct");
            account.setYn(yes);
            account.setCreateTime(curDate);
            account.setCreator(creator);


            Account rtnAcct = null;
            try {
                rtnAcct = accountService.createAccount(account);
            } catch (Exception e) { //存入Acct表失败
                e.printStackTrace();
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建acct，数据库操作异常！");
                return null;
            }

            if (null == rtnAcct) {    //存入Acct表失败
                //删除刚创建的对应的User表和Vender表数据
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建acct，数据库返回错误！");
                return null;
            }

            //设置角色
            Integer result= roleService.saveUserRole(retUser.getId(),4L);//TODO 这里需要修改
            if (result<=0){
                return null;
            }

            SysCfg sysCfg=sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_SMS_FEE.getVal());
            if (null==sysCfg){
                return null;
            }
            //设置短信费用
            SmsFee smsFee=new SmsFee();
            smsFee.setVenderId(rtnVender.getId());
            smsFee.setFee(Long.parseLong(sysCfg.getCodeVal()));
            SmsFee rtnSmsFee= smsFeeService.createSmsFee(curUser,smsFee);
            if(null== rtnSmsFee){
                return null;
            }


            venderVO.setId(retUser.getId());
            venderVO.setVenderId(rtnVender.getId());
            return venderVO;
        }

        return null;
    }

    /**
     * 修改商家基本信息
     *
     * @param venderVO
     * @return
     */
    @Transactional
    @Override
    public VenderVO modifyVender(VenderVO venderVO) {
        Long id = venderVO.getId();
        Long venderId = venderVO.getVenderId();
        Date curDate = new Date();

        User user = null;
        Vender vender = null;
        try {
            user = userDao.findById(id).get();
            vender = venderService.findVenderById(venderId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找user/vender，数据库操作异常！venderId={}",venderId,e);
            return null;
        }

        if (null != user && null != vender) {
            //修改user表信息
            user.setMobile(venderVO.getMobile());   //user中只能修改手机号码和密码
//            user.setPwd(venderVO.getPwd());
            user.setChanger(venderVO.getUserName());
            user.setChangeTime(curDate);

            User modifiedUser = null;
            try {
                modifiedUser = userDao.saveAndFlush(user);//更新user表信息
            } catch (Exception e) { //更新user表失败
                e.printStackTrace();
                LOGGER.error("更新user，数据库操作异常！userId={}",id,e);
                return null;
            }

            if (null != modifiedUser) {
                //修改vender表信息
                vender.setMobile(venderVO.getMobile());
                vender.setContact(venderVO.getContact());
                vender.setTaxIdNum(venderVO.getTaxIdNum());
                vender.setWebsite(venderVO.getWebsite());
                vender.setIndustryId(venderVO.getIndustryId());
                vender.setIndustryName(venderVO.getIndustryName());
                vender.setCityId(venderVO.getCityId());
                vender.setCityName(venderVO.getCityName());
                vender.setAddress(venderVO.getAddress());
                vender.setChanger(venderVO.getUserName());
                vender.setChangeTime(curDate);

                String blImageStr = venderVO.getBizlicense();//营业执照图片二进制
                String logoImageStr = venderVO.getLogo();//LOGO图片二进制
                String basePath = venderVO.getBasePath();//网站访问地址
                String realPath = venderVO.getRealPath();//项目物理地址

                Boolean flag = !(blImageStr.equals(vender.getBizlicense()));
                if (!(StringUtils.isNullOrEmpty(blImageStr)) && !(blImageStr.equals(vender.getBizlicense()))) {
                    String blPath = imageService.saveBizLicense(blImageStr, realPath, basePath);//营业执照访问路径
                    vender.setBizlicense(blPath);
                }

                if (!(StringUtils.isNullOrEmpty(logoImageStr)) && !(logoImageStr.equals(vender.getLogo()))) {
                    String logoPath = imageService.saveLogo(logoImageStr, realPath, basePath);//LOGO访问路径
                    vender.setLogo(logoPath);
                }

                Vender rtnVender = null;
                try {
                    rtnVender = venderService.updateVender(vender);//更新vender表信息
                } catch (Exception e) { //更新vender表失败
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("更新vender，数据库操作异常！vender={}",JSON.toJSON(vender).toString(),e);
                    return null;
                }

                if (null == rtnVender) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("更新vender，数据库返回错误！");
                    return null;
                }
                return venderVO;
            }
        }
        return null;
    }


    /**
     * 商家员工注册（商家操作）
     *
     * @param curUser
     * @param venderEmpVO
     * @return
     */
    @Transactional
    @Override
    public VenderEmpVO registerVenderEmp(UserVO curUser, VenderEmpVO venderEmpVO) {

        Date curDate = new Date();
        String creator = curUser.getUserName();
        Integer yes = YNEnum.YES.getVal();

        User user = new User();
        user.setUserName(venderEmpVO.getUserName());
        String pwd="88888888";//默认密码8个8
        try {
            pwd=EncryptionEnum.MD5_32BIT.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPwd(pwd);
        user.setMobile(venderEmpVO.getMobile());
        user.setUserType(UserTypeEnum.VENDER_EMP.getVal());
        user.setSignDate(curDate);
        user.setYn(yes);//标识该条数据可用
        user.setCreateTime(curDate);
        user.setCreator(creator);

        User rtnUser = null;
        try {
            rtnUser = userDao.save(user);   //新建user信息，存入数据库
        } catch (Exception e) { //存入失败
            e.printStackTrace();
            LOGGER.error("新建User，数据库操作异常！");
            return null;
        }

        if (null != rtnUser) {    //存入User表成功
            //初始化VenderEmp
            Long userId = rtnUser.getId();
            VenderEmp venderEmp = new VenderEmp();

            venderEmp.setEmpName(venderEmpVO.getEmpName());
            if (null!=venderEmpVO.getStoreId() && venderEmpVO.getStoreId()>0L) {
                venderEmp.setStoreId(venderEmpVO.getStoreId());
            }else{
                venderEmp.setStoreId(0L);
            }
            venderEmp.setVenderId(curUser.getVenderId());
            venderEmp.setMobile(venderEmpVO.getMobile());
            venderEmp.setAddress(venderEmpVO.getAddress());
            venderEmp.setJobDesc(venderEmpVO.getJobDesc());

            venderEmp.setUserId(userId);
            venderEmp.setYn(yes);
            venderEmp.setCreator(creator);
            venderEmp.setCreateTime(curDate);


            VenderEmp rtnVenderEmp = null;
            try {
                rtnVenderEmp = venderEmpService.createVenderEmp(venderEmp);
            } catch (Exception e) {
                //存入VenderEmp表失败。则删除刚创建的对应User表数据
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建venderEmp，数据库操作异常！");
                return null;
            }

            if (null == rtnVenderEmp) {  //存入VenderEmp表失败
                //则删除刚创建的对应User表数据
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("新建venderEmp，数据库返回错误！");
                return null;
            }

            Integer result= roleService.saveUserRole(userId,5L);//TODO 这里需要修改
            if (result<=0){
                return null;
            }



            //存入VenderEmp表成功
            venderEmpVO.setId(userId);
            venderEmpVO.setVenderEmpId(rtnVenderEmp.getId());

            return venderEmpVO;//操作成功
        }

        return null;//操作失败
    }

    /**
     * 商家员工使用。修改商家员工基本信息
     *
     * @param venderEmpVO
     * @return
     */
    @Transactional
    @Override
    public VenderEmpVO modifyVenderEmpByVdEmp(VenderEmpVO venderEmpVO) {

        Boolean flag = null;//是否修改了mobile
        Date curDate = new Date();
        String reMobile = venderEmpVO.getMobile();
        User user = null;

        try {
            user = userDao.findById(venderEmpVO.getId()).get();//查找对应user
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找user，数据库操作异常！");
            return null;
        }

        if (null != user) {
            flag = !(reMobile.equals(user.getMobile())); //是否修改了mobile
            user.setPwd(venderEmpVO.getPwd());
            user.setMobile(reMobile);
            user.setChanger(venderEmpVO.getUserName());
            user.setChangeTime(curDate);
            User rtnUser = null;
            try {
                rtnUser = userDao.saveAndFlush(user);//更新user表
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("更新User，数据库操作异常！");
                return null;
            }

            if (null != rtnUser) {

                if (!flag) return venderEmpVO;  //没有修改mobile，则操作完成并返回

                //修改了mobile，则需更新VenderEmp表
                VenderEmp venderEmp = null;
                try {
                    venderEmp = venderEmpService.findVenderEmpById(venderEmpVO.getVenderEmpId());//查找对应venderEmp
                } catch (Exception e) {
                    //查找异常
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("查找venderEmp，数据库操作异常！");
                    return null;
                }
                if(null == venderEmp){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("查找venderEmp，数据库返回错误！");
                    return null;
                }

                venderEmp.setMobile(reMobile);
                venderEmp.setChanger(venderEmpVO.getUserName());
                venderEmp.setChangeTime(curDate);
                VenderEmp rtnVenderEmp = null;
                try {
                    rtnVenderEmp = venderEmpService.updateVenderEmp(venderEmp); //更新venderEmp表
                } catch (Exception e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("更新venderEmp，数据库操作异常！");
                    return null;
                }

                if (null == rtnVenderEmp) { //更新VenderEmp表失败
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                    LOGGER.error("更新venderEmp，数据库返回错误！");
                    return null;
                }
                return venderEmpVO;
            }

        }

        return null;
    }

    /**
     * 商家使用，修改商家员工信息
     *
     * @param curUser
     * @param venderEmpVO
     * @return
     */
    @Transactional
    @Override
    public VenderEmpVO modifyVenderEmpByVender(UserVO curUser, VenderEmpVO venderEmpVO) {
        //empName，storeId，venderId，mobile，address，jobDesc，userId，yn，creator，createTime，changer，changeTime
        Date curDate = new Date();
        String changer = curUser.getUserName();
        String reMobile = venderEmpVO.getMobile();
        Boolean flag = null;//是否修改了mobile

        VenderEmp venderEmp = null;
        try {
            venderEmp = venderEmpService.findVenderEmpById(venderEmpVO.getVenderEmpId());//查找对应venderEmp
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找对应venderEmp，数据库操作异常！");
            return null;
        }
        if (null == venderEmp) {
            LOGGER.error("查找对应venderEmp，数据库返回异常！");
            return null;
        }

        venderEmp.setEmpName(venderEmpVO.getEmpName());
        if (null!=venderEmpVO.getStoreId() && venderEmpVO.getStoreId()>0L) {
            venderEmp.setStoreId(venderEmpVO.getStoreId());
        }else{
            venderEmp.setStoreId(0L);
        }
        venderEmp.setMobile(reMobile);
        venderEmp.setAddress(venderEmpVO.getAddress());
        venderEmp.setJobDesc(venderEmpVO.getJobDesc());
        venderEmp.setChanger(changer);
        venderEmp.setChangeTime(curDate);

        VenderEmp rtnVenderEmp = null;
        try {
            rtnVenderEmp = venderEmpService.updateVenderEmp(venderEmp);//更新venderEmp表
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("更新venderEmp，数据库操作异常！");
            return null;
        }
        if (null == rtnVenderEmp) {
            LOGGER.error("更新venderEmp，数据库返回错误！");
            return null;
        }

        flag = !(reMobile.equals(venderEmp.getMobile()));
        if (!flag) return venderEmpVO;   //没有修改mobile

        //修改了mobile
        User user = null;
        try {
            user = userDao.findById(venderEmpVO.getId()).get();//查找对应user
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("查找User，数据库操作异常！");
            return null;
        }
        if (null == user) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("查找User，数据库返回错误！");
            return null;
        }

        user.setMobile(reMobile);
        user.setChanger(changer);
        user.setChangeTime(curDate);
        User rtnUser = null;
        try {
            rtnUser = userDao.saveAndFlush(user);//更新对应user
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("更新User，数据库操作异常！");
            return null;
        }
        if (null == rtnUser) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("更新User，数据库返回错误！");
            return null;
        }

        return venderEmpVO;
    }

    /**
     * 删除指定venderEmpId的商家员工
     *
     * @param venderEmpId
     * @return
     */
    @Transactional
    @Override
    public Msg deleteVenderEmp(Long venderEmpId) {
        String succ = Msg.success().getMsg();
        VenderEmp venderEmp = null;
        try {
            venderEmp = venderEmpService.findVenderEmpById(venderEmpId); //查找venderEmp，以取得对应userId
        } catch (Exception e) {
            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            LOGGER.error("查找venderEmp，数据库操作异常！");
            return Msg.error("查找venderEmp表数据出错！");
        }
        if(null == venderEmp){
            LOGGER.error("查找venderEmp，数据库返回错误！");
            return Msg.error("查找venderEmp表数据返回null！");
        }

        Long id = venderEmp.getUserId();
        Msg msg = null;
        try {
            msg = venderEmpService.deleteVenderEmpById(venderEmpId);    //删除对应venderEmp
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("删除venderEmp，数据库操作异常！");
            return Msg.error("删除venderEmp表数据出错！");
        }

        if (succ.equals(msg.getMsg())) {  //VenderEmp表中删除成功
            //删除User表中信息
            try {
                userDao.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                LOGGER.error("删除User，数据库操作异常！");
                return Msg.error("删除user表数据出错！");
            }
        }
        return msg;
    }

    @Transactional
    @Override
    public Integer resetUserPwd(UserVO curUser, Long userId) {
        String pwd="88888888";//默认密码8个8
        try {
            pwd=EncryptionEnum.MD5_32BIT.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDao.modifyUserPwd(pwd,userId,curUser.getUserName(),new Date());
    }

    @Transactional
    @Override
    public Msg modifyUserPwd(UserVO curUser, String oldPwd, String newPwd) {

        Long userId=curUser.getId();
        String userName=curUser.getUserName();
        User user=userDao.findById(userId).get();

        if (null==user) return Msg.error("未找到用户信息");

        if (oldPwd.equals(user.getPwd())){

            Integer result=userDao.modifyUserPwd(newPwd,userId,userName,new Date());

            return result>0?Msg.success("修改成功"):Msg.error("修改失败");
        }
        return Msg.error("与原始密码不相同");
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName.trim());
    }

    /**
     * 删除指定id的user
     *
     * @param id
     */
    private void deleteUserById(Long id) {
        try {
            userDao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
