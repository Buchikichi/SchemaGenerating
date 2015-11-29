package to.kit.data;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaGeneratingMain {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SchemaGeneratingMain.class);

	private void scanSheet(Workbook book) {
		for (Sheet sheet : book) {
			for (Row row : sheet) {
				for (Cell cell : row) {
					int cellType = cell.getCellType();
					String val = null;

					if (cellType == Cell.CELL_TYPE_STRING) {
						val = cell.getStringCellValue();
					} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
						if (DateUtil.isCellDateFormatted(cell)) {
							val = String.valueOf(cell.getDateCellValue());
						} else {
							val = String.valueOf(cell.getNumericCellValue());
						}
					}
					System.out.println(val);
				}
			}
		}
	}

	public void execute(final File dir) throws EncryptedDocumentException, InvalidFormatException, IOException {
		for (File file : dir.listFiles()) {
			String name = file.getName();

			if (!name.endsWith(".xls") && !name.endsWith(".xlsx")) {
				continue;
			}
			try (Workbook book = WorkbookFactory.create(file)) {
				scanSheet(book);
			}
		}
	}

	public static void main(final String[] args) throws Exception {
		File dir = null;

		if (0 < args.length) {
			File file = new File(args[0]);
			if (file.exists() && file.isDirectory()) {
				dir = file;
			}
		}
		if (dir == null) {
			LOG.error("Tell me the directory.");
			return;
		}
		SchemaGeneratingMain app = new SchemaGeneratingMain();

		app.execute(dir);
	}
}
