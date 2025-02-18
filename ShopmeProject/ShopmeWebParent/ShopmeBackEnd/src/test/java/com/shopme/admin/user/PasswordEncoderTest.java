package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "ankitjoshi3011";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
	}
}
