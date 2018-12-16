package com.candybox.user.dao;

import com.candybox.user.dao.model.MatchRecordTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRecordTempDao extends JpaRepository<MatchRecordTemp,Long>,MatchRecordTempDaoExt {

    Long countByUserIdAndYn(Long userId,Integer yn);


}
