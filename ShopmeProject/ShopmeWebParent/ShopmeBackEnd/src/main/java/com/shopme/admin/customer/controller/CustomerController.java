package com.shopme.admin.customer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.customer.CustomerService;
import com.shopme.admin.customer.export.CustomerCsvExporter;
import com.shopme.common.entity.Customer;

import jakarta.servlet.http.HttpServletResponse;

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

	@GetMapping("/customers/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Customer> listCustomers = customerService.listAllCustomers();

		CustomerCsvExporter exporter = new CustomerCsvExporter();

		exporter.export(listCustomers, response);
	}
}
