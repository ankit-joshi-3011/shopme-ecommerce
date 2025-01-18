package com.shopme.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The page you requested is out of bounds")
public class PageOutOfBoundsException extends RuntimeException {
}
