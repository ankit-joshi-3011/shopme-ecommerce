package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	private static final int ROOT_CATEGORIES_PER_PAGE = 4;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listCategoriesInForm() {
		return listCategoriesInForm(new CategoryPageInformation(), 1, "asc");
	}

	public List<Category> listCategoriesInForm(String sortDir) {
		return listCategoriesInForm(new CategoryPageInformation(), 1, sortDir);
	}

	public List<Category> listCategoriesInForm(CategoryPageInformation categoryPageInformation, int pageNumber, String sortDir) {
		Sort sort = Sort.by("name");
		boolean ascendingOrDescending = !sortDir.equals("desc");

		Pageable pageable = PageRequest.of(pageNumber - 1, ROOT_CATEGORIES_PER_PAGE, (ascendingOrDescending ? sort.ascending() : sort.descending()));

		Page<Category> categories = categoryRepository.findRootCategories(pageable);
		categoryPageInformation.setTotalElements(categories.getTotalElements());
		categoryPageInformation.setTotalPages(categories.getTotalPages());

		List<Category> returnedCategories = new ArrayList<>();

		for (Category category : categories.getContent()) {
			returnedCategories.add(Category.createCopy(category));

			listSubCategoriesInForm(category, 2, returnedCategories, ascendingOrDescending);
		}

		return returnedCategories;
	}

	private void listSubCategoriesInForm(Category parent, int numberOfHyphens, List<Category> returnedCategories, boolean ascendingOrDescending) {
		StringBuilder hyphens = new StringBuilder();

		for (int i = 0; i < numberOfHyphens; i++) {
			hyphens.append("-");
		}

		Set<Category> sortedChildren = new TreeSet<>((a, b) -> a.getName().compareTo(b.getName()) * (ascendingOrDescending ? 1 : -1));
		sortedChildren.addAll(parent.getChildren());

		for (Category subCategory : sortedChildren) {
			Category copyOfSubCategory = Category.createCopy(subCategory);
			copyOfSubCategory.setName(hyphens.toString() + subCategory.getName());
			returnedCategories.add(copyOfSubCategory);

			if (!subCategory.getChildren().isEmpty()) {
				listSubCategoriesInForm(subCategory, numberOfHyphens + 2, returnedCategories, ascendingOrDescending);
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

	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		categoryRepository.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = categoryRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}

		categoryRepository.deleteById(id);
	}
}
