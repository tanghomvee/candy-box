package com.slst.vender.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.utils.PageableUtil;
import com.slst.common.utils.StringUtils;
import com.slst.common.web.vo.UserVO;
import com.slst.vender.dao.VenderDao;
import com.slst.vender.dao.model.Vender;
import com.slst.vender.service.VenderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 17:13
 */
@Service("venderService")
public class VenderServiceImpl extends BaseServiceImpl<Vender, Long> implements VenderService {

    @Resource
    private VenderDao venderDao;

    /**
     * 商家注册
     *
     * @param vender
     * @return
     */
    @Override
    public Vender createVender(Vender vender) {
        return venderDao.save(vender);
    }

    /**
     * 修改商家基本信息
     *
     * @param vender
     * @return
     */
    @Override
    public Vender updateVender(Vender vender) {
        return venderDao.saveAndFlush(vender);
    }

    /**
     * 根据id删除商家数据
     *
     * @param id
     */
    @Override
    public void deleteVenderById(Long id) {
        venderDao.deleteById(id);
    }


    /**
     * 根据id查找商家信息
     *
     * @param id
     * @return
     */
    @Override
    public Vender findVenderById(Long id) {

        Optional<Vender> optional = venderDao.findById(id);

        return null != optional && optional.isPresent() ? optional.get() : null;


    }

    /**
     * 根据userId查找商家
     *
     * @param userId
     * @return
     */
    @Override
    public Vender findVenderByUserId(Long userId) {
        return venderDao.findByUserId(userId);
    }

    /**
     * 查找此代理商(agentId)的旗下的商家
     *
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @param sortKey  按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order    升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    public Page<Vender> findVenderByAgentId(Long agentId, String companyName, int pageNum, int pageSize, String sortKey, String order) {

        if (StringUtils.isNullOrEmpty(sortKey)) {
            sortKey = "createTime";
        }

        if (StringUtils.isNullOrEmpty(order)) {
            order = "desc";
        }

        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);

        if (StringUtils.isNullOrEmpty(companyName)) {
            return venderDao.findByAgentId(agentId, pageable);
        } else {
            return venderDao.findByAgentIdAndCompanyNameContaining(agentId, companyName, pageable);
        }

    }

    /**
     * 查找指定代理商员工（agentEmpId != 0）旗下的商家
     *
     * @param agentEmpId 不能为0。
     * @param pageNum
     * @param pageSize
     * @param sortKey    按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order      升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<Vender> findVenderByAgentEmpId(Long agentEmpId, String companyName, int pageNum, int pageSize, String sortKey, String order) {

        if (StringUtils.isNullOrEmpty(sortKey)) {
            sortKey = "createTime";
        }

        if (StringUtils.isNullOrEmpty(order)) {
            order = "desc";
        }

        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);
        if (StringUtils.isNullOrEmpty(companyName)) {
            return venderDao.findByAgentEmpId(agentEmpId, pageable);
        } else {
            return venderDao.findByAgentEmpIdAndCompanyNameContaining(agentEmpId, companyName, pageable);
        }

    }

    /**
     * 查找由此代理商(agentId)直接分配（agentEmpId == 0）的商家
     *
     * @param agentId
     * @param pageNum
     * @param pageSize
     * @param sortKey  按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order    升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    @Override
    public Page<Vender> findDirectVenderByAgentId(Long agentId, int pageNum, int pageSize, String sortKey, String order) {

        if (StringUtils.isNullOrEmpty(sortKey)) {
            sortKey = "createTime";
        }

        if (StringUtils.isNullOrEmpty(order)) {
            order = "desc";
        }

        Pageable pageable = PageableUtil.getPageable(pageNum, pageSize, sortKey, order);

        return venderDao.findByAgentIdAndAgentEmpId(agentId, 0L, pageable);
    }

    /**
     * 计算指定代理商员工绑定的商家数
     *
     * @param agentEmpId
     * @return
     */
    @Override
    public Long countVenderOfAgentEmp(Long agentEmpId) {
        return venderDao.countByAgentEmpId(agentEmpId);
    }

    @Transactional
    @Override
    public Integer modifyCompanyName(UserVO curUser, String newName, Long venderId) {
        return venderDao.modifyCompanyName(newName, venderId, curUser.getUserName(), new Date());
    }


}
