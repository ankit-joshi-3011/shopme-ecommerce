package com.shopme.site.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Category;
import com.shopme.site.category.CategoryService;
import com.shopme.site.product.exception.CategoryNotFoundException;

@Controller
public class ProductController {
	private CategoryService categoryService;

	public ProductController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/category/{category_alias}")
	public String viewCategory(@PathVariable("category_alias") String alias, Model model) {
		Category category = categoryService.getCategory(alias);

		if (category == null) {
			throw new CategoryNotFoundException();
		}

		model.addAttribute("pageTitle", category.getName());

		return "products_by_category";
	}
}
