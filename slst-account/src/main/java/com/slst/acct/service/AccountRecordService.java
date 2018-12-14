package com.slst.acct.service;

import com.slst.acct.dao.model.AccountRecord;
import com.slst.acct.web.vo.AcctRecordVO;
import com.slst.common.enums.TransTypeAEnum;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.common.web.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/7 15:57
 */
public interface AccountRecordService extends BaseService<AccountRecord,Long> {



    /**
     * 创建账户交易记录
     * @param accountRecord
     * @return
     */
    AccountRecord createAcctRecord(AccountRecord accountRecord);



    /**
     * 删除账户交易记录
     * @param id
     */
    Msg deleteAcctRecordById(Long id);

    /**
     * 根据门店ID删除账户记录
     * @param storeId
     * @return
     */
    boolean deleteByStoreId(Long storeId);

    /**
     * 根据ID查询账户交易记录
     * @param id
     * @return
     */
    AccountRecord findAcctRecordById(Long id);

    Map<String, String> initTypeAList(UserTypeEnum userTypeEnum);

    /**
     * 根据条件查询账户交易记录
     * @param acctRecordVO
     * @param pager
     * @return
     */
    Pager findAcctRecord(AcctRecordVO acctRecordVO, Pager pager);


    /**
     * 查询代理商本月，每天的收入情况
     * @param curUser
     * @return Map<String,Long>。key：（eg：01/21）；value：Long（每天收入总金额）；
     */
    Map<String,Long> sumIncomeOfAgentForThisMonth(UserVO curUser);

    /**
     * 根据TransTypeAEnum类型（eg：收入等），和传入时间段，按天统计代理商的收入/消费等情况
     * @param curUser
     * @param transTypeAEnum
     * @param minDate
     * @param maxDate
     * @return List<Object[]>：[0]：当前日期（eg：2016-01-01）；[1]：统计金额
     */
    List<Object[]> sumIncomeOfAgentByDate(UserVO curUser, TransTypeAEnum transTypeAEnum, String minDate, String maxDate);
}
