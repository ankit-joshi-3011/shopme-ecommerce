package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE = 7;

	private BrandRepository brandRepository;

	public BrandService(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	public List<Brand> listAllBrands() {
		Iterable<Brand> brands = brandRepository.findAll();
		List<Brand> returnedBrands = new ArrayList<>();

		for (Brand brand : brands) {
			returnedBrands.add(brand);
		}

		return returnedBrands;
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNewBrand = (id == null || id == 0);

		Brand brandByName = brandRepository.findByName(name);

		if (brandByName != null && (isCreatingNewBrand || brandByName.getId() != id)) {
			return "DuplicateName";
		}

		return "OK";
	}

	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}

		brandRepository.deleteById(id);
	}

	public Page<Brand> listBrandsByPage(int pageNumber, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
		boolean ascendingOrDescending = !sortDir.equals("desc");

		Pageable pageable = PageRequest.of(pageNumber - 1, BRANDS_PER_PAGE, (ascendingOrDescending ? sort.ascending() : sort.descending()));

		if (keyword != null && !keyword.isEmpty()) {
			return brandRepository.findAll(keyword, pageable);
		}

		return brandRepository.findAll(pageable);
	}
}
