package com.slst.agent.service.impl;

import com.slst.agent.dao.AgentDao;
import com.slst.agent.dao.model.Agent;
import com.slst.agent.service.AgentService;
import com.slst.common.enums.AgentLevelEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:15
 */
@Service("agentService")
public class AgentServiceImpl extends BaseServiceImpl<Agent, Long> implements AgentService {
    @Resource
    private AgentDao agentDao;

    @Override
    public Agent createAgent(Agent agent) {

        Agent retAgent=null;

        Integer agentLevel=agent.getLevel();

        if(AgentLevelEnum.TOP_LEVEL.getVal().equals(agentLevel)){
            retAgent=topAgentCreate(agent);
        }else{
            retAgent=otherAgentCreate(agent);
        }

        return retAgent;
    }

    @Override
    public Agent updateAgent(Agent agent) {
        return agentDao.saveAndFlush(agent);
    }

    @Transactional
    @Override
    public Integer modifyCompanyName(UserVO curUser,String newName, Long agentId) {

        return agentDao.modifyCompanyName(newName,agentId,curUser.getUserName(),new Date());
    }

    /**
     * 删除指定代理商
     * @param id
     */
    public void deleteAgentById(Long id){
        agentDao.deleteById(id);
    }

    @Override
    public Agent findAgentById(Long id) {
        return agentDao.findById(id).get();
    }



    /**
     * 顶级代理商创建
     * @param agent
     * @return
     */
    private Agent topAgentCreate(Agent agent){

        Agent retAgent=null;
        try {
            retAgent = agentDao.save(agent);
        }catch (Exception e){
            e.printStackTrace();
        }

        return retAgent;
    }

    /**
     * 其他等级代理商创建
     * @param agent
     * @return
     */
    private Agent otherAgentCreate(Agent agent){
        return null;
    }

    /**
     * admin使用。根据代理商level（等级）查找代理商信息，并排序分页
     * @param agentLevel
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<Agent> findAgentByLevelForAdmin(Integer agentLevel,int pageNum,int pageSize,String sortKey,String order,String companyName){

        if (StringUtils.isNullOrEmpty(sortKey)){
            sortKey="createTime";
        }

        if (StringUtils.isNullOrEmpty(order)){
            order="desc";
        }
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,sortKey,order);

        if (StringUtils.isNullOrEmpty(companyName)){
            return agentDao.findByLevel(agentLevel,pageable);
        }else{
            return agentDao.findByLevelAndCompanyNameContaining(agentLevel,companyName,pageable);
        }

    }

    /**
     * admin和代理商使用。查找所属此代理商(agentId)的下一级代理商
     * @param userVO
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<Agent> findAgentByParentId(UserVO userVO,int pageNum,int pageSize,String sortKey,String order){
        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,sortKey,order);

        Long agentId = userVO.getAgentId(); //操作用户的agentId，就是要查询的代理商的parentId
        return agentDao.findByParentId(agentId,pageable);
    }

    @Override
    public Agent findByUserId(Long userId) {
        return agentDao.findByUserId(userId);
    }

}
