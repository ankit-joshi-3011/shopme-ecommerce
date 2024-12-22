package com.shopme.admin.category.exception;

@SuppressWarnings("serial")
public class CategoryNotFoundException extends Exception {
	public CategoryNotFoundException(String message) {
		super(message);
	}
}
