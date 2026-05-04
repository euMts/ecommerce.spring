package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.td.tsi.ecommerce.spring.domain.Product;
import br.edu.utfpr.td.tsi.ecommerce.spring.domain.ProductRepository;

@RestController
@RequestMapping("/api")
public class CatalogController {

	private final ProductRepository productRepository;

	public CatalogController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping("/products")
	public List<Product> listProducts() {
		return productRepository.findAll();
	}
}
