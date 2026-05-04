package br.edu.utfpr.td.tsi.ecommerce.spring.web;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.CartLineItem;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.TotalResponse;

@RestController
@RequestMapping("/api/distributed/checkout")
public class CartCalculationController {

	private static final Logger log = LoggerFactory.getLogger(CartCalculationController.class);
	private static final NumberFormat BRL = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

	@PostMapping("/calculate-total")
	public TotalResponse calculateTotal(@RequestBody Map<String, CartLineItem> cart) {
		double total = 0;
		for (CartLineItem item : cart.values()) {
			if (item.getQty() == null || item.getQty() <= 0) {
				throw new IllegalArgumentException("Invalid quantity for product " + item.getId());
			}
			if (item.getPrice() == null) {
				throw new IllegalArgumentException("Missing price for product " + item.getId());
			}
			total += item.getPrice() * item.getQty();
		}
		log.info("[Serviço de Carrinho] Total do carrinho calculado: {}", BRL.format(total));
		return new TotalResponse(total);
	}
}
