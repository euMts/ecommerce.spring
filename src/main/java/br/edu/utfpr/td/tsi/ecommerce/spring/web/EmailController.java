package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.DeliveryAddress;

@RestController
@RequestMapping("/api/distributed/checkout/emails")
public class EmailController {

	private static final Logger log = LoggerFactory.getLogger(EmailController.class);
	private static final NumberFormat BRL = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

	private final ObjectMapper objectMapper;

	public EmailController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@PostMapping("/order-confirmation")
	public ResponseEntity<?> sendOrderConfirmationEmail(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		double total = readDouble(body, "total");
		DeliveryAddress delivery = convert(body.get("delivery"), DeliveryAddress.class);
		log.info("[Serviço de E-mail] Confirmação da compra {}  total {}  entrega em {}, {}", orderId, BRL.format(total),
				delivery.getCity(), delivery.getState());
		return ResponseEntity.ok(Map.of("message", "Order confirmation email sent."));
	}

	@PostMapping("/payment-result")
	public ResponseEntity<?> sendPaymentResultEmail(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		String transactionId = readString(body, "transactionId");
		log.info("[Serviço de E-mail] Resultado do pagamento do pedido {}  transação {}", orderId, transactionId);
		return ResponseEntity.ok(Map.of("message", "Payment result email sent."));
	}

	@PostMapping("/invoice")
	public ResponseEntity<?> sendInvoiceEmail(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		String invoiceNumber = readString(body, "invoiceNumber");
		log.info("[Serviço de E-mail] Nota fiscal {} anexada ao pedido {}", invoiceNumber, orderId);
		return ResponseEntity.ok(Map.of("message", "Invoice email sent."));
	}

	@PostMapping("/shipping")
	public ResponseEntity<?> sendShippingEmail(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		String trackingCode = readString(body, "trackingCode");
		log.info("[Serviço de E-mail] Dados de entrega do pedido {}  rastreio {}", orderId, trackingCode);
		return ResponseEntity.ok(Map.of("message", "Shipping email sent."));
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
