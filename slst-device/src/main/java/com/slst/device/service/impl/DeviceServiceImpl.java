package com.slst.device.service.impl;

import com.slst.common.enums.DeviceStateEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.device.dao.DeviceDao;
import com.slst.device.dao.model.Device;
import com.slst.device.service.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
@Service("deviceService")
public class DeviceServiceImpl extends BaseServiceImpl<Device, Long> implements DeviceService {

    @Resource
    private DeviceDao deviceDao;



    @Override
    public Device findByStoreIdAndMac(Long storeId, String mac) {
        return deviceDao.findByStoreIdAndMacAndYn(storeId, mac, YNEnum.YES.getVal());
    }

    @Override
    public Device findByMac(String mac) {
        return deviceDao.findByMac(mac);
    }

    @Override
    public Device findById(Long id) {
        return deviceDao.findById(id).get();
    }

    /**
     * 保存设备信息
     *
     * @param device
     * @return
     */
    @Override
    public Msg saveDevice(UserVO curUser, Device device) {

        String macNo = device.getMac();
        Device theDevice = deviceDao.findByMac(macNo);
        if (null != theDevice) {
            return Msg.error("已经存在设备");
        }

        Date curDate = new Date();//当前时间
        Integer yes = YNEnum.YES.getVal();
        String creator = curUser.getUserName();

        device.setAgentId(0L);
        device.setAgentEmpId(0L);
        device.setVenderId(0L);
        device.setStoreId(0L);
        device.setDistance(0);
        device.setState(DeviceStateEnum.UN_ACTIVE.getVal());
        device.setYn(yes);
        device.setCreator(creator);
        device.setCreateTime(curDate);
        Device rtnDevice = deviceDao.save(device);
        if(null!= rtnDevice){
            return Msg.success("添加成功");
        }

        return Msg.error("添加失败");
    }

    @Override
    public Msg deleteById(Long id) {

        Device device=deviceDao.findById(id).get();

        Integer state=null;

        try{
            state=device.getState();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("可能未找到设备,接收deviceId={},msg={}",id,e);
            return Msg.error("未找到指定设备");
        }

        if (DeviceStateEnum.RUNNING.getVal().equals(state) || DeviceStateEnum.OFFLINE.getVal().equals(state)){
            return Msg.error("删除失败,设备正在被使用");
        }

        try{
            deviceDao.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("删除设备异常,msg={}",e);
            return Msg.error("系统开了个小差");
        }



        return Msg.success("删除成功");
    }

    @Override
    public Device updateDevice(UserVO curUser, Device device) {
        Device standBy=deviceDao.findById(device.getId()).get();

        standBy.setMac(device.getMac());
        standBy.setAVal(device.getAVal());
        standBy.setNVal(device.getNVal());
        standBy.setChanger(curUser.getUserName());
        standBy.setChangeTime(new Date());
        Device rtnDev= deviceDao.saveAndFlush(standBy);
        return rtnDev;
    }

    @Override
    public Device modifyDistance(UserVO curUser, Device device) {
        Device standBy=deviceDao.findById(device.getId()).get();

        standBy.setDistance(device.getDistance());
        standBy.setChanger(curUser.getUserName());
        standBy.setChangeTime(new Date());
        Device rtnDev= deviceDao.saveAndFlush(standBy);
        return rtnDev;
    }

    /**
     * admin使用,修改代理商ID
     *用于代理商设备的分配和回收
     * @param curUser
     * @param agentId 为0L时表示回收代理商设备
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public Integer modifyAgentIdByIds(UserVO curUser, Long agentId, String ids) {
        Date curDate = new Date();
        String userName = curUser.getUserName();
        if (ids.contains(",")) {
            String[] idsArr = ids.split(",");
            List<Long> idsList = new ArrayList<>();
            for (int i = 0; i < idsArr.length; i++) {
                Long s = Long.parseLong(idsArr[i]);
                idsList.add(s);
            }
            return deviceDao.modifyAgentIdByIds(agentId, idsList, userName, curDate);

        } else {
            if (!StringUtils.isNullOrEmpty(ids)) {
                return deviceDao.modifyAgentIdById(agentId, Long.parseLong(ids), userName, curDate);
            }
        }

        return 0;
    }

    /**
     * 代理商使用,用于代理商员工的设备的回收和分配
     * @param curUser
     * @param agentEmpId 为0L时,表示回收代理商员工设备
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public Integer modifyAgentEmpIdByIds(UserVO curUser, Long agentEmpId, String ids){
        Date curDate = new Date();
        String userName = curUser.getUserName();
        if(ids.contains(",")){
            String[] idsArr = ids.split(",");
            List<Long> idsList = new ArrayList<>();
            for (int i = 0; i < idsArr.length; i++) {
                Long s = Long.parseLong(idsArr[i]);
                idsList.add(s);
            }
            return deviceDao.modifyAgentEmpIdByIds(agentEmpId,idsList,userName,curDate);
        }else{
            if(!StringUtils.isNullOrEmpty(ids)){
                return deviceDao.modifyAgentEmpIdById(agentEmpId,Long.parseLong(ids),userName,curDate);
            }
        }

        return 0;

    }

    /**
     * 修改商家ID.
     * 代理商使用,用于商家设备的回收和分配
     * 代理商员工使用,仅可用于商家设备的分配
     * @param curUser
     * @param venderId 为0L时,表示代理商直接操作,回收设备商家设备
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public Integer modifyVenderIdByIds(UserVO curUser, Long venderId, String ids) {
        Date curDate = new Date();
        String userName = curUser.getUserName();

        if (ids.contains(",")) {
            String[] idsArr = ids.split(",");
            List<Long> idsList = new ArrayList<>();
            for (int i = 0; i < idsArr.length; i++) {
                Long s = Long.parseLong(idsArr[i]);
                idsList.add(s);
            }
            return deviceDao.modifyVenderIdByIds(venderId,idsList,userName,curDate);

        } else {
            if (!StringUtils.isNullOrEmpty(ids)) {
                return deviceDao.modifyVenderIdById(venderId,Long.parseLong(ids),userName,curDate);
            }
        }

        return 0;

    }

    @Transactional
    @Override
    public Integer modifyVenderIdAndStoreIdByIds(UserVO curUser, Long venderId, String ids) {
        Date curDate = new Date();
        String userName = curUser.getUserName();

        Integer state=DeviceStateEnum.UN_ACTIVE.getVal();

        if (ids.contains(",")) {
            String[] idsArr = ids.split(",");
            List<Long> idsList = new ArrayList<>();
            for (int i = 0; i < idsArr.length; i++) {
                Long s = Long.parseLong(idsArr[i]);
                idsList.add(s);
            }
            return deviceDao.modifyVenderIdAndStoreByIds(venderId,0L,state,0,idsList,userName,curDate);

        } else {
            if (!StringUtils.isNullOrEmpty(ids)) {
                return deviceDao.modifyVenderIdAndStoreById(venderId,0L,state,0,Long.parseLong(ids),userName,curDate);
            }
        }

        return 0;
    }

    /**
     * 商家使用,修改店铺ID
     * 用于店铺设备的分配和回收
     * @param curUser
     * @param devices 需要修改的设备集合
     * @return
     */
    @Transactional
    @Override
    public Integer modifyStoreIdByIds(UserVO curUser,  List<Device> devices) {
        Date curDate = new Date();
        String userName = curUser.getUserName();

        Integer state=DeviceStateEnum.RUNNING.getVal();

        Integer count=0;
        for (Device device : devices) {
            Device dataDev=findById(device.getId());//获取数据库里面的设备信息
            Integer result=0;

            if (0==dataDev.getDistance()){
                result= deviceDao.modifyStoreIdById(device.getStoreId(),state,device.getDistance(),device.getId(),userName,curDate);
            }else{
                result=deviceDao.modifyStoreIdById(device.getStoreId(),state,device.getId(),userName,curDate);
            }

            if (result>0){
               count++;
            }
        }

        return count;

//        if (storeId>0)
//            state=1;
//
//        if (ids.contains(",")) {
//            String[] idsArr = ids.split(",");
//            List<Long> idsList = new ArrayList<>();
//            for (int i = 0; i < idsArr.length; i++) {
//                Long s = Long.parseLong(idsArr[i]);
//                idsList.add(s);
//            }
//            return deviceDao.modifyStoreIdByIds(storeId,state,idsList,userName,curDate);
//
//        } else {
//            if (!StringUtils.isNullOrEmpty(ids)) {
//                return deviceDao.modifyStoreIdById(storeId,state,Long.parseLong(ids),userName,curDate);
//            }
//        }

    }

    @Transactional
    @Override
    public Integer modifyStoreIdByIds(UserVO curUser,Long storeId,Integer state, String ids) {

        Date curDate = new Date();
        String userName = curUser.getUserName();

        if (ids.contains(",")) {
            String[] idsArr = ids.split(",");
            List<Long> idsList = new ArrayList<>();
            for (int i = 0; i < idsArr.length; i++) {
                Long s = Long.parseLong(idsArr[i]);
                idsList.add(s);
            }
            return deviceDao.modifyStoreIdByIds(storeId,state,idsList,userName,curDate);

        } else {
            if (!StringUtils.isNullOrEmpty(ids)) {
                return deviceDao.modifyStoreIdById(storeId,state,Long.parseLong(ids),userName,curDate);
            }
        }

        return 0;
    }

    @Transactional
    @Override
    public Integer modifyStoreIdByStoreId(String changer, Long newStoreId, Long storeId, Integer state) {
        return deviceDao.modifyStoreIdByNewStoreId(newStoreId,state,storeId,changer,new Date());
    }


//    /**
//     * admin使用,查找全部未分配给代理商的设备
//     * 或者根据部分设备mac,模糊查找还未分配给代理商的设备
//     *
//     * @param macLike
//     * @return
//     */
//    @Override
//    public List<Device> findAviDeviceOfAgent(String macLike) {
//
//        if (StringUtils.isNullOrEmpty(macLike)) {
//            return deviceDao.findByAgentIdIs(0L);
//        }
//        return deviceDao.findByAgentIdAndMacContaining(0L, macLike);
//
//    }


    /**
     * admin使用,查找除指定agentId外,包含macLike字段的设备,主要用于查找所有已分配给代理商的设备
     * @param agentId 为0时,表示查找所有已分配给代理商的设备
     * @param macLike
     * @return
     */
    @Override
    public List<Device> findDeviceNotTheAgent(Long agentId, String macLike){

        if (StringUtils.isNullOrEmpty(macLike)) {
            return deviceDao.findByAgentIdNot(agentId);
        }
        return deviceDao.findByAgentIdNotAndMacContaining(agentId,macLike);
    }

    @Override
    public Page<Device> findAllDevices(String macLike, int pageNum, int pageSize) {
        Pageable pageable=PageRequest.of(pageNum,pageSize);

        if (StringUtils.isNullOrEmpty(macLike)) {
            return deviceDao.findAll(pageable);
        }
        return deviceDao.findByMacContaining(macLike,pageable);

    }

    /**
     * admin使用,查找全部指定代理商ID的设备
     * 或者根据部分设备mac,查找指定代理商ID的设备
     * @param agentId 为0L时,查找未分配给代理商的设备
     * @param macLike
     * @return
     */
    @Override
    public Page<Device> findDeviceOfAgent(Long agentId,String macLike,int pageNum,int pageSize) {

        Pageable pageable=PageRequest.of(pageNum,pageSize);

        if (StringUtils.isNullOrEmpty(macLike)) {
            return deviceDao.findByAgentIdIs(agentId,pageable);
        }
        return deviceDao.findByAgentIdAndMacContaining(agentId, macLike,pageable);

    }

    /**
     * 代理商使用,根据部分设备mac和指定代理商员工ID,查找设备
     * 代理商员工ID为0L时,表示查找未被分配给任何员工的设备
     * @param curUser
     * @param agentEmpId 为0L时,表示查找未被分配给任何员工的设备
     * @param macLike
     * @return
     */
    @Override
    public Page<Device> findDeviceOfAgentEmp(UserVO curUser, Long agentEmpId,String macLike,int pageNum,int pageSize){
        Pageable pageable=PageRequest.of(pageNum,pageSize);
        Long agId = curUser.getAgentId();
        if (StringUtils.isNullOrEmpty(macLike)) {
            return deviceDao.findByAgentIdAndAgentEmpId(agId,agentEmpId,pageable);
        }
        return deviceDao.findByAgentIdAndAgentEmpIdAndMacContaining(agId,agentEmpId,macLike,pageable);

    }

    /**
     * 代理商/其员工使用,查找指定商家ID的设备
     * 或者,查找指定商家ID的设备和mac中包含macLike字段的设备
     * @param curUser
     * @param venderId 为0L时,表示查找未分配给商家的设备
     * @param macLike
     * @return
     */
    @Override
    public Page<Device> findDeviceOfVender(UserVO curUser,Long venderId,String macLike,int pageNum,int pageSize){

        Long agId = curUser.getAgentId();
        Long agEmpId = curUser.getAgentEmpId();

        Pageable pageable=PageRequest.of(pageNum,pageSize);


        if (null==agEmpId && null==agId){
            if (StringUtils.isNullOrEmpty(macLike)){
                return deviceDao.findByVenderIdIs(venderId,pageable);
            }else{
                return deviceDao.findByVenderIdAndMacContaining(venderId,macLike,pageable);
            }
        }

        if(null == agEmpId || 0== agEmpId){   //代理商直接账号操作
            if (StringUtils.isNullOrEmpty(macLike)) {
                return deviceDao.findByAgentIdAndVenderId(agId,venderId,pageable);
            }
            return deviceDao.findByAgentIdAndVenderIdAndMacContaining(agId,venderId,macLike,pageable);
        }

        //代理商员工账号操作
        if(StringUtils.isNullOrEmpty(macLike)){
            return deviceDao.findByAgentEmpIdAndVenderId(agEmpId,venderId,pageable);
        }
        return deviceDao.findByAgentEmpIdAndVenderIdAndMacContaining(agEmpId,venderId,macLike,pageable);



    }

    /**
     * 商家使用,根据(部分设备mac和)指定店铺ID,查找设备
     * 店铺ID为0L时,表示查找未分配给店铺的设备
     * @param curUser
     * @param storeId 为0L时,表示查找未分配给店铺的设备
     * @param macLike
     * @return
     */
    @Override
    public Page<Device> findDeviceOfStore(UserVO curUser, Long storeId,String macLike,int pageNum,int pageSize) {
        Long vdId = curUser.getVenderId();

        Pageable pageable=PageRequest.of(pageNum,pageSize);

        if(null== vdId){
            if(StringUtils.isNullOrEmpty(macLike)){
                return deviceDao.findByStoreId(storeId,pageable);
            }
            return deviceDao.findByStoreIdAndMacContaining(storeId,macLike,pageable);
        }

        if(StringUtils.isNullOrEmpty(macLike)){
            return deviceDao.findByVenderIdAndStoreId(vdId,storeId,pageable);
        }
        return deviceDao.findByVenderIdAndStoreIdAndMacContaining(vdId,storeId,macLike,pageable);
    }



    /**
     * 计算指定代理商员工下绑定的设备数
     * @param agentEmpId
     * @return
     */
    @Override
    public Long countDeviceOfAgentEmp(Long agentEmpId){
        return deviceDao.countByAgentEmpId(agentEmpId);
    }

    /**
     * 计算指定店铺下绑定的设备数
     * @param storeId
     * @return
     */
    @Override
    public Long countDeviceOfStore(Long storeId) {

        return deviceDao.countByStoreId(storeId);
    }

    @Override
    public Long countByVenderId(Long venderId) {
        return deviceDao.countByVenderId(venderId);
    }

}


