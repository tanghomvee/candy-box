package com.candybox.user.service;

import com.candybox.common.service.BaseService;
import com.candybox.user.dao.model.Industry;

import java.util.List;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/4 12:27
 */
public interface IndustryService extends BaseService<Industry,Long> {

    List<Industry> getAllIndustry();

}
