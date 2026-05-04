package br.edu.utfpr.td.tsi.ecommerce.spring.config;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.edu.utfpr.td.tsi.ecommerce.spring.domain.Product;
import br.edu.utfpr.td.tsi.ecommerce.spring.domain.ProductRepository;

@Component
public class CatalogDataSeed implements ApplicationRunner {

	private final ProductRepository productRepository;

	public CatalogDataSeed(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (productRepository.count() > 0) {
			return;
		}
		productRepository.saveAll(sampleProducts());
	}

	private static List<Product> sampleProducts() {
		return List.of(
				new Product(UUID.randomUUID().toString(), "Camiseta Básica", 49.90, 120,
						"/img/camiseta.webp"),
				new Product(UUID.randomUUID().toString(), "Caneca de Cerâmica", 39.00, 45,
						"/img/caneca.webp"),
				new Product(UUID.randomUUID().toString(), "Fone Bluetooth", 199.99, 12,
						"/img/headphones.webp"),
				new Product(UUID.randomUUID().toString(), "Notebook", 2400.00, 0,
						"/img/notebook.webp"));
	}
}
