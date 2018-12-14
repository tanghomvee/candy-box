package com.slst.agent.service;

import com.slst.agent.dao.model.Agent;
import com.slst.common.service.BaseService;
import com.slst.common.web.vo.UserVO;
import org.springframework.data.domain.Page;

public interface AgentService extends BaseService<Agent, Long> {


    /**
     * 代理商注册
     * @param agent
     * @return
     */
    Agent createAgent(Agent agent);

    /**
     * 修改代理商基本信息
     * @param agent
     * @return
     */
    Agent updateAgent(Agent agent);

    /**
     * 修改公司名称
     * @param curUser
     * @param newName
     * @param agentId
     * @return
     */
    Integer modifyCompanyName(UserVO curUser,String newName,Long agentId);
    /**
     * 删除指定代理商
     * @param id
     */
    void deleteAgentById(Long id);

    /**
     * 根据ID获取代理商信息
     * @param id
     * @return
     */
    Agent findAgentById(Long id);

    /**
     * admin使用。根据代理商level（等级）查找代理商信息，并排序分页
     * @param agentLevel
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<Agent> findAgentByLevelForAdmin(Integer agentLevel, int pageNum, int pageSize, String sortKey, String order,String companyName);

    /**
     * admin和代理商使用。。查找所属此代理商(agentId)的下一级代理商
     * @param userVO
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<Agent> findAgentByParentId(UserVO userVO, int pageNum, int pageSize, String sortKey, String order);

    Agent findByUserId(Long userId);

}
