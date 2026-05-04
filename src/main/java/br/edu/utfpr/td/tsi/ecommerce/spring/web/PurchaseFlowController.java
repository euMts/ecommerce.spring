package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.td.tsi.ecommerce.spring.service.PostalCodeLookupService;
import br.edu.utfpr.td.tsi.ecommerce.spring.service.PurchaseOrchestrationService;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.AddressSuggestion;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.CheckoutRequest;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.PostalCodeLookupRequest;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.PurchaseFlowResult;

@RestController
@RequestMapping("/api")
public class PurchaseFlowController {

	private final PostalCodeLookupService postalCodeLookupService;
	private final PurchaseOrchestrationService purchaseOrchestrationService;

	public PurchaseFlowController(PostalCodeLookupService postalCodeLookupService,
			PurchaseOrchestrationService purchaseOrchestrationService) {
		this.postalCodeLookupService = postalCodeLookupService;
		this.purchaseOrchestrationService = purchaseOrchestrationService;
	}

	@PostMapping("/addresses/postal-code")
	public ResponseEntity<?> lookupPostalCode(@RequestBody(required = false) PostalCodeLookupRequest body) {
		try {
			if (body == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Send JSON with field 'postalCode' (8 digits)."));
			}
			AddressSuggestion suggestion = postalCodeLookupService.suggest(body.getPostalCode());
			return ResponseEntity.ok(suggestion);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/checkout/confirm")
	public ResponseEntity<?> confirmCheckout(@RequestBody CheckoutRequest body) {
		try {
			PurchaseFlowResult result = purchaseOrchestrationService.completeCheckout(body);
			return ResponseEntity.ok(result);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(Map.of("error", e.getMessage()));
		}
	}
}
