package com.shopme.admin.brand.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.ICsvBeanWriter;
import com.shopme.admin.export.AbstractCsvExporter;
import com.shopme.common.entity.Brand;
import jakarta.servlet.http.HttpServletResponse;

public class BrandCsvExporter extends AbstractCsvExporter {
	public void export(List<Brand> listBrand, HttpServletResponse response) throws IOException {
		this.setResponseHeader(response, "brands");

		ICsvBeanWriter csvWriter = this.getWriter(response);

		String[] csvHeader = { "Brand ID", "Brand Name", "Categories" };
		String[] fieldMapping = { "id", "name", "categories" };

		csvWriter.writeHeader(csvHeader);

		for (Brand brand : listBrand) {
			csvWriter.write(brand, fieldMapping);
		}

		csvWriter.close();
	}
}
