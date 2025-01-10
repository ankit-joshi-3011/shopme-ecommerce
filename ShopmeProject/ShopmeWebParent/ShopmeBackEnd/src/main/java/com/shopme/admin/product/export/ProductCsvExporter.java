package com.shopme.admin.product.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.ICsvBeanWriter;

import com.shopme.admin.export.AbstractCsvExporter;
import com.shopme.common.entity.Product;

import jakarta.servlet.http.HttpServletResponse;

public class ProductCsvExporter extends AbstractCsvExporter {
	public void export(List<Product> listProduct, HttpServletResponse response) throws IOException {
		this.setResponseHeader(response, "products");

		ICsvBeanWriter csvWriter = this.getWriter(response);

		String[] csvHeader = { "Product ID", "Product Name", "In Stock", "Cost", "Price", "Discount Percent", "Category", "Brand" };
		String[] fieldMapping = { "id", "name", "inStock", "cost", "price", "discountPercent", "category", "brand" };

		csvWriter.writeHeader(csvHeader);

		for (Product product : listProduct) {
			csvWriter.write(product, fieldMapping);
		}

		csvWriter.close();
	}
}
