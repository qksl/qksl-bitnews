package io.bitnews.user_sdk.constant;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ServerStatusResponse implements Serializable {

	private static final long serialVersionUID = -1692104601785918852L;
	private boolean result;
	private List<ServerStatus> services;
	private Long timestamp;

	public ServerStatusResponse() {
	}

	public ServerStatusResponse(boolean result) {
		this(result, null, null);
	}

	public ServerStatusResponse(boolean result, Long timestamp) {
		this(result, null, timestamp);
	}

	public ServerStatusResponse(List<ServerStatus> services) {
		this(true, services, null);
	}

	public ServerStatusResponse(boolean result, List<ServerStatus> services) {
		this(result, services, null);
	}

	public ServerStatusResponse(boolean result, List<ServerStatus> services, Long timestamp) {
		this.result = result;
		this.services = services;
		this.timestamp = timestamp;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<ServerStatus> getServices() {
		return services;
	}

	public void setServices(List<ServerStatus> services) {
		this.services = services;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return this.result ? 1 : 0;
	}

}
