package com.slst.vender.dao;

import com.slst.vender.dao.model.Store;
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
public interface StoreDao extends JpaRepository<Store, Long> {

    /**
     * 保存门店信息
     * @param store
     * @return
     */
    Store save(Store store);

    /**
     * 更新门店信息（保存并即时刷新）
     * @param store
     * @return
     */
    Store saveAndFlush(Store store);

    /**
     * 根据venderId查找门店
     * @param venderId
     * @param pageable
     * @return
     */
    Page<Store> findByVenderId(Long venderId, Pageable pageable);

    Page<Store> findByVenderIdAndStoreNameContaining(Long venderId,String storeName, Pageable pageable);

    /**
     * 根据id删除门店
     * @param id
     */
    void deleteById(Long id);

    Long countByVenderId(Long venderId);
}
