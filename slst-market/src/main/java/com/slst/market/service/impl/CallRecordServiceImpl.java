package com.slst.market.service.impl;

import com.google.common.collect.Maps;
import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.agent.dao.model.Agent;
import com.slst.agent.service.AgentService;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SoundToothService;
import com.slst.common.service.SysCfgService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.customer.dao.model.Customer;
import com.slst.customer.service.CustomerService;
import com.slst.market.dao.CallRecordDao;
import com.slst.market.dao.model.CallFee;
import com.slst.market.dao.model.CallRecord;
import com.slst.market.dao.model.Contacts;
import com.slst.market.service.CallFeeService;
import com.slst.market.service.CallRecordService;
import com.slst.market.service.ContactsService;
import com.slst.market.service.DialNumService;
import com.slst.vender.dao.model.Store;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.service.StoreService;
import com.slst.vender.service.VenderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 16:54
 */
@Service("callRecordService")
public class CallRecordServiceImpl extends BaseServiceImpl<CallRecord, Long> implements CallRecordService {

    @Resource
    private CallRecordDao callRecordDao;

    @Resource
    private ContactsService contactsService;

    @Resource
    private AccountService accountService;

    @Resource
    private CallFeeService callFeeService;

    @Resource
    private DialNumService dialNumService;

    @Resource
    private SoundToothService soundToothService;

    @Resource
    private SysCfgService sysCfgService;
    @Resource
    private AgentService agentService;
    @Resource
    private VenderService venderService;
    @Resource
    private StoreService storeService;


    @Override
    public CallRecord save(UserVO curUser, CallRecord callRecord) {
        callRecord.setYn(YNEnum.YES.getVal());
        callRecord.setCreator(curUser.getUserName());
        callRecord.setCreateTime(new Date());
        return callRecordDao.save(callRecord);
    }

    @Override
    public CallRecord modifyCallRecord(CallRecord callRecord) {
        callRecord.setChangeTime(new Date());
        callRecord.setChanger("sys");
        return callRecordDao.saveAndFlush(callRecord);
    }

    @Override
    public CallRecord findByCallId(String callId) {
        return callRecordDao.findByCallId(callId);
    }

    @Override
    public Msg call(UserVO curUser, Long id) {
        //目前只有商家可以打电话
        Long userId = curUser.getId();
        Long venderId = curUser.getVenderId();

        Vender vender=venderService.findVenderById(venderId);

        if (null == vender) {
            return Msg.error("未找到商家账户");
        }

        //查看该商家服务是否正常
        CallFee callFee = callFeeService.findByVenderId(venderId);
        Msg msg = Msg.error("尚未开通通话功能");
        msg.setFlag("700");
        if (null == callFee) return msg;
        Msg msg1 = Msg.error("服务已被叫停");
        msg1.setFlag("701");
        if (callFee.getYn().equals(YNEnum.NO.getVal())) return msg1;

        //获取用户自定义主叫号码
        String dialNum = dialNumService.getDialNumByUserId(curUser);//主叫号码
        if (StringUtils.isEmpty(dialNum)) return Msg.error("请先设置主叫号码");

        //查看账户余额是否充足
        Account account = accountService.findAcctByUserId(vender.getUserId());
        if (null == account) return Msg.error("未找到账户");
        Long balance = account.getBalance();
        if (balance < 50000) return Msg.error("余额不足50元,不能拨打电话");

        try {
            //获得预备通话记录
//            CallRecord callRecord = callRecordDao.findById(id).get();

            //获得联系人记录
            Contacts contacts = contactsService.findById(id);


            //设置区号,被叫号码ID和主叫号码用于调用接口
            String areaCode = callFee.getAreaCode();//区号
            String toNumberId = contacts.getToNumId();//被叫号码ID

            //开始调用运营商接口
            Msg rntCalMSG = soundToothService.call(areaCode, toNumberId, dialNum);
            //{"callId":"2160ab09788c424e941a487c2072663f","dateCreated":"2018/07/14 21:03:16","userData":"e6efdab6387531bed5b2ec65f15da7ad","statusCode":"000000"}

            //验证接口返回值
            if (rntCalMSG.getFlag().equals("error")) return Msg.error("运营商未响应");
            Object data = rntCalMSG.getData();
            //callId改成userData;
            String callId = "";
            String relayNumId = "no_pl_id_by_area";
            String relayNum = "no_telx_by_area";
            if (null != data && data.toString().contains("userData")) {
                Map<String, Object> map = (Map<String, Object>) data;
                callId = map.get("userData").toString();
                if (null != map.get("pl_id")) relayNumId = map.get("pl_id").toString();
                if (null != map.get("telX")) relayNum = map.get("telX").toString();
            }
            if (StringUtils.isEmpty(callId)) return Msg.error("通话失败,运行商未响应");

            //获得营销次数
            Integer touchTimes = contacts.getTouchtimes();
            if (null == touchTimes) {
                touchTimes = 1;
            } else {
                touchTimes += 1;
            }
            //修改联系人记录
            contacts.setFromNum(dialNum);
            contacts.setTouchtimes(touchTimes);

            Contacts rtnContacts = contactsService.modifyContact(curUser, contacts);

            if (null == rtnContacts) {
                LOGGER.error("通话成功,但是修改营销次数失败,被营销联系人ID={}", id);
                return Msg.error("通话成功,但是修改营销次数失败");
            }
            //新建通话记录
            CallRecord callRecord = new CallRecord();
            callRecord.setFromNum(dialNum);
            callRecord.setToNum(contacts.getToNum());
            callRecord.setToNumId(toNumberId);
            callRecord.setRelayNum(relayNum);
            callRecord.setRelayNumId(relayNumId);
            callRecord.setCallId(callId);
            callRecord.setCalltime(new Date());
            callRecord.setUserId(curUser.getId());
            callRecord.setVenderId(curUser.getVenderId());
            if (null != curUser.getStoreId() && curUser.getStoreId() > 0L) {
                callRecord.setStoreId(curUser.getStoreId());
            } else {
                callRecord.setStoreId(0L);
            }
            callRecord.setContactsId(contacts.getId());


            CallRecord rtnCallRecord = save(curUser, callRecord);
            //如果通话记录表修改成功则返回成功信息
            if (null != rtnCallRecord) {
                return Msg.success();
            }


        } catch (Exception e) {
            LOGGER.error("查询联系人记录异常,联系人记录ID={}", id, e);
        }

        return Msg.error("通话失败,未知系统错误,请联系客服");
    }

    /**
     * -----------------callrecord通话费用扣费操作---------------------
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean execute(String billId, String callId, Long duration) throws Exception {

        CallRecord callRecord = getCallRecord(callId);
        if (null == callRecord) {
            LOGGER.error("查询通话记录失败,通话记录callId={}", callId);
            return false;
        }
        if (billId.equals(callRecord.getBillId())) {
            LOGGER.info("话单已经处理callId={}", callId);
            return false;
        }

        Long venderId = callRecord.getVenderId();//获得商家ID
        Long storeId = callRecord.getStoreId();//获得门店ID
        Long acctRcdId = callRecord.getAcctRcdId();//冻结记录ID

        //计算admin成本费用
        Long platformCost = getPlatformCost(duration);
        if (platformCost.equals(-1L)) {
            LOGGER.error("admin成本费用计算失败,通话记录ID={}", callId);
            return false;
        }

        //计算商家消耗费用
        Long venderCost = getVenderCost(duration, venderId);
        if (venderCost.equals(-1L)) {
            LOGGER.error("商家成本计算失败,通话记录ID={}", callId);
            return false;
        }

        //计算代理商提成
        Map<String, Long> map = getAgentIncome(venderCost, venderId);
        if (null == map) {
            LOGGER.error("代理商成本计算失败,通话记录ID={}", callId);
            return false;
        }
        Long agentUserId = map.get("agentUserId");
        Long agentIncome = map.get("agentIncome");
        //如果系统或者商家消费为0 则不修改通话记录表,也不给商家解冻
        LOGGER.info("平台消费platformCost={},商家消费venderCost={}", platformCost, venderCost);

        //生成商家UserVO
        UserVO userVO = initUserVO(venderId, storeId);
        if (null == userVO) {
            LOGGER.error("生成商家UserVO,通话记录ID={}", callId);
            return false;
        }

        Long unfreezeAcctRcdId = 0L;
        if (null != acctRcdId && acctRcdId > 0) {
            //解冻并产生消费
            unfreezeAcctRcdId = accountService.unfreezeWithCallSrvCost(userVO, acctRcdId, agentUserId, platformCost, venderCost, agentIncome);
            if (unfreezeAcctRcdId <= 0L) {
                LOGGER.error("解冻商家冻结记录失败,通话记录ID={},商家ID={},冻结记录ID={}", callId, venderId, acctRcdId);
                throw new Exception("解冻商家冻结记录失败");
            }

        } else {
            //这里直接产生消费
            Integer costResult = accountService.callSrvCostWithEarning(userVO, agentUserId, platformCost, venderCost, agentIncome);
            if (costResult <= 0L) {
                LOGGER.error("商家打电话消费失败,通话记录ID={},商家ID={},", callId, venderId);
                throw new Exception("商家打电话消费失败");
            }
        }

        //修改通话记录中通话时间,通话费用,billId,将冻结记录ID改为解冻记录ID
        callRecord.setDuration(duration);
        callRecord.setFee(venderCost);
        callRecord.setBillId(billId);
        callRecord.setAcctRcdId(unfreezeAcctRcdId);
        CallRecord rtnCallRecord = this.modifyCallRecord(callRecord);


        if (null == rtnCallRecord) {
            LOGGER.error("修改callRecord失败,callRecordId={}", callRecord.getId());
            throw new Exception("修改callRecord失败");
        }
        return true;
    }


    /**
     * 获取通话记录
     *
     * @param callId
     * @return
     */
    private CallRecord getCallRecord(String callId) {
        return this.findByCallId(callId);
    }

    /**
     * 计算通话成本(通话分钟X单价)
     *
     * @param duration 通话时长(秒为单位)
     * @return 通话成本
     */
    private Long getPlatformCost(long duration) {

        long sysCallFee = 0L;
        try {
            String sysCallFeeStr = sysCfgService.findByCode(SysCfgEnum.SYS_SOUND_TOOTH_CALL_FEE.getVal()).getCodeVal();
            sysCallFee = Long.parseLong(sysCallFeeStr);
        } catch (Exception e) {
            LOGGER.error("获取语音服务单价配置信息出错,Code={}", SysCfgEnum.SYS_SOUND_TOOTH_CALL_FEE.getVal(), e);
            return -1L;
        }

        if (duration == 0L) {
            duration = 60;
        }

        //计算通话分钟
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(duration)).divide(new BigDecimal("60"), BigDecimal.ROUND_CEILING);
        long talkTime = bigDecimal.longValue();

        return talkTime * sysCallFee;//admin成本
    }

    /**
     * 获取商家通话费用
     *
     * @param duration
     * @param venderId
     * @return
     */
    private Long getVenderCost(long duration, Long venderId) {
        CallFee callFee = callFeeService.findByVenderId(venderId);
        if (null == callFee) return -1L;

        long venderCallFee = callFee.getFee();//商家语音服务单价

        if (duration == 0L) {
            duration = 60;
        }

        SysCfg sysCfg = sysCfgService.findByCode("VENDER_SECONDS_TO_MIN");
        if (null == sysCfg) {
            LOGGER.error("查找商家秒到分钟换算值失败,sysCfgCode={}", "VENDER_SECONDS_TO_MIN");
            return -1L;
        }
        String seconds2Min = sysCfg.getCodeVal();

        BigDecimal bigDecimal = new BigDecimal(String.valueOf(duration)).divide(new BigDecimal(seconds2Min), BigDecimal.ROUND_CEILING);

        long talkTime = bigDecimal.longValue();

        return talkTime * venderCallFee;//商家通话费用
    }

    private Map<String, Long> getAgentIncome(long venderCost, long venderId) {
        Long agentId = 0L;
        Long agentUserId = 0L;
        Integer earningsRate = 0;
        try {
            Vender vender = venderService.findVenderById(venderId);
            agentId = vender.getAgentId();
            Agent agent = agentService.findAgentById(agentId);
            agentUserId = agent.getUserId();
            earningsRate = agent.getEarningsRate();
        } catch (Exception e) {
            LOGGER.error("获取商家,或者代理商信息出错商家ID={},代理商ID={}", venderId, agentId, e);
            return null;
        }
        BigDecimal venderCostD = new BigDecimal(String.valueOf(venderCost));
        BigDecimal agentIncome = venderCostD.multiply(new BigDecimal(String.valueOf(earningsRate))).multiply(new BigDecimal("0.01"));

        Map<String, Long> map = Maps.newHashMap();
        map.put("agentUserId", agentUserId);
        map.put("agentIncome", agentIncome.longValue());
        return map;
    }

    private UserVO initUserVO(Long venderId, Long storeId) {

        UserVO userVO = new UserVO();

        try {
            Vender vender = venderService.findVenderById(venderId);
            userVO.setId(vender.getUserId());
            userVO.setVenderId(venderId);
            userVO.setVenderName(vender.getCompanyName());
            userVO.setUserName("sys");
        } catch (Exception e) {
            LOGGER.error("获取商家信息出错商家ID={}", venderId, e);
            return null;
        }

        if (null != storeId && !storeId.equals(0L)) {
            try {
                Store store = storeService.findStoreById(storeId);
                userVO.setStoreId(storeId);
                userVO.setStoreName(store.getStoreName());
            } catch (Exception e) {
                LOGGER.error("获取门店信息出错门店ID={}", storeId, e);
                return null;
            }

        }

        return userVO;
    }
}
