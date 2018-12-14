package com.slst.market.dao;

import com.slst.market.dao.model.SmsFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsFeeDao extends JpaRepository<SmsFee, Long> {

    /**
     * 新建并保存短信费率
     * @param smsFee
     * @return
     */
    SmsFee save(SmsFee smsFee);

    /**
     * 更新短信费率
     * @param smsFee
     * @return
     */
    SmsFee saveAndFlush(SmsFee smsFee);

    /**
     * 根据ID删除短信费率
     * @param Id
     */
    void deleteById(Long Id);

    /**
     * 根据venderId删除短信费率
     * @param venderId
     */
    void deleteByVenderId(Long venderId);

    /**
     * 根据venderID查看费率
     * @param venderId
     * @return
     */
    SmsFee findByVenderId(Long venderId);

}
