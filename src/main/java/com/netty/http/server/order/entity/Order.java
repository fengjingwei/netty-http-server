package com.netty.http.server.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 付款金额
     */
    private BigDecimal totalAmount;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 购买人
     */
    private String userId;
}
