package com.shopme.site.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.PageOutOfBoundsException;
import com.shopme.site.category.CategoryService;
import com.shopme.site.product.ProductService;

@Controller
public class ProductController {
	private CategoryService categoryService;
	private ProductService productService;

	public ProductController(CategoryService categoryService, ProductService productService) {
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@GetMapping("/category/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewCategoryByPage(alias, 1, model);
	}

	@GetMapping("/category/{category_alias}/page/{pageNumber}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable int pageNumber, Model model) {
		if (pageNumber <= 0) {
			throw new PageOutOfBoundsException();
		}

		String sortField = "name";

		if (sortField == null || sortField.isEmpty()) {
			sortField = "name";
		}

		String sortDir = "asc";

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Category category = categoryService.getCategory(alias);

		List<Category> parentsOfCategoryIncludingCategory = categoryService.getParentsOfCategoryIncludingCategory(category);

		Page<Product> enabledProductsByCategory = productService.listEnabledByCategory(pageNumber, category.getId());

		int totalPages = enabledProductsByCategory.getTotalPages();

		if (pageNumber > totalPages) {
			throw new PageOutOfBoundsException();
		}

		long startCount = (pageNumber - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

		long totalElements = enabledProductsByCategory.getTotalElements();

		if (endCount > totalElements) {
			endCount = totalElements;
		}

		String keyword = null;

		model.addAttribute("category", category);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", (sortDir.equals("asc") ? "desc" : "asc"));
		model.addAttribute("listCategoryAndParents", parentsOfCategoryIncludingCategory);
		model.addAttribute("productsByCategory", enabledProductsByCategory);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("keyword", keyword);

		return "product/products_by_category";
	}

	@GetMapping("/product/{product_alias}")
	public String viewProductDetails(@PathVariable("product_alias") String alias, Model model) {
		Product product = productService.getProductByAlias(alias);
		List<Category> parentsOfCategoryIncludingCategory = categoryService.getParentsOfCategoryIncludingCategory(product.getCategory());

		model.addAttribute("product", product);
		model.addAttribute("listCategoryAndParents", parentsOfCategoryIncludingCategory);

		return "product/product_details";
	}

	@GetMapping("/search/page/{pageNumber}")
	public String searchProduct(@Param("keyword") String keyword, @PathVariable int pageNumber, Model model) {
		if (pageNumber <= 0) {
			throw new PageOutOfBoundsException();
		}

		String sortField = "name";

		if (sortField == null || sortField.isEmpty()) {
			sortField = "name";
		}

		String sortDir = "asc";

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Page<Product> listProducts = productService.search(pageNumber, keyword);

		int totalPages = listProducts.getTotalPages();

		if (pageNumber > totalPages) {
			throw new PageOutOfBoundsException();
		}

		long startCount = (pageNumber - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

		long totalElements = listProducts.getTotalElements();

		if (endCount > totalElements) {
			endCount = totalElements;
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", (sortDir.equals("asc") ? "desc" : "asc"));
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", totalElements);

		return "product/search_results";
	}
}
