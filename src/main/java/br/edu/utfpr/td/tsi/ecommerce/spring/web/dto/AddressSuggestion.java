package br.edu.utfpr.td.tsi.ecommerce.spring.web.dto;

public class AddressSuggestion {

	private String postalCode;
	private String street;
	private String neighborhood;
	private String city;
	private String state;

	public AddressSuggestion() {
	}

	public AddressSuggestion(String postalCode, String street, String neighborhood, String city, String state) {
		this.postalCode = postalCode;
		this.street = street;
		this.neighborhood = neighborhood;
		this.city = city;
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
