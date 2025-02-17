package com.shopme.site.customer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.dto.CountryDTO;
import com.shopme.site.customer.CustomerService;

@Controller
public class CustomerController {
	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<CountryDTO> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);

		return "register/registration_form";
	}
}
