package com.shopme.admin.product;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabledOrDisabled);

	public Product findByName(String name);

	public Product findByAlias(String alias);

	public Long countById(Integer id);

	@Query("SELECT p.createdTime FROM Product p WHERE p.id = ?1")
	public Date getCreatedTime(Integer id);

	@Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.brand.name, ' ', p.category.name) LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentIds LIKE %?2%")
	public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatcher, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentIds LIKE %?2%) AND CONCAT(p.name, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.brand.name, ' ', p.category.name) LIKE %?3%")
	public Page<Product> findAllInCategoryByKeyword(Integer categoryId, String categoryIdMatcher, String keyword, Pageable pageable);
}
