package com.shopme.site.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.dto.CountryDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.setting.CountryRepository;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {
	private CountryRepository countryRepository;
	private CustomerRepository customerRepository;
	private PasswordEncoder passwordEncoder;

	public CustomerService(CountryRepository countryRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
		this.countryRepository = countryRepository;
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
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

	public void registerCustomer(Customer customer) {
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setVerificationCode(RandomString.make(64));

		customerRepository.save(customer);
	}

	public boolean verifyCustomer(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		}

		customerRepository.enable(customer.getId());
		return true;
	}
}
