package com.company.order.service;

public interface PricingService {
	double calculateFinalAmount(double total, boolean isPremium, boolean festival);
}
