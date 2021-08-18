package interlogica.challenge.southafricanumbers.rest.model;

import javax.validation.constraints.NotBlank;

public class CheckMobileNumberRequest {
	
	@NotBlank(message = "Missing/Empty 'number'")
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
}
