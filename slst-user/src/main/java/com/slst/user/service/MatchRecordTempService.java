package com.slst.user.service;

import com.slst.common.service.BaseService;
import com.slst.user.dao.model.MatchRecordTemp;
import com.slst.user.dao.model.User;


public interface MatchRecordTempService extends BaseService<MatchRecordTemp,Long> {

    /**
     * 保存匹配记录
     * @param matchRecordTemp 匹配记录
     * @return 返回保存成功的匹配记录
     */
    MatchRecordTemp save(MatchRecordTemp matchRecordTemp);

    /**
     * 创建匹配记录
     * @param type 匹配类型 1:电话匹配,2:标签匹配
     * @param rsp 匹配结果
     * @param user 用户对象
     * @return 返回保存成功的匹配记录
     */
    MatchRecordTemp createMatchRecordTemp(Integer type,String rsp,User user);

    /**
     * 根据用户ID和数据启用状态查询条数
     * @param userId 用户ID
     * @param yn 数据启用状态
     * @return 结果数
     */
    Long countByUserIdAndYn(Long userId,Integer yn);

}
