package com.company.order.service;

import java.util.List;

import com.company.order.model.Order;

public interface OrderService {
    Order createOrder(Order order,boolean festival);
    Order cancelOrder(String orderId,String reason);
    Order OrderRetrieveByOrderId(String orderId);
    List<Order> OrderRetriveByCustomerId(String customerId);
}
