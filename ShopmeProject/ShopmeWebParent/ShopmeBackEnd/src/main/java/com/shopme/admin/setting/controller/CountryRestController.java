package com.shopme.admin.setting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.setting.CountryService;
import com.shopme.common.dto.CountryDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.exception.CountryNotFoundException;

@RestController
public class CountryRestController {
	private CountryService countryService;

	public CountryRestController(CountryService countryService) {
		this.countryService = countryService;
	}

	@GetMapping("/countries/list")
	public List<CountryDTO> listAllCountries() {
		return countryService.listAllCountries();
	}

	@PostMapping("/countries/save")
	public String save(@RequestBody Country country) {
		Country savedCountry = countryService.save(country);

		return String.valueOf(savedCountry.getId());
	}

	@DeleteMapping("/countries/delete/{id}")
	public void delete(@PathVariable Integer id) throws CountryNotFoundException {
		countryService.delete(id);
	}
}
