package to.kit.data.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SchemaInfo implements Iterable<EntityInfo> {
	private final List<EntityInfo> entityList = new ArrayList<>();

	@Override
	public Iterator<EntityInfo> iterator() {
		return this.entityList.iterator();
	}
}
