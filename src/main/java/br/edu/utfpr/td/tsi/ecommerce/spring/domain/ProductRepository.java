package br.edu.utfpr.td.tsi.ecommerce.spring.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
