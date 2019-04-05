package com.sychev.order.grpc.service.v1.impl;

import com.sychev.common.grpc.model.Empty;
import com.sychev.order.converter.ModelConverter;
import com.sychev.order.grpc.model.v1.*;
import com.sychev.order.grpc.service.v1.OrderServiceV1Grpc;
import com.sychev.order.model.entity.OrderEntity;
import com.sychev.order.service.OrderService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@GRpcService
public class OrderServiceV1GrpcImpl extends OrderServiceV1Grpc.OrderServiceV1ImplBase {

    private final OrderService orderService;

    @Autowired
    public OrderServiceV1GrpcImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void getAllOrders(
            GGetAllOrdersRequest request,
            StreamObserver<GGetAllOrdersResponse> responseObserver) {

        Page<OrderEntity> orders = orderService.getAllOrders(ModelConverter.convert(request.getPageable()));

        responseObserver.onNext(GGetAllOrdersResponse.newBuilder()
                .setPage(ModelConverter.convert(orders))
                .addAllOrders(orders.getContent()
                        .stream()
                        .map(ModelConverter::convert)
                        .collect(Collectors.toList()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllOrdersByCustomer(
            GGetAllOrdersByCustomerRequest request,
            StreamObserver<GGetAllOrdersByCustomerResponse> responseObserver) {

        List<OrderEntity> orders = orderService.getAllOrdersByCustomer(ModelConverter.convert(request.getCustomerUid()));

        responseObserver.onNext(GGetAllOrdersByCustomerResponse.newBuilder()
                .addAllOrders(orders
                        .stream()
                        .map(ModelConverter::convert)
                        .collect(Collectors.toList()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getOrder(
            GGetOrderRequest request,
            StreamObserver<GGetOrderResponse> responseObserver) {

        OrderEntity order = orderService.getOrderByUid(ModelConverter.convert(request.getOrderUid()));

        responseObserver.onNext(GGetOrderResponse.newBuilder()
                .setOrder(ModelConverter.convert(order))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void addOrder(
            GAddOrderRequest request,
            StreamObserver<GAddOrderResponse> responseObserver) {

        UUID cafeUid = orderService.addOrder(ModelConverter.convert(request.getOrder()));

        responseObserver.onNext(GAddOrderResponse.newBuilder()
                .setOrderUid(ModelConverter.convert(cafeUid))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateOrder(
            GUpdateOrderRequest request,
            StreamObserver<Empty> responseObserver) {

        orderService.updateOrder(
                ModelConverter.convert(request.getOrder())
                        .setOrderUid(ModelConverter.convert(request.getOrder().getOrderUid())));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeOrder(
            GRemoveOrderRequest request,
            StreamObserver<Empty> responseObserver) {

        orderService.removeOrder(ModelConverter.convert(request.getOrderUid()));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeAllOrdersByCustomer(
            GRemoveAllOrdersByCustomerRequest request,
            StreamObserver<Empty> responseObserver
    ) {
        orderService.removeAllOrdersByCustomer(ModelConverter.convert(request.getCustomerUid()));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
