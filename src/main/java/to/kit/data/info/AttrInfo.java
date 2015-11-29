package to.kit.data.info;
import java.io.Serializable;

/**
 * @author Hidetaka Sasai
 */
public final class AttrInfo implements Serializable {
	/** 項目名(英字名). */
	private String name;
	/** 項目名(日本語). */
	private String kanji;
	/** 型. */
	private String type;
	/** 桁数. */
	private String size;
	/** 主キー. */
	private boolean primary;
	/** NOT NULL. */
	private boolean notNull;
	/** 備考. */
	private String remarks;

	public String getFieldName() {
		boolean isInitial = false;
		StringBuffer buff = new StringBuffer(this.name.toLowerCase());
		for (int ix = 0; ix < buff.length();) {
			char ch = buff.charAt(ix);
			if (isInitial) {
				if ('a' <= ch && ch <= 'z') {
					char upper = (char)(ch - ('a' - 'A'));
					buff.setCharAt(ix, upper);
				}
				isInitial = false;
			}
			if (ch == '_') {
				buff.deleteCharAt(ix);
				isInitial = true;
				continue;
			}
			ix++;
		}
		return buff.toString();
	}

	public String getMethodName() {
		StringBuffer result = new StringBuffer(getFieldName());
		char ch = result.charAt(0);
		char upper = (char)(ch - ('a' - 'A'));
		result.setCharAt(0, upper);
		return result.toString();
	}

	/**
	 * @return 型
	 */
	public String getJavaType() {
		String result = "String";
		if (this.type == null || this.type.length() == 0) {
			return result;
		}
		if ("BLOB".equals(this.type)) {
			result = "Blob";
		} else if ("NUMERIC".equals(this.type)) {
			result = "BigDecimal";
//		} else if ("DATE".equals(this.type)) {
//			result = "Date";
		} else if ("S".equals(this.type)) {
			result = "BigDecimal";
		}
		return result;
	}

	/**
	 * @return 型
	 */
	public String getJavaTypeMethod() {
		StringBuffer result = new StringBuffer(getJavaType());
		char ch = result.charAt(0);
		if ('a' <= ch && ch <= 'z') {
			// 半角英字の小文字の場合
			char upper = (char) (ch - ('a' - 'A'));
			result.setCharAt(0, upper);
		}
		return result.toString();
	}

	/**
	 * @return
	 */
	public String getAttr() {
		StringBuffer result = new StringBuffer();
		result.append(this.name);
		result.append(" ");
		result.append(this.type);
		if (this.size != null) {
			result.append("(");
			result.append(this.size);
			result.append(")");
		}
		if (this.notNull) {
			result.append(" NotNull");
		}
		return result.toString();
	}

	/**
	 * @return
	 */
	public int getSizeInt() {
		try {
			return Integer.parseInt(this.size);
		} catch (NumberFormatException e) {
			// 無視
			return 0;
		}
	}

	/**
	 * @return
	 */
	public boolean isFixed() {
		return "CHAR".equals(this.type) && 1 < getSizeInt();
	}

	/**
	 * 日付をあらわす文字列かどうか.
	 * @return
	 */
	public boolean isStrDate() {
		boolean isDateType = this.name.indexOf("_DATE") != -1 || this.name.indexOf("_KIGEN") != -1;
		return "CHAR".equals(this.type) && isDateType;
	}

	//-------------------------------------------------------------------------
	/**
	 * @return Returns the kanji.
	 */
	public String getKanji() {
		return this.kanji;
	}

	/**
	 * @param kanji The kanji to set.
	 */
	public void setKanji(String kanji) {
		this.kanji = kanji;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the notNull.
	 */
	public boolean isNotNull() {
		return this.notNull;
	}

	/**
	 * @param notNull The notNull to set.
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * @return Returns the primary.
	 */
	public boolean isPrimary() {
		return this.primary;
	}

	/**
	 * @param primary The primary to set.
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * @return Returns the remarks.
	 */
	public String getRemarks() {
		return this.remarks;
	}

	/**
	 * @param remarks The remarks to set.
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return Returns the size.
	 */
	public String getSize() {
		return this.size;
	}

	/**
	 * @param size The size to set.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

}
