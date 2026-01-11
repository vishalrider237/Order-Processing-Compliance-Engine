package com.company.order.service;

import java.util.List;

import com.company.order.model.Customer;
import com.company.order.model.Order;
import com.company.order.model.OrderItem;
import com.company.order.model.OrderStatus;

public class TestData {
	public static Order validOrder() {
        Customer customer = new Customer();
        customer.setActive(true);
        customer.setPremium(true);

        OrderItem item = new OrderItem();
        item.setPrice(500);
        item.setQuantity(2);

        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(List.of(item));

        return order;
    }

    public static Order orderWithoutItems() {
        Order order = new Order();
        order.setCustomer(new Customer());
        return order;
    }

    public static Order orderWithInactiveCustomer() {
        Customer customer = new Customer();
        customer.setActive(false);

        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(List.of(new OrderItem()));

        return order;
    }

}
