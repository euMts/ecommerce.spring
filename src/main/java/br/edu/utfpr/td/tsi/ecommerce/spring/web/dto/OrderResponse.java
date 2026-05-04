package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class OrderResponse {

	private String orderId;
	private Double total;

	public OrderResponse() {
	}

	public OrderResponse(String orderId, Double total) {
		this.orderId = orderId;
		this.total = total;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
