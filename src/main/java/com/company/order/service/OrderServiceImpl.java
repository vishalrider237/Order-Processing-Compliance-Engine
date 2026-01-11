package com.company.order.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.order.Repository.OrderRepository;
import com.company.order.exception.InvalidOrderException;
import com.company.order.exception.OrderNotFoundException;
import com.company.order.model.Order;
import com.company.order.model.OrderItem;
import com.company.order.model.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository repository;
	@Autowired
	private PricingService pricingService;
	@Autowired
	private ComplianceService complianceService;

	@Override
	public Order createOrder(Order order, boolean festival) {
		if (order == null) {
	        throw new InvalidOrderException("Order is null");
	    }
	    if (order.getItems() == null || order.getItems().isEmpty()) {
	        throw new InvalidOrderException("No items");
	    }

	    if (order.getCustomer() == null || !order.getCustomer().isActive()) {
	        throw new InvalidOrderException("Customer is inactive");
	    }
	    complianceService.validate(order);
		double total = order.getItems().stream().mapToDouble(OrderItem::getTotal).sum();

		order.setTotalAmount(pricingService.calculateFinalAmount(total, order.getCustomer().isPremium(), festival));

		
		order.setStatus(OrderStatus.CREATED);

		return repository.save(order);
	}

	@Override
	public Order cancelOrder(String orderId, String reason) {
		if (reason == null || reason.trim().isEmpty()) {
			throw new InvalidOrderException("Cancellation reason is mandatory");
		}

		Order order = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));

		if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new InvalidOrderException("Order already cancelled");
		}

		if (order.getStatus() != OrderStatus.CREATED) {
			throw new InvalidOrderException("Only CREATED orders can be cancelled");
		}

		order.setStatus(OrderStatus.CANCELLED);
		return repository.save(order);
	}

	@Override
	public Order OrderRetrieveByOrderId(String orderId) {
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new InvalidOrderException("Order ID cannot be null or empty");
		}

		return repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}

	@Override
	public List<Order> OrderRetriveByCustomerId(String customerId) {
		if (customerId == null || customerId.trim().isEmpty()) {
			return Collections.emptyList();
		}

		List<Order> orders = repository.findByCustomerId(customerId);
		return orders != null ? orders : Collections.emptyList();
	}

}
