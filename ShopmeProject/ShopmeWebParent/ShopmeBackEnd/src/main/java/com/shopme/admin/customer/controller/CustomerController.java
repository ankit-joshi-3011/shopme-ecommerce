package com.shopme.admin.customer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.customer.CustomerService;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {
	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/customers")
	public String listAllCustomers(Model model) {
		List<Customer> listCustomers = customerService.listAllCustomers();

		model.addAttribute("listCustomers", listCustomers);

		return "customers/customers";
	}
}
