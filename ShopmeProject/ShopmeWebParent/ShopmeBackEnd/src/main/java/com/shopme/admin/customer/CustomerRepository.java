package com.shopme.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>, PagingAndSortingRepository<Customer, Integer> {
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.id, ' ', c.email, ' ', c.firstName, ' ', c.lastName, ' ', c.city, ' ', c.state, ' ', c.country.name) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);
}
