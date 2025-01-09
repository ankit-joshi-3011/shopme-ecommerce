package com.shopme.admin.product.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.exception.PageOutOfBoundsException;
import com.shopme.admin.product.ProductService;
import com.shopme.admin.product.exception.ProductNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

@Controller
public class ProductController {
	private ProductService productService;
	private BrandService brandService;
	private CategoryService categoryService;

	public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService) {
		this.productService = productService;
		this.brandService = brandService;
		this.categoryService = categoryService;
	}

	@GetMapping("/products")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, "name", sortDir, null, 0, model);
	}

	@GetMapping("/products/page/{pageNumber}")
	public String listByPage(@PathVariable int pageNumber, @Param("sortField") String sortField, @Param("sortDir") String sortDir,
		@Param("keyword") String keyword, @Param("categoryId") Integer categoryId, Model model) {
		if (pageNumber <= 0) {
			throw new PageOutOfBoundsException();
		}

		if (sortField == null || sortField.isEmpty()) {
			sortField = "name";
		}

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Page<Product> listProducts = productService.listProductsByPage(pageNumber, sortField, sortDir, keyword, categoryId);

		List<Category> listCategories = categoryService.listCategoriesInForm();

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

		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", (sortDir.equals("asc") ? "desc" : "asc"));
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("keyword", keyword);

		return "products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		List<Brand> listBrands = brandService.getBrandsWithIdAndNameInformation();

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes attributes, @RequestParam("fileImage") MultipartFile mainImageMultipartFile,
		@RequestParam("extraImage") MultipartFile[] extraImageMultipartFiles,
		@RequestParam(required = false) String[] detailNames,
		@RequestParam(required = false) String[] detailValues,
		@RequestParam(required = false) String[] imageIds,
		@RequestParam(required = false) String[] imageNames,
		@RequestParam(required = false) String[] detailIds) throws IOException {
		String mainImageFileName = null;

		if (!mainImageMultipartFile.isEmpty()) {
			mainImageFileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
			product.setMainImage(mainImageFileName);
		}

		if (imageIds != null && imageIds.length > 0 && imageNames != null && imageNames.length > 0) {
			Set<ProductImage> images = new HashSet<>();

			for (int i = 0; i < imageIds.length; i++) {
				int id = Integer.parseInt(imageIds[i]);
				String name = imageNames[i];

				ProductImage image = new ProductImage();
				image.setId(id);
				image.setName(name);
				image.setProduct(product);

				images.add(image);
			}

			product.setImages(images);
		}

		List<String> validNewExtraImageFileNames = new ArrayList<>();
		List<MultipartFile> validNewExtraImageMultipartFiles = new ArrayList<>();

		for (MultipartFile extraImageMultipartFile : extraImageMultipartFiles) {
			if (!extraImageMultipartFile.isEmpty()) {
				String extraImageFileName = StringUtils.cleanPath(extraImageMultipartFile.getOriginalFilename());
				validNewExtraImageFileNames.add(extraImageFileName);
				validNewExtraImageMultipartFiles.add(extraImageMultipartFile);

				if (!product.containsExtraImageName(extraImageFileName)) {
					product.addImage(extraImageFileName);
				}
			}
		}

		if (detailNames != null && detailValues != null && detailIds != null
			&& detailNames.length > 0 && detailValues.length > 0 && detailIds.length > 0) {
			for (int i = 0; i < detailNames.length; i++) {
				if (detailNames[i] != null && detailValues[i] != null && detailIds[i] != null
					&& !detailNames[i].isEmpty() && !detailValues[i].isEmpty() && !detailIds[i].isEmpty()) {
					int detailId = Integer.parseInt(detailIds[i]);

					product.addDetail(detailId == 0 ? null : detailId, detailNames[i], detailValues[i]);
				}
			}
		}

		Product savedProduct = productService.save(product);

		String uploadDirectory = "../product-images/" + savedProduct.getId();

		if (mainImageFileName != null) {
			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, mainImageFileName, mainImageMultipartFile);
		}

		uploadDirectory = "../product-images/" + savedProduct.getId() + "/extras";

		for (int i = 0; i < validNewExtraImageFileNames.size(); i++) {
			FileUploadUtility.saveFile(uploadDirectory, validNewExtraImageFileNames.get(i), validNewExtraImageMultipartFiles.get(i));
		}

		Set<String> namesOfExtraImageFilesToKeep = new HashSet<>();

		for (ProductImage productImage : product.getImages()) {
			namesOfExtraImageFilesToKeep.add(productImage.getName());
		}

		FileUploadUtility.deleteUnneededFiles(uploadDirectory, namesOfExtraImageFilesToKeep);

		attributes.addFlashAttribute("message", "The product has been saved successfully.");

		return "redirect:/products";
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable Integer id, @PathVariable boolean status, RedirectAttributes attributes) {
		productService.updateProductEnabledStatus(id, status);
		attributes.addFlashAttribute("message", "The product ID " + id + " has been " + (status ? "enabled" : "disabled"));

		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Integer id, RedirectAttributes attributes) {
		try {
			productService.delete(id);

			String productExtraImagesDirectory = "../product-images/" + id + "/extras";
			FileUploadUtility.removeDirectory(productExtraImagesDirectory);

			String productImagesDirectory = "../product-images/" + id;
			FileUploadUtility.removeDirectory(productImagesDirectory);

			attributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.getBrandsWithIdAndNameInformation();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");

			return "products/product_form";
		} catch (ProductNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/detail/{id}")
	public String showProductDetails(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
		try {
			Product product = productService.get(id);

			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (ProductNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}
	}
}
