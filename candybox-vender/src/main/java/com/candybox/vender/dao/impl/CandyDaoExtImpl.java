package com.candybox.vender.dao.impl;

import com.candybox.common.dao.JpaDaoSupport;
import com.candybox.common.enums.OnlineEnum;
import com.candybox.common.enums.YNEnum;
import com.candybox.common.web.vo.Pager;
import com.candybox.vender.dao.CandyDaoExt;
import com.candybox.vender.dao.model.Candy;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 11:24
 */
public class CandyDaoExtImpl extends JpaDaoSupport<Candy, Long>  implements CandyDaoExt {


    @Override
    public Pager listPage(Pager pager) {

        StringBuffer sql = new StringBuffer("SELECT * FROM t_candy where yn=:yn AND online=:online");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        params.put("online" , OnlineEnum.ONLINE.getVal());
//        if(startTime != null){
//            sql.append(" AND UNIX_TIMESTAMP(createTime) >= :lowerCreateTime ");
//            params.put("lowerCreateTime" , startTime.getTime() / 1000);
//        }


        sql.append(" order by id desc , createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , Candy.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询糖果异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
