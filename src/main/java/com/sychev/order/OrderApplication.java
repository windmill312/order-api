package com.sychev.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication {

    //todo создать пользователей для бд, установить простые пароли
    //todo при создании заказа статус по умолчанию CREATED
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}