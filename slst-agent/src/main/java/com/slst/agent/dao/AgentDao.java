package com.slst.agent.dao;

import com.slst.agent.dao.model.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface AgentDao extends JpaRepository<Agent, Long> ,AgentDaoExt {

    /**
     * 保存代理商信息
     * @param agent
     * @return
     */
    Agent save(Agent agent);

    /**
     * 修改代理商信息
     * @param agent
     * @return
     */
    Agent saveAndFlush(Agent agent);

    /**
     * 根据ID删除代理商信息
     * @param id
     */
    void deleteById(Long id);


    /**
     * 根据代理商level（等级）查找代理商
     * @param level
     * @return
     */
    Page<Agent> findByLevel(Integer level, Pageable pageable);

    Page<Agent> findByLevelAndCompanyNameContaining(Integer level,String companyName,Pageable pageable);

    /**
     * 根据parentId查找代理商
     * @param parentId
     * @param pageable
     * @return
     */
    Page<Agent> findByParentId(Long parentId, Pageable pageable);

    /**
     * 根据UserID查找代理商
     * @param userId
     * @return
     */
    Agent findByUserId(Long userId);

    @Modifying
    @Query("update Agent a set a.companyName=:companyName,a.changer=:userName,a.changeTime=:curDate where a.id=:id")
    Integer modifyCompanyName(@Param("companyName") String companyName, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

}
