package com.shopme.admin.category.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.ICsvBeanWriter;
import com.shopme.admin.export.AbstractCsvExporter;
import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AbstractCsvExporter {
	public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
		this.setResponseHeader(response, "categories");

		ICsvBeanWriter csvWriter = this.getWriter(response);

		String[] csvHeader = { "Category ID", "Category Name" };
		String[] fieldMapping = { "id", "name" };

		csvWriter.writeHeader(csvHeader);

		for (Category category : listCategory) {
			category.setName(category.getName().replace('-', ' '));
			csvWriter.write(category, fieldMapping);
		}

		csvWriter.close();
	}
}
