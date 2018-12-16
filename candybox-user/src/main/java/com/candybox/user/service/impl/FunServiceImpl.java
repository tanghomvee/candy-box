package com.candybox.user.service.impl;

import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.common.web.vo.Msg;
import com.candybox.user.dao.FunDao;
import com.candybox.user.dao.model.Fun;
import com.candybox.user.service.FunService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("funService")
public class FunServiceImpl extends BaseServiceImpl<Fun,Long> implements FunService {

    @Resource
    private FunDao funDao;

    @Override
    public Fun createFun(Fun fun) {
        return null;
    }

    @Override
    public Fun modifyFun(Fun fun) {
        return null;
    }

    @Override
    public Integer modifyMenuIdByIds(Long menuId, String ids) {
        return null;
    }

    @Override
    public Msg deleteByIds(String ids) {
        return null;
    }

    @Override
    public List<Fun> findByMenuId(Long menuId) {
        return null;
    }

    @Override
    public List<Fun> findByMenuIdAndIds(Long menuId, List<Long> ids) {
        return null;
    }

    @Override
    public List<Fun> findByMenuIdAndIds(Long menuId, String ids) {
        return null;
    }

    @Override
    public Long countByMenuId(Long menuId) {
        return funDao.countByMenuId(menuId);
    }
}
