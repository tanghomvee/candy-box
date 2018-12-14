package com.slst.market.dao;

import com.slst.market.dao.model.SmsTpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:16
 */
public interface SmsTplDao extends JpaRepository<SmsTpl, Long>,SmsTplExtDao {

    /**
     * 新建并保存短信模板
     * @param smsTpl
     * @return
     */
    SmsTpl save(SmsTpl smsTpl);

    /**
     * 修改并更新短信模板
     * @param smsTpl
     * @return
     */
    SmsTpl saveAndFlush(SmsTpl smsTpl);

    /**
     * 根据Id删除短信模板
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据userId分页查找短信模板
     * @param userId
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据venderId分页查找短信模板
     * @param venderId
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByVenderId(Long venderId, Pageable pageable);


    /**
     * 根据storeId分页查找短信模板
     * @param storeId
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByStoreId(Long storeId, Pageable pageable);

    /**
     * 根据venderId和titleLike模糊分页查询
     * @param venderId
     * @param titleLike
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByVenderIdAndTitleContaining(Long venderId, String titleLike, Pageable pageable);

    /**
     * 根据storeId和titleLike模糊分页查询
     * @param storeId
     * @param titleLike
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByStoreIdAndTitleContaining(Long storeId, String titleLike, Pageable pageable);


    /**
     * 根据venderId分页查找短信模板
     * @param venderId
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByVenderIdAndState(Long venderId,Integer state, Pageable pageable);


    /**
     * 根据storeId分页查找短信模板
     * @param storeId
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByStoreIdAndState(Long storeId,Integer state, Pageable pageable);

    /**
     * 根据venderId和titleLike模糊分页查询
     * @param venderId
     * @param titleLike
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByVenderIdAndStateAndTitleContaining(Long venderId,Integer state, String titleLike, Pageable pageable);

    /**
     * 根据storeId和titleLike模糊分页查询
     * @param storeId
     * @param titleLike
     * @param pageable
     * @return
     */
    Page<SmsTpl> findByStoreIdAndStateAndTitleContaining(Long storeId,Integer state, String titleLike, Pageable pageable);

}
