package com.contact.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{

	public EmailAlreadyExistsException() {
		super("email already exists!");
	}

	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}

}
