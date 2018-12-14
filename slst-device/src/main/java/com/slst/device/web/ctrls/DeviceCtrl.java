package com.slst.device.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.slst.common.enums.DeviceStateEnum;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.device.dao.model.Device;
import com.slst.device.service.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = "/device")
public class DeviceCtrl extends BaseCtrl {

    @Resource
    private DeviceService deviceService;

    @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg list(String macLike, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = null;
        switch (curUser.getUserType()) {
            case 1://admin
                page = deviceService.findAllDevices(macLike, pageNum, pageSize);
                break;
            case 2://代理商
                page = deviceService.findDeviceOfAgent(curUser.getAgentId(), macLike, pageNum, pageSize);
                break;
            case 21://代理商员工
                page = deviceService.findDeviceOfAgentEmp(curUser, curUser.getAgentEmpId(), macLike, pageNum, pageSize);
                break;
            case 3://商家
                page = deviceService.findDeviceOfVender(curUser, curUser.getVenderId(), macLike, pageNum, pageSize);
                break;
        }

        return Msg.success("查询成功", page);
    }

    /**
     * 代理商设备列表(admin查看)
     *
     * @param agentId
     * @param macLike
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(path = {"/agentdevlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAgentDevList(Long agentId, String macLike, int pageNum, int pageSize, HttpSession session) {

        Page<Device> page = deviceService.findDeviceOfAgent(agentId, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    /**
     * admin未分配的设备列表
     *
     * @param macLike
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(path = {"/adminunusedevlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg adminUnuseDevList(String macLike, int pageNum, int pageSize, HttpSession session) {

        Page<Device> page = deviceService.findDeviceOfAgent(0L, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    /**
     * 为代理商分配设备
     *
     * @param ids
     * @param agentId
     * @param session
     * @return
     */
    @RequestMapping(path = {"/distributeforagent"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg distributeForAgent(String ids, Long agentId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = deviceService.modifyAgentIdByIds(curUser, agentId, ids);
        if (result > 0) {
            return Msg.success("分配成功");
        } else {
            return Msg.error("分配失败");
        }

    }

    /**
     * 代理商员工设备列表
     *
     * @param empId
     * @param macLike
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(path = {"/agtempdevlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getagtEmpDevList(Long empId, String macLike, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = deviceService.findDeviceOfAgentEmp(curUser, empId, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    @RequestMapping(path = {"/agentunusedevlistforemp"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg agentUnuseDevListForEmp(String macLike, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = deviceService.findDeviceOfAgentEmp(curUser, 0L, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    /**
     * 为代理商员工分配设备
     *
     * @param ids
     * @param empId
     * @param session
     * @return
     */
    @RequestMapping(path = {"/distributeforagtemp"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg distributeForAgtEmp(String ids, Long empId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = deviceService.modifyAgentEmpIdByIds(curUser, empId, ids);
        if (result > 0) {
            return Msg.success("分配成功");
        } else {
            return Msg.error("分配失败");
        }

    }


    /**
     * 商家设备列表(代理商或员工查看)
     *
     * @param venderId
     * @param macLike
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(path = {"/venderdevlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getVenderDevList(Long venderId, String macLike, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = deviceService.findDeviceOfVender(curUser, venderId, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }


    /**
     * 员工可和代理商公用此方法(查看代理商或员工手上还未分配出去的设备)
     *
     * @param macLike
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(path = {"/agentunusedevlistforvdr"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg agentUnuseDevListForVdr(String macLike, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = deviceService.findDeviceOfVender(curUser, 0L, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    /**
     * 为商家分配设备(代理商和员工公用此方法)
     *
     * @param ids
     * @param venderId
     * @param session
     * @return
     */
    @RequestMapping(path = {"/distributeforvender"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg distributeForVender(String ids, Long venderId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = deviceService.modifyVenderIdByIds(curUser, venderId, ids);
        if (result > 0) {
            return Msg.success("分配成功");
        } else {
            return Msg.error("分配失败");
        }

    }

    @RequestMapping(path = {"/storedevlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getStoreDevList(Long storeId, String macLike, int pageNum, int pageSize, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = deviceService.findDeviceOfStore(curUser, storeId, macLike, pageNum, pageSize);
        if (null != page) {
            return Msg.success("查询成功", page);
        }

        return Msg.error("查询失败");
    }


    @RequestMapping(path = {"/venderunusedevlist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg venderUnuseDevList(String macLike, int pageNum, int pageSize) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Page<Device> page = deviceService.findDeviceOfStore(curUser, 0L, macLike, pageNum, pageSize);
        return Msg.success("查询成功", page);
    }

    @RequestMapping(path = {"/distributeforstore"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg distributeForStore(String jsonDevices) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (jsonDevices.contains("请填写") || jsonDevices.contains("未填写")) {
            return Msg.error("请设置采集半径");
        }

        List<Device> devices = JSONObject.parseArray(jsonDevices, Device.class);

        Integer result = deviceService.modifyStoreIdByIds(curUser, devices);

        if (result > 0) {
            return Msg.success("修改成功");
        }
        return Msg.error("修改失败");
    }


    @RequestMapping(path = {"/add"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg addDevice(Device device) {


        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Msg msg = deviceService.saveDevice(curUser, device);

        return msg;

    }

    @RequestMapping(path = {"/isexist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg iseExist(String mac) {

        Device rtnDevice = deviceService.findByMac(mac);

        if (null != rtnDevice) {
            return Msg.error("已经存在的设备");
        } else {
            return Msg.success("可以添加的设备");
        }
    }

    @RequestMapping(path = {"/modify"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg modifyDevice(Device device) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Device rtnDevice = deviceService.updateDevice(curUser, device);

        if (null != rtnDevice) {
            return Msg.success("修改成功", rtnDevice);
        } else {
            return Msg.error("修改失败");
        }
    }

    @RequestMapping(path = {"/modifydistance"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg modifyDistance(Device device) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Device rtnDevice = deviceService.modifyDistance(curUser, device);

        if (null != rtnDevice) {
            return Msg.success("修改成功", rtnDevice);
        } else {
            return Msg.error("修改失败");
        }
    }


    @RequestMapping(path = {"/venderdevcount"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg venderDevCount(Long venderId) {

        Long count = deviceService.countByVenderId(venderId);

        return Msg.success("查询成功", count);
    }

    /**
     * ----------------------------------设备的删除和回收------------------------------------------------
     */

    @RequestMapping(path = {"/deletedev"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg deleteDev(Long deviceId) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (UserTypeEnum.ADMIN.getVal().equals(curUser.getUserType())) {

            return deviceService.deleteById(deviceId);
        }


        return Msg.error("删除失败");
    }


    @RequestMapping(path = {"/takebackdevfromagt"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg takeBackDevFromAgt(String ids) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (ids.contains(",")){
            return Msg.error("暂时不支持批量回收");
        }

        Long deviceId=Long.parseLong(ids);

        Device device = deviceService.findById(deviceId);
        Integer state = null;

        try {
            state = device.getState();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("未找到设备,设备ID={},msg=", deviceId, e);

            return Msg.error("未找到设备");
        }

        if (!device.getVenderId().equals(0L)){
            return Msg.error("该设备已分配给商家,不能回收,你可以把它分配给其他代理商");
        }

        if (DeviceStateEnum.RUNNING.getVal().equals(state) || DeviceStateEnum.OFFLINE.getVal().equals(state)) {
            return Msg.error("该设备已在使用,不能回收,你可以把它分配给其他代理商");
        }

        Integer result = deviceService.modifyAgentIdByIds(curUser, 0L, deviceId.toString());

        if (result > 0) {
            return Msg.success("回收成功");
        }

        return Msg.error("回收失败");

    }

    @RequestMapping(path = {"/changedevtonewagt"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg changeDevToNewAgt(Long deviceId, Long agentId) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = deviceService.modifyAgentIdByIds(curUser, agentId, deviceId.toString());

        if (result > 0) {
            return Msg.success("回收成功");
        }

        return Msg.error("分配失败");
    }


    @RequestMapping(path = {"/takebackdevfromagtemp"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg takeBackDevFromAgtEmp(String ids) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = 0;

        if (UserTypeEnum.AGENT.getVal().equals(curUser.getUserType())) {
            result = deviceService.modifyAgentEmpIdByIds(curUser, 0L, ids);
        }

        return result > 0 ? Msg.success("回收成功") : Msg.error("回收失败");

    }


    @RequestMapping(path = {"/takebackdevfromvender"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg takeBackDevFromVender(String ids) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = 0;

        if (UserTypeEnum.AGENT.getVal().equals(curUser.getUserType())) {
            result = deviceService.modifyVenderIdAndStoreIdByIds(curUser,0L,ids);
        }

        return result > 0 ? Msg.success("回收成功") : Msg.error("回收失败");

    }

    @RequestMapping(path = {"/takebackdevfromstore"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg takeBackDevFromStore(String ids) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Integer result = 0;

        if (UserTypeEnum.VENDER.getVal()==curUser.getUserType()){
            result=deviceService.modifyStoreIdByIds(curUser,0L,DeviceStateEnum.UN_ACTIVE.getVal(),ids);
        }

        return result > 0 ? Msg.success("回收成功") : Msg.error("回收失败");
    }



}
