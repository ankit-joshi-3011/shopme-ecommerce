package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listCategoriesInForm() {
		List<Category> categories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		List<Category> returnedCategories = new ArrayList<>();

		for (Category category : categories) {
			returnedCategories.add(Category.createCopy(category));

			listSubCategoriesInForm(category, 2, returnedCategories);
		}

		return returnedCategories;
	}

	private void listSubCategoriesInForm(Category parent, int numberOfHyphens, List<Category> returnedCategories) {
		StringBuilder hyphens = new StringBuilder();

		for (int i = 0; i < numberOfHyphens; i++) {
			hyphens.append("-");
		}

		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			Category copyOfSubCategory = Category.createCopy(subCategory);
			copyOfSubCategory.setName(hyphens.toString() + subCategory.getName());
			returnedCategories.add(copyOfSubCategory);

			if (!subCategory.getChildren().isEmpty()) {
				listSubCategoriesInForm(subCategory, numberOfHyphens + 2, returnedCategories);
			}
		}
	}

	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}

	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNewCategory = (id == null || id == 0);

		Category categoryByName = categoryRepository.findByName(name);
		Category categoryByAlias = categoryRepository.findByAlias(alias);

		if (categoryByName != null && (isCreatingNewCategory || categoryByName.getId() != id)) {
			return "DuplicateName";
		}

		if (categoryByAlias != null && (isCreatingNewCategory || categoryByAlias.getId() != id)) {
			return "DuplicateAlias";
		}

		return "OK";
	}
}
