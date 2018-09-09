package cz.mxmx.memoryanalyzer.model.raw;

import java.util.ArrayList;
import java.util.List;

public class RawObjectArrayDump {
	private final Long objectId;
	private final Long itemClassObjectId;
	private final List<Long> items = new ArrayList<>();

	public RawObjectArrayDump(Long objectId, Long itemClassObjectId) {
		this.objectId = objectId;
		this.itemClassObjectId = itemClassObjectId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public Long getItemClassObjectId() {
		return itemClassObjectId;
	}

	public List<Long> getItems() {
		return items;
	}

	public void addItem(Long objectId) {
		this.items.add(objectId);
	}
}
