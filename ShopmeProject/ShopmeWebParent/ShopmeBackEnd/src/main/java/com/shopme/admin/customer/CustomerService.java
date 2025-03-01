package com.shopme.admin.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;

@Service
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;

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

	public Page<Customer> listByPage(int pageNumber, String sortField, String sortDirection, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNumber - 1, CUSTOMERS_PER_PAGE, sort);

		if (keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}

		return customerRepository.findAll(pageable);
	}
}
