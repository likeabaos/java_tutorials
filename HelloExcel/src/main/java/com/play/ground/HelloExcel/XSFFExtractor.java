package com.play.ground.HelloExcel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class XSFFExtractor {
	abstract public String processCell(String value);

	abstract public void processRow(List<String> values);

	public void extract(XSSFSheet sheet) {
		int emptyRowCount = 0;
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			int emptyCellCount = 0;
			boolean isRowEmpty = true;
			List<String> values = new ArrayList<String>();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.iterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String value = cell.getStringCellValue();
				if (StringUtils.isBlank(value)) {
					if (emptyCellCount >= 5)
						break;
					emptyCellCount++;
					continue;
				}
				isRowEmpty = false;
				values.add(this.processCell(value));
			}
			if (isRowEmpty) {
				emptyRowCount++;
				if (emptyRowCount >= 5)
					break;
				continue;
			}
			this.processRow(values);
		}
	}
}
