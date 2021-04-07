package io.bitnews.user_sdk.service;

import io.bitnews.user_sdk.constant.ServerStatus;

public interface ServerStatusService {

	public ServerStatus getRedisStatus();

	public ServerStatus getDatabaseStatus();

	public ServerStatus getUtilStatus();

	ServerStatus getQtumStatus(String address, int port);

	ServerStatus getVechainStatus(String address, int port);

	ServerStatus getJadeStatus(String address, int port);
}
