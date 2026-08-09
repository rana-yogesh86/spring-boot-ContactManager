package com.contact.dto;

import com.contact.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message="can't be empty")
	@Email(message = "invalid email !")
	private String userName;

	@NotBlank(message = "please ,enter password")
	private String password;


	private UserRole loginType;

	public LoginRequest() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getLoginType() {
		return loginType;
	}

	public void setLoginType(UserRole loginType) {
		this.loginType = loginType;
	}




}
