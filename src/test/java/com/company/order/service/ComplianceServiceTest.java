package com.company.order.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.order.exception.ComplianceViolationException;
import com.company.order.model.Customer;
import com.company.order.model.Order;

@ExtendWith(MockitoExtension.class)
public class ComplianceServiceTest {
	private ComplianceService Complianceservice;

	@BeforeEach
	void setup() {
		Complianceservice = new ComplianceServiceImpl();
	}
	@Test
	@DisplayName("Inactive Customer Fail")
    void inactiveCustomer_shouldFail() {
        Order order = new Order();
        Customer c = new Customer();
        c.setActive(false);
        order.setCustomer(c);
        order.setTotalAmount(1000);

        assertThrows(ComplianceViolationException.class,
            () -> Complianceservice.validate(order));
    }
}
