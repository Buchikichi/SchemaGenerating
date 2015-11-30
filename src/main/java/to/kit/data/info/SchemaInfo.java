package to.kit.data.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * スキーマ情報.
 * @author Hidetaka Sasai
 */
public final class SchemaInfo implements Iterable<EntityInfo> {
	/** エンティティリスト. */
	private final List<EntityInfo> entityList = new ArrayList<>();

	/**
	 * エンティティ情報を追加.
	 * @param entity エンティティ情報
	 */
	public void add(EntityInfo entity) {
		this.entityList.add(entity);
	}

	@Override
	public Iterator<EntityInfo> iterator() {
		return this.entityList.iterator();
	}
}
