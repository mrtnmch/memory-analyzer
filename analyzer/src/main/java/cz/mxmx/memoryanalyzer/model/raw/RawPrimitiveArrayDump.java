package cz.mxmx.memoryanalyzer.model.raw;

import java.util.ArrayList;
import java.util.List;

public class RawPrimitiveArrayDump {
	private final Long objectId;
	private final String itemType;
	private final List<Object> items = new ArrayList<>();

	public RawPrimitiveArrayDump(Long objectId, String itemClassObjectId) {
		this.objectId = objectId;
		this.itemType = itemClassObjectId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public String getItemClassObjectId() {
		return itemType;
	}

	public List<Object> getItems() {
		return items;
	}

	public void addItem(Object object) {
		this.items.add(object);
	}
}
