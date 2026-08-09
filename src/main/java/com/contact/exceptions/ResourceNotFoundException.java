package com.contact.exceptions;

public class ResourceNotFoundException  extends RuntimeException{

	public ResourceNotFoundException() {
		super("resource not found");
	}

	public ResourceNotFoundException(String msg) {
		super(msg);
	}

}
