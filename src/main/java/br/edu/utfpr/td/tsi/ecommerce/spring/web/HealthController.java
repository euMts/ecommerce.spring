package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/")
	public HealthResponse health() {
		return new HealthResponse(Instant.now().toString(), "UP", "ecommerce-spring");
	}

	public record HealthResponse(String timestamp, String status, String application) {
	}
}
