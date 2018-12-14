package com.slst.market.dao;

import com.slst.market.dao.model.CallRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CallRecordDao extends JpaRepository<CallRecord, Long>,CallRecordExtDao {


    /**
     * 根据CallId查找通话记录
     * @param callId
     * @return
     */
    @Query(value = "SELECT * FROM t_call_record where callId=?1",nativeQuery = true)
    CallRecord findByCallId(String callId);

    /**
     * TODO 待删除方法
     */
//    Page<CallRecord> findByActivityId(Long activityId, Pageable pageable);



}
