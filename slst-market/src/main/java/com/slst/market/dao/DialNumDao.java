package com.slst.market.dao;

import com.slst.market.dao.model.DialNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface DialNumDao extends JpaRepository<DialNum, Long>,DialNumExtDao {

    /**
     * 新增主叫号码
     * @param dialNum
     * @return
     */
    DialNum save(DialNum dialNum);


    /**
     * 获得主叫号码
     * @param userId
     * @return
     */
    DialNum findByUserId(Long userId);


    @Modifying
    @Query("update DialNum d set d.mobile=:mobile,d.changer=:username,d.changeTime=:changeTime where d.userId=:userId")
    Integer modifyDialNum(@Param("userId") Long userId,@Param("mobile") String mobile,@Param("username") String userName,@Param("changeTime") Date curDate);





}
