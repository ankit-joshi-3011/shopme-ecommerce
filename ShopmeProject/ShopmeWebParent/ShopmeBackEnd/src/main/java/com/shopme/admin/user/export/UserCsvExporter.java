package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.ICsvBeanWriter;
import com.shopme.admin.export.AbstractCsvExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractCsvExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		this.setResponseHeader(response, "users");

		ICsvBeanWriter csvWriter = this.getWriter(response);

		String[] csvHeader = { "User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled" };
		String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enabled" };

		csvWriter.writeHeader(csvHeader);

		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}

		csvWriter.close();
	}
}
