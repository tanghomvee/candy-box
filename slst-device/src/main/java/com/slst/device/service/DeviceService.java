package com.slst.device.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.device.dao.model.Device;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface DeviceService extends BaseService<Device, Long> {

    /**
     * find by StoreIdAndMac
     *
     * @param storeId
     * @param mac
     * @return
     */
    Device findByStoreIdAndMac(Long storeId, String mac);

    Device findByMac(String mac);

    /**
     * 根据ID查找设备
     * @param id
     * @return
     */
    Device findById(Long id);

    /**
     * 保存设备信息
     *
     * @param device
     * @return
     */
    Msg saveDevice(UserVO curUser, Device device);

    /**
     * 根据ID删除设备
     * @param id
     * @return
     */
    Msg deleteById(Long id);


    /**
     * 修改设备信息
     * @param curUser
     * @param device
     * @return
     */
    Device updateDevice(UserVO curUser,Device device);

    /**
     * 修改设备距离
     * @param curUser
     * @param device
     * @return
     */
    Device modifyDistance(UserVO curUser,Device device);

    /**
     * admin使用,修改代理商ID
     *用于代理商设备的分配和回收
     * @param curUser
     * @param agentId 为0L时表示回收代理商设备
     * @param ids
     * @return
     */
    Integer modifyAgentIdByIds(UserVO curUser, Long agentId, String ids);

    /**
     * 修改商家ID.
     * 代理商使用,用于商家设备的回收和分配
     * 代理商员工使用,仅可用于商家设备的分配
     * @param curUser
     * @param venderId 为0L时,表示代理商直接操作,回收商家设备
     * @param ids
     * @return
     */
    Integer modifyVenderIdByIds(UserVO curUser, Long venderId, String ids);

    /**
     * 代理商回收商家设备方法
     * @param curUser
     * @param venderId 为0L时,表示代理商直接操作,回收商家设备
     * @param ids
     * @return
     */
    Integer modifyVenderIdAndStoreIdByIds(UserVO curUser, Long venderId, String ids);

    /**
     * 代理商使用,用于代理商员工的设备的回收和分配
     * @param curUser
     * @param agentEmpId 为0L时,表示回收代理商员工设备
     * @param ids
     * @return
     */
    Integer modifyAgentEmpIdByIds(UserVO curUser, Long agentEmpId, String ids);

    /**
     * 商家使用,修改店铺ID
     * 用于店铺设备的分配和回收
     * @param curUser
     * @param devices
     * @return
     */
    Integer modifyStoreIdByIds(UserVO curUser, List<Device> devices);


    /**
     * 商家使用,修改店铺ID
     * 用于店铺设备的分配和回收
     * @param curUser
     * @param ids
     * @return
     */
    Integer modifyStoreIdByIds(UserVO curUser,Long storeId,Integer state, String ids);


    Integer modifyStoreIdByStoreId(String changer,Long newStoreId,Long storeId,Integer state);


//    /**
//     * admin使用,根据部分设备mac,模糊查找还未分配给代理商的设备
//     * @param macLike
//     * @return
//     */
//    List<Device> findAviDeviceOfAgent(String macLike);


    /**
     * admin使用,查找除指定agentId外,包含macLike字段的设备
     * @param agentId 为0时,表示查找所有已分配给代理商的设备
     * @param macLike
     * @return
     */
    List<Device> findDeviceNotTheAgent(Long agentId, String macLike);

    Page<Device> findAllDevices(String macLike, int pageNum, int pageSize);

    /**
     * admin使用,根据(部分设备mac和)指定代理商ID,查找设备
     * 代理商ID为0时,查找未分配给代理商的设备
     * @param agentId 为0时,查找未分配给代理商的设备
     * @param macLike
     * @return
     */
    Page<Device> findDeviceOfAgent(Long agentId, String macLike, int pageNum, int pageSize);

    /**
     * 代理商使用,根据(部分设备mac和)指定代理商员工ID,查找设备
     * 代理商员工ID为0L时,表示设备未被分配给任何员工
     * @param curUser
     * @param agentEmpId 为0L时,表示设备未被分配给任何员工
     * @param macLike
     * @return
     */
    Page<Device> findDeviceOfAgentEmp(UserVO curUser, Long agentEmpId, String macLike, int pageNum, int pageSize);

    /**
     * 代理商使用/其员工使用,根据(部分设备mac和)指定商家ID,查找设备
     * 商家ID为0L时,表示查找未分配给商家的设备
     * @param curUser
     * @param venderId 为0L时,表示查找未分配给商家的设备
     * @param macLike
     * @return
     */
    Page<Device> findDeviceOfVender(UserVO curUser, Long venderId, String macLike, int pageNum, int pageSize);

//    List<Device> findAviDeviceOfEmp(UserVO curUser, String macLike);

    /**
     * 商家使用,根据(部分设备mac和)指定店铺ID,查找设备
     * 店铺ID为0L时,表示查找未分配给店铺的设备
     * @param curUser
     * @param storeId 为0L时,表示查找未分配给店铺的设备
     * @param macLike
     * @return
     */
    Page<Device> findDeviceOfStore(UserVO curUser, Long storeId, String macLike, int pageNum, int pageSize);



    /**
     * 计算指定代理商员工下绑定的设备数
     * @param agentEmpId
     * @return
     */
    Long countDeviceOfAgentEmp(Long agentEmpId);

    /**
     * 计算指定店铺下绑定的设备数
     * @param storeId
     * @return
     */
    Long countDeviceOfStore(Long storeId);

    /**
     * 计算指定storeId下的设备数
     * @param venderId
     * @return
     */
    Long countByVenderId(Long venderId);



}
