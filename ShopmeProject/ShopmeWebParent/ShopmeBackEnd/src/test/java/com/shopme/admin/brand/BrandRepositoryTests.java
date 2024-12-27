package com.shopme.admin.brand;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	private BrandRepository repository;
	private TestEntityManager entityManager;

	@Autowired
	public BrandRepositoryTests(BrandRepository repository, TestEntityManager entityManager) {
		this.repository = repository;
		this.entityManager = entityManager;
	}

	@Test
	public void testCreateBrand() {
		Brand acer = new Brand("Acer", "MissingImage.png");

		Category laptops = entityManager.find(Category.class, 19);
		acer.getCategories().add(laptops);

		Brand apple = new Brand("Apple", "MissingImage.png");

		Category cellPhoneAndAccessories = entityManager.find(Category.class, 8);
		apple.getCategories().add(cellPhoneAndAccessories);
		Category tablets = entityManager.find(Category.class, 20);
		apple.getCategories().add(tablets);

		Brand samsung = new Brand("Samsung", "MissingImage.png");

		Category ram = entityManager.find(Category.class, 29);
		samsung.getCategories().add(ram);
		Category internalHardDisk = entityManager.find(Category.class, 24);
		samsung.getCategories().add(internalHardDisk);

		repository.saveAll(List.of(acer, apple, samsung));
	}
}
