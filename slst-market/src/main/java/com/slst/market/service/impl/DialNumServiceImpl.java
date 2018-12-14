package com.slst.market.service.impl;

import com.slst.common.enums.YNEnum;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.DialNumDao;
import com.slst.market.dao.model.DialNum;
import com.slst.market.service.DialNumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service("dialNumService")
public class DialNumServiceImpl extends BaseServiceImpl<DialNum,Long> implements DialNumService {

    @Resource
    private DialNumDao dialNumDao;

    @Override
    public DialNum save(DialNum dialNum) {
        return dialNumDao.save(dialNum);
    }

    @Transactional
    @Override
    public Integer modifyDialNum(UserVO curUser, String mobile) {
        String rtnMobile= getDialNumByUserId(curUser);

        if (StringUtils.isEmpty(rtnMobile)){
            DialNum dialNum=new DialNum();
            dialNum.setUserId(curUser.getId());
            dialNum.setMobile(mobile);
            dialNum.setYn(YNEnum.YES.getVal());
            dialNum.setCreator(curUser.getUserName());
            dialNum.setChangeTime(new Date());

            DialNum rtn= save(dialNum);

            if(null!= rtn){
                return 1;
            }
        }

        return dialNumDao.modifyDialNum(curUser.getId(),mobile,curUser.getUserName(),new Date());
    }

    @Override
    public String getDialNumByUserId(UserVO curUser) {


        DialNum dialNum=dialNumDao.findByUserId(curUser.getId());

        if(null!= dialNum){
           return dialNum.getMobile();
        }

        return null;
    }
}
