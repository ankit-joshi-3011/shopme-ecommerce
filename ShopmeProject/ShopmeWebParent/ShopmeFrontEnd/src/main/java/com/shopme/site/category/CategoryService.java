package com.shopme.site.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {
	private CategoryRepository repository;

	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}

	public List<Category> listEnabledCategoriesWithNoChildren() {
		List<Category> enabledCategories = repository.findAllEnabled();

		List<Category> enabledCategoriesWithNoChildren = new ArrayList<>();

		enabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();

			if (children == null || children.size() == 0) {
				enabledCategoriesWithNoChildren.add(category);
			}
		});

		return enabledCategoriesWithNoChildren;
	}

	public Category getCategory(String alias) {
		Category category = repository.findByAliasEnabled(alias);

		if (category == null) {
			throw new CategoryNotFoundException("Could not find category with alias " + alias);
		}

		return category;
	}

	public List<Category> getParentsOfCategoryIncludingCategory(Category category) {
		List<Category> listParentsIncludingCategory = new ArrayList<>();

		listParentsIncludingCategory.add(category);

		while (true) {
			Category parent = category.getParent();

			if (parent == null) {
				break;
			}

			listParentsIncludingCategory.add(0, parent);

			category = parent;
		}

		return listParentsIncludingCategory;
	}
}
