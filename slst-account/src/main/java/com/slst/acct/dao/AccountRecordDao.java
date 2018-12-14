package com.slst.acct.dao;

import com.slst.acct.dao.model.AccountRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/6/7 14:58
 */
public interface AccountRecordDao extends JpaRepository<AccountRecord,Long>,AccountRecordDaoExt {

    /**
     * 新建交易记录
     * @param accountRecord
     * @return
     */
    AccountRecord save(AccountRecord accountRecord);

    /**
     * 根据id删除记录
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据门店ID删除账户记录
     * @param storeId
     */
    void deleteByStoreId(Long storeId);

    /**
     * 根据userId查找记录
     * @param userId
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据storeId查找记录
     * @param storeId
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByStoreId(Long storeId, Pageable pageable);

    /**
     * 根据userId和typea查找记录
     * @param userId
     * @param typea
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByUserIdAndTypea(Long userId, Integer typea, Pageable pageable);

    /**
     * 根据userId和typeb查找记录
     * @param userId
     * @param typeb
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByUserIdAndTypeb(Long userId, Integer typeb, Pageable pageable);

    /**
     * 根据storeId和typea查找记录
     * @param storeId
     * @param typea
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByStoreIdAndTypea(Long storeId, Integer typea, Pageable pageable);

    /**
     * 根据storeId和typeb查找记录
     * @param storeId
     * @param typeb
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByStoreIdAndTypeb(Long storeId, Integer typeb, Pageable pageable);

    /**
     * 根据userId,typea和typeb查找记录
     * @param userId
     * @param typea
     * @param typeb
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByUserIdAndTypeaAndTypeb(Long userId, Integer typea, Integer typeb, Pageable pageable);

    /**
     * 根据storeId,typea和typeb查找记录
     * @param storeId
     * @param typea
     * @param typeb
     * @param pageable
     * @return
     */
    Page<AccountRecord> findByStoreIdAndTypeaAndTypeb(Long storeId, Integer typea, Integer typeb, Pageable pageable);


    /**
     * 根据AcctId查询记录条数
     * @param storeId
     * @return
     */
    Long countByStoreId(Long storeId);


    /**
     * 根据userId，typea，和时间段，统计金额
     * @param userId
     * @param minDate
     * @param maxDate
     * @return
     */
    @Query(value = "SELECT DATE(a.createTime),SUM(a.amount) FROM t_account_record a WHERE a.userId =:userId AND a.typea=:typea AND a.createTime>=:minDate AND a.createTime<=:maxDate GROUP BY DATE(a.createTime)",
            nativeQuery = true)
    List<Object[]> sumAmountByUserIdAndTypeaAndDate(@Param("userId") long userId, @Param("typea") Integer typea, @Param("minDate") String minDate, @Param("maxDate") String maxDate);

}
