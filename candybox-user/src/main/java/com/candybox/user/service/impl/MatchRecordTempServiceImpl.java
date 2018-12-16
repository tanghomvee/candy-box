package com.candybox.user.service.impl;

import com.candybox.common.enums.YNEnum;
import com.candybox.common.service.impl.BaseServiceImpl;
import com.candybox.user.dao.MatchRecordTempDao;
import com.candybox.user.dao.model.MatchRecordTemp;
import com.candybox.user.dao.model.User;
import com.candybox.user.service.MatchRecordTempService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service("matchRecordTempService")
public class MatchRecordTempServiceImpl extends BaseServiceImpl<MatchRecordTemp,Long> implements MatchRecordTempService {

    @Resource
    private MatchRecordTempDao matchRecordTempDao;

    @Override
    public MatchRecordTemp save(MatchRecordTemp matchRecordTemp) {
        return matchRecordTempDao.save(matchRecordTemp);
    }

    @Override
    public MatchRecordTemp createMatchRecordTemp(Integer type, String rsp, User user) {

        if (null==user){
            return null;
        }

        MatchRecordTemp matchRecordTemp=new MatchRecordTemp();
        matchRecordTemp.setType(type);
        matchRecordTemp.setUserId(user.getId());
        matchRecordTemp.setRsp(rsp);
        matchRecordTemp.setYn(YNEnum.YES.getVal());
        matchRecordTemp.setCreator(user.getUserName());
        matchRecordTemp.setCreateTime(new Date());

        return this.save(matchRecordTemp);
    }

    @Override
    public Long countByUserIdAndYn(Long userId, Integer yn) {
        return matchRecordTempDao.countByUserIdAndYn(userId,yn);
    }
}
