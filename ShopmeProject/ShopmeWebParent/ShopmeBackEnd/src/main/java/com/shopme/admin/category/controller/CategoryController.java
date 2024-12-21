package com.shopme.admin.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = service.listAll();
		model.addAttribute("listCategories", listCategories);

		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		Category category = new Category();
		category.setEnabled(true);

		List<Category> listCategories = service.listCategoriesInForm();

		model.addAttribute("category", category);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		return "categories/category_form";
	}
}
