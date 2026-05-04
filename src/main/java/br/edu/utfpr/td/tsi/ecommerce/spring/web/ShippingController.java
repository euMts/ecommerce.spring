package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.DeliveryAddress;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.ShippingResponse;

@RestController
@RequestMapping("/api/distributed/checkout")
public class ShippingController {

	private static final Logger log = LoggerFactory.getLogger(ShippingController.class);

	private final ObjectMapper objectMapper;

	public ShippingController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@PostMapping("/shipments")
	public ShippingResponse handOffToCarrier(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		DeliveryAddress delivery = convert(body.get("delivery"), DeliveryAddress.class);
		String code = "BR" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
		log.info("[Serviço de Entrega] Pedido {} pronto para entrega CEP {} código {}", orderId,
				delivery.getPostalCode(), code);
		return new ShippingResponse(code);
	}

	private String readString(Map<String, Object> body, String field) {
		Object value = body.get(field);
		if (value == null) {
			throw new IllegalArgumentException("Missing field: " + field);
		}
		return value.toString();
	}

	private <T> T convert(Object value, Class<T> type) {
		if (value == null) {
			throw new IllegalArgumentException("Missing object for type: " + type.getSimpleName());
		}
		return objectMapper.convertValue(value, type);
	}
}
