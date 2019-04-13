package com.github.windmill312.order.grpc.service.v1.impl;

import com.github.windmill312.customer.grpc.model.v1.*;
import com.github.windmill312.customer.grpc.service.v1.CustomerServiceV1Grpc;
import com.github.windmill312.order.converter.ModelConverter;
import com.github.windmill312.order.model.entity.CustomerEntity;
import com.github.windmill312.order.service.CustomerService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.UUID;
import java.util.stream.Collectors;

@GRpcService
public class CustomerServiceV1GrpcImpl extends CustomerServiceV1Grpc.CustomerServiceV1ImplBase {

    private final CustomerService customerService;

    @Autowired
    public CustomerServiceV1GrpcImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void getAllCustomers(
            GGetAllCustomersRequest request,
            StreamObserver<GGetAllCustomersResponse> responseObserver) {

        Page<CustomerEntity> Customers = customerService.getAllCustomers(ModelConverter.convert(request.getPageable()));

        responseObserver.onNext(GGetAllCustomersResponse.newBuilder()
                .setPage(ModelConverter.convert(Customers))
                .addAllCustomers(Customers.getContent()
                        .stream()
                        .map(ModelConverter::convert)
                        .collect(Collectors.toList()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomer(
            GGetCustomerRequest request,
            StreamObserver<GGetCustomerResponse> responseObserver) {

        CustomerEntity customer = customerService.getCustomerByUid(ModelConverter.convert(request.getCustomerUid()));

        responseObserver.onNext(GGetCustomerResponse.newBuilder()
                .setCustomer(ModelConverter.convert(customer))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void addCustomer(
            GAddCustomerRequest request,
            StreamObserver<GAddCustomerResponse> responseObserver) {

        UUID customerUid = customerService.addCustomer(ModelConverter.convert(request.getCustomer()));

        responseObserver.onNext(GAddCustomerResponse.newBuilder()
                .setCustomerUid(ModelConverter.convert(customerUid))
                .build());
        responseObserver.onCompleted();
    }
    
}
