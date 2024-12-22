package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

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
	@Autowired
	private CategoryRepository repository;

	@Test
	public void testCreateRootCategory() {
		Category computers = new Category("Computers", "Computers", "default.png");
		Category electronics = new Category("Electronics", "Electronics", "default.png");

		repository.saveAll(List.of(computers, electronics));
	}

	@Test
	public void testCreateSubCategory() {
		Category computers = new Category(1);
		Category desktops = new Category("Desktops", "Desktops", "default.png", computers);
		Category laptops = new Category("Laptops", "Laptops", "default.png", computers);
		Category computerComponents = new Category("Computer Components", "Computer Components", "default.png",
			computers);

		Category electronics = new Category(2);
		Category cameras = new Category("Cameras", "Cameras", "default.png", electronics);
		Category smartphones = new Category("Smartphones", "Smartphones", "default.png", electronics);

		repository.saveAll(List.of(desktops, laptops, computerComponents, cameras, smartphones));

		laptops = new Category(4);
		Category gamingLaptops = new Category("Gaming Laptops", "Gaming Laptops", "default.png", laptops);

		computerComponents = new Category(5);
		Category ram = new Category("RAM", "RAM", "default.png", computerComponents);

		smartphones = new Category(7);
		Category iPhone = new Category("iPhone", "iPhone", "default.png", smartphones);

		repository.saveAll(List.of(gamingLaptops, ram, iPhone));
	}

	@Test
	public void testGetCategoryAndChildren() {
		Category computers = repository.findById(1).get();

		System.out.println(computers.getName());

		printSubCategories(computers, 2);
	}

	private void printSubCategories(Category parent, int numberOfHyphens) {
		StringBuilder hyphens = new StringBuilder();

		for (int i = 0; i < numberOfHyphens; i++) {
			hyphens.append("-");
		}

		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			System.out.println(hyphens.toString() + subCategory.getName());

			if (!subCategory.getChildren().isEmpty()) {
				printSubCategories(subCategory, numberOfHyphens + 2);
			}
		}
	}

	@Test
	public void testGetAllCategoriesAndChildren() {
		Iterable<Category> categories = repository.findAll();

		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());

				printSubCategories(category, 2);
			}
		}
	}

	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repository.findRootCategories();
		rootCategories.forEach(category -> System.out.println(category.getName()));
	}

	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = repository.findByName(name);

		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
}
