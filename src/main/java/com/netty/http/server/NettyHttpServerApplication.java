package com.netty.http.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.netty.http.server.order.mapper"})
public class NettyHttpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyHttpServerApplication.class, args);
    }
}