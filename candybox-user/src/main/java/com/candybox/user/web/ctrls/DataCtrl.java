package com.candybox.user.web.ctrls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.candybox.common.enums.EncryptionEnum;
import com.candybox.common.enums.YNEnum;
import com.candybox.common.service.SoundToothService;
import com.candybox.common.web.ctrls.BaseCtrl;
import com.candybox.common.web.vo.Msg;
import com.candybox.user.dao.model.MatchRecordTemp;
import com.candybox.user.dao.model.User;
import com.candybox.user.service.MatchRecordTempService;
import com.candybox.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "/srv")
public class DataCtrl extends BaseCtrl {
    @Resource
    private SoundToothService soundToothService;

    @Resource
    private UserService userService;

    @Resource
    private MatchRecordTempService matchRecordTempService;

    private static String CALL_KEY = "123";

    private Integer upCount = 5000;

    @RequestMapping(path = {"/call"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg call(String phoneId, String fromPhoneNum, String sign) {
        if (StringUtils.isEmpty(phoneId) || StringUtils.isEmpty(fromPhoneNum) || StringUtils.isEmpty(sign)) {
            LOGGER.error("呼叫参数错误:phoneId={} , fromPhoneNum={} , sign={}", phoneId, fromPhoneNum, sign);
            return Msg.error("参数错误");
        }

        String key = phoneId + fromPhoneNum + CALL_KEY;
        Msg msg = verify(sign, key);
        if (!msg.isSuccess()) {
            return msg;
        }


        return soundToothService.call(phoneId, fromPhoneNum);
    }


    @RequestMapping(path = {"/phoneNum"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg matchPhoneNum(String mac, String sign) {
        if (StringUtils.isEmpty(mac) || StringUtils.isEmpty(sign)) {
            LOGGER.error("匹配手机号码参数错误:mac={} , sign={}", mac, sign);
            return Msg.error("参数错误");
        }

        String userStr = redisComponent.get(sign);

        if (StringUtils.isEmpty(userStr)) {
            return Msg.error("登录签名已过期,请重新获取");
        }

//        String key = mac + CALL_KEY;
//        Msg msg = verify(sign, key);
//        if (!msg.isSuccess()) {
//            return msg;
//        }


        User user = null;
        try {
            user = JSON.parseObject(userStr, User.class);
        } catch (Exception e) {
            LOGGER.error("Json转用户实体失败,jsonStr={}", userStr, e);
            return Msg.error("未知系统错误,请重试");
        }
        //判断剩余次数
        Long count = matchRecordTempService.countByUserIdAndYn(user.getId(), YNEnum.YES.getVal());

        if (count >= upCount) {
            return Msg.error("匹配次数已达上限,请充值后重试");
        }

        Msg msg = null;
        try {
            msg = soundToothService.findCustomerDeviceByMac(mac);
        } catch (Exception e) {
            LOGGER.error("声牙查询用户画像出错,MAC={}", mac, e);
            return Msg.error("未知连接错误,请稍后重试");
        }

        //保存匹配记录
        if (msg.isSuccess()) {
            MatchRecordTemp matchRecordTemp = matchRecordTempService.createMatchRecordTemp(1, msg.getData().toString(), user);
            if (null == matchRecordTemp) {
                return Msg.error("保存查询数据失败,请稍后重试");
            }
        }


        return msg;
    }

    @RequestMapping(path = {"/detail"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg matchDetail(String mac, String sign) {

        String userStr = redisComponent.get(sign);

        if (StringUtils.isEmpty(userStr)) {
            return Msg.error("登录签名已过期,请重新获取");
        }

//        String key = mac + CALL_KEY;
//        Msg msg = verify(sign, key);
//        if (!msg.isSuccess()) {
//            return msg;
//        }


        User user = null;
        try {
            user = JSON.parseObject(userStr, User.class);
        } catch (Exception e) {
            LOGGER.error("Json转用户实体失败,jsonStr={}", userStr, e);
            return Msg.error("未知系统错误,请重试");
        }
        //判断剩余次数
        Long count = matchRecordTempService.countByUserIdAndYn(user.getId(), YNEnum.YES.getVal());

        if (count >= upCount) {
            return Msg.error("匹配次数已达上限,请充值后重试");
        }

        Msg msg = null;
        try {
            msg = soundToothService.findCustomerBaseInfoByMac(mac);
        } catch (Exception e) {
            LOGGER.error("声牙查询用户画像出错,MAC={}", mac, e);
            return Msg.error("未知连接错误,请稍后重试");
        }


        //保存匹配记录
        if (msg.isSuccess()) {
            Map<String , JSONObject> map=(Map<String , JSONObject>)msg.getData();
            String rsp = JSON.toJSONString(map);
            MatchRecordTemp matchRecordTemp = matchRecordTempService.createMatchRecordTemp(2, rsp, user);

            if (null == matchRecordTemp) {
                return Msg.error("保存查询数据失败,请稍后重试");
            }
        }

        return msg;
    }

    @RequestMapping(path = {"/tpl/add"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg createSmsTpl(String title, String content, String cmpSign, String sign) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(cmpSign) || StringUtils.isEmpty(content)) {
            LOGGER.error("创建短信模板参数错误:title={} ,content={}, cmpSign={} ,sign={}", title, content, cmpSign, sign);
            return Msg.error("参数错误");
        }

        String key = title + content + cmpSign + CALL_KEY;
        Msg msg = verify(sign, key);
        if (!msg.isSuccess()) {
            return msg;
        }

        return soundToothService.createSMSTemplate(title, content, cmpSign);
    }

    @RequestMapping(path = {"/tpl/state"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg showSmsTpl(String tplId, String sign) {
        if (StringUtils.isEmpty(tplId) || StringUtils.isEmpty(sign)) {
            LOGGER.error("查看短信模板状态参数错误:tplId={},sign={}", tplId, sign);
            return Msg.error("参数错误");
        }

        String key = tplId + CALL_KEY;
        Msg msg = verify(sign, key);
        if (!msg.isSuccess()) {
            return msg;
        }

        return soundToothService.findSMSTemplateAuthStatus(tplId);
    }

    @RequestMapping(path = {"/sms/send"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg showSmsTpl(String phoneId, String tplId, String sign) {
        if (StringUtils.isEmpty(tplId) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(phoneId)) {
            LOGGER.error("发送短信参数错误:tplId={},phoneId={},sign={}", tplId, phoneId, sign);
            return Msg.error("参数错误");
        }

        String key = phoneId + tplId + CALL_KEY;
        Msg msg = verify(sign, key);
        if (!msg.isSuccess()) {
            return msg;
        }

        return soundToothService.sendSMSMsg(phoneId, tplId);
    }

    @RequestMapping(path = {"/getSign"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg getSign(String userName, String pwd) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)) {
            LOGGER.error("发送短信参数错误:userName={},pwd={}", userName, pwd);
            return Msg.error("参数错误");
        }

        try {
            pwd = EncryptionEnum.MD5_32BIT.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("MD5转码失败,需要转码的密码={}", pwd, e);
            return Msg.error("服务器获取账号失败,请重试");
        }

        List<User> users = userService.userLogin(userName, pwd);
        if (null == users || users.size() <= 0) {
            return Msg.error("用户名或者密码错误");
        }

        if (users.size() > 1) {
            return Msg.error("暂不支持手机号直接登录!");
        }

        User user = users.get(0);
        String userStr = JSON.toJSONString(user);
        String sign = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        redisComponent.set(sign, userStr, 1800L);
        return Msg.success("获取签名成功",sign);
    }

    private Msg verify(String sign, String key) {
        String encrypted = null;
        try {
//            encrypted = EncryptionEnum.MD5_2_HEX.encrypt(key);
            encrypted = EncryptionEnum.MD5_32BIT.encrypt(key);
        } catch (Exception e) {
            LOGGER.error("MD5异常:{}", key, e);
        }
        if (!sign.equalsIgnoreCase(encrypted)) {
            return Msg.error("签名错误");
        }
        return Msg.success();
    }
}
