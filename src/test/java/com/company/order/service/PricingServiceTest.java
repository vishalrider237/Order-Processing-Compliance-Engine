package com.company.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {
   private PricingService priceService;
   
   @BeforeEach
   void setup() {
	   priceService=new PricingServiceImpl();
   }
   @ParameterizedTest
   @CsvSource({
       "9000,false,false,10620",
       "10000,false,false,11210",
       "25000,true,true,23600"
   })
   @DisplayName("Discount & tax calculation")
   void calculateFinalAmount(double total,
                             boolean premium,
                             boolean festival,
                             double expected) {

       double result =
    		   priceService.calculateFinalAmount(total, premium, festival);
       // here 0.01 means tolerance in case of descimal calculation if expected and actual is difer ny 0.01 then can be consider as equal
       assertEquals(expected, result, 0.01,
           "Final amount calculation incorrect");
   }
}
