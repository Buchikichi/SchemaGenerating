package to.kit.workbook;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

/**
 * Workbookのセルについてのユーティリティー.
 * @author Hidetaka Sasai
 */
public class CellUtils {
	public static String getStringCellValue(Cell cell) {
		String val = null;
		int cellType = cell.getCellType();

		if (cellType == Cell.CELL_TYPE_STRING) {
			val = cell.getStringCellValue();
		} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				val = String.valueOf(cell.getDateCellValue());
			} else {
				DataFormatter fmt = new DataFormatter();
				val = fmt.formatCellValue(cell);
			}
		}
		return val;
	}

	public static int getIntCellValue(Cell cell) {
		int val = 0;
		int cellType = cell.getCellType();

		if (cellType == Cell.CELL_TYPE_STRING) {
			val = NumberUtils.toInt(cell.getStringCellValue());
		} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
			val = (int) cell.getNumericCellValue();
		}
		return val;
	}

	public static String getStringCellValue(Row row, int columnIndex) {
		Cell cell = CellUtil.getCell(row, columnIndex);

		return getStringCellValue(cell);
	}

	public static int getIntCellValue(Row row, int columnIndex) {
		Cell cell = CellUtil.getCell(row, columnIndex);

		return getIntCellValue(cell);
	}

	public static String getStringCellValue(int rowIndex, int columnIndex, Sheet sheet) {
		Row row = CellUtil.getRow(rowIndex, sheet);

		return getStringCellValue(row, columnIndex);
	}
}
