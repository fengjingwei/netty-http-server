package com.netty.http.server.order.controller;

import com.google.common.collect.Lists;
import com.netty.http.server.annotation.RequestMapping;
import com.netty.http.server.annotation.RequestParam;
import com.netty.http.server.annotation.RestController;
import com.netty.http.server.common.response.GeneralResponse;
import com.netty.http.server.common.utils.SpringContextHolder;
import com.netty.http.server.order.entity.Order;
import com.netty.http.server.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(uri = "v1/order")
public class OrderController {

    private static final OrderService orderService = SpringContextHolder.getBean(OrderService.class);

    private static List<Order> buildOrderList() {
        List<Order> data = Lists.newArrayList();
        data.add(new Order("1", 1, "1", new BigDecimal("99.98"), 1, "1"));
        data.add(new Order("2", 2, "2", new BigDecimal("199.99"), 2, "1"));
        data.add(new Order("3", 1, "1", new BigDecimal("99.88"), 1, "2"));
        return data;
    }

    @RequestMapping(uri = "findAll")
    public GeneralResponse<List<Order>> findAll() {
        return GeneralResponse.success(buildOrderList());
    }

    @RequestMapping(uri = "findByCondition")
    public GeneralResponse<List<Order>> findByCondition(@RequestParam(name = "orderNo") String orderNo,
                                                        @RequestParam(name = "status", required = false) Byte status,
                                                        @RequestParam(name = "userIds") String[] userIds) {
        List<Order> data = Lists.newArrayList();
        buildOrderList().stream()
                .filter(order -> StringUtils.equals(order.getOrderNo(), orderNo))
                .forEach(data::add);
        return GeneralResponse.success(data);
    }

    @RequestMapping(uri = "create", method = RequestMethod.POST)
    public GeneralResponse<Order> create(Order order) {
        return GeneralResponse.success(order);
    }

    @RequestMapping(uri = "update", method = RequestMethod.PUT)
    public GeneralResponse<Order> update(Order order) {
        return GeneralResponse.success(order);
    }

    @RequestMapping(uri = "delete", method = RequestMethod.DELETE)
    public GeneralResponse<String> delete(@RequestParam(name = "orderNo") String orderNo) {
        return GeneralResponse.success(orderNo);
    }
}
