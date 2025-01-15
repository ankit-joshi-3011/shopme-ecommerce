package com.shopme.site.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 10;

	private ProductRepository repository;

	public ProductService(ProductRepository repository) {
		this.repository = repository;
	}

	public Page<Product> listEnabledByCategory(int pageNumber, Integer categoryId) {
		StringBuilder categoryIdMatcherBuilder = new StringBuilder().append("-").append(categoryId).append("-");

		Pageable pageable = PageRequest.of(pageNumber - 1, PRODUCTS_PER_PAGE);

		return repository.listEnabledByCategory(categoryId, categoryIdMatcherBuilder.toString(), pageable);
	}
}