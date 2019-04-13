package com.github.windmill312.order.service;

import com.github.windmill312.order.model.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {

    Page<CustomerEntity> getAllCustomers(Pageable pageable);

    CustomerEntity getCustomerByUid(UUID customerUid);

    UUID addCustomer(CustomerEntity entity);

}
