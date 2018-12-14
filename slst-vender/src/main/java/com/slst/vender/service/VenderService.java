package com.slst.vender.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.UserVO;
import com.slst.vender.dao.model.Vender;
import org.springframework.data.domain.Page;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface VenderService extends BaseService <Vender, Long>{

    /**
     * 商家注册
     * @param vender
     * @return
     */
    Vender createVender(Vender vender);

    /**
     * 修改商家基本信息
     * @param vender
     * @return
     */
    Vender updateVender(Vender vender);

    /**
     * 根据id删除商家数据
     * @param id
     */
    void deleteVenderById(Long id);

    /**
     * 根据id查找商家信息
     * @param id
     * @return
     */
    Vender findVenderById(Long id);

    /**
     * 根据userId查找商家
     * @param userId
     * @return
     */
    Vender findVenderByUserId(Long userId);

    /**
     * 查找此代理商(agentId)的旗下的商家
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<Vender> findVenderByAgentId(Long agentId,String companyName, int pageNum, int pageSize, String sortKey, String order);

    /**
     * 查找指定代理商员工（agentEmpId）旗下的商家
     * @param agentEmpId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<Vender> findVenderByAgentEmpId(Long agentEmpId,String companyName, int pageNum, int pageSize, String sortKey, String order);

    /**
     * 查找由此代理商(agentId)直接分配（agentEmpId == 0）的商家
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @param sortKey
     * @param order
     * @return
     */
    Page<Vender> findDirectVenderByAgentId(Long agentId, int pageNum, int pageSize, String sortKey, String order);

    /**
     * 计算指定代理商员工绑定的商家数
     * @param agentEmpId
     * @return
     */
    Long countVenderOfAgentEmp(Long agentEmpId);

    Integer modifyCompanyName(UserVO curUser,String newName, Long venderId);
}
