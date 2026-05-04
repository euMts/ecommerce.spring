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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.PaymentInfo;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.PaymentProcessingResponse;

@RestController
@RequestMapping("/api/distributed/checkout")
public class PaymentController {

	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
	private static final NumberFormat BRL = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

	private final ObjectMapper objectMapper;

	public PaymentController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@PostMapping("/payments")
	public PaymentProcessingResponse processPayment(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		double total = readDouble(body, "total");
		PaymentInfo payment = convert(body.get("payment"), PaymentInfo.class);
		String lastFour = payment.getCardNumber() == null || payment.getCardNumber().length() < 4 ? "****"
				: "****" + payment.getCardNumber().substring(Math.max(0, payment.getCardNumber().length() - 4));
		String transactionId = UUID.randomUUID().toString().substring(0, 8);
		log.info("[Serviço de Pagamento] Pedido {}  valor {}  cartão {}  titular {}", orderId, BRL.format(total), lastFour,
				payment.getCardHolderName());
		return new PaymentProcessingResponse(transactionId, "APPROVED");
	}

	private String readString(Map<String, Object> body, String field) {
		Object value = body.get(field);
		if (value == null) {
			throw new IllegalArgumentException("Missing field: " + field);
		}
		return value.toString();
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

	private <T> T convert(Object value, Class<T> type) {
		if (value == null) {
			throw new IllegalArgumentException("Missing object for type: " + type.getSimpleName());
		}
		return objectMapper.convertValue(value, type);
	}
}
