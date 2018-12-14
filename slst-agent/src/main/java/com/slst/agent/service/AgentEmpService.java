package com.slst.agent.service;

import com.slst.agent.dao.model.AgentEmp;
import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AgentEmpService extends BaseService<AgentEmp,Long> {

    /**
     * 代理商员工注册
     * @param agentEmp
     * @return
     */
    AgentEmp createAgentEmp(AgentEmp agentEmp);


    /**
     * 修改代理商员工基本信息
     * @param agentEmp
     * @return
     */
    AgentEmp updateAgentEmp(AgentEmp agentEmp);

//    /**
//     * 根据id删除代理商员工
//     * @param id
//     * @return
//     */
//    void deleteAgentEmpById(Long id);

    /**
     * 在UserService调用。代理商使用。删除员工
     * @param id
     * @return
     */
    Msg deleteAgentEmp(Long id);

    /**
     * 根据id查找代理商员工
     * @param id
     * @return
     */
    AgentEmp findAgentEmpById(Long id);

    /**
     * 查找指定代理商（agentId）旗下的代理商员工
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @param sortKey
     * @param order
     * @return
     */
    Page<AgentEmp> findAgEmpByAgentId(Long agentId, int pageNum, int pageSize, String sortKey, String order,String empName);

    /**
     * 根据userId查找代理商员工
     * @param userId
     * @return
     */
    AgentEmp findAgEmpByUserId(Long userId);

    /**
     * 查找当前代理商下员工的设备销售总榜前十
     * @param curUser
     * @return
     */
    Map<String,Long> deviceSalesTopTenOfAgEmp(UserVO curUser);


}
