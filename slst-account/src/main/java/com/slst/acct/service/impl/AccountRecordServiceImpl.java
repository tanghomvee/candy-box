package com.slst.acct.service.impl;

import com.google.common.collect.Maps;
import com.slst.acct.dao.AccountRecordDao;
import com.slst.acct.dao.model.AccountRecord;
import com.slst.acct.service.AccountRecordService;
import com.slst.acct.web.vo.AcctRecordVO;
import com.slst.common.enums.TransTypeAEnum;
import com.slst.common.enums.UserTypeEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.DateUtils;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.Pager;
import com.slst.common.web.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/7 15:58
 */
@Service("accountRecordService")
public class AccountRecordServiceImpl extends BaseServiceImpl<AccountRecord, Long> implements AccountRecordService {

    @Resource
    private AccountRecordDao accountRecordDao;


    /**
     * 创建账户交易记录
     *
     * @param accountRecord
     * @return
     */
    @Override
    public AccountRecord createAcctRecord(AccountRecord accountRecord) {

        AccountRecord rtnAccountRecord = null;
        try {
            rtnAccountRecord = accountRecordDao.save(accountRecord);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("新建accountRecord，数据库操作异常。账户ID={},备注={}",accountRecord.getAcctId(),accountRecord.getRemark(),e);
            return null;
        }
        return rtnAccountRecord;
    }

    /**
     * 删除账户交易记录
     *
     * @param id
     */
    @Transactional
    @Override
    public Msg deleteAcctRecordById(Long id) {

        try {
            accountRecordDao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("删除acountRecord，ID：" + id + "，数据库操作异常。");
            return Msg.error("删除账户记录出现异常。");
        }
        return Msg.success();
    }

    @Override
    public boolean deleteByStoreId(Long storeId) {

        Long count = accountRecordDao.countByStoreId(storeId);
        if (count > 0) {
            try {
                accountRecordDao.deleteByStoreId(storeId);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("删除记录时异常,门店ID={},Msg={}", storeId, e);
                return false;
            }
        }
        return true;
    }

    /**
     * 根据ID查询账户交易记录
     *
     * @param id
     * @return
     */
    @Override
    public AccountRecord findAcctRecordById(Long id) {
        AccountRecord rtnAccountRecord = null;
        try {
            rtnAccountRecord = accountRecordDao.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查找acountRecord，ID：" + id + "，数据库操作异常。");
            return null;
        }
        return rtnAccountRecord;
    }

    @Override
    public Map<String, String> initTypeAList(UserTypeEnum userTypeEnum) {
        Map<String, String> map = null;
        switch (userTypeEnum) {
            case ADMIN:
                map = adminTypeA();
                break;
            case ADMIN_EMP:
                map = adminTypeA();
                break;
            case AGENT:
                map=agentTypeA();
                break;
            case AGENT_EMP:
                map=agentTypeA();
                break;
            case VENDER:
                map=venderTypeA();
                break;
            case VENDER_EMP:
                map=venderTypeA();
                break;
            case DATA_USER:
                break;
        }

        return map;
    }

    private Map<String, String> adminTypeA() {

        Map<String, String> map = Maps.newHashMap();
//        CHARGE_IN(2,"入账"),COST(4,"消费"),RETRIEVE(5,"回收"),
        map.put("2", "入账");
        map.put("4", "消费");
        map.put("5", "回收");
        return map;
    }


    private Map<String, String> agentTypeA() {
        Map<String, String> map = Maps.newHashMap();
//        CHARGING(1,"充值"),INCOME(3,"收入"),RETRIEVE(5,"回收"),RETREAT(6,"退回"),PAYOUT(7,"划出"),GET_IN(8,"划入"),
        map.put("1", "充值");
        map.put("3", "收入");
        map.put("5", "回收");
        map.put("6", "退回");
        map.put("7", "为他人充值");
        return map;
    }

    private Map<String, String> venderTypeA() {
        Map<String, String> map = Maps.newHashMap();
//        COST(4,"消费"),RETRIEVE(5,"回收"),RETREAT(6,"退回"),PAYOUT(7,"划出"),GET_IN(8,"划入"),FROZEN(9,"冻结"),UNFROZEN(10,"解冻")
        map.put("8", "充值");
        map.put("4", "消费");
        map.put("5", "回收");
        map.put("6", "退回");
        map.put("7", "为门店充值");
        map.put("9", "冻结");
        map.put("10", "解冻");
        return map;
    }

    /**
     * 根据条件查询账户交易记录
     *
     * @param acctRecordVO
     * @param pager
     * @return
     */
    @Override
    public Pager findAcctRecord(AcctRecordVO acctRecordVO, Pager pager) {

        String endTime = acctRecordVO.getEndTime();

        Date date = DateUtils.addOrSubDate(DateUtils.str2Date(endTime, DateUtils.DateTimeFormatters.YYYY_MM_DD), 1);

        endTime = DateUtils.date2Str(date, DateUtils.DateTimeFormatters.YYYY_MM_DD);

        acctRecordVO.setEndTime(endTime);

        return accountRecordDao.findAcctRecordByCondition(acctRecordVO, pager);
    }

    /**
     * 查询代理商本月，每天的收入情况
     * @param curUser
     * @return Map<String,Long>。key：（eg：01/21）；value：Long（每天收入总金额）；
     */
    @Override
    public Map<String, Long> sumIncomeOfAgentForThisMonth(UserVO curUser) {
        List<Object[]> rtnList = null;
        TransTypeAEnum typea = TransTypeAEnum.INCOME;
        Date curDate = new Date(); //当前时间
        String strCurDate = DateUtils.date2Str(curDate, DateUtils.DateTimeFormatters.YYYY_MM_DD);

        int totalDays = DateUtils.getDaysOfMonth(curDate); //本月总天数
        String minDate = DateUtils.getMonthStart(strCurDate); //本月第一天
        //计算本月最后一天
        Date date = DateUtils.str2Date(minDate, DateUtils.DateTimeFormatters.YYYY_MM_DD);
        Date newDate = DateUtils.addOrSubDate(date, totalDays);
        String maxDate = DateUtils.date2Str(newDate, DateUtils.DateTimeFormatters.YYYY_MM_DD); //本月最后一天

        rtnList = sumIncomeOfAgentByDate(curUser, typea, minDate, maxDate);

        Map<String, Long> resultMap = Maps.newLinkedHashMap();
        for (int i = 1; i <= totalDays; i++) {
            if (i < 10) {
                resultMap.put("0" + String.valueOf(i), null);
            } else {
                resultMap.put(String.valueOf(i), null);
            }
        }

        for (Object[] objects : rtnList) {
            String key = objects[0].toString();
            key = key.substring(key.lastIndexOf("-")+1,key.length());
            resultMap.replace(key,Long.parseLong(objects[1].toString()));
        }

        return resultMap;
    }


    /**
     * 根据TransTypeAEnum类型（eg：收入等），和传入时间段，按天统计代理商的收入/消费等情况
     *
     * @param curUser
     * @param transTypeAEnum
     * @param minDate
     * @param maxDate
     * @return List<Object   [   ]>：[0]：当前日期（eg：2016-01-01）；[1]：统计金额
     */
    @Override
    public List<Object[]> sumIncomeOfAgentByDate(UserVO curUser, TransTypeAEnum transTypeAEnum, String minDate, String maxDate) {
        if (null == transTypeAEnum || StringUtils.isNullOrEmpty(minDate) || StringUtils.isNullOrEmpty(maxDate)) {
            return null;
        }

        Long curUserId = curUser.getId();
        List<Object[]> rtnList = null;
        Integer typea = transTypeAEnum.getVal();
        try {
            rtnList = accountRecordDao.sumAmountByUserIdAndTypeaAndDate(curUserId, typea, minDate, maxDate);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("统计代理商收入（天）出错，userId={},Msg={}", curUserId, e);
            return null;
        }

        return rtnList;
    }

}
