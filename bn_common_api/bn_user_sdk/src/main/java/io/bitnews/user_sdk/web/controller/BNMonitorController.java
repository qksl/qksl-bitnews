package io.bitnews.user_sdk.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.bitnews.user_sdk.constant.ServerStatus;
import io.bitnews.user_sdk.constant.ServerStatusResponse;
import io.bitnews.user_sdk.model.BaseResponse;
import io.bitnews.user_sdk.service.ServerStatusService;

@RestController
public class BNMonitorController {

	@Autowired
	private ServerStatusService serverStatusService;

	@Value("${qtum.host:#{null}}")
	private String qAddress;

	@Value("${qtum.port:0}")
	private int qPort;

	@Value("${thor.host:#{null}}")
	private String vAddress;

	@Value("${thor.port:0}")
	private int vPort;

	@Value("${jade.pool:#{null}}")
	private String jadeUrl;

	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	public BaseResponse heartbeat() {
		return new BaseResponse();
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ServerStatusResponse status() {
		List<ServerStatus> list = new ArrayList<ServerStatus>(3);
		list.add(serverStatusService.getDatabaseStatus());
		list.add(serverStatusService.getRedisStatus());
		list.add(serverStatusService.getUtilStatus());
		if(StringUtils.isNotEmpty(qAddress) && qPort!= 0){
			list.add(serverStatusService.getQtumStatus(qAddress, qPort));
		}

		if(StringUtils.isNotEmpty(vAddress) && vPort!= 0){
			list.add(serverStatusService.getVechainStatus(vAddress, vPort));
		}

		if(StringUtils.isNotEmpty(jadeUrl)){
			String regex = "^http://([^:]+):(\\d+)$";
			String jadeAddress = jadeUrl.trim().replaceAll(regex, "$1");
			int jadePort = Integer.parseInt(jadeUrl.trim().replaceAll(regex, "$2"));
			list.add(serverStatusService.getJadeStatus(jadeAddress, jadePort));
		}
		boolean result = true;
		for (ServerStatus status : list) {
			if (!status.getConnected()) {
				result = false;
				break;
			}
		}
		return new ServerStatusResponse(result, list, System.currentTimeMillis());
	}
}
