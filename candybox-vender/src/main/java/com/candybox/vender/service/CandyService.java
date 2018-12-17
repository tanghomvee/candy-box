package com.candybox.vender.service;

import com.candybox.common.service.BaseService;
import com.candybox.common.web.vo.Pager;
import com.candybox.vender.dao.model.Candy;


public interface CandyService extends BaseService<Candy, Long> {


    Pager listPage(Pager pager);
}
