package com.slst.vender.dao;

import com.slst.vender.dao.model.Vender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface VenderDao extends JpaRepository<Vender, Long>, VenderDaoExt {
    /**
     * 保存商家信息
     * @param vender
     * @return
     */
    Vender save(Vender vender);

    /**
     * 储存并立即更新商家信息
     * @param vender
     * @return
     */
    Vender saveAndFlush(Vender vender);


    /**
     * 根据id删除商家信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 通过userId查找商家
     * @param userId
     * @return
     */
    Vender findByUserId(Long userId);

    /**
     * 通过代理商ID：agentId查找商家
     * @param agentId
     * @return
     */
    Page<Vender> findByAgentId(Long agentId, Pageable pageable);

    Page<Vender> findByAgentIdAndCompanyNameContaining(Long agentId,String companyName, Pageable pageable);

    /**
     * 根据代理商员工ID:agentEmpId查找商家
     * @param agentEmpId
     * @param pageable
     * @return
     */
    Page<Vender> findByAgentEmpId(Long agentEmpId, Pageable pageable);

    Page<Vender> findByAgentEmpIdAndCompanyNameContaining(Long agentId,String companyName, Pageable pageable);

    /**
     * 根据agentId和agentEmpId查找商家（主要用于查询agentEmpId==0的商家）
     * @param agentId
     * @param agentEmpId
     * @param pageable
     * @return
     */
    Page<Vender> findByAgentIdAndAgentEmpId(Long agentId, Long agentEmpId, Pageable pageable);

    /**
     * 计算指定代理商员工（agentEmpId）绑定的的商家数
     * @param agentEmpId
     * @return
     */
    Long countByAgentEmpId(Long agentEmpId);

    @Modifying
    @Query("update Vender v set v.companyName=:companyName,v.changer=:userName,v.changeTime=:curDate where v.id=:id")
    Integer modifyCompanyName(@Param("companyName") String companyName, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

}
