package com.shopme.admin.brand.controller;

import java.io.IOException;
import java.util.List;

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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.exception.BrandNotFoundException;
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

	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, RedirectAttributes attributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);

			String uploadDirectory = "../brand-logo-images/" + savedBrand.getId();

			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}

		attributes.addFlashAttribute("message", "The brand has been saved successfully.");

		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesInForm();

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

			return "brands/brand_form";
		} catch (BrandNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable Integer id, RedirectAttributes attributes) {
		try {
			brandService.delete(id);

			String brandImageDirectory = "../brand-logo-images/" + id;
			FileUploadUtility.removeDirectory(brandImageDirectory);

			attributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/brands";
	}
}
