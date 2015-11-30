package to.kit.data;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.data.creating.DDLCreateor;
import to.kit.data.info.AttrInfo;
import to.kit.data.info.EntityInfo;
import to.kit.data.info.SchemaInfo;
import to.kit.workbook.CellUtils;

public class SchemaGeneratingMain {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SchemaGeneratingMain.class);
	private static final String SIG_DB_SPEC = "DB設計書";
	private static final int ROW_BEGIN = 13;
	private static final int COL_KANJI = 3;
	private static final int COL_NAME = 16;
	private static final int COL_TYPE = 24;
	private static final int COL_SIZE = 29;
	private static final int COL_PK = 32;
	private static final int COL_NOT_NULL = 34;
	private static final int COL_IDX = 36;
	private static final int COL_DEF = 38;
	private static final int COL_REM = 43;

	private AttrInfo scanRow(Row row) {
		String kanji = CellUtils.getStringCellValue(row, COL_KANJI);
		String name = CellUtils.getStringCellValue(row, COL_NAME);
		String type = CellUtils.getStringCellValue(row, COL_TYPE);
		int size = CellUtils.getIntCellValue(row, COL_SIZE);
		String pk = CellUtils.getStringCellValue(row, COL_PK);
		String nn = CellUtils.getStringCellValue(row, COL_NOT_NULL);
		String idx = CellUtils.getStringCellValue(row, COL_IDX);
		String def = CellUtils.getStringCellValue(row, COL_DEF);
		String rem = CellUtils.getStringCellValue(row, COL_REM);

		if (StringUtils.isBlank(kanji) || StringUtils.isBlank(name)) {
			return null;
		}
//		LOG.debug("\tCOL:{},{}", kanji, name);
		AttrInfo attr = new AttrInfo();
		attr.setKanji(kanji);
		attr.setName(name);
		attr.setType(type);
		attr.setSize(size);
		if ("○".equals(pk)) {
			attr.setPrimary(true);
		} else if (StringUtils.isNotBlank(pk)) {
			attr.setUnique(NumberUtils.toInt(pk));
		}
		attr.setNotNull(StringUtils.isNotBlank(nn));
		if (StringUtils.isNotBlank(idx)) {
			attr.setIdx(NumberUtils.toInt(idx));
		}
		attr.setDefaultVal(def);
		attr.setRemarks(rem);
		return attr;
	}

	private EntityInfo scanSheet(final Sheet sheet) {
		EntityInfo entity = new EntityInfo();
		String tableID = CellUtils.getStringCellValue(7, 1, sheet);
		String tableName = CellUtils.getStringCellValue(9, 1, sheet);

		LOG.debug("tableID:{}({})", tableName, tableID);
		entity.setName(tableID);
		entity.setKanji(tableName);
		for (Row row : sheet) {
			if (row.getRowNum() < ROW_BEGIN) {
				continue;
			}
			AttrInfo attr = scanRow(row);

			if (attr == null) {
				continue;
			}
			entity.add(attr);
		}
		return entity;
	}

	private void scanBook(final SchemaInfo schema, final Workbook book) {
		for (final Sheet sheet : book) {
			String sig = CellUtils.getStringCellValue(1, 1, sheet);

			sig = StringUtils.replace(sig, StringUtils.SPACE, "");
			if (!SIG_DB_SPEC.equals(sig)) {
				continue;
			}
			LOG.debug("sheet[{}]", sheet.getSheetName());
			EntityInfo entity = scanSheet(sheet);

			schema.add(entity);
		}
	}

	private void save(SchemaInfo schema) throws IOException {
		File dir = new File("ddl");
		DDLCreateor ddl = new DDLCreateor();

		ddl.save(dir, schema);
	}

	public void execute(final File dir) throws EncryptedDocumentException, InvalidFormatException, IOException {
		SchemaInfo schema = new SchemaInfo();

		for (File file : dir.listFiles()) {
			String name = file.getName();

			if (!name.endsWith(".xls") && !name.endsWith(".xlsx")) {
				continue;
			}
			LOG.debug(file.getAbsolutePath());
			try (Workbook book = WorkbookFactory.create(file)) {
				scanBook(schema, book);
			}
		}
		save(schema);
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
