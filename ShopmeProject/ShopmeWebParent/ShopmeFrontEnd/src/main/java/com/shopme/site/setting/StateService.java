package com.shopme.site.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.exception.CountryNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StateService {
	private CountryRepository countryRepository;
	private StateRepository stateRepository;

	public StateService(CountryRepository countryRepository, StateRepository stateRepository) {
		this.countryRepository = countryRepository;
		this.stateRepository = stateRepository;
	}

	public List<StateDTO> listAllStates(Integer countryId) throws CountryNotFoundException {
		Optional<Country> country = countryRepository.findById(countryId);

		if (country.isEmpty()) {
			throw new CountryNotFoundException("Could not find any country with ID " + countryId);
		}

		List<State> states = stateRepository.findByCountryOrderByNameAsc(country.get());

		List<StateDTO> listStates = new ArrayList<>();

		for (State state : states) {
			listStates.add(new StateDTO(state));
		}

		return listStates;
	}
}
