package com.slst.common.service;

import com.slst.common.dao.model.MatchRsp;

public interface MatchRspService extends BaseService<MatchRsp, Long> {


    /**
     * save
     * @param matchRsp
     * @return
     */
    MatchRsp save(MatchRsp matchRsp);

    /**
     * find by mac
     * @param mac
     * @return
     */
    MatchRsp findByMac(String mac);

    /**
     * find by imei
     * @param imei
     * @return
     */
    MatchRsp findByImei(String imei);
}
