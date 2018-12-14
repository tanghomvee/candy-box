package com.slst.acct.dao;

import com.slst.acct.web.vo.AcctRecordVO;
import com.slst.common.web.vo.Pager;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/7 15:04
 */
public interface AccountRecordDaoExt {

    /**
     * 根据制定条件查询
     * @param acctRecordVO
     * @param pager
     * @return
     */
    Pager findAcctRecordByCondition(AcctRecordVO acctRecordVO, Pager pager);
}
