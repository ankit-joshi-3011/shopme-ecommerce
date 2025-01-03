package com.shopme.admin.product;

import java.util.ArrayList;
import java.util.Date;
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

	public Product save(Product product) {
		Date currentDate = new Date();

		if (product.getId() == null) {
			product.setCreatedTime(currentDate);
		}

		product.setAlias(transformAlias(product.getName(), product.getAlias()));

		product.setUpdatedTime(currentDate);

		return productRepository.save(product);
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
}
