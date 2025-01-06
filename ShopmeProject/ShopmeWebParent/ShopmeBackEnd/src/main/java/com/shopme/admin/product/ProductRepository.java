package com.shopme.admin.product;

import java.util.Date;

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
}
