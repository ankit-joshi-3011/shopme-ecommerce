package com.shopme.admin.setting;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

// This is not a unit test but rather an integration test. This will run
// the whole Spring Boot application.
@SpringBootTest
// Tell the Spring framework to inject an instance of the MockMvc object
// into the test class
@AutoConfigureMockMvc
public class CountryRestControllerTests {
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Autowired
	public CountryRestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "Admin")
	public void testListCountries() throws Exception {
		String url = "/countries/list";

		MvcResult result = mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);

		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

		for (Country country : countries) {
			System.out.println(country.getName() + " " + country.getCode());
		}
	}

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "Admin")
	public void testCreateCountry() throws Exception {
		String url = "/countries/save";

		String countryName = "Canada";
		String countryCode = "CA";
		Country canada = new Country(countryName, countryCode, new HashSet<State>());

		MvcResult result = mockMvc.perform(post(url)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(canada))
			.with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);
	}
}
