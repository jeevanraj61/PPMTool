package io.ppmtool.code.exceptions;

public class UsernameDuplicateExceptionResponse {

	private String usernameDuplicate;

	public UsernameDuplicateExceptionResponse(String usernameDuplicate) {
		this.usernameDuplicate = usernameDuplicate;
	}

	public String getUsernameDuplicate() {
		return usernameDuplicate;
	}

	public void setUsernameDuplicate(String usernameDuplicate) {
		this.usernameDuplicate = usernameDuplicate;
	}
	
	
}
