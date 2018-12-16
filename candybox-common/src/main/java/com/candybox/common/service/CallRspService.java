package com.candybox.common.service;

import com.candybox.common.dao.model.CallRsp;

public interface CallRspService extends BaseService<CallRsp, Long> {


    /**
     * find by bill
     * @param billId
     * @return
     */
    CallRsp findByBillId(String billId);

    /**
     * save
     * @param callRsp
     * @return
     */
    CallRsp save(CallRsp callRsp);
}
