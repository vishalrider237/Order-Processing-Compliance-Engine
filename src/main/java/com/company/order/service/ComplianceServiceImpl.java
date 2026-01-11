package com.company.order.service;

import org.springframework.stereotype.Service;

import com.company.order.exception.ComplianceViolationException;
import com.company.order.model.Order;

@Service
public class ComplianceServiceImpl implements ComplianceService{

	@Override
	public void validate(Order order) {
		if (!order.getCustomer().isActive()) {
            throw new ComplianceViolationException("Inactive customer");
        }
        if (order.getTotalAmount() > 500000) {
            throw new ComplianceViolationException("Amount exceeds limit");
        }
	}

}
