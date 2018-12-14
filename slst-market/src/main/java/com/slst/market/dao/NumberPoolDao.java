package com.slst.market.dao;

import com.slst.market.dao.model.NumberPool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface NumberPoolDao extends JpaRepository<NumberPool, Long>, NumberPoolExtDao {


    /**
     * 保存中继号码到号码池
     *
     * @param numberPool
     * @return
     */
    NumberPool save(NumberPool numberPool);


    /**
     * 根据声牙提供中继号码ID查询NumberPool
     *
     * @param plId
     * @return
     */
    NumberPool findByPlId(String plId);

    /**
     * 通过venderId获取
     * @param venderId
     * @return
     */
    List<NumberPool> findByVenderId(Long venderId);

    Long countByAreaCode(String areaCode);

    /**
     * 获取admin还没分配的设备
     * @param agentId
     * @param agentEmpId
     * @param venderId
     * @param storeId
     * @param pageable
     * @return
     */
    Page<NumberPool> findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(Long agentId, Long agentEmpId, Long venderId, Long storeId, Pageable
            pageable);


    @Modifying
    @Query("update NumberPool n set n.agentId=:agentId,n.changer=:userName,n.changeTime=:curDate where n.id in :idsList")
    Integer modifyAgentId(@Param("agentId") Long agentId, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);

    @Modifying
    @Query("update NumberPool n set n.agentEmpId=:agentEmpId,n.changer=:userName,n.changeTime=:curDate where n.id in :idsList")
    Integer modifyAgentEmpId(@Param("agentEmpId") Long agentId, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);


    @Modifying
    @Query("update NumberPool n set n.venderId=:venderId,n.changer=:userName,n.changeTime=:curDate where n.id in :idsList")
    Integer modifyVenderId(@Param("venderId") Long agentId, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);




}
