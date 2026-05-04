package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class PaymentProcessingResponse {

	private String transactionId;
	private String status;

	public PaymentProcessingResponse() {
	}

	public PaymentProcessingResponse(String transactionId, String status) {
		this.transactionId = transactionId;
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
