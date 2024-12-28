package com.shopme.admin.brand.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;

@Controller
public class BrandController {
	private BrandService service;

	public BrandController(BrandService service) {
		this.service = service;
	}

	@GetMapping("/brands")
	public String listAllBrands(Model model) {
		List<Brand> listBrands = service.listAllBrands();

		model.addAttribute("listBrands", listBrands);

		return "brands/brands";
	}
}
