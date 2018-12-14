package com.slst.common.service;

import com.slst.common.dao.model.Nic;

import java.util.List;

public interface NicService extends BaseService<Nic, Long> {

    /**
     * mac地址长度
     */
    Integer MAC_LEN = 17;
    Integer MAC_CORPORATION_LEN = 8;
    /**
     * is mobile mac
     * @param mac
     * @return
     */
    boolean isMobileMac(String mac);
    /**
     * find
     * @param mac
     * @return
     */
    List<Nic> findByMac(String mac);

    /**
     * save
     * @param nics
     * @return
     */
    List<Nic> save(Nic... nics);


    /**
     * 根据macType查询手机品牌信息
     * @param macType
     * @param yn
     * @return
     */
    List<Nic> findByMacTypeAndYn(Integer macType,Integer yn);

}
