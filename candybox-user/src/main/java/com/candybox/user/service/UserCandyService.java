package com.candybox.user.service;

import com.candybox.common.enums.OperateKindEnum;
import com.candybox.common.service.BaseService;
import com.candybox.common.web.vo.Pager;
import com.candybox.user.dao.model.UserCandy;


public interface UserCandyService extends BaseService<UserCandy, Long> {

    int received(Long candyId, Long userId, Long amt) throws Exception;

    int reward(Long candyId, Long userId, Long amt , OperateKindEnum operateKindEnum);

    Pager listUserCandy(Long userId, Pager pager);
}
