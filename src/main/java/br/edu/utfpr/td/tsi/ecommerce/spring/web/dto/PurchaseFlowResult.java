package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

import java.util.ArrayList;
import java.util.List;

public class PurchaseFlowResult {

	private String orderId;
	private String invoiceNumber;
	private String paymentResult;
	private String trackingCode;
	private Double total;
	private List<String> completedSteps = new ArrayList<>();

	public PurchaseFlowResult() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getPaymentResult() {
		return paymentResult;
	}

	public void setPaymentResult(String paymentResult) {
		this.paymentResult = paymentResult;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<String> getCompletedSteps() {
		return completedSteps;
	}

	public void setCompletedSteps(List<String> completedSteps) {
		this.completedSteps = completedSteps;
	}
}
