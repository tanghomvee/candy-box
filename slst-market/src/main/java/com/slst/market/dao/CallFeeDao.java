package com.slst.market.dao;

import com.slst.market.dao.model.CallFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/14 14:07
 */
public interface CallFeeDao extends JpaRepository<CallFee,Long>,CallFeeExtDao {

    /**
     * 保存商家通话月租费和单价
     * @param callFee
     * @return
     */
    CallFee save(CallFee callFee);

    CallFee findByVenderId(Long venderId);

    /**
     * 根据是否启用的值找出CallFee列表
     * @param yn
     * @return
     */
    List<CallFee> findByYn(Integer yn);

    @Modifying
    @Query("update CallFee c set c.yn=:yn where c.venderId=:venderId")
    Integer modifyYn(@Param("yn") Integer yn,@Param("venderId") Long venderId);
}
