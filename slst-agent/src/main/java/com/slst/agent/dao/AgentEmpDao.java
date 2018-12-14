package com.slst.agent.dao;

import com.slst.agent.dao.model.AgentEmp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentEmpDao extends JpaRepository<AgentEmp, Long>, AgentEmpDaoExt {

    /**
     * 保存代理商员工信息
     *
     * @param agentEmp
     * @return
     */
    AgentEmp save(AgentEmp agentEmp);

    /**
     * 更新代理商员工信息
     *
     * @param agentEmp
     * @return
     */
    AgentEmp saveAndFlush(AgentEmp agentEmp);

    /**
     * 根据ID删除理商员工信息
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据agentId查找代理商员工
     *
     * @param agentId
     * @param pageable
     * @return
     */
    Page<AgentEmp> findByAgentId(Long agentId, Pageable pageable);

    /**
     * 根据agentId查找代理商员工
     *
     * @param agentId
     * @param pageable
     * @return
     */
    Page<AgentEmp> findByAgentIdAndEmpNameContaining(Long agentId, String empName, Pageable pageable);

    /**
     * 根据userId查找代理商员工
     *
     * @param userId
     * @return
     */
    AgentEmp findByUserId(Long userId);

    /**
     * 根据AgentEmpId，查看销售前十销售设备数量
     *
     * @param agentId
     * @return Object[]：0：agentEmpId；1，设备数量
     */
    @Query(value = "SELECT d.agentEmpId,COUNT(d.id) FROM t_device d WHERE d.agentId =:agentId AND d.agentEmpId > 0 GROUP BY d.agentEmpId ORDER BY COUNT(d.id) DESC LIMIT 0,10",
            nativeQuery = true)
    List<Object[]> findSalesTopTenOfEmpByAgentId(@Param("agentId") Long agentId);

}
