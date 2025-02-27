package com.shopme.admin.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;

@Service
public class CustomerService {
	private CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public List<Customer> listAllCustomers() {
		Iterable<Customer> listCustomers = customerRepository.findAll();

		List<Customer> returnedCustomers = new ArrayList<>();

		for (Customer customer : listCustomers) {
			returnedCustomers.add(customer);
		}

		return returnedCustomers;
	}
}
