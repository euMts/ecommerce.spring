package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class ShippingResponse {

	private String trackingCode;

	public ShippingResponse() {
	}

	public ShippingResponse(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}
}
