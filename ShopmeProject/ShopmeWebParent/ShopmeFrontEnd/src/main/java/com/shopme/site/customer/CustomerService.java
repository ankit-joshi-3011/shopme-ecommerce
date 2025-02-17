package com.shopme.site.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.dto.CountryDTO;
import com.shopme.common.entity.Country;
import com.shopme.site.setting.CountryRepository;

@Service
public class CustomerService {
	private CountryRepository countryRepository;

	public CustomerService(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	public List<CountryDTO> listAllCountries() {
		List<Country> countries = countryRepository.findAllByOrderByNameAsc();

		List<CountryDTO> countryDtos = new ArrayList<>();

		for (Country country : countries) {
			countryDtos.add(new CountryDTO(country));
		}

		return countryDtos;
	}
}
