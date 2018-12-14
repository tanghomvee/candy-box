package com.slst.user.dao.impl;

import com.slst.common.dao.JpaDaoSupport;
import com.slst.user.dao.UserDaoExt;
import com.slst.user.dao.model.User;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:24
 */
public class UserDaoExtImpl extends JpaDaoSupport<User, Long>  implements UserDaoExt {

    /*
    Demo
    @Override
    public List findRptByFrameNoAndkindCodeFlag(String frameNo, Integer kindCodeflag, Date updateTime) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT o.userId,o.id,car.licenseNo,o.showId");
        sql.append(" FROM b_order_detail o,");
        sql.append("      b_car car,");
        sql.append("      b_precision_price p");
        sql.append(" WHERE o.orderStatus='已支付' ");
        sql.append("   AND car.id=o.carId");
        sql.append("   AND o.precisionPriceId=p.id");
        sql.append("   AND car.frameNo=:frameNo");
        sql.append("   AND o.updateTime > :updateTime");

        switch (kindCodeflag) {
            case 1:
                sql.append("   AND p.biPremium > 0 ");
                break;
            case 2:
                sql.append("   AND p.ciPremium > 0 ");
                break;
            case 3:
                sql.append("   AND (p.biPremium > 0 OR p.ciPremium > 0)");
                break;
            default:
                break;
        }
        sql.append(" order by o.updateTime DESC");

        Map<String,Object> map = new HashMap<>();
        map.put("frameNo", frameNo);
        map.put("updateTime", updateTime.getTime() / 1000);

        LOGGER.info("OrderDetailDaoExtImpl.findRptByFrameNoAndkindCodeFlag的SQl[{}],参数[{}]",sql.toString() , map);

        Query query =  createLocalQuery(HashMap.class , sql.toString());
        for (String paramName : map.keySet()){
            query.setParameter(paramName , map.get(paramName));
        }
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findCountPriceCfg(String relationId) {
        String sql = "SELECT provinceCode,agentCode,relationId,insureComCode,channelCode,fixedCount,changeCount " +
                "FROM m_count_price_config WHERE deleFlag='1' AND (relationId =:relationId OR relationId ='ALL') ORDER BY level DESC ";

        Map<String,Object> map = new HashMap<>();
        map.put("relationId", relationId);
        Query query =  createLocalQuery(HashMap.class , sql.toString());
        for (String paramName : map.keySet()){
            query.setParameter(paramName , map.get(paramName));
        }
        return query.getResultList();
    }*/
}
