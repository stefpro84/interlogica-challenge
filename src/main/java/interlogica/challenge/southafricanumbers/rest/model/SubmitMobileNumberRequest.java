package interlogica.challenge.southafricanumbers.rest.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SubmitMobileNumberRequest {
	
	@NotBlank(message = "Missing/Empty mobile number")
	@Size(message = "mobile number length out of range [1, 32]", min = 1, max = 32)
	private String mobileNumber;

	public String getmobileNumber() {
		return mobileNumber;
	}

	public void setmobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	
	
}
