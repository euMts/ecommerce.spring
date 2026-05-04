package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.CheckoutRequest;

@RestController
@RequestMapping("/api/distributed/checkout")
public class CheckoutValidationController {

	private static final Logger log = LoggerFactory.getLogger(CheckoutValidationController.class);

	@PostMapping("/validate-input")
	public ResponseEntity<?> validateInput(@RequestBody CheckoutRequest request) {
		if (request.getCart() == null || request.getCart().isEmpty()) {
			throw new IllegalArgumentException("Cart cannot be empty.");
		}
		if (request.getPayment() == null) {
			throw new IllegalArgumentException("Payment details are required.");
		}
		if (request.getDelivery() == null) {
			throw new IllegalArgumentException("Delivery address is required.");
		}
		log.info("[Serviço de Validação] Dados do checkout aceitos com {} item(ns) no carrinho",
				request.getCart().size());
		return ResponseEntity.ok(Map.of("message", "Input validated."));
	}
}
