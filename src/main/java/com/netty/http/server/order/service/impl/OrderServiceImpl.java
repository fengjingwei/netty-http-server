package com.netty.http.server.order.service.impl;

import com.netty.http.server.order.entity.Order;
import com.netty.http.server.order.mapper.OrderMapper;
import com.netty.http.server.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> findAllOrder(Map<String, String> parameterMap) {
        return orderMapper.findAll(parameterMap.get("keyword"));
    }
}
