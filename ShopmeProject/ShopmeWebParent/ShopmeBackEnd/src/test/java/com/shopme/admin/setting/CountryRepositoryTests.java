package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {
	private CountryRepository countryRepository;

	@Autowired
	public CountryRepositoryTests(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@Test
	public void testCreateCountry() {
		Country country = countryRepository.save(new Country("India", "IN", new HashSet<State>()));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}

	@Test
	public void testListCountries() {
		List<Country> listCountries = countryRepository.findAllByOrderByNameAsc();
		listCountries.forEach(country -> System.out.println(country.getName() + " " + country.getCode()));

		assertThat(listCountries.size()).isGreaterThan(0);
	}

	@Test
	public void testUpdateCountry() {
		Integer id = 1;
		String name = "Republic of India";

		Country country = countryRepository.findById(id).get();
		country.setName(name);

		Country updatedCountry = countryRepository.save(country);

		assertThat(updatedCountry.getName()).isEqualTo(name);
	}

	@Test
	public void testDeleteCountry() {
		Integer id = 1;
		countryRepository.deleteById(id);

		Optional<Country> country = countryRepository.findById(id);

		assertThat(country.isEmpty());
	}
}
