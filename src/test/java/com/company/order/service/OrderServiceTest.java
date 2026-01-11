package com.company.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.order.Repository.OrderRepository;
import com.company.order.exception.InvalidOrderException;
import com.company.order.exception.OrderNotFoundException;
import com.company.order.model.Order;
import com.company.order.model.OrderStatus;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Order Service Unit Tests")
public class OrderServiceTest {
	@Mock
	OrderRepository repository;
	@Mock
	PricingService pricingService;
	@Mock
	ComplianceService complianceService;
	@InjectMocks
	OrderServiceImpl service;

	Order order;

	@BeforeEach
	void setUp() {
		order = TestData.validOrder();
	}

	@Test
	@org.junit.jupiter.api.Order(1)
	@DisplayName("Create order successfully when all inputs are valid")
	void createOrder_success() {

		when(pricingService.calculateFinalAmount(anyDouble(), anyBoolean(), anyBoolean())).thenReturn(1000.0);

		when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Order result = service.createOrder(order, false);

		assertEquals(OrderStatus.CREATED, result.getStatus(), "Order status should be CREATED");

		verify(pricingService).calculateFinalAmount(anyDouble(), eq(true), eq(false));

		verify(complianceService).validate(order);
		verify(repository).save(order);
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	@DisplayName("Create order should fail when no items are present")
	void createOrder_noItems_shouldFail() {

		Order order = TestData.orderWithoutItems();

		InvalidOrderException exception = assertThrows(InvalidOrderException.class,
				() -> service.createOrder(order, false), "Order without items must fail");

		assertEquals("No items", exception.getMessage(), "Correct exception message should be thrown");
	}

	@ParameterizedTest(name = "Total={0}, Premium={1}, Festival={2}")
	@org.junit.jupiter.api.Order(3)
	@DisplayName("Discount & tax calculation using CSV source")
	@CsvSource({ "9000,false,false,10620", "10000,false,false,11210", "25000,true,true,23600" })
	void calculateFinalAmount_csvSource(double total, boolean premium, boolean festival, double expected) {

		PricingService pricingService = new PricingServiceImpl();

		double result = pricingService.calculateFinalAmount(total, premium, festival);

		assertEquals(expected, result, 0.01, "Final amount calculation should match expected value");
	}

	@ParameterizedTest
	@org.junit.jupiter.api.Order(4)
	@MethodSource("invalidOrderProvider")
	@DisplayName("Create order should fail for invalid orders")
	void createOrder_invalidOrders_shouldFail(Order invalidOrder) {

		InvalidOrderException ex =
		        assertThrows(InvalidOrderException.class,
		            () -> service.createOrder(invalidOrder, false));

		    assertNotNull(ex.getMessage(),
		        "Exception message should be present");
	}

	static Stream<Order> invalidOrderProvider() {
		return Stream.of(TestData.orderWithoutItems(), TestData.orderWithInactiveCustomer());
	}

	@Test
	@DisplayName("Cancel order successfully when in CREATED state")
	void cancelOrder_success() {

		Order order = new Order();
		order.setStatus(OrderStatus.CREATED);

		when(repository.findById("1")).thenReturn(Optional.of(order));

		when(repository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

		Order result = service.cancelOrder("1", "Customer request");

		assertEquals(OrderStatus.CANCELLED, result.getStatus());
	}

	@Test
	void cancelOrder_whenOrderNotFound_shouldThrowException() {

		when(repository.findById("1")).thenReturn(Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> service.cancelOrder("1", "Reason"));
	}

	@Test
	void cancelOrder_whenAlreadyCancelled_shouldFail() {

		Order order = new Order();
		order.setStatus(OrderStatus.CANCELLED);

		when(repository.findById("1")).thenReturn(Optional.of(order));

		assertThrows(InvalidOrderException.class, () -> service.cancelOrder("1", "Reason"));
	}

	@Test
	void cancelOrder_whenReasonMissing_shouldFail() {

		assertThrows(InvalidOrderException.class, () -> service.cancelOrder("1", ""));
	}

	@Test
	@DisplayName("Fetch by Order ID")
	void getOrderById_success() {

		Order order = new Order();

		when(repository.findById("1")).thenReturn(Optional.of(order));

		Order result = service.OrderRetrieveByOrderId("1");

		assertNotNull(result);
	}

	@Test
	void getOrderById_whenNotFound_shouldThrowException() {

		when(repository.findById("1")).thenReturn(Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> service.OrderRetrieveByOrderId("1"));
	}

	@Test
	void getOrdersByCustomerId_whenNull_shouldReturnEmptyList() {

		List<Order> orders = service.OrderRetriveByCustomerId(null);

		assertTrue(orders.isEmpty());
	}

	@Test
	@DisplayName("Fetch by Customer ID")
	void getOrdersByCustomerId_success() {

		when(repository.findByCustomerId("C1")).thenReturn(List.of(new Order()));

		List<Order> orders = service.OrderRetriveByCustomerId("C1");

		assertEquals(1, orders.size());
	}

}
