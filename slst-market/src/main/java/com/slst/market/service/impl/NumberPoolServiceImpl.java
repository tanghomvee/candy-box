package com.slst.market.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.common.dao.model.SysCfg;
import com.slst.common.enums.SysCfgEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.SoundToothService;
import com.slst.common.service.SysCfgService;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.NumberPoolDao;
import com.slst.market.dao.model.CallFee;
import com.slst.market.dao.model.NumberPool;
import com.slst.market.service.CallFeeService;
import com.slst.market.service.NumberPoolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.slst.common.service.SoundToothService.*;

@Service("numberPoolService")
public class NumberPoolServiceImpl extends BaseServiceImpl<NumberPool,Long> implements NumberPoolService {

    @Resource
    private NumberPoolDao numberPoolDao;

    @Resource
    private SoundToothService soundToothService;

    @Resource
    private SysCfgService sysCfgService;

    @Resource
    private AccountService accountService;

    @Resource
    private CallFeeService callFeeService;


    @Override
    public Msg saveRelayNumber() {
        Integer pageNum=1;
        Integer pageSize=1000;
        boolean flag=false;
        Map<String,Object> map=(Map<String,Object>) soundToothService.listPhoneNumByToken(pageNum,pageSize).getData();


        List<Map<String ,String>> phones = (List<Map<String, String>>) map.get(SoundToothService.SOUND_TOOTH_RESULT_LIST);

        for (Map<String, String> phone : phones) {
            String plId = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_ID , "");
            String uid = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_USR_ID , "");
            String phoneNum=phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_PHONE_NUM , "");
            String area = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_AREA , "");
            String appId = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_APP , "");
            String acct = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_ACCT , "");
            String city = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_CIRY , "");
            String province = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_PROVINCE , "");
            String seatNo = phone.getOrDefault(SOUND_TOOTH_PHONE_RESULT_LIST_SEAT , "");
            String result=JSONObject.toJSONString(phone);

            NumberPool numberPool=numberPoolDao.findByPlId(plId);

            if(null==numberPool){
                numberPool=new NumberPool();
                numberPool.setPlId(plId);
                numberPool.setUid(uid);
                numberPool.setPhone(phoneNum);
                numberPool.setAreaCode(area);
                numberPool.setAppid(appId);
                numberPool.setMyName(acct);
                numberPool.setCity(city);
                numberPool.setProvince(province);
                numberPool.setiSeatNo(seatNo);
                numberPool.setResult(result);
                numberPool.setAgentId(0L);
                numberPool.setAgentEmpId(0L);
                numberPool.setVenderId(0L);
                numberPool.setStoreId(0L);
                numberPool.setYn(YNEnum.YES.getVal());
                numberPool.setCreator("sys");
                numberPool.setCreateTime(new Date());
                NumberPool rtnnumPool=numberPoolDao.save(numberPool);
                if(null!= rtnnumPool){
                    flag=true;
                }
            }


        }

        return flag?Msg.success("添加完毕"):Msg.error("操作失败");

    }

    @Override
    public Msg pageNumberPool(int pageNum, int pageSize, String sortKey, String orderKey) {

        sortKey="createTime";
        orderKey="desc";
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<NumberPool> page=numberPoolDao.findAll(pageable);

        return Msg.success("查询成功",page);
    }

    @Override
    public List<NumberPool> findByVenderId(Long venderId) {
        return numberPoolDao.findByVenderId(venderId);
    }

    @Override
    public Page<NumberPool> findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(Long agentId, Long agentEmpId, Long venderId, Long storeId, int pageNum, int pageSize) {
        String sortKey="createTime";
        String orderKey="desc";
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, orderKey);

        Page<NumberPool> page=numberPoolDao.findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(agentId,agentEmpId,venderId,storeId,pageable);


        return page;
    }

    @Override
    public Page<NumberPool> findNotUsedFromAdmin(int pageNum, int pageSize) {
        Long agentId =0L;
        Long agentEmpId=0L;
        Long venderId=0L;
        Long storeId=0L;

        return findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(agentId,agentEmpId,venderId,storeId,pageNum,pageSize);
    }

    @Override
    public Page<NumberPool> findNotUsedFromAgentToEmpOrVender(Long agentId, int pageNum, int pageSize) {
        Long agentEmpId=0L;
        Long venderId=0L;
        Long storeId=0L;
        return findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(agentId,agentEmpId,venderId,storeId,pageNum,pageSize);
    }

    @Override
    public Page<NumberPool> findNotUsedFromAgentEmpToVender(Long agentId, Long agentEmpId, int pageNum, int pageSize) {
        Long venderId=0L;
        Long storeId=0L;
        return findByAgentIdAndAgentEmpIdAndVenderIdAndStoreId(agentId,agentEmpId,venderId,storeId,pageNum,pageSize);
    }

    @Transactional
    @Override
    public Msg modifyAgentId(String changer, Long agentId, List<Long> ids) {

        Integer result=numberPoolDao.modifyAgentId(agentId,ids,changer,new Date());

        if (result>0){
            return Msg.success("修改成功");
        }

        return Msg.error("修改失败");
    }

    @Transactional
    @Override
    public Msg modifyAgentEmpId(String changer, Long agentEmpId, List<Long> ids) {
        Integer result=numberPoolDao.modifyAgentEmpId(agentEmpId,ids,changer,new Date());

        if (result>0){
            return Msg.success("修改成功");
        }

        return Msg.error("修改失败");
    }

    @Transactional
    @Override
    public Msg modifyVenderId(String changer, Long venderId, List<Long> ids) {
        Integer result=numberPoolDao.modifyVenderId(venderId,ids,changer,new Date());

        if (result>0){
            return Msg.success("修改成功");
        }

        return Msg.error("修改失败");
    }

    @Transactional
    @Override
    public Msg buyRelayCard(UserVO curUser, List<Long> ids) {



        String cardFeeVal="";
        String rentFeeVal="";
        String callFeeVal="";

        Long cardFee=0L;
        Long rentFee=0L;
        Long callFee=0L;

        SysCfg sysCfg=null;
        try{
            sysCfg = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_CARD_FEE.getVal());

            cardFeeVal=sysCfg.getCodeVal();

            sysCfg = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_RENT_FEE.getVal());

            rentFeeVal=sysCfg.getCodeVal();

            sysCfg = sysCfgService.findByCode(SysCfgEnum.VENDER_SOUND_TOOTH_CALL_FEE.getVal());

            callFeeVal=sysCfg.getCodeVal();


            cardFee=Long.parseLong(cardFeeVal);

            rentFee=Long.parseLong(rentFeeVal);

            callFee=Long.parseLong(callFeeVal);


        }catch (Exception e){
            LOGGER.error("获取商家默认费用报错",e);
            return Msg.error("购买号卡失败");
        }

        Long userId=curUser.getId();

        Account account= accountService.findAcctByUserId(userId);

        boolean balanceEnough=account.getBalance()-cardFee-rentFee<0;//余额不足

        if (balanceEnough){
            return Msg.success("余额不足");
        }

        //TODO 这里需要扣除卡费和一个月的月租费


        CallFee callFee1=new CallFee();
        callFee1.setFee(callFee);
        callFee1.setRent(rentFee);
        callFee1.setVenderId(curUser.getVenderId());
        callFee1.setAreaCode("");
        CallFee rtnFee= callFeeService.saveCallFee(callFee1);

        if(null==rtnFee){
            return Msg.error("设置月租费，通话单价失败");
        }

        Integer result=numberPoolDao.modifyVenderId(curUser.getVenderId(),ids,curUser.getUserName(),new Date());

        if (result>0){
            return Msg.success("购买成功");
        }

        return Msg.error("修改失败");

    }

    @Override
    public Long countByAreaCode(String areaCode) {
        return numberPoolDao.countByAreaCode(areaCode);
    }


}
