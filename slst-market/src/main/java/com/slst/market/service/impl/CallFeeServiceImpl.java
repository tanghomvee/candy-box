package com.slst.market.service.impl;

import com.slst.acct.service.AccountService;
import com.slst.common.dao.model.AreaCode;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.AreaCodeService;
import com.slst.common.service.SysCfgService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.CallFeeDao;
import com.slst.market.dao.model.CallFee;
import com.slst.market.service.CallFeeService;
import com.slst.market.service.NumberPoolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("callFeeService")
public class CallFeeServiceImpl extends BaseServiceImpl<CallFee, Long> implements CallFeeService {

    @Resource
    private CallFeeDao callFeeDao;

    @Resource
    private SysCfgService sysCfgService;

    @Resource
    private AccountService accountService;

    @Resource
    private AreaCodeService areaCodeService;

    @Resource
    private NumberPoolService numberPoolService;

    @Override
    public CallFee saveCallFee(CallFee callFee) {
        callFee.setCreateTime(new Date());
        callFee.setYn(YNEnum.YES.getVal());
        return callFeeDao.save(callFee);
    }

    @Override
    public CallFee findByVenderId(Long venderId) {
        return callFeeDao.findByVenderId(venderId);
    }

    @Override
    public List<CallFee> findByYn(Integer yn) {
        return callFeeDao.findByYn(yn);
    }

    @Override
    public Msg createCallSrv(UserVO curUser,String city) {
        Long venderId = curUser.getVenderId();

        CallFee existCF= this.findByVenderId(venderId);
        if(null!= existCF)return Msg.error("请不要重复开通服务");

        AreaCode areaCode=areaCodeService.findByCity(city);

        if (null==areaCode) return Msg.error("未找到所在城市");

        String areaCodeNum=areaCode.getAreaCode();
        Long count= numberPoolService.countByAreaCode(areaCodeNum);
        if (count<=0) return Msg.error("您所在区域目前尚未开通该功能");



        //查询语音服务资费标准
        Long srvFee = null; //总服务费(一个月月租+卡费)
        Long rent = null;//月租
        Long cardFee = null;//服务费
        Long callPrice = null;//通话单价
        try {
            String rentStr = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_RENT_FEE.getVal()).getCodeVal();
            String cardFeeStr = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_CARD_FEE.getVal()).getCodeVal();
            String callFeeStr = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_CALL_FEE.getVal()).getCodeVal();

            rent = Long.parseLong(rentStr);
            cardFee = Long.parseLong(cardFeeStr);
            callPrice = Long.parseLong(callFeeStr);

            srvFee = rent + cardFee;
        } catch (Exception e) {
            LOGGER.error("系统配置文件缺失,配置编码:VENDER_SOUND_TOOTH_RENT_FEE,VENDER_SOUND_TOOTH_CARD_FEE", e);
            return Msg.error("系统配置文件缺失,请联系服务商");
        }

        //扣除月租费和服务费

        try {
            Integer result = accountService.venderCallSrvCost(curUser, srvFee);
            if (result <= 0) return Msg.error("余额不足");
        } catch (Exception e) {
            LOGGER.error("扣除开通服务费失败,userID={},srvFee={}", curUser.getId(), srvFee);
            return Msg.error("扣除服务费时异常,操作中断,请联系我们");
        }


        //创建通话费用等信息
        CallFee callFee = new CallFee();
        callFee.setCreator(curUser.getUserName());
        callFee.setFee(callPrice);
        callFee.setRent(rent);
        callFee.setVenderId(venderId);
        callFee.setAreaCode(areaCodeNum);
        CallFee rtnCallFee = this.saveCallFee(callFee);

        return null != rtnCallFee ? Msg.success("服务开通完成") : Msg.error("服务开通失败");

    }

    @Transactional
    @Override
    public Msg closeCallSrv(UserVO curUser) {
        Long venderId=curUser.getVenderId();
        Integer result= callFeeDao.modifyYn(YNEnum.NO.getVal(),venderId);
        return result>0?Msg.success("关闭服务成功"):Msg.error("关闭服务失败");
    }

    @Transactional
    @Override
    public Msg recoveryCallSrv(UserVO curUser) {

        Long userId=curUser.getId();
        Long venderId=curUser.getVenderId();
        CallFee existCF= this.findByVenderId(venderId);

        if (null==existCF) return Msg.error("没有开通过语音服务");

        //扣除月租费和服务费

        try {
            Integer result = accountService.venderCallSrvCost(curUser, existCF.getRent());
            if (result <= 0) return Msg.error("余额不足");
        } catch (Exception e) {
            LOGGER.error("扣除开通服务费失败,userID={},srvFee={}", curUser.getId(), existCF.getRent());
            return Msg.error("扣除服务费时异常,操作中断,请联系我们");
        }

        Integer result= callFeeDao.modifyYn(YNEnum.YES.getVal(),venderId);
        return result>0?Msg.success("恢复服务成功"):Msg.error("恢复服务失败");

    }


}
