package to.kit.data.info;

/**
 * インデックス情報.
 * @author Hidetaka Sasai
 */
public final class IndexKey implements Comparable<IndexKey> {
	private int seq;
	private String name;

	@Override
	public int compareTo(IndexKey o) {
		return this.seq - o.seq;
	}

	public int getSeq() {
		return this.seq;
	}
	public void setSeq(int ix) {
		this.seq = ix;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * インスタンス生成.
	 * @param ix 順序
	 * @param name カラム名
	 */
	public IndexKey(int ix, String name) {
		this.seq = ix;
		this.name = name;
	}
}
