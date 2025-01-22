package com.shopme.site.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {
	@Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 OR p.category.allParentIds LIKE %?2%) ORDER BY p.name ASC")
	public Page<Product> listEnabledByCategory(Integer categoryId, String categoryIdMatcher, Pageable pageable);

	public Product findByAlias(String alias);

	@Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.shortDescription, ' ', p.fullDescription) LIKE %?1%")
	public Page<Product> search(String keyword, Pageable pageable);
}
