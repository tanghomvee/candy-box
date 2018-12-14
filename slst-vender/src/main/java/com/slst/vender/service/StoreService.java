package com.slst.vender.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.vender.dao.model.Store;
import org.springframework.data.domain.Page;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface StoreService extends BaseService <Store, Long>{

    /**
     * 创建门店信息
     * @param userVO
     * @param store
     * @return
     */
    Store createStore(UserVO userVO, Store store);

    Msg deleteStore(UserVO cuser, Long storeId);

    /**
     * 根据id查找门店
     * @param id
     * @return
     */
    Store findStoreById(Long id);

    /**
     * 根据venderId查找门店
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    Page<Store> findStoreByVenderId(Long venderId,String storeName, int pageNum, int pageSize, String sortKey, String order);

    /**
     * 修改门店信息
     * @param userVO
     * @param store
     * @return
     */
    Store updateStore(UserVO userVO, Store store);

    /**
     * 删除门店信息
     * @param storeId
     * @return -1：删除失败，此门店下还有绑定的设备；1：删除成功
     */
    Long deleteStore(Long storeId);

    Long countByVenderId(Long venderId);

}
