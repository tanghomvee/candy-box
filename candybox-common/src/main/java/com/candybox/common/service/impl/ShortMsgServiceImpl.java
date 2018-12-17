package com.candybox.common.service.impl;

import com.candybox.common.components.RedisComponent;
import com.candybox.common.dao.ShortMsgDao;
import com.candybox.common.dao.model.ShortMsg;
import com.candybox.common.service.ShortMsgService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service("shortMsgService")
public class ShortMsgServiceImpl extends BaseServiceImpl<ShortMsg , Long> implements ShortMsgService {

    @Resource
    private ShortMsgDao shortMsgDao;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public ShortMsg save(String from, String to, String content, String remark) {
        ShortMsg shortMsg = new ShortMsg();
        shortMsg.setSender(from);
        shortMsg.setReceiver(to);
        shortMsg.setContent(content);
        shortMsg.setRemark(remark);
        return shortMsgDao.save(shortMsg);
    }

    @Override
    public ShortMsg sendChkMsg(String to, String chkCode, String remark) {

        if (!StringUtils.isEmpty(redisComponent.get(to))){
            return null;
        }
        Long expireTime = 60L;
        String content = "手机短信验证码:" + chkCode;
        ShortMsg shortMsg = this.save("SYS" , to , content , remark);


        if (shortMsg == null){
            LOGGER.error("保存短信失败");
            return null;
        }

        //TODO 发送消息
        if (true){
            redisComponent.set(to , chkCode , expireTime);
        }
        return shortMsg;
    }
}
