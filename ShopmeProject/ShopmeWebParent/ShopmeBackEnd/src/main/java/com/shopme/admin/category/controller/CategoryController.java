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
import com.shopme.admin.category.CategoryPageInformation;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.category.exception.CategoryNotFoundException;
import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.user.exception.PageOutOfBoundsException;
import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}

	@GetMapping("/categories/page/{pageNumber}")
	public String listByPage(@PathVariable int pageNumber, @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
		if (pageNumber <= 0) {
			throw new PageOutOfBoundsException();
		}

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		CategoryPageInformation categoryPageInformation = new CategoryPageInformation();
		List<Category> listCategories = service.listCategoriesOnPage(categoryPageInformation, pageNumber, sortDir, keyword);

		if (pageNumber > categoryPageInformation.getTotalPages()) {
			throw new PageOutOfBoundsException();
		}

		long startCount = (pageNumber - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;

		if (endCount > categoryPageInformation.getTotalElements()) {
			endCount = categoryPageInformation.getTotalElements();
		}

		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", (sortDir.equals("asc") ? "desc" : "asc"));
		model.addAttribute("totalPages", categoryPageInformation.getTotalPages());
		model.addAttribute("totalItems", categoryPageInformation.getTotalElements());
		model.addAttribute("sortField", "name");
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);

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

	@GetMapping("/categories/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Category> listCategory = service.listCategoriesInForm();

		CategoryCsvExporter exporter = new CategoryCsvExporter();

		exporter.export(listCategory, response);
	}
}
