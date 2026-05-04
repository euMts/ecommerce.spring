package br.edu.utfpr.td.tsi.ecommerce.spring.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.CheckoutRequest;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.InvoiceResponse;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.OrderResponse;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.PaymentProcessingResponse;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.PurchaseFlowResult;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.ShippingResponse;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.TotalResponse;

@Service
public class PurchaseOrchestrationService {

	private static final Logger log = LoggerFactory.getLogger(PurchaseOrchestrationService.class);
	private final RestClient checkoutStepsClient;

	public PurchaseOrchestrationService(@Value("${server.port:8080}") int serverPort) {
		this.checkoutStepsClient = RestClient.create("http://localhost:" + serverPort + "/api/distributed/checkout");
	}

	public PurchaseFlowResult completeCheckout(CheckoutRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Request body is required.");
		}

		log.info("[Orquestrador] Iniciando orquestração distribuída da compra");
		callStep("/validate-input", request, Void.class, "validar dados do checkout");

		TotalResponse totalResponse = callStep("/calculate-total", request.getCart(), TotalResponse.class,
				"calcular total do carrinho");
		double total = totalResponse.getTotal();

		callStep("/validate-stock", request.getCart(), Void.class, "validar disponibilidade em estoque");

		OrderResponse orderResponse = callStep("/orders", Map.of("total", total), OrderResponse.class, "criar pedido");
		String orderId = orderResponse.getOrderId();

		PurchaseFlowResult result = new PurchaseFlowResult();
		result.setOrderId(orderId);
		result.setTotal(total);
		result.getCompletedSteps().add("Order created: " + orderId);

		callStep("/emails/order-confirmation", Map.of("orderId", orderId, "total", total, "delivery", request.getDelivery()),
				Void.class, "enviar e-mail de confirmação da compra");
		result.getCompletedSteps().add("Purchase confirmation email sent.");

		PaymentProcessingResponse payment = callStep("/payments",
				Map.of("orderId", orderId, "total", total, "payment", request.getPayment()),
				PaymentProcessingResponse.class, "processar pagamento");
		String paymentTxId = payment.getTransactionId();
		result.setPaymentResult("APPROVED transaction id: " + paymentTxId);
		result.getCompletedSteps().add("Payment processed successfully.");

		callStep("/emails/payment-result", Map.of("orderId", orderId, "transactionId", paymentTxId), Void.class,
				"enviar e-mail com resultado do pagamento");
		result.getCompletedSteps().add("Payment outcome email sent.");

		InvoiceResponse invoice = callStep("/invoices", Map.of("orderId", orderId, "cart", request.getCart()),
				InvoiceResponse.class, "gerar nota fiscal e baixar estoque");
		String invoiceNumber = invoice.getInvoiceNumber();
		result.setInvoiceNumber(invoiceNumber);
		result.getCompletedSteps().add("Invoice issued and stock updated.");

		callStep("/emails/invoice", Map.of("orderId", orderId, "invoiceNumber", invoiceNumber), Void.class,
				"enviar e-mail com a nota fiscal");
		result.getCompletedSteps().add("Invoice email sent.");

		ShippingResponse shipping = callStep("/shipments", Map.of("orderId", orderId, "delivery", request.getDelivery()),
				ShippingResponse.class, "disponibilizar produtos para entrega");
		String trackingCode = shipping.getTrackingCode();
		result.setTrackingCode(trackingCode);
		result.getCompletedSteps().add("Order handed off to carrier.");

		callStep("/emails/shipping", Map.of("orderId", orderId, "trackingCode", trackingCode), Void.class,
				"enviar e-mail com dados da entrega");
		result.getCompletedSteps().add("Shipping details email sent.");

		log.info("Checkout concluído para o pedido {} nota fiscal {} rastreio {}", orderId, invoiceNumber, trackingCode);
		return result;
	}

	private <T> T callStep(String path, Object body, Class<T> responseType, String description) {
		try {
			log.info("[Orquestrador] Chamando endpoint {} para {}", path, description);
			T response = checkoutStepsClient.post().uri(path).body(body).retrieve().body(responseType);
			log.info("[Orquestrador] Endpoint {} concluído: {}", path, description);
			return response;
		}
		catch (RestClientResponseException e) {
			String message = "Endpoint " + path + " falhou com HTTP " + e.getStatusCode() + ": "
					+ e.getResponseBodyAsString();
			log.error("[Orquestrador] {}", message);
			if (e.getStatusCode().is4xxClientError()) {
				throw new IllegalArgumentException(message);
			}
			throw new IllegalStateException(message, e);
		}
	}
}
