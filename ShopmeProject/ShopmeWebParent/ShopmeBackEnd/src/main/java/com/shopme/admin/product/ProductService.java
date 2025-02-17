package com.shopme.admin.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 5;

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> listAll() {
		Iterable<Product> products = productRepository.findAll();

		List<Product> returnedProducts = new ArrayList<>();

		for (Product product : products) {
			returnedProducts.add(product);
		}

		return returnedProducts;
	}

	public Product save(Product product) {
		Date currentDate = new Date();

		if (product.getId() == null) {
			product.setCreatedTime(currentDate);
		} else {
			product.setCreatedTime(productRepository.getCreatedTime(product.getId()));
		}

		product.setAlias(transformAlias(product.getName(), product.getAlias()));

		product.setUpdatedTime(currentDate);

		return productRepository.save(product);
	}

	public Product savePricingInformation(Product productInForm) {
		Product productInDatabase = get(productInForm.getId());

		productInDatabase.setCost(productInForm.getCost());
		productInDatabase.setPrice(productInForm.getPrice());
		productInDatabase.setDiscountPercent(productInForm.getDiscountPercent());

		Date currentDate = new Date();
		productInDatabase.setCreatedTime(productRepository.getCreatedTime(productInForm.getId()));
		productInDatabase.setUpdatedTime(currentDate);

		return productRepository.save(productInDatabase);
	}

	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNewProduct = (id == null || id == 0);

		alias = transformAlias(name, alias);

		Product productByName = productRepository.findByName(name);
		Product productByAlias = productRepository.findByAlias(alias);

		if (productByName != null && (isCreatingNewProduct || productByName.getId() != id)) {
			return "DuplicateName";
		}

		if (productByAlias != null && (isCreatingNewProduct || productByAlias.getId() != id)) {
			return "DuplicateAlias";
		}

		return "OK";
	}

	private String transformAlias(String name, String alias) {
		if (alias == null || alias.isEmpty()) {
			return name.replace(" ", "-");
		}

		return alias.replace(" ", "-");
	}

	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) {
		Long countById = productRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}

		productRepository.deleteById(id);
	}

	public Product get(Integer id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Could not find any product with ID " + id));
	}

	public Page<Product> listProductsByPage(int pageNumber, String sortField, String sortDir, String keyword, Integer categoryId) {
		Sort sort = Sort.by(sortField);
		boolean ascendingOrDescending = !sortDir.equals("desc");

		Pageable pageable = PageRequest.of(pageNumber - 1, PRODUCTS_PER_PAGE, (ascendingOrDescending ? sort.ascending() : sort.descending()));

		boolean isKeywordValid = keyword != null && !keyword.isEmpty();
		boolean isCategoryIdValid = categoryId != null && categoryId > 0;

		if (isCategoryIdValid) {
			StringBuilder categoryIdMatcherBuilder = new StringBuilder();
			categoryIdMatcherBuilder.append("-").append(categoryId).append("-");

			if (isKeywordValid) {
				return productRepository.findAllInCategoryByKeyword(categoryId, categoryIdMatcherBuilder.toString(), keyword, pageable);
			} else {
				return productRepository.findAllInCategory(categoryId, categoryIdMatcherBuilder.toString(), pageable);
			}
		} else if (isKeywordValid) {
			return productRepository.findAll(keyword, pageable);
		} else {
			return productRepository.findAll(pageable);
		}
	}
}
