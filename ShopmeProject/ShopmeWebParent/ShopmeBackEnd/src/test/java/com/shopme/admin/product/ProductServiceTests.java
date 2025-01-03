package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	@MockitoBean
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService service;

	@Test
	public void testCheckUniqueInNewModeWithDuplicateName() {
		Integer id = null;
		String name = "Dell Inspiron 7770";
		String alias = "Android-Phone";

		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setAlias(alias);

		Mockito.when(productRepository.findByName(name)).thenReturn(product);
		Mockito.when(productRepository.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInNewModeWithDuplicateAlias() {
		Integer id = null;
		String name = "Acer Laptop";
		String alias = "acer-desktop-new";

		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setAlias(alias);

		Mockito.when(productRepository.findByName(name)).thenReturn(null);
		Mockito.when(productRepository.findByAlias(alias)).thenReturn(product);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}

	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "Android Phone";
		String alias = "Android-Phone";

		Mockito.when(productRepository.findByName(name)).thenReturn(null);
		Mockito.when(productRepository.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testCheckUniqueInEditModeWithDuplicateName() {
		Integer id = 1;
		String name = "Dell Inspiron 7770";
		String alias = "Android-Phone";

		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setAlias(alias);

		Mockito.when(productRepository.findByName(name)).thenReturn(product);
		Mockito.when(productRepository.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(2, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInEditModeWithDuplicateAlias() {
		Integer id = 1;
		String name = "Acer Laptop";
		String alias = "acer-desktop-new";

		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setAlias(alias);

		Mockito.when(productRepository.findByName(name)).thenReturn(null);
		Mockito.when(productRepository.findByAlias(alias)).thenReturn(product);

		String result = service.checkUnique(2, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}

	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Dell Inspiron 7770";
		String alias = "acer-desktop-new";

		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setAlias(alias);

		Mockito.when(productRepository.findByName(name)).thenReturn(product);
		Mockito.when(productRepository.findByAlias(alias)).thenReturn(product);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}
}
