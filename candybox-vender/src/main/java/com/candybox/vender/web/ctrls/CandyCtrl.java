package com.candybox.vender.web.ctrls;

import com.candybox.common.web.ctrls.BaseCtrl;
import com.candybox.common.web.vo.Msg;
import com.candybox.common.web.vo.Pager;
import com.candybox.vender.dao.model.Candy;
import com.candybox.vender.service.CandyService;
import com.candybox.vender.web.vo.CandyVO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
@RequestMapping(path = "/candy")
public class CandyCtrl extends BaseCtrl {

    @Resource
    private CandyService candyService;

    @RequestMapping(path = "/listPage" , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg listPage(Pager pager){
        pager = candyService.listPage(pager);
        if (pager == null){
            return Msg.error("查询失败");
        }

        if (!CollectionUtils.isEmpty(pager.getData())){
            pager.setData(Lists.transform(pager.getData(), new Function<Object,CandyVO>() {
                @Override
                public CandyVO apply(Object obj) {
                    Candy candy = (Candy) obj;
                    CandyVO candyVO = new CandyVO();
                    BeanUtils.copyProperties(candy ,candyVO);
                    return candyVO;
                }
            }));
        }
        return Msg.success(pager);
    }


}
