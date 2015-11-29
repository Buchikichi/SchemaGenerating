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
	private final List<AttrInfo> fieldList = new ArrayList<>();
	/** プライマリーキーとなっている項目の個数 */
	private int numOfKey;
	private List indexList;
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

	@Override
	public Iterator<AttrInfo> iterator() {
		return this.fieldList.iterator();
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
	 * @return Returns the fieldList.
	 */
	public List<AttrInfo> getFieldList() {
		return this.fieldList;
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
	 * @return Returns the indexList.
	 */
	public List getIndexList() {
		return this.indexList;
	}

	/**
	 * @param indexList The indexList to set.
	 */
	public void setIndexList(List indexList) {
		this.indexList = indexList;
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

	/**
	 * @return Returns the numOfKey.
	 */
	public int getNumOfKey() {
		return this.numOfKey;
	}

	/**
	 * @param numOfKey The numOfKey to set.
	 */
	public void setNumOfKey(int numOfKey) {
		this.numOfKey = numOfKey;
	}
}
