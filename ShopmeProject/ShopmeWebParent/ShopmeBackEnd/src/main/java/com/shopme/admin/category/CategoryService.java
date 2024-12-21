package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.shopme.common.entity.Category;

public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listAll() {
		Iterable<Category> categories = categoryRepository.findAll();
		List<Category> returnedCategories = new ArrayList<>();

		for (Category category : categories) {
			returnedCategories.add(category);
		}

		return returnedCategories;
	}
}
