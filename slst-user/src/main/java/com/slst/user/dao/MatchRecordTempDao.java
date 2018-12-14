package com.slst.user.dao;

import com.slst.user.dao.model.MatchRecordTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRecordTempDao extends JpaRepository<MatchRecordTemp,Long>,MatchRecordTempDaoExt {

    Long countByUserIdAndYn(Long userId,Integer yn);


}
