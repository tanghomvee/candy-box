package com.slst.market.dao.impl;

import com.google.common.collect.Maps;
import com.slst.common.dao.JpaDaoSupport;
import com.slst.common.web.vo.Pager;
import com.slst.market.dao.ActivityVenderEmpRelationDaoExt;
import com.slst.market.dao.model.Activity;
import com.slst.market.dao.model.ActivityVenderEmpRelation;
import org.springframework.util.StringUtils;

import java.util.Map;

public class ActivityVenderEmpRelationDaoExtImpl extends JpaDaoSupport<ActivityVenderEmpRelation, Long> implements ActivityVenderEmpRelationDaoExt {

    @Override
    public Pager listVenderEmpActivity(Integer type, Long venderEmpId, String activNameLike, Integer pageNum, Integer pageSize) throws Exception {
        Map<String, Object> mapParam = Maps.newHashMap();
        StringBuffer sql = new StringBuffer("select a.* from t_activity a ");
            sql.append("left join t_activity_vender_emp_relation b ");
            sql.append("on a.id = b.activityId ");
            sql.append("where (b.venderEmpId = :venderEmpId or a.empId= :venderEmpId) ");

            if(type != null){
                sql.append("and a.type = :type ");
                mapParam.put("type", type);
            }
            if(!StringUtils.isEmpty(activNameLike)){
                sql.append("and LOCATE(:activNameLike,a.activName) ");
                mapParam.put("activNameLike", activNameLike);
            }
            mapParam.put("venderEmpId", venderEmpId);
            sql.append("order by createTime desc");
        return super.doSQLPage(sql.toString(), mapParam, Activity.class, pageNum, pageSize);
    }
}
