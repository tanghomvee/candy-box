package com.slst.customer.service.impl;

import com.google.common.collect.Lists;
import com.slst.common.service.impl.BaseServiceImpl;
import com.slst.customer.dao.CustomerInterestDao;
import com.slst.customer.dao.model.CustomerInterest;
import com.slst.customer.service.CustomerInterestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("customerInterestService")
public class CustomerInterestServiceImpl extends BaseServiceImpl<CustomerInterest , Long> implements CustomerInterestService {

    @Resource
    private CustomerInterestDao customerInterestDao;

    @Override
    public List<CustomerInterest> save(CustomerInterest ... customerInterest) {
        return customerInterestDao.saveAll(Lists.newArrayList(customerInterest));
    }

    @Override
    public List<CustomerInterest> findByCustomerIdAndYn(Long customerId, Integer yn) {
        return customerInterestDao.findByCustomerIdAndYn(customerId , yn);
    }
}
