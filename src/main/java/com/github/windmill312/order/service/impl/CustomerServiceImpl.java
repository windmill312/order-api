package com.github.windmill312.order.service.impl;

import com.github.windmill312.order.exception.NotFoundCustomerException;
import com.github.windmill312.order.model.entity.CustomerEntity;
import com.github.windmill312.order.repository.CustomerRepository;
import com.github.windmill312.order.service.CustomerService;
import com.github.windmill312.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            OrderService orderService) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }

    @Override
    public Page<CustomerEntity> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public CustomerEntity getCustomerByUid(UUID customerUid) {
        return customerRepository.findByUuid(customerUid).orElseThrow(() -> {
            logger.info("Not found customer with uid={}", customerUid);
            return new NotFoundCustomerException("Not found customer with uid=" + customerUid);
        });
    }

    @Override
    public UUID addCustomer(CustomerEntity entity) {
        logger.debug("Add new customer with uid={} ", entity.getUuid());

        return customerRepository.save(entity).getUuid();
    }

    @Override
    public void removeCustomer(UUID customerUid) {
        logger.debug("Remove customer with uid={} ", customerUid);
        customerRepository.deleteByUuid(customerUid);

        logger.debug("Remove orders by customer with uid={} ", customerUid);
        orderService.removeAllOrdersByCustomer(customerUid);
    }
}
