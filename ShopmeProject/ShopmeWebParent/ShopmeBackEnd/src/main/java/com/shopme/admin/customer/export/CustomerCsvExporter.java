package com.shopme.admin.customer.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.ICsvBeanWriter;

import com.shopme.admin.export.AbstractCsvExporter;
import com.shopme.common.entity.Customer;
import jakarta.servlet.http.HttpServletResponse;

public class CustomerCsvExporter extends AbstractCsvExporter {
	public void export(List<Customer> listCustomers, HttpServletResponse response) throws IOException {
		this.setResponseHeader(response, "customers");

		ICsvBeanWriter csvWriter = this.getWriter(response);

		String[] csvHeader = { "Customer ID", "E-mail", "First Name", "Last Name", "Phone Number", "Address Line 1", "Address Line 2", "City", "State", "Country", "Postal Code", "Enabled", "Created Time" };
		String[] fieldMapping = { "id", "email", "firstName", "lastName", "phoneNumber", "addressLine1", "addressLine2", "city", "state", "country", "postalCode", "enabled", "createdTime" };

		csvWriter.writeHeader(csvHeader);

		for (Customer customer : listCustomers) {
			csvWriter.write(customer, fieldMapping);
		}

		csvWriter.close();
	}
}
