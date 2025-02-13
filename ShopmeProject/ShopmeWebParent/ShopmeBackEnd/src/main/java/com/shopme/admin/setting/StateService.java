package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shopme.admin.setting.exception.StateNotFoundException;
import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StateService {
	private StateRepository stateRepository;

	public StateService(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	public List<StateDTO> listAllStates(Country country) {
		List<State> states = stateRepository.findByCountryOrderByNameAsc(country);

		List<StateDTO> listStates = new ArrayList<>();

		for (State state : states) {
			listStates.add(new StateDTO(state));
		}

		return listStates;
	}

	public State save(State state) {
		return stateRepository.save(state);
	}

	public void delete(Integer id) throws StateNotFoundException {
		Optional<State> state = stateRepository.findById(id);

		if (state.isEmpty()) {
			throw new StateNotFoundException("Could not find any state with ID " + id);
		}

		stateRepository.deleteById(id);
	}
}
