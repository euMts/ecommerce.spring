package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = { CheckoutValidationController.class, CartCalculationController.class,
		StockController.class, OrderController.class, EmailController.class, PaymentController.class,
		InvoiceController.class, ShippingController.class })
public class DistributedCheckoutExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
	}
}
