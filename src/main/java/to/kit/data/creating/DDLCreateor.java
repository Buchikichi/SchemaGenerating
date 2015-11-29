package to.kit.data.creating;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import to.kit.data.info.AttrInfo;
import to.kit.data.info.EntityInfo;
import to.kit.data.info.SchemaInfo;

/**
 * CREATE TABLE 文を生成.
 * @author Hidetaka Sasai
 */
public final class DDLCreateor {
	/**
	 * CREATE TABLE を作成.
	 * @param entity テーブル情報
	 * @return CREATE TABLE 文
	 */
	private String makeTable(final EntityInfo entity) {
		StringBuilder buff = new StringBuilder();
		List<String> pkList = new ArrayList<>();
		String tableName = entity.getName();

		buff.append("--\n-- ");
		buff.append(entity.getKanji());
		buff.append("\n--\n");

		buff.append("-- DROP TABLE ");
		buff.append(tableName);
		buff.append(";\n");

		buff.append("CREATE TABLE ");
		buff.append(tableName);
		buff.append(" (\n");

		for (AttrInfo attr : entity) {
			String name = attr.getName();

			buff.append("\t");
			buff.append(name);
			buff.append("\t");
			buff.append(attr.getType());
			String size = attr.getSize();
			if (size != null) {
				buff.append("(");
				buff.append(size);
				buff.append(")");
			}
			if (attr.isNotNull()) {
				buff.append("\tNOT NULL");
			}
			buff.append(",");
			buff.append("\t-- ");
			buff.append(attr.getKanji());
			buff.append("\n");
			if (attr.isPrimary()) {
				pkList.add(name);
			}
		}
		buff.append("\tPRIMARY KEY(");
		buff.append(StringUtils.join(pkList, ','));
		buff.append(")\n");
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
	 * @param schema
	 * @throws IOException 入出力エラー
	 */
	public void save(final File dir, final SchemaInfo schema) throws IOException {
		dir.mkdirs();
		for (EntityInfo table : schema) {
			String name = table.getName().toLowerCase() + ".sql";
			File file = new File(dir, name);

			try (OutputStream stream = new FileOutputStream(file);
					Writer writer = new OutputStreamWriter(stream, "SJIS")) {
				writer.write(makeTable(table));
				writer.write(makeComment(table));
			}
		}
	}
}
