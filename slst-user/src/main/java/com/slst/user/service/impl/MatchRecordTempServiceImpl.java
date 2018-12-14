package com.slst.user.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.Msg;
import com.slst.user.dao.MatchRecordTempDao;
import com.slst.user.dao.model.MatchRecordTemp;
import com.slst.user.dao.model.User;
import com.slst.user.service.MatchRecordTempService;
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
