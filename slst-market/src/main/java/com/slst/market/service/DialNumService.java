package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.DialNum;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 14:11
 */
public interface DialNumService extends BaseService<DialNum,Long> {

    /**
     * 保存主叫号码
     * @param dialNum
     * @return
     */
    DialNum save(DialNum dialNum);

    Integer modifyDialNum(UserVO curUser, String mobile);

    String getDialNumByUserId(UserVO curUser);
}
