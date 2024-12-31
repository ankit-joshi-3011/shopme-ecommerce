package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.common.entity.Brand;

@Service
public class BrandService {
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
}
