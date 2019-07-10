package com.netty.http.server.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String orderNo;

    private Integer amount;

    private BigDecimal totalBalance;
}
