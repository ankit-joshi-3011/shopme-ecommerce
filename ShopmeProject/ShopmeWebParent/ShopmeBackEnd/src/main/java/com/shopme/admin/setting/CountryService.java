package com.shopme.admin.setting;

import java.util.List;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;

@Service
public class CountryService {
	private CountryRepository countryRepository;

	public CountryService(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
}
