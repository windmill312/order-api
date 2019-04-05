package com.sychev.order.service;

import com.sychev.order.model.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Page<OrderEntity> getAllOrders(Pageable pageable);

    List<OrderEntity> getAllOrdersByCustomer(UUID customerUid);

    OrderEntity getOrderByUid(UUID orderUid);

    UUID addOrder(OrderEntity entity);

    void updateOrder(OrderEntity entity);

    void removeOrder(UUID orderUid);

    void removeAllOrdersByCustomer(UUID customerUid);

}
