package com.shopme.admin.export;

import java.io.IOException;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractCsvExporter extends AbstractExporter {
	protected void setResponseHeader(HttpServletResponse response, String fileNamePrefix) {
		this.setResponseHeader(response, fileNamePrefix, "text/csv", ".csv");
	}

	protected ICsvBeanWriter getWriter(HttpServletResponse response) throws IOException {
		return new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
	}
}
