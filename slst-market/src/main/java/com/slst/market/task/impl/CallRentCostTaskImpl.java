package com.slst.market.task.impl;

import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.common.components.RedisComponent;
import com.slst.common.enums.YNEnum;
import com.slst.common.utils.DateUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.CallFee;
import com.slst.market.service.CallFeeService;
import com.slst.market.task.CallRentCostTask;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.service.VenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component("callRentCostTask")
public class CallRentCostTaskImpl implements CallRentCostTask {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CallFeeService callFeeService;
    @Resource
    private VenderService venderService;
    @Resource
    private AccountService accountService;
    @Resource
    RedisComponent redisComponent;

    private final static String EXECUTED_KEY="EXECUTED_KEY";

    @Override
    public void execute() {
        LOGGER.info("准点进入月租扣费程序,时间={}", DateUtils.date2Str(new Date()));

        String isExecuted= redisComponent.get(EXECUTED_KEY);
        if (StringUtils.isEmpty(isExecuted)){
            redisComponent.set(EXECUTED_KEY,"Yes",259200L);
        }else{
            return;
        }

        List<CallFee> callFees = callFeeService.findByYn(YNEnum.YES.getVal());
        for (CallFee callFee : callFees) {
            Long venderId = callFee.getVenderId();

            Vender vender = venderService.findVenderById(venderId);
            Long userId = 0L;
            try {
                userId = vender.getUserId();
            } catch (Exception e) {
                LOGGER.error("扣除月租费时,获取商家信息异常,商家ID={}", venderId, e);
                continue;
            }

            Account account = accountService.findAcctByUserId(userId);
            if (null == account) continue;
            String acctName = account.getAcctName();

            UserVO userVO = new UserVO();
            userVO.setId(userId);
            userVO.setVenderId(venderId);
            userVO.setUserName("sys");
            Integer result= accountService.venderCallSrvCost(userVO, callFee.getRent());

            if (result<=0){
                LOGGER.error("用户余额不足,用户ID={}", userId);
                Msg msg= callFeeService.closeCallSrv(userVO);
                if (msg.getFlag().equals("error")) LOGGER.error(",商家ID={}", venderId);
            }
        }
    }

}
