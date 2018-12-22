package com.candybox.user.dao.impl;

import com.candybox.common.dao.JpaDaoSupport;
import com.candybox.common.enums.YNEnum;
import com.candybox.common.web.vo.Pager;
import com.candybox.user.dao.UserCandyDaoExt;
import com.candybox.user.dao.model.UserCandy;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class UserCandyDaoExtImpl extends JpaDaoSupport<UserCandy , Long> implements UserCandyDaoExt {
    @Override
    public Pager listUserCandy(Long userId, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT tc.id , tc.icon,tc.venderName ,tuc.amt , tucr.operateTime  FROM " );
        sql.append("t_candy tc " )
            .append("left join t_user_candy tuc " )
            .append("left join (select * from t_user_candy_record where userId=:userIdTmp and operateTime >=:minTime and operateTime <=:maxTime ) tucr " )
            .append("on tuc.candyId = tc.id and tucr.candyId=tc.id")
            .append(" where tc.yn=:yn and tuc.userId=:userId ");


        Map<String , Object> params = Maps.newHashMap();
        params.put("userIdTmp" , userId);
        params.put("minTime" ,   DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate().getTime() / 1000);
        params.put("maxTime" , DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate().getTime() / 1000);
        params.put("userId" , userId);
        params.put("yn" , YNEnum.YES.getVal());

        sql.append(" order by tc.id desc");
        try{

            LOGGER.info("listUserCandy的SQL[{}],参数[{}]",sql.toString() , params);
            Pager retPager = super.doSQLPage(sql.toString() , params , HashMap.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("listUserCandy的SQL糖果异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
