package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.td.tsi.ecommerce.spring.domain.Product;
import br.edu.utfpr.td.tsi.ecommerce.spring.domain.ProductRepository;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.CartLineItem;

@RestController
@RequestMapping("/api/distributed/checkout")
public class StockController {

	private static final Logger log = LoggerFactory.getLogger(StockController.class);

	private final ProductRepository productRepository;

	public StockController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@PostMapping("/validate-stock")
	public ResponseEntity<?> validateStock(@RequestBody Map<String, CartLineItem> cart) {
		for (CartLineItem item : cart.values()) {
			Product product = loadProduct(item);
			int requested = item.getQty();
			Integer inStock = product.getStockQuantity();
			if (inStock == null || inStock < requested) {
				throw new IllegalArgumentException(
						"Insufficient stock for " + product.getName() + " (requested: " + requested + ").");
			}
			log.info("[Serviço de Estoque] Produto {} possui {} unidade(s), solicitado {}", product.getName(), inStock,
					requested);
		}
		return ResponseEntity.ok(Map.of("message", "Stock validated."));
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
}
