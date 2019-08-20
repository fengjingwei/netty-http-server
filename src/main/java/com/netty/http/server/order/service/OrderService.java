package com.netty.http.server.order.service;

import com.netty.http.server.order.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> findAllOrder(final Map<String, String> parameterMap);
}
