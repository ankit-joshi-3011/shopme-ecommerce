package com.shopme.admin.setting;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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

		mockMvc.perform(get(url))
			.andExpect(status().isOk());
	}
}
