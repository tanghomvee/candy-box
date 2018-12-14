package com.slst.vender.web.ctrls;

import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.vender.dao.model.VenderEmp;
import com.slst.vender.service.VenderEmpService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping(path = "/venderemp")
public class VenderEmpCtrl extends BaseCtrl {

    @Resource
    private VenderEmpService venderEmpService;


    @RequestMapping(path = {"/vdrempinfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getVdrEmpInfo(Long empId) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        if (null == empId || empId <= 0L) {
            empId = curUser.getVenderEmpId();
        }

        VenderEmp venderEmp = venderEmpService.findVenderEmpById(empId);

        if (null != venderEmp) {
            return Msg.success("查询成功", venderEmp);
        }

        return Msg.error("查询失败");
    }

    @RequestMapping(path = {"/vdremplist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getVdrEmpList(int pageNum, int pageSize, String sortKey, String order) {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Page<VenderEmp> page=venderEmpService.findVenderEmpByVenderId(curUser.getVenderId(),pageNum,pageSize,sortKey,order);
        return Msg.success("查询成功",page);
    }

    /**
     * 查询商家下面的所有员工
     * @return
     */
    @RequestMapping(path = {"/vdrallemplist"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getVdrAllEmpList() {

        UserVO curUser = getUser();

        if (null == curUser) {
            return Msg.error("请先登录后操作!");
        }

        Integer pageSize=venderEmpService.countByVenderId(curUser.getVenderId()).intValue();
        if (pageSize>0) {

            Page<VenderEmp> page = venderEmpService.findVenderEmpByVenderId(curUser.getVenderId(), 0,pageSize , null, null);
            return Msg.success("查询成功", page.getContent());
        }
        return Msg.success("查询成功", null);
    }

}
