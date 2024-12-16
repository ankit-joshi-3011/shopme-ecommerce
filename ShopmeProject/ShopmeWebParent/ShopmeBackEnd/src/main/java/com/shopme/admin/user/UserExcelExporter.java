package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx");

		writeHeaderLine();
		writeDataLines(listUsers);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);

		outputStream.close();
		workbook.close();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);

		createCell(row, 0, "User ID", cellStyle);
		createCell(row, 1, "E-mail", cellStyle);
		createCell(row, 2, "First Name", cellStyle);
		createCell(row, 3, "Last Name", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enabled", cellStyle);
	}

	private void createCell(XSSFRow row, int columnIndex, Object cellValue, CellStyle cellStyle) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);

		if (cellValue instanceof Integer) {
			cell.setCellValue((Integer) cellValue);
		} else if (cellValue instanceof Boolean) {
			cell.setCellValue((Boolean) cellValue);
		} else {
			cell.setCellValue((String) cellValue);
		}

		cell.setCellStyle(cellStyle);
	}

	private void writeDataLines(List<User> listUsers) {
		int rowIndex = 1;

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);

		for (User user : listUsers) {
			XSSFRow currentRow = sheet.createRow(rowIndex);

			int columnIndex = 0;
			createCell(currentRow, columnIndex++, user.getId(), cellStyle);
			createCell(currentRow, columnIndex++, user.getEmail(), cellStyle);
			createCell(currentRow, columnIndex++, user.getFirstName(), cellStyle);
			createCell(currentRow, columnIndex++, user.getLastName(), cellStyle);
			createCell(currentRow, columnIndex++, user.getRoles().toString(), cellStyle);
			createCell(currentRow, columnIndex++, user.isEnabled(), cellStyle);

			rowIndex++;
		}
	}
}