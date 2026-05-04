package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class CheckoutRequest {

	private Map<String, CartLineItem> cart = new LinkedHashMap<>();
	private PaymentInfo payment;
	private DeliveryAddress delivery;

	public CheckoutRequest() {
	}

	public Map<String, CartLineItem> getCart() {
		return cart;
	}

	public void setCart(Map<String, CartLineItem> cart) {
		this.cart = cart != null ? cart : new LinkedHashMap<>();
	}

	public PaymentInfo getPayment() {
		return payment;
	}

	public void setPayment(PaymentInfo payment) {
		this.payment = payment;
	}

	public DeliveryAddress getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryAddress delivery) {
		this.delivery = delivery;
	}
}
