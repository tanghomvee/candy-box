package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.market.dao.model.ActivityVenderEmpRelation;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 14:11
 */
public interface ActivityVenderEmpRelationService extends BaseService<ActivityVenderEmpRelation,Long> {
    /**
     * 保存商家员工与活动的关系
     * @param activityVenderEmpRelation
     * @return
     */
    Msg save(ActivityVenderEmpRelation activityVenderEmpRelation);

    /**
     * 通过参数查询商家员工所有活动
     * @param type
     * @param venderEmpId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Pager listVenderEmpActivity(Integer type, Long venderEmpId, String activNameLike, Integer pageNum, Integer pageSize) throws Exception;
}
