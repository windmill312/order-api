package com.github.windmill312.order.repository;

import com.github.windmill312.order.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

}
