package com.shopme.site.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.dto.CountryDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.setting.CountryRepository;

@Service
public class CustomerService {
	private CountryRepository countryRepository;
	private CustomerRepository customerRepository;

	public CustomerService(CountryRepository countryRepository, CustomerRepository customerRepository) {
		this.countryRepository = countryRepository;
		this.customerRepository = customerRepository;
	}

	public List<CountryDTO> listAllCountries() {
		List<Country> countries = countryRepository.findAllByOrderByNameAsc();

		List<CountryDTO> countryDtos = new ArrayList<>();

		for (Country country : countries) {
			countryDtos.add(new CountryDTO(country));
		}

		return countryDtos;
	}

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);

		return customer == null;
	}
}
