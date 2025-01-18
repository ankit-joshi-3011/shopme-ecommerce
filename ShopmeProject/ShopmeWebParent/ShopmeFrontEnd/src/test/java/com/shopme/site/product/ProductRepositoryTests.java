package com.shopme.site.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	private ProductRepository repository;

	@Autowired
	public ProductRepositoryTests(ProductRepository repository) {
		this.repository = repository;
	}

	@Test
	public void testFindByAlias() {
		String alias = "Acer-Aspire-5-Slim-Laptop";
		Product product = repository.findByAlias(alias);

		assertThat(product).isNotNull();
	}
}
