package com.play.ground.HelloExcel;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.play.ground.HelloHelper.LoggingHelper;

public class App {
	private static final LoggingHelper LOG = new LoggingHelper(App.class.getName());

	public static void main(String[] args) throws InvalidFormatException, IOException {
		LOG.info("APP STARTED");

		File file = new File("data/input/simple_grouped_data.xlsx");
		LOG.info("Reading file from: " + file.getAbsolutePath());

		try (XSSFWorkbook wb = new XSSFWorkbook(file)) {
			XSSFSheet sheet = wb.getSheetAt(0);
			
			int emptyRowCount = 0;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				int emptyCellCount = 0;
				boolean isRowEmpty = true;
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.iterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String value = cell.getStringCellValue();
					if (StringUtils.isBlank(value)) {
						if (emptyCellCount >= 5) break;
						emptyCellCount++;
						continue;
					}
					isRowEmpty = false;
					System.out.print("<" + value + "> ");
					
				}
				System.out.println();
				if (isRowEmpty) {
					emptyRowCount ++;
					if (emptyRowCount >= 5) break;
					continue;
				}
			}
		}

		LOG.info("APP FINISHED");
	}
}
