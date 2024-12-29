package com.shopme.admin.brand.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.brand.BrandService;

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
}
