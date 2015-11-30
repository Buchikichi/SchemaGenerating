package to.kit.data.creating;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import to.kit.data.info.AttrInfo;
import to.kit.data.info.EntityInfo;
import to.kit.data.info.IndexKey;
import to.kit.data.info.SchemaInfo;

/**
 * CREATE TABLE 文を生成.
 * @author Hidetaka Sasai
 */
public final class DDLCreateor {
	/** DDLのキャラセット. */
	private static final String CHARSET = "UTF-8";

	private String makeUniq(final EntityInfo entity) {
		StringBuilder buff = new StringBuilder();
		List<IndexKey> uniqList = entity.getUniqList();
		List<String> list = new ArrayList<>();

		Collections.sort(uniqList);
		//  CONSTRAINT file_input_errors_infotype_infoid_rowno_key UNIQUE (infotype, infoid, rowno)
		for (IndexKey uniq : uniqList) {
			list.add(uniq.getName());
		}
		if (!list.isEmpty()) {
			buff.append("\tUNIQUE(");
			buff.append(StringUtils.join(list, ","));
			buff.append(")\n");
		}
		return buff.toString();
	}

	/**
	 * CREATE TABLE を作成.
	 * @param entity テーブル情報
	 * @return CREATE TABLE 文
	 */
	private String makeTable(final EntityInfo entity) {
		StringBuilder buff = new StringBuilder();
		List<String> pkList = new ArrayList<>();
		String tableName = entity.getName();
		List<String> lineList = new ArrayList<>();

		buff.append("--\n-- ");
		buff.append(entity.getKanji());
		buff.append("\n--\n");

		buff.append("-- DROP TABLE IF EXISTS ");
		buff.append(tableName);
		buff.append(" CASCADE;\n");

		buff.append("CREATE TABLE ");
		buff.append(tableName);
		buff.append(" (\n");

		for (AttrInfo attr : entity) {
			StringBuilder line = new StringBuilder();
			String name = attr.getName();
			int size = attr.getSize();
			String defaultVal = attr.getDefaultVal();

			line.append("\t");
			line.append(name);
			line.append("\t");
			line.append(attr.getType());
			if (0 < size) {
				line.append("(");
				line.append(size);
				line.append(")");
			}
			if (attr.isNotNull()) {
				line.append("\tNOT NULL");
			}
			if (StringUtils.isNotBlank(defaultVal)) {
				line.append("\tDEFAULT ");
				line.append(defaultVal);
			}
			lineList.add(line.toString());
			if (attr.isPrimary()) {
				pkList.add(name);
			}
		}
		lineList.add("\tPRIMARY KEY(" + StringUtils.join(pkList, ',') + ")");
		lineList.add(makeUniq(entity));
		buff.append(StringUtils.join(lineList, ",\n"));
		buff.append(");\n");
		return buff.toString();
	}

	/**
	 * カラムのコメントを作成.
	 * @param entity テーブル情報
	 * @return コメント作成ステートメント
	 */
	private String makeComment(final EntityInfo entity) {
		StringBuilder buff = new StringBuilder();

		buff.append("COMMENT ON TABLE ");
		buff.append(entity.getName());
		buff.append(" IS '");
		buff.append(entity.getKanji());
		buff.append("';\n");
		//"COMMENT ON COLUMN tableName.columnName IS 'コメント';";
		for (AttrInfo attr : entity) {
			buff.append("COMMENT ON COLUMN ");
			buff.append(entity.getName());
			buff.append(".");
			buff.append(attr.getName());
			buff.append(" IS '");
			buff.append(attr.getKanji());
			buff.append("';\n");
		}
		return buff.toString();
	}

	/**
	 * DDLを保存.
	 * @param dir 出力先ディレクトリー
	 * @param schema スキーマ情報
	 * @throws IOException 入出力エラー
	 */
	public void save(final File dir, final SchemaInfo schema) throws IOException {
		dir.mkdirs();
		for (EntityInfo table : schema) {
			String name = "create_" + table.getName().toLowerCase() + ".sql";
			File file = new File(dir, name);

			try (OutputStream stream = new FileOutputStream(file);
					Writer writer = new OutputStreamWriter(stream, CHARSET)) {
				writer.write(makeTable(table));
				writer.write(makeComment(table));
			}
		}
	}
}
