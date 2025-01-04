package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

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
		product.setMainImage("MissingImage.png");
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

	@Test
	public void testListAllProducts() {
		Iterable<Product> allProducts = repository.findAll();

		allProducts.forEach(product -> System.out.println(product));
	}

	@Test
	public void testGetProduct() {
		Product samsungGalaxy = repository.findById(1).get();

		System.out.println(samsungGalaxy);
	}

	@Test
	public void testUpdateProduct() {
		Product samsungGalaxy = repository.findById(1).get();
		samsungGalaxy.setEnabled(true);
		samsungGalaxy.setInStock(true);
		samsungGalaxy.setPrice(25000);

		repository.save(samsungGalaxy);

		Product updatedProduct = entityManager.find(Product.class, 1);

		assertThat(updatedProduct.getPrice()).isEqualTo(25000);
	}

	@Test
	public void testDeleteProduct() {
		repository.deleteById(1);

		Optional<Product> product = repository.findById(1);

		assertThat(!product.isPresent());
	}

	@Test
	public void testFindByName() {
		String name = "Acer Desktop";
		Product product = repository.findByName(name);

		assertThat(product).isNotNull();
		assertThat(product.getName()).isEqualTo(name);
	}

	@Test
	public void testFindByAlias() {
		String alias = "Dell-Inspiron-7770";
		Product product = repository.findByAlias(alias);

		assertThat(product).isNotNull();
		assertThat(product.getAlias()).isEqualTo(alias);
	}

	@Test
	public void testSaveProductWithImages() {
		Integer productId = 2;
		Product product = repository.findById(productId).get();

		product.addImage("extra-image-1.png");
		product.addImage("extra-image-2.png");
		product.addImage("extra-image-3.png");

		Product savedProduct = repository.save(product);

		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}

	@Test
	public void testSaveProductWithDetails() {
		Integer productId = 6;
		Product product = repository.findById(productId).get();

		product.addDetail("Memory Size", "8 GB");
		product.addDetail("CPU Manufacturer", "Snapdragon");
		product.addDetail("CPU Speed", "1.2 GHz");

		Product savedProduct = repository.save(product);

		assertThat(savedProduct.getDetails().size()).isEqualTo(3);
	}
}
