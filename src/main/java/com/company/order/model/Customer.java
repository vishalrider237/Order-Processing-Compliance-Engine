package com.company.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Customer {
	@Column(name = "customer_id")
	private String id;
	private boolean active;
	private boolean premium;
}
