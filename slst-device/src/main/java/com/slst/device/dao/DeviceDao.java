package com.slst.device.dao;

import com.slst.device.dao.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface DeviceDao extends JpaRepository<Device, Long> {
    /**
     * find By StoreIdAndMacAndYn
     *
     * @param storeId
     * @param mac
     * @param yn
     * @return
     */
    Device findByStoreIdAndMacAndYn(Long storeId, String mac, Integer yn);


    /**
     * 查找指定mac的设备信息
     *
     * @param mac
     * @return 该mac对应的设备信息
     */
    Device findByMac(String mac);

    /**
     * 根据设备ID修改代理商ID
     *
     * @param agentId
     * @param id
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.agentId=:agentId,d.changer=:userName,d.changeTime=:curDate where d.id=:id")
    Integer modifyAgentIdById(@Param("agentId") Long agentId, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据多个设备id,批量修改代理商ID
     *
     * @param agentId
     * @param idsList
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.agentId=:agentId,d.changer=:userName,d.changeTime=:curDate where d.id in :idsList")
    Integer modifyAgentIdByIds(@Param("agentId") Long agentId, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据设备id,修改一条代理商员工ID
     *
     * @param agentEmpId
     * @param id
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.agentEmpId=:agentEmpId,d.changer=:userName,d.changeTime=:curDate where d.id=:id")
    Integer modifyAgentEmpIdById(@Param("agentEmpId") Long agentEmpId, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据多个设备id,批量修改代理商员工ID
     *
     * @param agentEmpId
     * @param idsList
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.agentEmpId=:agentEmpId,d.changer=:userName,d.changeTime=:curDate where d.id in :idsList")
    Integer modifyAgentEmpIdByIds(@Param("agentEmpId") Long agentEmpId, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据设备id,修改一条商家ID
     *
     * @param venderId
     * @param id
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.venderId=:venderId,d.changer=:userName,d.changeTime=:curDate where d.id=:id")
    Integer modifyVenderIdById(@Param("venderId") Long venderId, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据多个设备ID,批量修改商家ID
     *
     * @param venderId
     * @param idsList
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.venderId=:venderId,d.changer=:userName,d.changeTime=:curDate where d.id in :idsList")
    Integer modifyVenderIdByIds(@Param("venderId") Long venderId, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);

    @Modifying
    @Query("update Device d set d.venderId=:venderId,d.storeId=:storeId,d.state=:state,d.distance=:distance,d.changer=:userName,d.changeTime=:curDate where d.id =:id")
    Integer modifyVenderIdAndStoreById(@Param("venderId") Long venderId, @Param("storeId") Long storeId, @Param("state") Integer state, @Param("distance") Integer distance, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    @Modifying
    @Query("update Device d set d.venderId=:venderId,d.storeId=:storeId,d.state=:state,d.distance=:distance,d.changer=:userName,d.changeTime=:curDate where d.id in :idsList")
    Integer modifyVenderIdAndStoreByIds(@Param("venderId") Long venderId, @Param("storeId") Long storeId, @Param("state") Integer state, @Param("distance") Integer distance, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据设备id,修改一条店铺ID
     *
     * @param storeId
     * @param id
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.storeId=:storeId,d.state=:state,d.distance=:distance,d.changer=:userName,d.changeTime=:curDate where d.id=:id")
    Integer modifyStoreIdById(@Param("storeId") Long storeId, @Param("state") Integer state, @Param("distance") Integer distance, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据多个设备ID,批量修改店面ID
     *
     * @param storeId
     * @param idsList
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.storeId=:storeId,d.state=:state,d.distance=:distance,d.changer=:userName,d.changeTime=:curDate where d.id in :idsList")
    Integer modifyStoreIdByIds(@Param("storeId") Long storeId, @Param("state") Integer state, @Param("distance") Integer distance, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 根据设备id,修改一条店铺ID
     *
     * @param storeId
     * @param id
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.storeId=:storeId,d.state=:state,d.changer=:userName,d.changeTime=:curDate where d.id=:id")
    Integer modifyStoreIdById(@Param("storeId") Long storeId, @Param("state") Integer state, @Param("id") Long id, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * 根据多个设备ID,批量修改店面ID
     *
     * @param storeId
     * @param idsList
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.storeId=:storeId,d.state=:state,d.changer=:userName,d.changeTime=:curDate where d.id in :idsList")
    Integer modifyStoreIdByIds(@Param("storeId") Long storeId, @Param("state") Integer state, @Param("idsList") List<Long> idsList, @Param("userName") String userName, @Param("curDate") Date curDate);


    /**
     * 根据StoreId,修改StoreID,用于商家将门店设备全部回收
     * @param newStoreId
     * @param state
     * @param storeId
     * @param userName
     * @param curDate
     * @return
     */
    @Modifying
    @Query("update Device d set d.storeId=:newStoreId,d.state=:state,d.changer=:userName,d.changeTime=:curDate where d.storeId=:storeId")
    Integer modifyStoreIdByNewStoreId(@Param("newStoreId") Long newStoreId, @Param("state") Integer state, @Param("storeId") Long storeId, @Param("userName") String userName, @Param("curDate") Date curDate);

    /**
     * admin使用,查询 排除指定代理商ID外 的设备,此方法是查找(agentId Not Xxx)的数据
     *
     * @param agentId 为0时,表示查找全部已经分配给代理商的设备
     * @return
     */
    List<Device> findByAgentIdNot(Long agentId);

    /**
     * admin使用,查询 排除指定代理商ID外,mac包含macLike字段的设备
     *
     * @param agentId 为0时,表示查找全部已经分配给代理商的设备
     * @param macLike
     * @return
     */
    List<Device> findByAgentIdNotAndMacContaining(Long agentId, String macLike);

    Page<Device> findByMacContaining(String macLike, Pageable pageable);

    /**
     * admin使用,根据agentId代理商号查找设备
     *
     * @param agentId 为0时即为查找未分配给代理商的设备
     * @return
     */
    Page<Device> findByAgentIdIs(Long agentId, Pageable pageable);

    /**
     * admin使用.根据指定代理商ID,查询mac包含macLike字段的设备
     *
     * @param agentId 为0时即为查找未分配给代理商的设备
     * @param macLike
     * @return
     */
    Page<Device> findByAgentIdAndMacContaining(Long agentId, String macLike, Pageable pageable);


    /**
     * admin使用,根据商家ID查找设备
     *
     * @param venderId 为0时即为查找未分配给代理商的设备
     * @return
     */
    Page<Device> findByVenderIdIs(Long venderId, Pageable pageable);

    /**
     * admin使用.根据指定代理商ID,查询mac包含macLike字段的设备
     *
     * @param venderId 为0时即为查找未分配给代理商的设备
     * @param macLike
     * @return
     */
    Page<Device> findByVenderIdAndMacContaining(Long venderId, String macLike, Pageable pageable);

    /**
     * 代理商使用,根据指定商家ID查找设备
     *
     * @param agentId
     * @param venderId 为0时即为查找未分配给商家的设备
     * @return
     */
    Page<Device> findByAgentIdAndVenderId(Long agentId, Long venderId, Pageable pageable);

    /**
     * 代理商使用.根据指定商家ID,查询mac包含macLike字段的设备
     *
     * @param agentId
     * @param venderId 为0时即为查找未分配给商家的设备
     * @param macLike
     * @return
     */
    Page<Device> findByAgentIdAndVenderIdAndMacContaining(Long agentId, Long venderId, String macLike, Pageable pageable);

    /**
     * 代理商使用,根据指定的代理商员工ID,查找其名下的设备
     *
     * @param agentId
     * @param agentEmpId 为0时即为查找未分配给员工的设备
     * @return
     */
    Page<Device> findByAgentIdAndAgentEmpId(Long agentId, Long agentEmpId, Pageable pageable);

    /**
     * 代理商使用.根据指定代理商员工ID,查询其名下mac包含macLike字段的设备
     *
     * @param agentId
     * @param agentEmpId 为0时即为查找未分配给员工的设备
     * @param macLike
     * @return
     */
    Page<Device> findByAgentIdAndAgentEmpIdAndMacContaining(Long agentId, Long agentEmpId, String macLike, Pageable pageable);

    /**
     * 代理商员工使用,根据VenderId商家号查找设备
     *
     * @param agentEmpId
     * @param venderId   为0时即为查找未分配给商家的设备
     * @return
     */
    Page<Device> findByAgentEmpIdAndVenderId(Long agentEmpId, Long venderId, Pageable pageable);

    /**
     * 代理商员工使用.根据指定商家ID,查询mac包含macLike字段的设备
     *
     * @param agentEmpId
     * @param venderId   为0时即为查找未分配给商家的设备
     * @param macLike
     * @return
     */
    Page<Device> findByAgentEmpIdAndVenderIdAndMacContaining(Long agentEmpId, Long venderId, String macLike, Pageable pageable);

    /**
     * 商家使用,根据storeId门店ID查找设备
     *
     * @param venderId
     * @param storeId  为0时即为查找未分配给店铺的设备
     * @return
     */
    Page<Device> findByVenderIdAndStoreId(Long venderId, Long storeId, Pageable pageable);

    /**
     * 商家使用,根据storeId门店ID查找设备
     *
     * @param storeId 为0时即为查找未分配给店铺的设备
     * @return
     */
    Page<Device> findByStoreId(Long storeId, Pageable pageable);



    /**
     * 商家使用.根据指定门店ID,查询mac包含macLike字段的设备
     *
     * @param venderId
     * @param storeId  为0时即为查找未分配给店铺的设备
     * @param macLike
     * @return
     */
    Page<Device> findByVenderIdAndStoreIdAndMacContaining(Long venderId, Long storeId, String macLike, Pageable pageable);

    /**
     * 商家使用.根据指定门店ID,查询mac包含macLike字段的设备
     *
     * @param storeId 为0时即为查找未分配给店铺的设备
     * @param macLike
     * @return
     */
    Page<Device> findByStoreIdAndMacContaining(Long storeId, String macLike, Pageable pageable);

    /**
     * 计算指定agentEmpId下的设备数
     *
     * @param agentEmpId
     * @return
     */
    Long countByAgentEmpId(Long agentEmpId);

    /**
     * 计算指定storeId下的设备数
     *
     * @param storeId
     * @return
     */
    Long countByStoreId(Long storeId);

    /**
     * 计算指定storeId下的设备数
     *
     * @param venderId
     * @return
     */
    Long countByVenderId(Long venderId);


}
