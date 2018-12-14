package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsTpl;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
public interface SmsTplService extends BaseService <SmsTpl, Long>{

    /**
     * 查看短信模板审核状态
     * @param thirdPartyId
     * @return
     */
    Map<String,Object> findSmsTemplateStatus(String thirdPartyId);

    /**
     * 新建短信模板
     * @param curUser
     * @param smsTpl
     * @return
     */
    SmsTpl createSmsTpl(UserVO curUser, SmsTpl smsTpl);

    /**
     * 修改短信模板
     * @param curUser
     * @param smsTpl
     * @return
     */
    SmsTpl modifySmsTpl(UserVO curUser, SmsTpl smsTpl);

    /**
     * 根据Id，批量删除短信模板
     * @param ids
     * @return
     */
    Msg deleteSmsTpl(String ids);

    /**
     * 根据ID查找短信模板
     * @param id
     * @return
     */
    SmsTpl findSmsTplById(Long id);

    /**
     * 根据userId分页查找短信模板
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsTpl> findSmsTplByUserId(Long userId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据venderId分页查找短信模板
     * @param venderId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsTpl> findSmsTplByVenderId(Long venderId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据storeId分页查找短信模板
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsTpl> findSmsTplByStoreId(Long storeId, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据venderId和titleLike模糊分页查询
     * @param venderId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsTpl> findSmsTplByVenderIdAndTitle(Long venderId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据storeId和titleLike模糊分页查询
     * @param storeId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsTpl> findSmsTplByStoreIdAndTitle(Long storeId, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey);

    /**
     * 根据标题和状态分页查询短信模板
     * @param storeId
     * @param titleLike
     * @param pageNum
     * @param pageSize
     * @param sortKey 排序关键字。默认createTime
     * @param orderKey 升序（ASC）/降序（DESC）。默认降序
     * @return
     */
    Page<SmsTpl> findSmsTplByStateAndTitle(Long venderId,Long storeId,Integer state, String titleLike, int pageNum, int pageSize, String sortKey, String orderKey);

}
