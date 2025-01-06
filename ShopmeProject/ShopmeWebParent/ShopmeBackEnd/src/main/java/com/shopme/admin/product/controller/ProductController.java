package com.shopme.admin.product.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.shopme.admin.product.ProductService;
import com.shopme.admin.product.exception.ProductNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	private ProductService productService;
	private BrandService brandService;

	public ProductController(ProductService productService, BrandService brandService) {
		this.productService = productService;
		this.brandService = brandService;
	}

	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();

		model.addAttribute("listProducts", listProducts);

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
		model.addAttribute("numberOfExistingExtraImages", 0);
		model.addAttribute("numberOfExistingProductDetails", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes attributes, @RequestParam("fileImage") MultipartFile mainImageMultipartFile,
		@RequestParam("extraImage") MultipartFile[] extraImageMultipartFiles,
		@RequestParam(required = false) String[] detailNames,
		@RequestParam(required = false) String[] detailValues) throws IOException {
		String mainImageFileName = null;

		if (!mainImageMultipartFile.isEmpty()) {
			mainImageFileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
			product.setMainImage(mainImageFileName);
		}

		List<String> validExtraImageFileNames = new ArrayList<>();
		List<MultipartFile> validExtraImageMultipartFiles = new ArrayList<>();

		for (MultipartFile extraImageMultipartFile : extraImageMultipartFiles) {
			if (!extraImageMultipartFile.isEmpty()) {
				String extraImageFileName = StringUtils.cleanPath(extraImageMultipartFile.getOriginalFilename());
				validExtraImageFileNames.add(extraImageFileName);
				validExtraImageMultipartFiles.add(extraImageMultipartFile);
				product.addImage(extraImageFileName);
			}
		}

		if (detailNames != null && detailValues != null && detailNames.length > 0 && detailValues.length > 0) {
			for (int i = 0; i < detailNames.length; i++) {
				if (detailNames[i] != null && detailValues[i] != null && !detailNames[i].isEmpty() && !detailValues[i].isEmpty()) {
					product.addDetail(detailNames[i], detailValues[i]);
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

		for (int i = 0; i < validExtraImageFileNames.size(); i++) {
			FileUploadUtility.saveFile(uploadDirectory, validExtraImageFileNames.get(i), validExtraImageMultipartFiles.get(i));
		}

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
			model.addAttribute("numberOfExistingExtraImages", product.getImages().size());
			model.addAttribute("numberOfExistingProductDetails", product.getDetails().size());

			return "products/product_form";
		} catch (ProductNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}
	}
}
