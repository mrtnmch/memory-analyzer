package cz.mxmx.memoryanalyzer.model;

public abstract class GenericDump {
	protected final Long instanceId;

	public GenericDump(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Long getInstanceId() {
		return instanceId;
	}
}
