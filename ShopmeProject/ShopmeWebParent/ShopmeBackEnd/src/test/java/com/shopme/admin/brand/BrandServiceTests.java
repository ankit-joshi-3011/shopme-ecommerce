package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {
	@MockitoBean
	private BrandRepository brandRepository;

	@InjectMocks
	private BrandService service;

	@Test
	public void testCheckUniqueInNewModeWithDuplicateName() {
		Integer id = null;
		String name = "Acer";

		Brand brand = new Brand();
		brand.setId(id);
		brand.setName(name);

		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "Samsung";

		Mockito.when(brandRepository.findByName(name)).thenReturn(null);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testCheckUniqueInEditModeWithDuplicateName() {
		Integer id = 1;
		String name = "Samsung";

		Brand brand = new Brand();
		brand.setId(id);
		brand.setName(name);

		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(2, name);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Acer";

		Brand brand = new Brand();
		brand.setId(id);
		brand.setName(name);

		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("OK");
	}
}
