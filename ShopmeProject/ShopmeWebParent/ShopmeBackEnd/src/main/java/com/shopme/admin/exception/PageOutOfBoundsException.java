package com.shopme.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The page you requested is out of bounds")
public class PageOutOfBoundsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3783725811438398319L;
}
