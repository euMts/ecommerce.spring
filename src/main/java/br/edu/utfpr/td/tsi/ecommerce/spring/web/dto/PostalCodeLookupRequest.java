package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class PostalCodeLookupRequest {

	private String postalCode;

	public PostalCodeLookupRequest() {
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
