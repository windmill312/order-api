package com.github.windmill312.order.service.impl;

import com.github.windmill312.order.exception.NotFoundCustomerException;
import com.github.windmill312.order.exception.NotFoundOrderException;
import com.github.windmill312.order.model.entity.CustomerEntity;
import com.github.windmill312.order.repository.CustomerRepository;
import com.github.windmill312.order.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<CustomerEntity> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public CustomerEntity getCustomerByUid(UUID customerUid) {
        return customerRepository.findByExtId(customerUid).orElseThrow(() -> {
            logger.info("Not found customer with uid={}", customerUid);
            return new NotFoundCustomerException("Not found customer with uid=" + customerUid);
        });
    }

    @Override
    public UUID addCustomer(CustomerEntity entity) {
        logger.debug("Add new customer with uid={} ", entity.getExtId());

        return customerRepository.save(entity).getExtId();
    }

}
