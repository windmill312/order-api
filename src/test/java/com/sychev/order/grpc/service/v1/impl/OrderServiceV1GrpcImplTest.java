package com.sychev.order.grpc.service.v1.impl;

import com.sychev.common.grpc.model.GPageable;
import com.sychev.common.grpc.model.GUuid;
import com.sychev.order.converter.ModelConverter;
import com.sychev.order.grpc.model.v1.*;
import com.sychev.order.model.OrderStatus;
import com.sychev.order.model.entity.OrderEntity;
import com.sychev.order.service.OrderService;
import io.grpc.stub.StreamObserver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceV1GrpcImplTest {

    private static final UUID ORDER_UID = UUID.fromString("517df602-4ffb-4e08-9626-2a0cf2db4849");
    private static final UUID CAFE_UID = UUID.fromString("123df602-4ffb-4e08-9626-2a0cf2db4849");
    private static final UUID CUSTOMER_UID = UUID.fromString("789df602-4ffb-4e08-9626-2a0cf2db4849");
    private static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
    private static final Double TOTAL_PRICE = 12.3;
    private static final Long CREATE_DTTM = 123456789L;
    private static final Long RECEIVE_DTTM = 123789456L;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderServiceV1GrpcImpl orderServiceV1Grpc;

    @Test
    public void getAllOrders() {

        Page<OrderEntity> serviceResponse = new PageImpl<>(Collections.singletonList(getMockObjOrderEntity()));

        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(serviceResponse);

        GGetAllOrdersRequest request = GGetAllOrdersRequest.newBuilder()
                .setPageable(GPageable.newBuilder().setPage(0).setSize(20).build())
                .build();

        StreamObserver<GGetAllOrdersResponse> observer = mock(StreamObserver.class);

        orderServiceV1Grpc.getAllOrders(request, observer);
        verify(observer, times(1)).onCompleted();

        ArgumentCaptor<GGetAllOrdersResponse> captor = ArgumentCaptor.forClass(GGetAllOrdersResponse.class);
        verify(observer, times(1)).onNext(captor.capture());

        GGetAllOrdersResponse response = captor.getValue();
        assertEquals(response.getOrdersList().size(), serviceResponse.getTotalElements());
        assertEquals(ModelConverter.convert(response.getOrdersList().get(0).getCafeUid()), serviceResponse.getContent().get(0).getCafeUid());
        assertEquals(ModelConverter.convert(response.getOrdersList().get(0).getCafeUid()), serviceResponse.getContent().get(0).getCafeUid());
        assertEquals(ModelConverter.convert(response.getOrdersList().get(0).getCustomerUid()), serviceResponse.getContent().get(0).getCustomerUid());
        assertEquals(response.getOrdersList().get(0).getCreateMills(), serviceResponse.getContent().get(0).getCreateDttm().toEpochMilli());
        assertEquals(response.getOrdersList().get(0).getReceiveMills(), serviceResponse.getContent().get(0).getReceiveDttm().toEpochMilli());
        assertEquals((Double)response.getOrdersList().get(0).getPrice(), serviceResponse.getContent().get(0).getTotalPrice());
        assertEquals(response.getOrdersList().get(0).getStatus().name(), serviceResponse.getContent().get(0).getStatus().name());

    }

    @Test
    public void getAllOrdersByCustomer() {

        List<OrderEntity> serviceResponse = Collections.singletonList(getMockObjOrderEntity());

        when(orderService.getAllOrdersByCustomer(CUSTOMER_UID)).thenReturn(serviceResponse);

        GGetAllOrdersByCustomerRequest request = GGetAllOrdersByCustomerRequest.newBuilder().setCustomerUid(ModelConverter.convert(CUSTOMER_UID)).build();

        StreamObserver<GGetAllOrdersByCustomerResponse> observer = mock(StreamObserver.class);

        orderServiceV1Grpc.getAllOrdersByCustomer(request, observer);
        verify(observer, times(1)).onCompleted();

        ArgumentCaptor<GGetAllOrdersByCustomerResponse> captor = ArgumentCaptor.forClass(GGetAllOrdersByCustomerResponse.class);
        verify(observer, times(1)).onNext(captor.capture());

        GGetAllOrdersByCustomerResponse response = captor.getValue();
        assertEquals(response.getOrdersList().size(), serviceResponse.size());
        assertEquals(ModelConverter.convert(response.getOrders(0).getCafeUid()), serviceResponse.get(0).getCafeUid());
        assertEquals(ModelConverter.convert(response.getOrders(0).getCustomerUid()), serviceResponse.get(0).getCustomerUid());
        assertEquals(response.getOrders(0).getCreateMills(), serviceResponse.get(0).getCreateDttm().toEpochMilli());
        assertEquals(response.getOrders(0).getReceiveMills(), serviceResponse.get(0).getReceiveDttm().toEpochMilli());
        assertEquals((Double)response.getOrders(0).getPrice(), serviceResponse.get(0).getTotalPrice());
        assertEquals(response.getOrders(0).getStatus().name(), serviceResponse.get(0).getStatus().name());

    }

    @Test
    public void getOrder() {

        OrderEntity serviceResponse = getMockObjOrderEntity();

        when(orderService.getOrderByUid(any(UUID.class))).thenReturn(serviceResponse);

        GGetOrderRequest request = GGetOrderRequest.newBuilder()
                .setOrderUid(GUuid.newBuilder().setUuid(ORDER_UID.toString()).build())
                .build();

        StreamObserver<GGetOrderResponse> observer = mock(StreamObserver.class);

        orderServiceV1Grpc.getOrder(request, observer);
        verify(observer, times(1)).onCompleted();

        ArgumentCaptor<GGetOrderResponse> captor = ArgumentCaptor.forClass(GGetOrderResponse.class);
        verify(observer, times(1)).onNext(captor.capture());

        GGetOrderResponse response = captor.getValue();
        assertEquals(ModelConverter.convert(response.getOrder().getOrderUid()), serviceResponse.getOrderUid());
        assertEquals(ModelConverter.convert(response.getOrder().getCafeUid()), serviceResponse.getCafeUid());
        assertEquals(ModelConverter.convert(response.getOrder().getCustomerUid()), serviceResponse.getCustomerUid());
        assertEquals(response.getOrder().getCreateMills(), serviceResponse.getCreateDttm().toEpochMilli());
        assertEquals(response.getOrder().getReceiveMills(), serviceResponse.getReceiveDttm().toEpochMilli());
        assertEquals((Double)response.getOrder().getPrice(), serviceResponse.getTotalPrice());
        assertEquals(response.getOrder().getStatus().name(), serviceResponse.getStatus().name());

    }

    @Test
    public void addOrder() {

        when(orderService.addOrder(any(OrderEntity.class))).thenReturn(ORDER_UID);

        GAddOrderRequest request = GAddOrderRequest.newBuilder()
                .setOrder(GOrderInfo.newBuilder()
                        .setReceiveMills(RECEIVE_DTTM)
                        .setCreateMills(CREATE_DTTM)
                        .setPrice(TOTAL_PRICE)
                        .setCustomerUid(ModelConverter.convert(CUSTOMER_UID))
                        .setCafeUid(ModelConverter.convert(CAFE_UID)))
                .build();

        StreamObserver<GAddOrderResponse> observer = mock(StreamObserver.class);

        orderServiceV1Grpc.addOrder(request, observer);
        verify(observer, times(1)).onCompleted();

        ArgumentCaptor<GAddOrderResponse> captor = ArgumentCaptor.forClass(GAddOrderResponse.class);
        verify(observer, times(1)).onNext(captor.capture());

        GAddOrderResponse response = captor.getValue();
        assertEquals(response.getOrderUid().getUuid(), ORDER_UID.toString());

    }

    private OrderEntity getMockObjOrderEntity() {
        return new OrderEntity()
                .setOrderUid(ORDER_UID)
                .setCafeUid(CAFE_UID)
                .setCustomerUid(CUSTOMER_UID)
                .setReceiveDttm(Instant.ofEpochMilli(RECEIVE_DTTM))
                .setCreateDttm(Instant.ofEpochMilli(CREATE_DTTM))
                .setTotalPrice(TOTAL_PRICE)
                .setStatus(ORDER_STATUS);
    }
}