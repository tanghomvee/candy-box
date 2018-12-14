package com.slst.acct.dao.impl;

import com.google.common.collect.Maps;
import com.slst.acct.dao.AccountRecordDaoExt;
import com.slst.acct.dao.model.AccountRecord;
import com.slst.acct.web.vo.AcctRecordVO;
import com.slst.common.dao.JpaDaoSupport;
import com.slst.common.web.vo.Pager;

import java.util.Map;

public class AccountRecordDaoExtImpl extends JpaDaoSupport<AccountRecord,Long> implements AccountRecordDaoExt {

    /**
     * 根据制定条件查询
     * @param acctRecordVO
     * @param pager
     * @return
     */
    @Override
    public Pager findAcctRecordByCondition(AcctRecordVO acctRecordVO, Pager pager) {

        StringBuffer sql = new StringBuffer("SELECT * FROM t_account_record WHERE ");
        Map<String, Object> params = Maps.newHashMap();
        Long userId = acctRecordVO.getUserId();
        Long storeId = acctRecordVO.getStoreId();
        Integer typea = acctRecordVO.getTypea();
        Integer typeb = acctRecordVO.getTypeb();
        String startTime = acctRecordVO.getStartTime();
        String endTime = acctRecordVO.getEndTime();
        String sortKey = acctRecordVO.getSortKey();
        String orderKey = acctRecordVO.getOrderKey();

        if(null == userId && null == storeId) return null;//userId和storeId必定有且只有一个
        if(null != userId){
            sql.append("userId = :userId ");
            params.put("userId",userId);
        }else {
            sql.append("storeId = :storeId ");
            params.put("storeId",storeId);
        }
        if(null != typea){
            sql.append("AND typea = :typea ");
            params.put("typea",typea);
        }
        if(null != typeb){
            sql.append("AND typeb = :typeb ");
            params.put("typeb",typeb);
        }
        if(null != startTime){
            sql.append("AND time >= str_to_date(:startTime ,'%Y-%m-%d') ");
            params.put("startTime",startTime);
        }
        if(null != endTime){
            sql.append("AND time <= str_to_date(:endTime ,'%Y-%m-%d') ");
            params.put("endTime",endTime);
        }
        if(null != sortKey){
            sql.append("ORDER BY ");
            sql.append(sortKey);
            sql.append(" ");
//            params.put("sortKey",sortKey);
        }else {
            sql.append("ORDER BY time ");
        }
        if(null != orderKey){
            sql.append(orderKey);
//            params.put("orderKey",orderKey);
        }else {
            sql.append("DESC");
        }

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , AccountRecord.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询特定账户交易记录异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
