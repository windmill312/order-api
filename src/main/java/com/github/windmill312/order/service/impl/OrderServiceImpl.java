package com.github.windmill312.order.service.impl;

import com.github.windmill312.order.exception.InvalidReceiveTimeException;
import com.github.windmill312.order.exception.NotFoundCustomerException;
import com.github.windmill312.order.exception.NotFoundOrderException;
import com.github.windmill312.order.model.entity.OrderEntity;
import com.github.windmill312.order.repository.CustomerRepository;
import com.github.windmill312.order.repository.OrderRepository;
import com.github.windmill312.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<OrderEntity> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<OrderEntity> getAllOrdersByCustomer(UUID customerUid) {
        return orderRepository.findAllByCustomerUid(customerUid);
    }

    @Override
    public OrderEntity getOrderByUid(UUID orderUid) {
        return orderRepository.findByOrderUid(orderUid).orElseThrow(() -> {
            logger.info("Not found order with uid={}", orderUid);
            return new NotFoundOrderException("Not found order with uid=" + orderUid);
        });
    }

    @Override
    public UUID addOrder(OrderEntity entity) {
        logger.debug("Add new order with customer uid={} and cafe uid={}", entity.getCustomerUid(), entity.getCafeUid());

        if (entity.getCreateDttm().plus(5, ChronoUnit.MINUTES).isAfter(entity.getReceiveDttm()))
            throw new InvalidReceiveTimeException("Invalid receive time");

        customerRepository.findByExtId(entity.getCustomerUid()).orElseThrow(() -> {
            logger.info("Not found customer with uid: {}", entity.getCustomerUid());
            return new NotFoundCustomerException("Not found customer with uid:" + entity.getCustomerUid());
        });

        return orderRepository.save(entity).getOrderUid();
    }

    @Override
    public void updateOrder(OrderEntity entity) {
        OrderEntity order = orderRepository.findByOrderUid(entity.getOrderUid()).orElseThrow(() -> {
            logger.info("Not found order with uid={}", entity.getOrderUid());
            return new NotFoundOrderException("Not found order with uid=" + entity.getOrderUid());
        });
        logger.debug("Update order with uid={}", entity.getOrderUid());
        orderRepository.save(order.copy(entity));
    }

    @Override
    public void removeOrder(UUID orderUid) {
        orderRepository.deleteByOrderUid(orderUid);
    }

    @Override
    public void removeAllOrdersByCustomer(UUID customerUid) {
        orderRepository.deleteAllByCustomerUid(customerUid);
    }

}
