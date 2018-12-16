package com.candybox.user.service;

import com.candybox.common.service.BaseService;
import com.candybox.common.web.vo.Msg;
import com.candybox.common.web.vo.UserVO;
import com.candybox.user.dao.model.User;
import com.candybox.user.web.vo.AgentEmpVO;
import com.candybox.user.web.vo.AgentVO;
import com.candybox.user.web.vo.VenderEmpVO;
import com.candybox.user.web.vo.VenderVO;

import java.util.List;


public interface UserService extends BaseService<User, Long> {

    /**
     * 用户登录
     * @param loginName(登录名可以是用户名,也可以是手机号)
     * @param pwd 用户密码
     * @return
     */
    List<User> userLogin(String loginName,String pwd);

    UserVO initUserVO(User user);

    /**
     * 注册代理商
     * @return
     */
    AgentVO registerAgent(UserVO curUser, AgentVO agentVO);

    /**
     * 通过Admin注册代理商
     * @param curUser
     * @param agentVO
     * @return
     */
    AgentVO registerAgentByAdmin(UserVO curUser, AgentVO agentVO);

    /**
     * 修改代理商基本信息
     * @param agentVO
     * @return
     */
    AgentVO modifyAgent(AgentVO agentVO);



    /**
     * 代理商员工注册（通过代理商开户）
     * @param curUser
     * @param agentEmpVO
     * @return
     */
    AgentEmpVO registerAgentEmp(UserVO curUser, AgentEmpVO agentEmpVO);

    /**
     * 代理商员工修改密码
     * @return
     */
    AgentEmpVO modifyAgentEmpByAgEmp(AgentEmpVO agentEmpVO);

    /**
     * 代理商使用。修改代理商员工信息
     * @param curUser
     * @param agentEmpVO
     * @return
     */
    AgentEmpVO modifyAgentEmpByAgent(UserVO curUser,AgentEmpVO agentEmpVO);

    /**
     * 删除指定AgentEmpId的代理商员工
     * @param agentEmpId
     * @return
     */
    Msg deleteAgentEmp(Long agentEmpId);


    /**
     * 商家注册（通过代理商/代理商员工开户）
     * @param curUser 当前操作的代理商
     * @param venderVO 开户商家
     * @return
     */
    VenderVO registerVender(UserVO curUser, VenderVO venderVO);

    /**
     * 修改商家基本信息
     * @param venderVO
     * @return
     */
    VenderVO modifyVender(VenderVO venderVO);


    /**
     * 商家员工注册（商家操作）
     * @param curUser
     * @param venderEmpVO
     * @return
     */
    VenderEmpVO registerVenderEmp(UserVO curUser, VenderEmpVO venderEmpVO);

    /**
     * 商家员工使用。修改商家员工基本信息
     * @param venderEmpVO
     * @return
     */
    VenderEmpVO modifyVenderEmpByVdEmp(VenderEmpVO venderEmpVO);

    /**
     * 商家使用，修改商家员工信息
     * @param curUser
     * @param venderEmpVO
     * @return
     */
    VenderEmpVO modifyVenderEmpByVender(UserVO curUser, VenderEmpVO venderEmpVO);

    /**
     * 删除指定venderEmpId的商家员工
     * @param venderEmpId
     * @return
     */
    Msg deleteVenderEmp(Long venderEmpId);

    /**
     * 重置密码
     * @param curUser
     * @param userId
     * @return
     */
    Integer resetUserPwd(UserVO curUser,Long userId);

    /**
     * 修改密码
     * @param curUser
     * @param oldPwd
     * @param  newPwd
     * @return
     */
    Msg modifyUserPwd(UserVO curUser,String oldPwd,String newPwd);

    User findByUserName(String userName);


}
