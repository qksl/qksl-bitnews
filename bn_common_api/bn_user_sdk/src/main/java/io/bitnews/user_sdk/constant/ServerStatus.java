package io.bitnews.user_sdk.constant;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ServerStatus implements Serializable {

	private static final long serialVersionUID = -2408176086503485486L;
	private String service;
	private boolean connected;
	private String host;
	private int port;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean getConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getStatus() {
		return this.connected ? 1 : 0;
	}
}
