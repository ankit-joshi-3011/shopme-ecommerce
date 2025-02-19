package com.shopme.site.setting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.dto.StateDTO;
import com.shopme.common.exception.CountryNotFoundException;
import com.shopme.site.setting.StateService;

@RestController
public class StateRestController {
	private StateService stateService;

	public StateRestController(StateService stateService) {
		this.stateService = stateService;
	}

	@GetMapping("/states/list/{id}")
	public List<StateDTO> listAllStates(@PathVariable("id") Integer countryId) throws CountryNotFoundException {
		return stateService.listAllStates(countryId);
	}
}
