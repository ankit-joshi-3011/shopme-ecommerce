package com.shopme.site.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	private CustomerRepository customerRepository;
	private TestEntityManager entityManager;

	@Autowired
	public CustomerRepositoryTests(CustomerRepository customerRepository, TestEntityManager entityManager) {
		this.customerRepository = customerRepository;
		this.entityManager = entityManager;
	}

	@Test
	public void testCreateCustomer() {
		Integer countryId = 106;
		Country india = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();

		customer.setEmail("abc@def.com");
		customer.setPassword("abc123");
		customer.setFirstName("ABC");
		customer.setLastName("DEF");
		customer.setPhoneNumber("123-456-7890");
		customer.setAddressLine1("Address 1");
		customer.setAddressLine2("Address 2");
		customer.setCity("City");
		customer.setState("State");
		customer.setVerificationCode("123456");
		customer.setPostalCode("123456");
		customer.setCreatedTime(new Date());
		customer.setCountry(india);

		Customer savedCustomer = customerRepository.save(customer);

		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testRetrieveCustomer() {
		Optional<Customer> savedCustomer = customerRepository.findById(1);

		assertThat(savedCustomer.isPresent());
	}

	@Test
	public void testUpdateCustomer() {
		Optional<Customer> optionalCustomer = customerRepository.findById(1);

		Customer customer = optionalCustomer.get();

		String newVerificationCode = "123457";
		String newAddressLine1 = "Address 3";

		customer.setVerificationCode(newVerificationCode);
		customer.setAddressLine1(newAddressLine1);

		Customer savedCustomer = customerRepository.save(customer);

		assertThat(savedCustomer.getVerificationCode()).isEqualTo(newVerificationCode);
		assertThat(savedCustomer.getAddressLine1()).isEqualTo(newAddressLine1);
	}

	@Test
	public void testDeleteCustomer() {
		Integer customerId = 1;

		customerRepository.deleteById(customerId);

		Optional<Customer> customer = customerRepository.findById(customerId);

		assertThat(customer.isEmpty());
	}

	@Test
	public void testFindByEmail() {
		Customer savedCustomer = customerRepository.findByEmail("abc@def.com");

		assertThat(savedCustomer).isNotNull();
	}

	@Test
	public void testFindByVerificationCode() {
		Customer savedCustomer = customerRepository.findByVerificationCode("123456");

		assertThat(savedCustomer).isNotNull();
	}

	@Test
	public void testEnable() {
		Integer customerId = 2;

		customerRepository.enable(customerId);

		Optional<Customer> savedCustomer = customerRepository.findById(customerId);

		assertThat(savedCustomer.get().isEnabled());
	}
}
