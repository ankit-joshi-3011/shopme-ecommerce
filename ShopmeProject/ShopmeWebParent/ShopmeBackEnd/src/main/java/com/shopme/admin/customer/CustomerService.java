package com.shopme.admin.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.CountryRepository;
import com.shopme.common.dto.CountryDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;

	private CustomerRepository customerRepository;
	private CountryRepository countryRepository;
	private PasswordEncoder passwordEncoder;

	public CustomerService(CustomerRepository customerRepository, CountryRepository countryRepository, PasswordEncoder passwordEncoder) {
		this.customerRepository = customerRepository;
		this.countryRepository = countryRepository;
		this.passwordEncoder = passwordEncoder;
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

	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepository.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws CustomerNotFoundException {
		Long countById = customerRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}

		customerRepository.deleteById(id);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}
	}

	public boolean isEmailUnique(Integer id, String email) {
		Customer customer = customerRepository.findByEmail(email);

		return customer == null || customer.getId() == id;
	}

	public List<CountryDTO> listAllCountries() {
		List<Country> countries = countryRepository.findAllByOrderByNameAsc();

		List<CountryDTO> countryDtos = new ArrayList<>();

		for (Country country : countries) {
			countryDtos.add(new CountryDTO(country));
		}

		return countryDtos;
	}

	public Customer save(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);

		return customerRepository.save(customer);
	}
}
