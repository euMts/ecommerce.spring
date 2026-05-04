package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class InvoiceResponse {

	private String invoiceNumber;

	public InvoiceResponse() {
	}

	public InvoiceResponse(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
}
