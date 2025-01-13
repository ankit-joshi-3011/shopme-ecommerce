package com.shopme.site;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.Category;
import com.shopme.site.category.CategoryService;

@Controller
public class MainController {
	private CategoryService categoryService;

	public MainController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> enabledCategoriesWithNoChildren = categoryService.listEnabledCategoriesWithNoChildren();

		model.addAttribute("listCategories", enabledCategoriesWithNoChildren);

		return "index";
	}
}
