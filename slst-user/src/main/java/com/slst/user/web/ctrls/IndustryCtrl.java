package com.slst.user.web.ctrls;

import com.slst.common.web.ctrls.BaseCtrl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.user.dao.model.Industry;
import com.slst.user.service.IndustryService;
import com.slst.vender.dao.model.Store;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/4 12:31
 */

@Controller
@RequestMapping(path = "/industry")
public class IndustryCtrl extends BaseCtrl {

    @Resource
    private IndustryService industryService;

    @RequestMapping(path = {"/getall"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getAll(Store store, HttpSession session) {

        UserVO curUser = getUser();

        if (null==curUser){
            return Msg.error("请先登录后操作!");
        }

        List list=new ArrayList();
        List<Industry> industries=industryService.getAllIndustry();
        for (Industry industry : industries) {
            if (industry.getParentId()==0){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("name",industry.getIndustryName());
                map.put("value",industry.getId().toString());
                list.add(map);
            }else{
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("name",industry.getIndustryName());
                map.put("value",industry.getId().toString());
                map.put("parent",industry.getParentId().toString());
                list.add(map);
            }
        }

        return Msg.success("查询成功",list);
    }
}
