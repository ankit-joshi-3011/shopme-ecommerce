package com.shopme.admin.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
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
}
