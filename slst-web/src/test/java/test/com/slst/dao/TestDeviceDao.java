package test.com.slst.dao;

import com.slst.common.enums.YNEnum;
import com.slst.device.dao.DeviceDao;
import com.slst.device.dao.model.Device;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/1 19:37
 */
public class TestDeviceDao extends BaseTest {

    @Resource
    private DeviceDao deviceDao;

    @Test
    public void testSave(){
        Device device=new Device();
        device.setMac("AB:CD:EF:GH:IJ:ZL");
        device.setAVal(52);
        device.setNVal(24);

        Date curDate=new Date();//当前时间
        Integer yes=YNEnum.YES.getVal();
        String creator="ssss";

        device.setAgentId(0L);
        device.setAgentEmpId(0L);
        device.setVenderId(0L);
        device.setStoreId(0L);
        device.setDistance(0);
        device.setState(-1);
        device.setYn(yes);
        device.setCreator(creator);
        device.setCreateTime(curDate);

        Device rtnDevice=deviceDao.save(device);
    }

    @Test
    public void testGetById(){

        Device device=deviceDao.findById(1L).get();

        System.out.println(device);
    }




}
