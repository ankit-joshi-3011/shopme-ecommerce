package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	@Bean
	UserDetailsService getUserDetailsService() {
		return new ShopmeUserDetailsService();
	}

	@Bean
	DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(getUserDetailsService());
		authenticationProvider.setPasswordEncoder(getPasswordEncoder());

		return authenticationProvider;
	}

	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
		http.authenticationProvider(getAuthenticationProvider());

		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
			.requestMatchers("/users/**").hasAuthority("Admin")
			.requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
			.requestMatchers("/brands/**").hasAnyAuthority("Admin", "Editor")
			.requestMatchers("/products", "/products/page/**", "/products/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
			.requestMatchers("/products/new", "/products/delete/**", "/products/*/enabled/**", "/products/export/**").hasAnyAuthority("Admin", "Editor")
			.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
			.requestMatchers("/settings/**", "/countries/**", "/states/**").hasAnyAuthority("Admin")
			.requestMatchers("/customers/**").hasAnyAuthority("Admin", "Salesperson")
			.anyRequest().authenticated())
			.formLogin(form -> form
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll())
			.logout(logout -> logout
				.permitAll())
			.rememberMe(remember -> remember
				.key("AbcDefgHijKlmnOpqrs_1234567890")
				.tokenValiditySeconds(7 * 24 * 60 * 60));

		return http.build();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
