package com.sychev.order.converter;

import com.sychev.common.grpc.model.GPage;
import com.sychev.common.grpc.model.GPageable;
import com.sychev.common.grpc.model.GUuid;
import com.sychev.order.grpc.model.v1.GOrderInfo;
import com.sychev.order.model.OrderStatus;
import com.sychev.order.model.entity.OrderEntity;
import com.sychev.order.model.entity.OrderProducts;
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
                .setCreateMills(entity.getCreateDttm().toEpochMilli())
                .setReceiveMills(entity.getReceiveDttm().toEpochMilli())
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

    private static GOrderInfo.OrderStatus convert(OrderStatus status) {
        return GOrderInfo.OrderStatus.valueOf(status.name());
    }

    private static OrderStatus convert(GOrderInfo.OrderStatus status) {
        return OrderStatus.valueOf(status.name());
    }

    public static OrderEntity convert(GOrderInfo entity) {
        return new OrderEntity()
                .setCustomerUid(convert(entity.getCustomerUid()))
                .setCafeUid(convert(entity.getCafeUid()))
                .setTotalPrice(entity.getPrice())
                .setStatus(convert(entity.getStatus()))
                .setProducts(entity.getProductsList().stream()
                        .map(ModelConverter::convert)
                        .collect(Collectors.toSet())
                )
                .setCreateDttm(Instant.ofEpochMilli(entity.getCreateMills()))
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
}
