package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.NumberPool;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NumberPoolService extends BaseService<NumberPool, Long> {

    /**
     * 保存中继号码
     *
     * @return
     */
    Msg saveRelayNumber();

    /**
     * 获取admin中继号码列表
     * @param pageNum
     * @param pageSize
     * @param sortKey
     * @param orderKey
     * @return
     */
    Msg pageNumberPool(int pageNum, int pageSize, String sortKey, String orderKey);


    /**
     * 通过venderId获取
     * @param venderId
     * @return
     */
    List<NumberPool> findByVenderId(Long venderId);

    /**
     * 根据代理商ID,代理商员工ID,商家ID,门店ID查询中继号码列表
     * @param agentId
     * @param agentEmpId
     * @param venderId
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<NumberPool> findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(Long agentId, Long agentEmpId, Long venderId, Long storeId,int pageNum,int pageSize);


    /**
     * admin还未分配中继号码
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<NumberPool> findNotUsedFromAdmin(int pageNum,int pageSize);


    /**
     * 代理商还未分配给员工或商家的中继号码
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<NumberPool> findNotUsedFromAgentToEmpOrVender(Long agentId,int pageNum,int pageSize);


    /**
     * 员工还未分配给商家的中继号码
     * @param agentId
     * @param agentEmpId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<NumberPool> findNotUsedFromAgentEmpToVender(Long agentId,Long agentEmpId,int pageNum,int pageSize);

//    /**
//     * 商家还未分配给门店的中继号码
//     * @return  TODO 给商家分配中继号码暂未开放
//     */
//    Page<NumberPool> findNotUsedFromVenderToStore();


    /**
     * 用于修改中继号码的代理商ID
     * @param changer
     * @param agentId
     * @param ids
     * @return
     */
    Msg modifyAgentId(String changer,Long agentId, List<Long> ids);



    /**
     * 用于修改中继号码的代理商员工ID
     * @param changer
     * @param agentEmpId
     * @param ids
     * @return
     */
    Msg modifyAgentEmpId(String changer,Long agentEmpId, List<Long> ids);

    /**
     * 用于修改中继号码的商家ID
     * @param changer
     * @param venderId
     * @param ids
     * @return
     */
    Msg modifyVenderId(String changer,Long venderId, List<Long> ids);

    /**
     * 商家购买中继号码
     * @param curUser
     * @param ids
     * @return
     */
    Msg buyRelayCard(UserVO curUser,List<Long> ids);


    Long countByAreaCode(String areaCode);

}
