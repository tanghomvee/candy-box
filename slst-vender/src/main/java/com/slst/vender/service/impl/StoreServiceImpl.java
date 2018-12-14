package com.slst.vender.service.impl;

import com.slst.acct.dao.model.Account;
import com.slst.acct.service.AccountService;
import com.slst.common.enums.DeviceStateEnum;
import com.slst.common.enums.RuleExpEnum;
import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.device.service.DeviceService;
import com.slst.vender.dao.StoreDao;
import com.slst.vender.dao.model.Store;
import com.slst.vender.dao.model.StoreRuleRelation;
import com.slst.vender.service.StoreRuleRelationService;
import com.slst.vender.service.StoreService;
import com.slst.vender.service.VenderEmpService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-19 15:44
 */
@Service("storeService")
public class StoreServiceImpl extends BaseServiceImpl<Store, Long> implements StoreService {

    @Resource
    private StoreDao storeDao;

    @Resource
    private DeviceService deviceService;

    @Resource
    private AccountService accountService;

    @Resource
    private VenderEmpService venderEmpService;

    @Resource
    private StoreRuleRelationService storeRuleRelationService;

    /**
     * 创建门店
     *
     * @param userVO
     * @param store
     * @return
     */
    @Override
    public Store createStore(UserVO userVO, Store store) {
        Date curDate = new Date();
        String creator = userVO.getUserName();
        Integer yes = YNEnum.YES.getVal();

        store.setVenderId(userVO.getVenderId());
        store.setVenderName(userVO.getVenderName());
        store.setCreateTime(curDate);
        store.setCreator(creator);
        store.setYn(yes);

        Store rtnStore = null;  //将创建的Store存入数据库
        try {
            rtnStore = storeDao.save(store);  //将创建的Store存入数据库
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (null != rtnStore) {   //如果存入Store成功
            //创建此Store的账户
            Account account = new Account();
            account.setUserId(0L);
            account.setStoreId(rtnStore.getId());
            account.setAcctName("vender_" + rtnStore.getVenderId() + "store_" + rtnStore.getId() + "acct");
            account.setYn(yes);
            account.setCreateTime(curDate);
            account.setCreator(creator);

            Account rtnAcct = null;
            try {
                rtnAcct = accountService.createAccount(account);  //创建店铺账户
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                return null;
            }
            if (null == rtnAcct) {    //账户创建失败
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //手动回滚
                return null;
            }

            StoreRuleRelation storeRuleRelation= storeRuleRelationService.save(rtnStore.getId(),1L,RuleExpEnum.LE,"100");

            if (null == storeRuleRelation) {
                return null;
            }

            return rtnStore;
        }

        return null;    //门店或者账号创建失败
    }

    @Override
    public Msg deleteStore(UserVO cuser, Long storeId) {

        boolean isSuc = accountService.deleteStoreAcct(storeId);//删除账户信息

        if (!isSuc) return Msg.error("删除账户信息失败,删除门店被中断");

        Long countOfDev = deviceService.countDeviceOfStore(storeId);//查询门店上拥有设备数量

        if (countOfDev > 0) {
            Integer result = deviceService.modifyStoreIdByStoreId(cuser.getUserName(), 0L, storeId, DeviceStateEnum.UN_ACTIVE.getVal());

            if (result <= 0) return Msg.error("设备回收失败,删除门店被中断");
        }


        Long countOfEmp = venderEmpService.countByStoreId(storeId);

        if (countOfEmp > 0) {
            Integer result = venderEmpService.modifyStoreIdByStoreId(cuser.getUserName(), 0L, storeId);

            if (result <= 0) return Msg.error("回收门店员工失败,删除门店被中断");
        }

        try {
            storeDao.deleteById(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("删除门店时异常,门店ID={},Msg={}", storeId, e);
            return Msg.error("删除门店失败");
        }

        return Msg.success("删除成功");

    }

    /**
     * 根据（门店表）id查找门店
     *
     * @param id
     * @return
     */
    @Override
    public Store findStoreById(Long id) {
        Optional<Store> optional = storeDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    /**
     * 根据商家的venderId，查找其旗下的门店
     *
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey  按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order    升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<Store> findStoreByVenderId(Long venderId, String storeName, int pageNum, int pageSize, String sortKey, String order) {
        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);
        if (StringUtils.isNullOrEmpty(storeName)) {
            return storeDao.findByVenderId(venderId, pageable);
        } else {
            return storeDao.findByVenderIdAndStoreNameContaining(venderId, storeName, pageable);
        }

    }

    /**
     * 修改门店信息
     *
     * @param userVO
     * @param store
     * @return
     */
    @Override
    public Store updateStore(UserVO userVO, Store store) {
        Date curDate = new Date();
        String changer = userVO.getUserName();

        if (null == store.getId() || store.getId().equals(0)) {
            LOGGER.error("修改门店,获取门店信息失败,门店ID={}", store.getId());
            return null;
        }

        Store rtnStore = storeDao.findById(store.getId()).get();//找到数据库中对应的Store信息

        if (null != rtnStore) {
            //以下为允许修改的门店信息
            rtnStore.setStoreName(store.getStoreName());
            rtnStore.setCityId(store.getCityId());
            rtnStore.setCityName(store.getCityName());
            rtnStore.setAddress(store.getAddress());
            rtnStore.setLat(store.getLat());
            rtnStore.setLng(store.getLng());
            rtnStore.setTaxIdNum(store.getTaxIdNum());
            rtnStore.setChangeTime(curDate);
            rtnStore.setChanger(changer);

            //将更新后的门店信息更新到数据库
            return storeDao.saveAndFlush(rtnStore);
        }

        return null;
    }

    /**
     * 删除门店信息
     *
     * @param storeId
     * @return n>0L：删除失败，此门店下还有n个绑定的设备；0L：删除成功；-1:删除失败
     */
    @Override
    public Long deleteStore(Long storeId) {

        Long count = deviceService.countDeviceOfStore(storeId);
        if (count > 0) {
            return count;
        }

        try {
            storeDao.deleteById(storeId);
            return 0L;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1L;
        }
    }

    @Override
    public Long countByVenderId(Long venderId) {
        return storeDao.countByVenderId(venderId);
    }


}
