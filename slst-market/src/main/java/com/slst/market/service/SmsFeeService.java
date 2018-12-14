package com.slst.market.service;

import com.slst.common.service.BaseService;
import com.slst.common.web.vo.Msg;
import com.slst.common.web.vo.UserVO;
import com.slst.market.dao.model.SmsFee;

public interface SmsFeeService extends BaseService<SmsFee, Long> {

    /**
     * 新建短信费率
     * @param userVO
     * @param smsFee
     * @return
     */
    SmsFee createSmsFee(UserVO userVO, SmsFee smsFee);

    /**
     * 更新短信费率
     * @param userVO
     * @param smsFee
     * @return
     */
    SmsFee modifySmsFee(UserVO userVO, SmsFee smsFee);

    /**
     * 根据venderId查找费率
     * @param venderId
     * @return
     */
    SmsFee findSmsFeeByVenderId(Long venderId);

    /**
     * 根据Id查找短信费率
     * @param id
     * @return
     */
    SmsFee findSmsFeeById(Long id);

    /**
     * 根据id批量删除短信费率
     * @param ids
     * @return
     */
    Msg deleteSmsFeeById(String ids);

    /**
     * 根据venderIds批量删除短信费率
     * @param venderIds
     * @return
     */
    Msg deleteSmsFeeByvenderId(String venderIds);

}
