package com.slst.user.service.impl;

import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.user.dao.IndustryDao;
import com.slst.user.dao.model.Industry;
import com.slst.user.service.IndustryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/4 12:28
 */
@Service("industryService")
public class IndustryServiceImpl extends BaseServiceImpl<Industry,Long> implements IndustryService {

    @Resource
    private IndustryDao industryDao;

    @Override
    public List<Industry> getAllIndustry() {
        return industryDao.findAll();
    }
}
