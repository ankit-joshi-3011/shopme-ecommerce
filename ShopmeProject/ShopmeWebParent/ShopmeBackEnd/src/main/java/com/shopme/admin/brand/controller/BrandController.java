package com.shopme.admin.brand.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	private BrandService brandService;
	private CategoryService categoryService;

	public BrandController(BrandService brandService, CategoryService categoryService) {
		this.brandService = brandService;
		this.categoryService = categoryService;
	}

	@GetMapping("/brands")
	public String listAllBrands(Model model) {
		List<Brand> listBrands = brandService.listAllBrands();

		model.addAttribute("listBrands", listBrands);

		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		Brand brand = new Brand();

		List<Category> listCategories = categoryService.listCategoriesInForm();

		model.addAttribute("brand", brand);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";
	}

}
