package com.sychev.order.repository;

import com.sychev.order.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findByOrderUid(UUID orderUid);

    List<OrderEntity> findAllByCustomerUid(UUID customerUid);

    void deleteByOrderUid(UUID orderUid);

    void deleteAllByCustomerUid(UUID customerUid);
}
