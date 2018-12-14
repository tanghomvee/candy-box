package com.slst.market.dao;

import com.slst.common.web.vo.Pager;


public interface ActivityVenderEmpRelationDaoExt{


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
