package io.bitnews.user_sdk.service.impl;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.bitnews.user_sdk.constant.ServerStatus;
import io.bitnews.user_sdk.service.ServerStatusService;

@Service
public class ServerStatusServiceImpl implements ServerStatusService {

	private static final int CONNECTION_TIMEOUT = 5000;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.datasource.name:''}")
	private String dbName;

	@Value("${spring.datasource.host:''}")
	private String dbHost;

	@Value("${spring.datasource.port:0}")
	private int dbPort;

	@Value("${spring.redis.database:''}")
	private String redisDatabase;

	@Value("${spring.redis.host:''}")
	private String redisHost;

	@Value("${spring.redis.port:0}")
	private int redisPort;

	@Value("${util.name:''}")
	private String utilName;

	@Value("${util.host:''}")
	private String utilHost;

	@Value("${util.port:0}")
	private int utilPort;

	@Override
	public ServerStatus getRedisStatus() {
		ServerStatus status = connectServer(redisHost, redisPort, redisDatabase);
		return status;
	}

	@Override
	public ServerStatus getDatabaseStatus() {
		ServerStatus status = connectServer(dbHost, dbPort, dbName);
		return status;
	}

	@Override
	public ServerStatus getUtilStatus() {
		ServerStatus status = connectServer(utilHost, utilPort, utilName);
		return status;
	}

	@Override
	public ServerStatus getQtumStatus(String address, int port) {
		ServerStatus status = connectServer(address, port, "qtum");
		return status;
	}

	@Override
	public ServerStatus getVechainStatus(String address, int port) {
		ServerStatus status = connectServer(address, port, "vechain");
		return status;
	}

	@Override
	public ServerStatus getJadeStatus(String address, int port) {
		ServerStatus status = connectServer(address, port, "jade");
		return status;
	}

	protected ServerStatus connectServer(String host, int port, String service) {
		TelnetClient telnetClient = new TelnetClient();
		telnetClient.setConnectTimeout(CONNECTION_TIMEOUT);
		boolean connected = false;
		try {
			telnetClient.connect(host, port);
			connected = true;
		} catch (SocketException e) {
			logger.debug("Server: " + service, e);
		} catch (IOException e) {
			logger.debug("Server: " + service, e);
		} finally {
			try {
				telnetClient.disconnect();
			} catch (IOException e) {
				// silent close
			}
		}
		ServerStatus status = new ServerStatus();
		status.setService(service);
		status.setConnected(connected);
		status.setHost(host);
		status.setPort(port);
		return status;
	}

}
