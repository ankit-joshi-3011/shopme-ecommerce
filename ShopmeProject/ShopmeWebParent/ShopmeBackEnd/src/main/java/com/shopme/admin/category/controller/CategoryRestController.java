package com.shopme.admin.category.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.category.CategoryService;

@RestController
public class CategoryRestController {
	private CategoryService service;

	public CategoryRestController(CategoryService service) {
		this.service = service;
	}

	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
}
