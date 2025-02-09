package com.shopme.admin.setting;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shopme.admin.setting.exception.CountryNotFoundException;
import com.shopme.common.entity.Country;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CountryService {
	private CountryRepository countryRepository;

	public CountryService(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}

	public Country save(Country country) {
		return countryRepository.save(country);
	}

	public void delete(Integer id) throws CountryNotFoundException {
		Optional<Country> country = countryRepository.findById(id);

		if (country.isEmpty()) {
			throw new CountryNotFoundException("Could not find any country with ID " + id);
		}

		countryRepository.deleteById(id);
	}
}
