package com.example.orderservice.controller;

import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;

    private final OrderService orderService;

    @GetMapping("/health_check")
    public String status(){
        return String.format("orderService 포트는 %s",env.getProperty("local.server.port"));
    }



}
