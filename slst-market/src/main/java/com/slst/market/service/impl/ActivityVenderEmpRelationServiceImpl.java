package com.slst.market.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.market.dao.ActivityVenderEmpRelationDao;
import com.slst.market.dao.model.ActivityVenderEmpRelation;
import com.slst.market.service.ActivityVenderEmpRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("activityVenderEmpRelationService")
public class ActivityVenderEmpRelationServiceImpl extends BaseServiceImpl<ActivityVenderEmpRelation, Long> implements ActivityVenderEmpRelationService {

    @Resource
    private ActivityVenderEmpRelationDao activityVenderEmpRelationDao;

    @Override
    public Msg save(ActivityVenderEmpRelation activityVenderEmpRelation) {
        ActivityVenderEmpRelation save = activityVenderEmpRelationDao.save(activityVenderEmpRelation);
        return (save!=null && save.getId()!=null) ? Msg.success("创建成功") : Msg.error("创建失败");
    }

    @Override
    public Pager listVenderEmpActivity(Integer type, Long venderEmpId, String activNameLike, Integer pageNum, Integer pageSize) throws Exception {
        return activityVenderEmpRelationDao.listVenderEmpActivity(type, venderEmpId, activNameLike, pageNum, pageSize);
    }
}
