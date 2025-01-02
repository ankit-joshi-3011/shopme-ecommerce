package com.shopme.admin.brand.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.exception.BrandNotFoundExceptionRestApi;
import com.shopme.admin.category.CategoryDataTransferObject;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {
	private BrandService service;

	public BrandRestController(BrandService service) {
		this.service = service;
	}

	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return service.checkUnique(id, name);
	}

	@GetMapping("/brands/{id}/categories")
	public List<CategoryDataTransferObject> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundExceptionRestApi {
		try {
			Brand brand = service.get(brandId);
			Set<Category> categories = brand.getCategories();
			List<CategoryDataTransferObject> result = new ArrayList<>();

			for (Category category : categories) {
				CategoryDataTransferObject categoryDataTransferObject = new CategoryDataTransferObject(category.getId(), category.getName());
				result.add(categoryDataTransferObject);
			}

			return result;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundExceptionRestApi();
		}
	}
}
