package to.kit.data.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hidetaka Sasai
 */
public final class EntityInfo implements Iterable<AttrInfo> {
	private String name;
	private String kanji;
	private String comment;
	private boolean unlogged;
	private final List<AttrInfo> attrList = new ArrayList<>();
	private final List<IndexKey> uniqList = new ArrayList<>();
	private final List<IndexKey> indexList = new ArrayList<>();
	/** 削除フラグ(DEL_FLAG) の項目があるかどうか */
	private boolean hasDelFlag;

	/**
	 * @return
	 */
	public List getKeyList() {
		List result = new ArrayList();

		for (Iterator it = this.indexList.iterator(); it.hasNext();) {
			AttrInfo fieldInfo = (AttrInfo) it.next();

			if (fieldInfo.isPrimary()) {
				result.add(fieldInfo);
			}
		}
		return result;
	}

	public void add(AttrInfo attr) {
		int uniq = attr.getUnique();

		this.attrList.add(attr);
		if (0 < uniq) {
			this.uniqList.add(new IndexKey(uniq, attr.getName()));
		}
	}

	public void addUniq(IndexKey val) {
		this.uniqList.add(val);
	}

	@Override
	public Iterator<AttrInfo> iterator() {
		return this.attrList.iterator();
	}

	//-------------------------------------------------------------------------
	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return Returns the hasDelFlag.
	 */
	public boolean isHasDelFlag() {
		return this.hasDelFlag;
	}

	/**
	 * @param hasDelFlag The hasDelFlag to set.
	 */
	public void setHasDelFlag(boolean hasDelFlag) {
		this.hasDelFlag = hasDelFlag;
	}

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

	public boolean isUnlogged() {
		return this.unlogged;
	}

	public void setUnlogged(boolean unlogged) {
		this.unlogged = unlogged;
	}

	public List<IndexKey> getUniqList() {
		return this.uniqList;
	}

	public List<IndexKey> getIndexList() {
		return this.indexList;
	}
}
