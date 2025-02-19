package com.shopme.common.exception;

@SuppressWarnings("serial")
public class CountryNotFoundException extends Exception {
	public CountryNotFoundException(String message) {
		super(message);
	}
}
