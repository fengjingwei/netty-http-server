package com.netty.http.server.order.mapper;

import com.netty.http.server.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM t_order_0 WHERE order_no LIKE CONCAT('%', #{keyword}, '%') ORDER BY create_time DESC")
    List<Order> findAll(@Param("keyword") String keyword);
}
