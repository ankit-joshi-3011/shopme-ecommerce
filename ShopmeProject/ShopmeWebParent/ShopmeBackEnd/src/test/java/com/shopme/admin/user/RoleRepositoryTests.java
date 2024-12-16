package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
// By default Spring Data JPA will run tests against an in-memory database.
// In order to test with a real database, we need to override the default configuration
// as below.
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	private RoleRepository repository;

	@Autowired
	public RoleRepositoryTests(RoleRepository repository) {
		this.repository = repository;
	}

	@Test
	public void testCreateAdminRole() {
		Role roleAdmin = new Role("Admin", "Manage everything");
		Role savedRole = repository.save(roleAdmin);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateRemainingRoles() {
		Role roleSalesperson = new Role("Salesperson",
				"Manage product price, customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menus");
		Role roleShipper = new Role("Shipper", "View products, view orders and update order status");
		Role roleAssistant = new Role("Assistant", "Manage questions and reviews");

		repository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
}
