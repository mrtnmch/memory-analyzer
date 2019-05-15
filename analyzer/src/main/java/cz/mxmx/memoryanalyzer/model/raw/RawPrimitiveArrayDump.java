package cz.mxmx.memoryanalyzer.model.raw;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw primitive type array.
 */
public class RawPrimitiveArrayDump {
	private final Long objectId;
	private final String itemType;
	private final List<Object> items = new ArrayList<>();

	/**
	 * Creates a primitive type array.
	 * @param objectId Object ID of the array.
	 * @param itemClassObjectId Class ID of the type.
	 */
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
