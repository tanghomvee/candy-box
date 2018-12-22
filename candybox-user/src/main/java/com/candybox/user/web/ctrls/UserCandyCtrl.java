package com.candybox.user.web.ctrls;

import com.candybox.common.constants.RedisKey;
import com.candybox.common.enums.SeparatorEnum;
import com.candybox.common.web.ctrls.BaseCtrl;
import com.candybox.common.web.vo.Msg;
import com.candybox.common.web.vo.Pager;
import com.candybox.user.service.UserCandyService;
import com.candybox.user.web.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:14
 */
@Controller
@RequestMapping(path = "/userCandy")
public class UserCandyCtrl extends BaseCtrl {

    @Resource
    private UserCandyService userCandyService;


    /**
     * 领取糖果
     * @param candyId
     * @return
     */
    @RequestMapping(path = "/received" , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg received(Long candyId){

        UserVO userVO = (UserVO) getUser();
        Long amt = 1L;
        try {
            String userCandyVal = userVO.getId() + SeparatorEnum.UNDERLINE.getVal() + candyId;
            String userCandyKey = RedisKey.USER_CANDY + SeparatorEnum.UNDERLINE.getVal() + userCandyVal;
            if (userCandyVal.equals(redisComponent.get(userCandyKey))){
                LOGGER.info("今天已签到领取糖果,{}" , userCandyKey);
                return Msg.error("今天已签到");
            }
            int rs = userCandyService.received(candyId , userVO.getId() , amt);
            if (rs == 0){
                return Msg.error("今天已签到");
            }

            if (rs == -1){
                return Msg.error("签到失败");
            }
        } catch (Exception e) {
            LOGGER.error("签到异常" , e);
            return Msg.error("签到失败");
        }


        return Msg.success();
    }

    /**
     * 用户已经领取的糖果
     * @param pager
     * @return
     */
    @RequestMapping(path = "/listPage" , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg listPage(Pager pager){

        UserVO userVO = (UserVO) getUser();
        try {
           pager =  userCandyService.listUserCandy(userVO.getId() , pager);
        } catch (Exception e) {
            LOGGER.error("查询用户领取的糖果异常" , e);
            return Msg.error("查询用户领取的糖果失败");
        }

        return Msg.success(pager);
    }

}
