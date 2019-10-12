package com.netty.http.server.order.controller;

import com.netty.http.server.annotation.RequestMapping;
import com.netty.http.server.annotation.RestController;
import com.netty.http.server.common.response.GeneralResponse;
import com.netty.http.server.common.utils.RequestUtils;
import com.netty.http.server.common.utils.SpringContextHolder;
import com.netty.http.server.order.entity.Order;
import com.netty.http.server.order.service.OrderService;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(uri = "v1/order")
public class OrderController {

    private static final OrderService orderService = SpringContextHolder.getBean(OrderService.class);

    @RequestMapping(uri = "findAllOrder")
    public GeneralResponse findAllOrder(FullHttpRequest request) {
        final Map<String, String> parameterMap = RequestUtils.getParameterMap(request);
        final List<Order> orderList = orderService.findAllOrder(parameterMap);
        return GeneralResponse.success(orderList);
    }

    @RequestMapping(uri = "createOrder", method = RequestMethod.POST)
    public GeneralResponse createOrder(FullHttpRequest request) {
        final Order order = RequestUtils.postEntity(request, Order.class);
        return GeneralResponse.success(order);
    }
}
