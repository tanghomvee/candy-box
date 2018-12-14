package com.slst.market.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.SmsFeeDao;
import com.slst.market.dao.model.SmsFee;
import com.slst.market.service.SmsFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("SmsFeeService")
public class SmsFeeServiceImpl extends BaseServiceImpl<SmsFee,Long> implements SmsFeeService {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SmsFeeDao smsFeeDao;

    @Override
    public SmsFee createSmsFee(UserVO userVO, SmsFee smsFee) {

        String creator = userVO.getUserName();
        Date curDate = new Date();
        Integer yes = YNEnum.YES.getVal();

        smsFee.setYn(yes);
        smsFee.setCreator(creator);
        smsFee.setCreateTime(curDate);
        SmsFee rtnSmsFee = null;
        try {
            rtnSmsFee = smsFeeDao.save(smsFee);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("新建SmsFee，venderId："+smsFee.getVenderId()+"，数据库操作异常。"+e);
            return null;
        }

        return rtnSmsFee;
    }

    @Override
    public SmsFee modifySmsFee(UserVO userVO, SmsFee smsFee) {

        String changer = userVO.getUserName();
        Date curDate = new Date();

        smsFee.setChanger(changer);
        smsFee.setChangeTime(curDate);
        SmsFee rtnSmsFee = null;
        try {
            rtnSmsFee = smsFeeDao.saveAndFlush(smsFee);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("更新SmsFee，venderId："+smsFee.getVenderId()+"，数据库操作异常。"+e);
            return null;
        }

        return rtnSmsFee;
    }

    @Override
    public SmsFee findSmsFeeByVenderId(Long venderId) {

        SmsFee rtnSmsFee = null;
        try {
            rtnSmsFee = smsFeeDao.findByVenderId(venderId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找SmsFee，venderId："+venderId+"，数据库操作异常。"+e);
            return null;
        }

        return rtnSmsFee;
    }

    @Override
    public SmsFee findSmsFeeById(Long id) {

        SmsFee rtnSmsFee = null;
        try {
            rtnSmsFee = smsFeeDao.findById(id).get();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查找SmsFee，id："+id+"，数据库操作异常。"+e);
            return null;
        }

        return rtnSmsFee;
    }

    @Override
    public Msg deleteSmsFeeById(String ids) {
        if(StringUtils.isNullOrEmpty(ids)){
            return Msg.error("未选择要删除的费率。");
        }
        if(ids.contains(",")){
            String[] idsArr = ids.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                try {
                    smsFeeDao.deleteById(Long.parseLong(idsArr[i].trim()));
                }catch (Exception e){
                    e.printStackTrace();
                    LOGGER.error("删除SmsFee，ID："+idsArr[i]+"，数据库操作异常。"+e);
                    return Msg.error("删除出错，请重试。");
                }
            }

        }else {
            try {
                smsFeeDao.deleteById(Long.parseLong(ids.trim()));
            }catch (Exception ex){
                ex.printStackTrace();
                LOGGER.error("删除SmsFee，ID："+ids+"，数据库操作异常。"+ex);
                return Msg.error("删除出错，请重试。");
            }
        }
        return Msg.success();
    }

    @Override
    public Msg deleteSmsFeeByvenderId(String venderIds) {
        if(StringUtils.isNullOrEmpty(venderIds)){
            return Msg.error("未选择要删除的费率。");
        }
        if(venderIds.contains(",")){
            String[] idsArr = venderIds.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                try {
                    smsFeeDao.deleteByVenderId(Long.parseLong(idsArr[i].trim()));
                }catch (Exception e){
                    e.printStackTrace();
                    LOGGER.error("删除SmsFee，VenderId："+idsArr[i]+"，数据库操作异常。"+e);
                    return Msg.error("删除出错，请重试。");
                }
            }

        }else {
            try {
                smsFeeDao.deleteByVenderId(Long.parseLong(venderIds.trim()));
            }catch (Exception ex){
                ex.printStackTrace();
                LOGGER.error("删除SmsFee，VenderId："+venderIds+"，数据库操作异常。"+ex);
                return Msg.error("删除出错，请重试。");
            }
        }
        return Msg.success();
    }
}
