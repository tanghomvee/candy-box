package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.CallRecord;
import org.springframework.data.domain.Page;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 14:11
 */
public interface CallRecordService extends BaseService<CallRecord,Long> {

    /**
     * 新建通话记录
     * @param callRecord
     * @return
     */
    CallRecord save(UserVO curUser,CallRecord callRecord);

    /**
     * 修改通话记录
     * @param callRecord
     * @return
     */
    CallRecord modifyCallRecord(CallRecord callRecord);

    /**
     * 根据CallId查找通话记录
     * @param callId
     * @return
     */
    CallRecord findByCallId(String callId);

    /**
     * 拨打电话
     * @param curUser
     * @param id 通话记录ID
     * @return
     */
    Msg call(UserVO curUser,Long id);

    boolean execute(String billId, String callId, Long duration) throws Exception;
}
