package com.candybox.user.dao;

import com.candybox.user.dao.model.UserCandyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-16 10:50
 */
public interface UserCandyRecordDao extends JpaRepository<UserCandyRecord, Long> , UserCandyRecordDaoExt {


}
