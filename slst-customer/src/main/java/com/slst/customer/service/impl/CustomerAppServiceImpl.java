package com.slst.customer.service.impl;

import com.google.common.collect.Lists;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.customer.dao.CustomerAppDao;
import com.slst.customer.dao.model.CustomerApp;
import com.slst.customer.service.CustomerAppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("customerAppService")
public class CustomerAppServiceImpl extends BaseServiceImpl<CustomerApp , Long> implements CustomerAppService {

    @Resource
    private CustomerAppDao customerAppDao;

    @Override
    public List<CustomerApp> save(CustomerApp... customerApp) {
        return customerAppDao.saveAll(Lists.newArrayList(customerApp));
    }

    @Override
    public List<CustomerApp> findByCustomerIdAndYn(Long customerId, Integer yn) {
        return customerAppDao.findByCustomerIdAndYn(customerId , yn);
    }
}
