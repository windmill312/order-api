package com.sychev.order.service.impl;

import com.sychev.order.exception.NotFoundOrderException;
import com.sychev.order.model.entity.OrderEntity;
import com.sychev.order.repository.OrderRepository;
import com.sychev.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
    @Transactional
    public void removeOrder(UUID orderUid) {
        orderRepository.deleteByOrderUid(orderUid);
    }

    @Override
    @Transactional
    public void removeAllOrdersByCustomer(UUID customerUid) {
        orderRepository.deleteAllByCustomerUid(customerUid);
    }

}
