package com.contact.exceptions;


public class NoContactsFoundException extends RuntimeException {

	public NoContactsFoundException() {
		super("no contacts found!");
	}


}
