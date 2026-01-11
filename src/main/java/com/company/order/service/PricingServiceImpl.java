package com.company.order.service;

import org.springframework.stereotype.Service;

@Service
public class PricingServiceImpl implements PricingService {

	@Override
	public double calculateFinalAmount(double total, boolean premium, boolean festival) {
		double discount = 0;

		if (total >= 25000) {
			discount = 0.10;
		} else if (total >= 10000) {
			discount = 0.05;
		}
		if (premium) {
			discount += 0.05;
		}
		if (festival) {
			discount += 0.05;
		}

		discount = Math.min(discount, 0.25);

		double discountedAmount = total - (total * discount);
		double gst = discountedAmount * 0.18;

		return Math.round((discountedAmount + gst) * 100.0) / 100.0;
	}

}
