package com.shopme.site.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
	private CategoryRepository repository;

	@Autowired
	public CategoryRepositoryTests(CategoryRepository repository) {
		this.repository = repository;
	}

	@Test
	public void testListEnabledCategories() {
		List<Category> enabledCategories = repository.findAllEnabled();

		enabledCategories.forEach(category -> {
			System.out.println(category.getName() + " (" + category.isEnabled() + ")");
		});
	}

	@Test
	public void testFindEnabledCategoryByAlias() {
		String alias = "camera_bags_cases";

		Category category = repository.findByAliasEnabled(alias);

		assertThat(category).isNotNull();
	}
}
