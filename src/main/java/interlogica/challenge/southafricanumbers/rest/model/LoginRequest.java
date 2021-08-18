package interlogica.challenge.southafricanumbers.rest.model;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	
	@NotBlank(message = "Missing username")
	private String username;
	
	@NotBlank(message = "Missing password")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginRequest [username=")
		    	.append(username)
		    	.append(", password=")
		    	.append("*******")
		    	.append("]");
		return builder.toString();
	}
	
	
	
	

}
