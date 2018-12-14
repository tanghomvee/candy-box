package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.CallFee;

import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 14:11
 */
public interface CallFeeService extends BaseService<CallFee,Long> {

    CallFee saveCallFee(CallFee callFee);


    CallFee findByVenderId(Long venderId);


    /**
     * 根据是否启用的值找出CallFee列表
     * @param yn
     * @return
     */
    List<CallFee> findByYn(Integer yn);

    /**
     * 开通通话功能,存入商家对应消费金额和其他信息
     * @param curUser 当前用户
     * @return
     */
    Msg createCallSrv(UserVO curUser,String city);

    /**
     * 关闭语音服务
     * @param curUser
     * @return
     */
    Msg closeCallSrv(UserVO curUser);

    /**
     * 回复语音服务
     * @param curUser
     * @return
     */
    Msg recoveryCallSrv(UserVO curUser);
}
