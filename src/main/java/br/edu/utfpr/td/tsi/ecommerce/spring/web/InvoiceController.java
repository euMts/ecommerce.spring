package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.td.tsi.ecommerce.spring.domain.Product;
import br.edu.utfpr.td.tsi.ecommerce.spring.domain.ProductRepository;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.CartLineItem;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.InvoiceResponse;

@RestController
@RequestMapping("/api/distributed/checkout")
public class InvoiceController {

	private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

	private final ProductRepository productRepository;
	private final ObjectMapper objectMapper;

	public InvoiceController(ProductRepository productRepository, ObjectMapper objectMapper) {
		this.productRepository = productRepository;
		this.objectMapper = objectMapper;
	}

	@PostMapping("/invoices")
	public InvoiceResponse generateInvoiceAndReduceStock(@RequestBody Map<String, Object> body) {
		String orderId = readString(body, "orderId");
		Map<String, CartLineItem> cart = convertCart(body.get("cart"));
		String invoiceNumber = "INV-" + orderId.substring(0, 8).toUpperCase();
		for (CartLineItem item : cart.values()) {
			Product product = loadProduct(item);
			int newStock = product.getStockQuantity() - item.getQty();
			product.setStockQuantity(newStock);
			productRepository.save(product);
			log.info("[Serviço de Nota Fiscal] Produto {}  baixadas {} unidade(s)  restante {}", product.getName(),
					item.getQty(), newStock);
		}
		log.info("[Serviço de Nota Fiscal] {} emitida para o pedido {}", invoiceNumber, orderId);
		return new InvoiceResponse(invoiceNumber);
	}

	private Product loadProduct(CartLineItem item) {
		if (item.getId() == null || item.getId().isBlank()) {
			throw new IllegalArgumentException("Product id is required for each cart line.");
		}
		if (item.getQty() == null || item.getQty() <= 0) {
			throw new IllegalArgumentException("Invalid quantity for product " + item.getId());
		}
		return productRepository.findById(item.getId())
				.orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getId()));
	}

	private String readString(Map<String, Object> body, String field) {
		Object value = body.get(field);
		if (value == null) {
			throw new IllegalArgumentException("Missing field: " + field);
		}
		return value.toString();
	}

	private Map<String, CartLineItem> convertCart(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Missing cart.");
		}
		return objectMapper.convertValue(value,
				objectMapper.getTypeFactory().constructMapType(Map.class, String.class, CartLineItem.class));
	}
}
