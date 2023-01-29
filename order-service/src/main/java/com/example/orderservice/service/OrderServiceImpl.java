package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;


    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        return null;
    }

    @Override
    public Iterable<OrderEntity> getOrderByUserId(String userId) {
        return null;
    }
}
