package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

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
}
