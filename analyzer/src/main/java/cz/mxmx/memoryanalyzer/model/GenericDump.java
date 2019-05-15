package cz.mxmx.memoryanalyzer.model;

/**
 * Generic memory dump.
 */
public abstract class GenericDump {
	protected final Long instanceId;

	/**
	 * Creates a generic dump.
	 * @param instanceId Instance ID of the processed object.
	 */
	public GenericDump(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Long getInstanceId() {
		return instanceId;
	}
}
