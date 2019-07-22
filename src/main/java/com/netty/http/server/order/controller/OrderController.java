package com.netty.http.server.order.controller;

import com.netty.http.server.annotation.RequestMapping;
import com.netty.http.server.common.response.GeneralResponse;
import com.netty.http.server.common.utils.RequestUtils;
import com.netty.http.server.order.entity.Order;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@RequestMapping(uri = "v1/order")
public class OrderController {

    @RequestMapping(uri = "findAllOrder")
    public GeneralResponse findAllOrder(FullHttpRequest request) {
        final Map<String, String> parameterMap = RequestUtils.getParameterMap(request);
        return GeneralResponse.success(parameterMap);
    }

    @RequestMapping(uri = "createOrder", method = RequestMethod.POST)
    public GeneralResponse createOrder(FullHttpRequest request) {
        final Order order = RequestUtils.getEntity(request, Order.class);
        return GeneralResponse.success(order);
    }
}
