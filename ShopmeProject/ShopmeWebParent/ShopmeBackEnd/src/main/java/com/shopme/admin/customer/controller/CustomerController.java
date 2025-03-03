package com.shopme.admin.customer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.customer.CustomerService;
import com.shopme.admin.customer.export.CustomerCsvExporter;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.PageOutOfBoundsException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CustomerController {
	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/customers")
	public String listAllCustomers(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}

	@GetMapping("/customers/page/{pageNumber}")
	public String listByPage(@PathVariable int pageNumber, Model model, @Param("sortField") String sortField,
		@Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		if (pageNumber <= 0) {
			throw new PageOutOfBoundsException();
		}

		Page<Customer> page = customerService.listByPage(pageNumber, sortField, sortDir, keyword);
		int totalPages = page.getTotalPages();

		if (pageNumber > totalPages) {
			throw new PageOutOfBoundsException();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		List<Customer> pageCustomers = page.getContent();

		long startCount = (pageNumber - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("listCustomers", pageCustomers);

		return "customers/customers";
	}

	@GetMapping("/customers/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Customer> listCustomers = customerService.listAllCustomers();

		CustomerCsvExporter exporter = new CustomerCsvExporter();

		exporter.export(listCustomers, response);
	}

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable Integer id, @PathVariable boolean status,
		RedirectAttributes attributes) {
		customerService.updateCustomerEnabledStatus(id, status);
		attributes.addFlashAttribute("message", "The customer ID " + id + " has been " + (status ? "enabled" : "disabled"));

		return "redirect:/customers";
	}
}
