package com.shopme.admin.setting;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Autowired
	public StateRestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "Admin")
	public void testListStates() throws Exception {
		Integer countryId = 1;

		String url = "/states/list/" + countryId;

		MvcResult result = mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);

		State[] states = objectMapper.readValue(jsonResponse, State[].class);

		for (State state : states) {
			System.out.println(state.getName());
		}
	}

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "Admin")
	public void testCreateState() throws Exception {
		String url = "/states/save";

		String stateName = "Gujarat";
		State gujarat = new State(stateName, new Country(1, "Republic of India", "IN", new HashSet<State>()));

		MvcResult result = mockMvc.perform(post(url)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(gujarat))
			.with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);
	}

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "Admin")
	public void testUpdateState() throws Exception {
		String url = "/states/save";
		Integer stateId = 3;

		String stateName = "Madhya Pradesh";
		State madhyaPradesh = new State(stateId, stateName, new Country(1, "Republic of India", "IN", new HashSet<State>()));

		mockMvc.perform(post(url)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(madhyaPradesh))
			.with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string(String.valueOf(stateId)));
	}

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "Admin")
	public void testDeleteState() throws Exception {
		Integer stateId = 3;
		String url = "/states/delete/" + stateId;

		mockMvc.perform(get(url))
			.andExpect(status().isOk());
	}
}
