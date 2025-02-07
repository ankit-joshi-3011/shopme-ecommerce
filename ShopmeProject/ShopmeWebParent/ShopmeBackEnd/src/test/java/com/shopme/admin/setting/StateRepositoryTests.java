package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {
	private StateRepository stateRepository;
	private TestEntityManager entityManager;

	@Autowired
	public StateRepositoryTests(StateRepository stateRepository, TestEntityManager entityManager) {
		this.stateRepository = stateRepository;
		this.entityManager = entityManager;
	}

	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);

		State maharashtra = stateRepository.save(new State("Maharashtra", country));
		State gujarat = stateRepository.save(new State("Gujarat", country));

		assertThat(maharashtra).isNotNull();
		assertThat(gujarat).isNotNull();

		assertThat(maharashtra.getId()).isGreaterThan(0);
		assertThat(gujarat.getId()).isGreaterThan(0);
	}

	@Test
	public void testListStatesByCountry() {
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);

		List<State> listStates = stateRepository.findByCountryOrderByNameAsc(country);
		listStates.forEach(state -> System.out.println(state.getName()));

		assertThat(listStates.size()).isGreaterThan(0);
	}

	@Test
	public void testUpdateState() {
		Integer id = 2;
		String name = "Madhya Pradesh";

		State state = stateRepository.findById(id).get();
		state.setName(name);

		State updatedState = stateRepository.save(state);

		assertThat(updatedState.getName()).isEqualTo(name);
	}

	@Test
	public void testDeleteState() {
		Integer id = 2;
		stateRepository.deleteById(id);

		Optional<State> state = stateRepository.findById(id);

		assertThat(state.isEmpty());
	}
}
