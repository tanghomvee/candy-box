package com.slst.agent.service.impl;

import com.google.common.collect.Maps;
import com.slst.agent.dao.AgentEmpDao;
import com.slst.agent.dao.model.AgentEmp;
import com.slst.agent.service.AgentEmpService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.device.service.DeviceService;
import com.slst.vender.service.VenderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("agentEmpService")
public class AgentEmpServiceImpl extends BaseServiceImpl<AgentEmp, Long> implements AgentEmpService {

    @Resource
    private AgentEmpDao agentEmpDao;

    @Resource
    private DeviceService deviceService;

    @Resource
    private VenderService venderService;

    /**
     * 代理商员工注册
     * @param agentEmp
     * @return
     */
    @Override
    public AgentEmp createAgentEmp(AgentEmp agentEmp) {
        return agentEmpDao.save(agentEmp);
    }




    /**
     * 修改代理商员工基本信息
     * @param agentEmp
     * @return
     */
    @Override
    public AgentEmp updateAgentEmp(AgentEmp agentEmp) {
        return agentEmpDao.saveAndFlush(agentEmp);
    }

//    /**
//     * 根据id删除代理商员工
//     * @param id
//     * @return
//     */
//    public void deleteAgentEmpById(Long id){
//         agentEmpDao.deleteById(id);
//    }

    /**
     * 在UserService调用。代理商使用。删除员工
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Msg deleteAgentEmp(Long id){
        Msg msg = null;
        Long deviceCount = deviceService.countDeviceOfAgentEmp(id);//代理商员工旗下的设备数
        if(deviceCount>0){    //代理商员工绑定了设备或商家
            String errMsg = "删除中断,请先回收员工的设备";
            return Msg.error(errMsg);
        }
        //代理商员工未绑定设备和商家
        try {
            agentEmpDao.deleteById(id);//删除agentEmp表数据
        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("agentEmp表数据删除失败！");//数据库删除数据失败
        }

        return Msg.success();
    }

    /**
     * 根据id查找代理商员工
     * @param id
     * @return
     */
    @Override
    public AgentEmp findAgentEmpById(Long id) {

        AgentEmp agentEmp = null;
        try {
            agentEmp = agentEmpDao.findById(id).get();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据agentEmpId查找代理商员工信息出错。id={},Msg={}",id,e);
            return null;
        }

        return agentEmp;
    }

    /**
     * 查找指定代理商（agentId）旗下的代理商员工
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @param sortKey
     * @param order
     * @return
     */
    @Override
    public Page<AgentEmp> findAgEmpByAgentId(Long agentId, int pageNum, int pageSize, String sortKey, String order,String empName) {

        if (StringUtils.isNullOrEmpty(sortKey)){
            sortKey="createTime";
        }

        if (StringUtils.isNullOrEmpty(order)){
            order="desc";
        }

        Pageable pageable = PageableUtil.getPageable(pageNum,pageSize,sortKey,order);

        if (StringUtils.isNullOrEmpty(empName)){
            return agentEmpDao.findByAgentId(agentId,pageable);
        }else{
            return agentEmpDao.findByAgentIdAndEmpNameContaining(agentId,empName,pageable);
        }


    }

    /**
     * 根据userId查找代理商员工
     * @param userId
     * @return
     */
    @Override
    public AgentEmp findAgEmpByUserId(Long userId) {
        return agentEmpDao.findByUserId(userId);
    }


    /**
     * 查找当前代理商下员工的设备销售总榜前十
     * @param curUser
     * @return
     */
    @Override
    public Map<String,Long> deviceSalesTopTenOfAgEmp(UserVO curUser){
        List<Object[]> rtnData = null;
        Map<String,Long> resultMap = Maps.newLinkedHashMap();
        try{
            rtnData = agentEmpDao.findSalesTopTenOfEmpByAgentId(curUser.getAgentId());

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("统计改代理下销售设备数前十的员工出错。agentId={},Msg={}",curUser.getAgentId(),e);
            return null;
        }

        for (int i = 0; i < rtnData.size(); i++) {
            String agEmpName = null;
            Object arr[] = rtnData.get(i);
            Long agEmpId = Long.parseLong(arr[0].toString());   //agentEmpId
            Long counts = Long.parseLong(arr[1].toString());    //统计的设备数量
            AgentEmp agentEmp = findAgentEmpById(agEmpId);  //查找员工信息

            if (null == agentEmp){  //查找员工信息错误
                agEmpName = "系统发呆中，未取得姓名...";
            }else {
                agEmpName = agentEmp.getEmpName();
            }

            resultMap.put(agEmpName,counts);
        }

        return resultMap;
    }
}
