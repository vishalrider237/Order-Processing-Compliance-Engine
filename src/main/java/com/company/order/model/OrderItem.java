package com.company.order.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OrderItem {
	private double price;
	private int quantity;

	public double getTotal() {
		return price * quantity;
	}
}
