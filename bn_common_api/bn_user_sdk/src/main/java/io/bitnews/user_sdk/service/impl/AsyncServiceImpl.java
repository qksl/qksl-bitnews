package io.bitnews.user_sdk.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.bitnews.user_sdk.exception.BNException;
import io.bitnews.user_sdk.service.AsyncService;

@Service
public class AsyncServiceImpl implements AsyncService {

	@Async
	@Override
	public void asyncInvoke(AsyncExec consumer) throws BNException {
		consumer.exec();
	}

}
