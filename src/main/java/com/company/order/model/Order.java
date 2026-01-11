package com.company.order.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
	@Id
	private String id=UUID.randomUUID().toString();;
	@Embedded
    private Customer customer;
	 @ElementCollection
    private List<OrderItem> items;
    private double totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
