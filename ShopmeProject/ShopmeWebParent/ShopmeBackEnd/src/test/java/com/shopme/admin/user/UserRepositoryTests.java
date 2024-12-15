package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	private UserRepository repository;
	private TestEntityManager entityManager;

	@Autowired
	public UserRepositoryTests(UserRepository repository, TestEntityManager entityManager) {
		this.repository = repository;
		this.entityManager = entityManager;
	}

	@Test
	public void testCreateAdminUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userAnkitJ = new User("ankit.joshi3011@gmail.com", "ankitjoshi3011", "Ankit", "Joshi");
		userAnkitJ.addRole(roleAdmin);
		User savedUser = repository.save(userAnkitJ);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateEditorAndSalespersonUser() {
		Role roleSalesperson = entityManager.find(Role.class, 2);
		Role roleAssistant = entityManager.find(Role.class, 5);

		User userDishaJ = new User("dj2651991@gmail.com", "dj2651991", "Disha", "Joshi");
		userDishaJ.addRole(roleSalesperson);
		userDishaJ.addRole(roleAssistant);

		User savedUser = repository.save(userDishaJ);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> users = repository.findAll();

		users.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		Optional<User> userAnkitJ = repository.findById(1);
		assertThat(userAnkitJ).isNotEqualTo(Optional.empty());
	}

	@Test
	public void testUpdateUserDetails() {
		User userAnkitJ = repository.findById(1).get();
		userAnkitJ.setEnabled(true);
		repository.save(userAnkitJ);
	}

	@Test
	public void testDeleteUserById() {
		Integer userId = 2;
		repository.deleteById(userId);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "ankit.joshi3011@gmail.com";
		User user = repository.getUserByEmail(email);

		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 5;
		Long countById = repository.countById(id);

		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser() {
		Integer id = 7;
		repository.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnableUser() {
		Integer id = 7;
		repository.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repository.findAll(pageable);

		List<User> pageContent = page.getContent();

		assertThat(pageContent.size()).isEqualTo(pageSize);
	}
}
