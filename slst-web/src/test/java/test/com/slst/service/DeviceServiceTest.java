package test.com.slst.service;

import com.slst.common.web.vo.UserVO;
import com.slst.device.dao.model.Device;
import com.slst.device.service.DeviceService;
import org.junit.Test;
import org.springframework.data.domain.Page;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/1 20:11
 */
public class DeviceServiceTest extends BaseTest {

    @Resource
    private DeviceService deviceService;

    @Test
    public void testDeviceSave(){
        UserVO user=new UserVO();
        user.setUserName("kkkk");

        Device device=new Device();
        device.setMac("AB:SH:EF:AA:IJ:MM");
        device.setAVal(52);
        device.setNVal(24);


//        Device rtnDevice=deviceService.saveDevice(user,device);

//        System.out.println(rtnDevice.getId()+"___________");
    }

    @Test
    public void testModifyAgentIds(){
        UserVO user=new UserVO();
        user.setUserName("aaaaa");
        Integer result=deviceService.modifyAgentIdByIds(user,1L,"1");
        System.out.println(result);
    }

    @Test
    public void testModifyAgentEmpIds(){
        UserVO user=new UserVO();
        user.setUserName("bbbbb");
        Integer result=deviceService.modifyAgentEmpIdByIds(user,111L,"4");
        System.out.println(result);
    }

    @Test
    public void testModifyVenderIds(){
        UserVO user=new UserVO();
        user.setUserName("emp");
        Integer result=deviceService.modifyVenderIdByIds(user,66L,"1,4,6,5");
        System.out.println(result);
    }

    @Test
    public void testModifyStoreIds(){
//        UserVO user=new UserVO();
//        user.setUserName("emp");
//        Integer result=deviceService.modifyStoreIdByIds(user,99L,"1,4,6");
//        System.out.println(result);
    }

    @Test
    public void testFindDevNotAgentId(){
        List<Device> list = deviceService.findDeviceNotTheAgent(0L,"AA");
        for (Device device : list) {
            System.out.println(device.getMac());
        }
    }

    @Test
    public void testFindDevByAgentId(){
        Page<Device> page = deviceService.findDeviceOfAgent(0L,null,0,2);
        for (Device device : page.getContent()) {
            System.out.println(device.getMac());
        }
    }

    @Test
    public void testFindDevByAgentIdAndMac(){
        Page<Device> page = deviceService.findDeviceOfAgent(0L,"AA",0,2);
        for (Device device : page.getContent()) {
            System.out.println(device.getMac());
        }
    }

    @Test
    public void testFindDevByAgIDAndAgEmpId(){
        UserVO user=new UserVO();
        user.setAgentId(1L);
        Page<Device> page = deviceService.findDeviceOfAgentEmp(user,12L,"AA",0,2);
        System.out.println(page.getContent().size());
        for (Device device : page.getContent()) {
            System.out.println(device.getMac());
        }
    }

    @Test
    public void testFindDevByVenderIdAndMac(){
        UserVO user=new UserVO();
        user.setAgentId(1L);
        user.setAgentEmpId(0L); //未设置EmpId会空指针异常
       Page<Device> page =  deviceService.findDeviceOfVender(user,0L,"GH",0,2);
        for (Device device : page.getContent()) {
            System.out.println(device.getMac());
        }
    }

    @Test
    public void testFindDevByStoreIdAndMac(){
        UserVO user=new UserVO();
        user.setVenderId(55L);
        Page<Device> page =  deviceService.findDeviceOfStore(user,0L,"IJ",0,2);

        System.out.println(page.getContent().size());
        for (Device device : page.getContent()) {
            System.out.println(device.getMac());
        }
    }

    @Test
    public void testFindAll(){

        Page<Device> page =  deviceService.findAllDevices("d5:d5:",0,5);
        for (Device device : page.getContent()) {
            System.out.println(device.getMac());
        }
    }

}
