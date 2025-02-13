package com.shopme.admin.setting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.setting.StateService;
import com.shopme.admin.setting.exception.CountryNotFoundException;
import com.shopme.admin.setting.exception.StateNotFoundException;
import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.State;

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

	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State savedState = stateService.save(state);

		return String.valueOf(savedState.getId());
	}

	@GetMapping("/states/delete/{id}")
	public void delete(@PathVariable Integer id) throws StateNotFoundException {
		stateService.delete(id);
	}
}
