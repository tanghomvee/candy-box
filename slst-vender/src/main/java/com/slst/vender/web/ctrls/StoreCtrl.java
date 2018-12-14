package com.slst.vender.web.ctrls;

import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.vender.dao.model.Store;
import com.slst.vender.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/store")
public class StoreCtrl extends BaseCtrl {


    @Resource
    private StoreService storeService;

    @RequestMapping(path = {"/count"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getStoreCount(Long venderId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Long count=storeService.countByVenderId(venderId);

        return Msg.success("查询成功",count);
    }

    @RequestMapping(path = {"/add"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg addStore(Store store, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Store rtnStore= storeService.createStore(curUser,store);
        if(null!= rtnStore){
            return Msg.success("创建成功",rtnStore);
        }else{
            return Msg.error("创建失败");
        }
    }

    /**
     * 删除门店
     * @param storeId 门店ID
     * @param session session
     * @return
     */
    @RequestMapping(path = {"/delstore"}, method = RequestMethod.POST)
    @ResponseBody
    public Msg addStore(Long storeId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        return storeService.deleteStore(curUser,storeId);
    }

    @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getList(Long venderId,int pageNum,int pageSize, String sortKey, String order,String storeName, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        if (null==venderId || venderId.equals(0)){
            venderId=curUser.getVenderId();
        }

        Page<Store> page=storeService.findStoreByVenderId(venderId,storeName,pageNum,pageSize,sortKey,order);

        if (null!=page){
            return Msg.success("查询成功",page);
        }else{
            return Msg.error("查询失败");
        }
    }

    @RequestMapping(path = {"/storeinfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getStoreInfo(Long storeId, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Store store= storeService.findStoreById(storeId);

        if(null!= store){
            return Msg.success("查询成功",store);
        }
        return Msg.error("查询失败");
    }

    @RequestMapping(path = {"/modifystore"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg modifyStore(Store store, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        Store rtnStore= storeService.updateStore(curUser,store);
        if (null!=rtnStore){
            return Msg.success("修改成功",rtnStore);
        }
        return Msg.error("修改失败");
    }
}
