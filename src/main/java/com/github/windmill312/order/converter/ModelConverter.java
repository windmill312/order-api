package com.github.windmill312.order.converter;

import com.github.windmill312.common.grpc.model.GPage;
import com.github.windmill312.common.grpc.model.GPageable;
import com.github.windmill312.common.grpc.model.GUuid;
import com.github.windmill312.customer.grpc.model.v1.GCustomerInfo;
import com.github.windmill312.order.grpc.model.v1.GOrderInfo;
import com.github.windmill312.order.grpc.model.v1.GOrderStatus;
import com.github.windmill312.order.model.OrderStatus;
import com.github.windmill312.order.model.entity.CustomerEntity;
import com.github.windmill312.order.model.entity.OrderEntity;
import com.github.windmill312.order.model.entity.OrderProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModelConverter {

    public static GPage convert(Page page) {
        return GPage.newBuilder()
                .setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalElements(page.getTotalElements())
                .build();
    }

    public static Pageable convert(GPageable pageable) {
        return PageRequest.of(pageable.getPage(), pageable.getSize());
    }

    public static GUuid convert(UUID uuid) {
        return GUuid.newBuilder()
                .setUuid(String.valueOf(uuid))
                .build();
    }

    public static GOrderInfo convert(OrderEntity entity) {
        return GOrderInfo.newBuilder()
                .setCafeUid(convert(entity.getCafeUid()))
                .setCustomerUid(convert(entity.getCustomerUid()))
                .setOrderUid(convert(entity.getOrderUid()))
                .setReceiveMills(entity.getReceiveDttm().toEpochMilli())
                .setCreateMills(entity.getCreateDttm().toEpochMilli())
                .setPrice(entity.getTotalPrice())
                .setStatus(convert(entity.getStatus()))
                .addAllProducts(entity.getProducts().stream()
                        .map(ModelConverter::convert)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private static GOrderInfo.OrderProduct convert(OrderProducts products) {
        return GOrderInfo.OrderProduct.newBuilder()
                .setProductUid(convert(products.getProductUid()))
                .setQuantity(products.getQuantity())
                .build();
    }

    private static GOrderStatus convert(OrderStatus status) {
        return GOrderStatus.valueOf(status.name());
    }

    public static OrderStatus convert(GOrderStatus status) {
        return OrderStatus.valueOf(status.name());
    }

    public static OrderEntity convert(GOrderInfo entity) {
        return new OrderEntity()
                .setCustomerUid(convert(entity.getCustomerUid()))
                .setCafeUid(convert(entity.getCafeUid()))
                .setTotalPrice(entity.getPrice())
                .setStatus(OrderStatus.CREATED)
                .setProducts(entity.getProductsList().stream()
                        .map(ModelConverter::convert)
                        .collect(Collectors.toSet())
                )
                .setCreateDttm(Instant.now())
                .setReceiveDttm(Instant.ofEpochMilli(entity.getReceiveMills()));
    }

    private static OrderProducts convert(GOrderInfo.OrderProduct product) {
        return new OrderProducts(
                convert(product.getProductUid()),
                product.getQuantity());
    }

    public static UUID convert(GUuid guuid) {
        return UUID.fromString(guuid.getUuid());
    }

    public static GCustomerInfo convert(CustomerEntity customerEntity) {
        return GCustomerInfo.newBuilder()
                .setBirthDate(customerEntity.getBirthDate().toEpochMilli())
                .setExtId(convert(customerEntity.getUuid()))
                .setName(customerEntity.getName())
                .build();
    }

    public static CustomerEntity convert(GCustomerInfo customer) {
        return new CustomerEntity()
                .setName(customer.getName())
                .setBirthDate(Instant.ofEpochMilli(customer.getBirthDate()))
                .setUuid(convert(customer.getExtId()));

    }
}
