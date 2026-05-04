package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.OrderResponse;

@RestController
@RequestMapping("/api/distributed/checkout")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	private static final NumberFormat BRL = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

	@PostMapping("/orders")
	public OrderResponse createOrder(@RequestBody Map<String, Object> body) {
		double total = readDouble(body, "total");
		String orderId = UUID.randomUUID().toString();
		log.info("[Serviço de Pedido] Pedido {} criado com total {}", orderId, BRL.format(total));
		return new OrderResponse(orderId, total);
	}

	private double readDouble(Map<String, Object> body, String field) {
		Object value = body.get(field);
		if (value instanceof Number number) {
			return number.doubleValue();
		}
		if (value != null) {
			return Double.parseDouble(value.toString());
		}
		throw new IllegalArgumentException("Missing field: " + field);
	}
}
