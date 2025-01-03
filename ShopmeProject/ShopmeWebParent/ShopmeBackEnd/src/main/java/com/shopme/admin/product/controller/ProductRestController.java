package com.shopme.admin.product.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.product.ProductService;

@RestController
public class ProductRestController {
	private ProductService service;

	public ProductRestController(ProductService service) {
		this.service = service;
	}

	@PostMapping("/products/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
}
