package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class TotalResponse {

	private Double total;

	public TotalResponse() {
	}

	public TotalResponse(Double total) {
		this.total = total;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
