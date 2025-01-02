package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	private ProductRepository repository;
	private TestEntityManager entityManager;

	@Autowired
	public ProductRepositoryTests(ProductRepository repository, TestEntityManager entityManager) {
		this.repository = repository;
		this.entityManager = entityManager;
	}

	@Test
	public void testCreateProduct() {
		Brand samsung = entityManager.find(Brand.class, 10);
		Category unlockedCellPhones = entityManager.find(Category.class, 15);

		Product product = new Product();
		product.setName("Samsung Galaxy A31");
		product.setAlias("Samsung Galaxy A31");
		product.setShortDescription("A budget smartphone from Samsung");
		product.setFullDescription("A budget smartphone from Samsung with all the essential features");
		product.setBrand(samsung);
		product.setCategory(unlockedCellPhones);
		product.setPrice(23000);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repository.save(product);

		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
}
