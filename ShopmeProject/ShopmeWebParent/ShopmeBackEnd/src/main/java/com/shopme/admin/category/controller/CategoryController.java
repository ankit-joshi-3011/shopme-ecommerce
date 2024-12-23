package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.category.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listAll(@Param("sortDir") String sortDir, Model model) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		List<Category> listCategories = service.listCategoriesInForm(sortDir);

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", (sortDir.equals("asc") ? "desc" : "asc"));

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

	@PostMapping("/categories/save")
	public String saveCategory(Category category, RedirectAttributes attributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category savedCategory = service.save(category);

			String uploadDirectory = "../category-images/" + savedCategory.getId();

			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, fileName, multipartFile);
		} else {
			service.save(category);
		}

		attributes.addFlashAttribute("message", "The category has been saved successfully.");

		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
		try {
			Category category = service.get(id);
			List<Category> listCategories = service.listCategoriesInForm();

			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

			return "categories/category_form";
		} catch (CategoryNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable Integer id, @PathVariable boolean status, RedirectAttributes attributes) {
		service.updateCategoryEnabledStatus(id, status);
		attributes.addFlashAttribute("message", "The category ID " + id + " has been " + (status ? "enabled" : "disabled"));

		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable Integer id, RedirectAttributes attributes) {
		try {
			service.delete(id);

			String categoryImageDirectory = "../category-images" + id;
			FileUploadUtility.removeDirectory(categoryImageDirectory);

			attributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
		} catch (CategoryNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/categories";
	}
}
