package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
	@MockitoBean
	private CategoryRepository categoryRepository;

	// The CategoryRepository mock created above will be
	// injected into the below CategoryService using the
	// @InjectMocks annotation
	@InjectMocks
	private CategoryService service;

	@Test
	public void testCheckUniqueInNewModeWithDuplicateName() {
		Integer id = null;
		String name = "Computers";
		String alias = "Android Phone";

		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);

		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInNewModeWithDuplicateAlias() {
		Integer id = null;
		String name = "Android Phone";
		String alias = "Computers";

		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);

		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}

	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "Android Phone";
		String alias = "Android Phone";

		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testCheckUniqueInEditModeWithDuplicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "Android Phone";

		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);

		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(2, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInEditModeWithDuplicateAlias() {
		Integer id = 1;
		String name = "Android Phone";
		String alias = "Computers";

		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);

		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(2, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}

	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Android Phone";
		String alias = "Android Phone";

		Category category = new Category();
		category.setId(id);
		category.setName(name);
		category.setAlias(alias);

		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}
}
