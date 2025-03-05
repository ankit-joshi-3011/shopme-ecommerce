package com.shopme.admin.customer.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.customer.CustomerService;

@RestController
public class CustomerRestController {
	private CustomerService customerService;

	public CustomerRestController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@Param("email") String email) {
		return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
